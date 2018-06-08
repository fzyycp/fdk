/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 6/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.common.db;

import cn.faury.fdk.common.utils.JsonUtil;
import cn.faury.fdk.common.utils.StringUtil;

import java.io.Serializable;
import java.util.Map;

/**
 * 分页参数
 */
public class PageParam implements Serializable {

    /**
     * 页码，默认是第一页
     */
    private int pageNo = 1;

    /**
     * 每页显示的记录数，默认是20
     */
    private int pageSize = 20;

    /**
     * 排序字段
     */
    private String order = null;

    /**
     * 排序顺序
     */
    private String sort = null;

    /**
     * 构造函数
     *
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @param order    排序字段
     * @param sort     排序方式
     */
    public PageParam(int pageNo, int pageSize, String order, String sort) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.order = order;
        this.sort = sort;
    }

    /**
     * 根据默认参数构造分页对象
     * @param param 参数map
     * @return 分页结果
     */
    public static PageParam buildDefaultIns(Map<String, Object> param) {
        PageParam pageBounds = new PageParam();
        if (param != null) {
            String pageNo = (String)param.get("pageNo");
            String pageSize = (String)param.get("pageSize");
            if (StringUtil.isEmpty(pageNo)) {
                pageNo = "1";
            }
            if (StringUtil.isEmpty(pageSize)) {
                pageSize = "20";
            }
            pageBounds.setPageNo(Integer.parseInt(pageNo));
            pageBounds.setPageSize(Integer.parseInt(pageSize));
        }

        return pageBounds;
    }

    /**
     * 拷贝构造函数
     *
     * @param pageParam 分页参数
     */
    public PageParam(PageParam pageParam) {
        if (pageParam != null) {
            this.setPageNo(pageParam.getPageNo());
            this.setPageSize(pageParam.getPageSize());
            this.setOrder(pageParam.getOrder());
            this.setSort(pageParam.getSort());
        }
    }

    /**
     * 构造函数
     *
     * @param pageNo   页码
     * @param pageSize 分页大小
     */
    public PageParam(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    /**
     * 构造函数
     */
    public PageParam() {
    }

    /**
     * 参数名常量
     */
    public interface KEY {
        /**
         * 分页参数名：页码
         */
        String KEY_PAGE_NO = "pageNo";
        /**
         * 分页参数名：分页大小
         */
        String KEY_PAGE_SIZE = "pageSize";
    }

    /**
     * 设置页码
     *
     * @return 页码
     */
    public int getPageNo() {
        return pageNo;
    }

    /**
     * 获取页码
     */
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    /**
     * @return the pageSize
     */
    public final int getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public final void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return the order
     */
    public final String getOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public final void setOrder(String order) {
        this.order = order;
    }

    /**
     * @return the sort
     */
    public final String getSort() {
        return sort;
    }

    /**
     * @param sort the sort to set
     */
    public final void setSort(String sort) {
        this.sort = sort;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return JsonUtil.objectToJson(this);
    }

}
