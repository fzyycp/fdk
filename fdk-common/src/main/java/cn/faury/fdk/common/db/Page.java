/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 6/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.common.db;

import cn.faury.fdk.common.utils.JsonUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对分页的基本数据进行一个简单的封装
 */
public class Page<T> extends PageParam {
    /**
     * 总记录数
     */
    private int totalRecord = 0;

    /**
     * 总页数
     */
    private int totalPage = 1;

    /**
     * 对应的当前页记录
     */
    private List<T> results =new ArrayList<>();

    /**
     * 其他的参数我们把它分装成一个Map对象
     */
    private Map<String,Serializable> params = new HashMap();

    /**
     * 构造函数
     */
    public Page() {
        super();
    }

    /**
     * 构造函数
     *
     * @param pageNo      页码
     * @param pageSize    每页显示的记录数
     * @param totalRecord 总记录数
     * @param totalPage   总页数
     * @param results     当前页记录
     */
    public Page(int pageNo, int pageSize, int totalRecord, int totalPage, List<T> results) {
        super(pageNo,pageSize);
        this.totalRecord = totalRecord;
        this.totalPage = totalPage;
        this.results = results;
    }

    /**
     * 构造函数
     */
    public Page(List<T> results) {
        super();
        if(results!=null){
            this.setPageSize(results.size());
            this.setTotalRecord(results.size());
            this.setResults(results);
        }
    }

    /**
     * 构造函数
     * @param param 分页参数
     */
    public Page(PageParam param) {
        super(param);
    }

    /**
     * @return the totalRecord
     */
    public int getTotalRecord() {
        return totalRecord;
    }

    /**
     * @param totalRecord the totalRecord to set
     */
    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    /**
     * @return the totalPage
     */
    public int getTotalPage() {
        return totalPage;
    }

    /**
     * @param totalPage the totalPage to set
     */
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    /**
     * @return the results
     */
    public List<T> getResults() {
        return results;
    }

    /**
     * @param results the results to set
     */
    public void setResults(List<T> results) {
        this.results = results;
    }

    /**
     * @return the params
     */
    public Object getParams() {
        return params;
    }

    /**
     * @param params the params to set
     */
    public void setParams(Map<String,Serializable> params) {
        this.params = params;
    }

    /**
     * 格式化输出
     *
     * @return
     */
    @Override
    public String toString() {
        return JsonUtil.objectToJson(this);
    }

}  