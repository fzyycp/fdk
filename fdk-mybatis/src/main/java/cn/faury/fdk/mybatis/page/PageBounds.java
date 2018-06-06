package cn.faury.fdk.mybatis.page;

import cn.faury.fdk.common.db.Page;
import cn.faury.fdk.common.db.PageParam;
import cn.faury.fdk.common.utils.StringUtil;
import org.apache.ibatis.session.RowBounds;

import java.util.Map;

/**
 * 分页用bounds
 */
public class PageBounds extends RowBounds {

    /**
     * 分页数据
     */
    private Page page;

    /**
     * 构造函数
     */
    public PageBounds() {
        super();
        this.page = new Page();
    }

    /**
     * 构造函数
     *
     * @param param 分页参数
     */
    public PageBounds(PageParam param) {
        super();
        this.page = new Page(param);
    }

    /**
     * 根据查询参数构建默认的分页对象
     *
     * @param param 查询参数
     * @return 分页对象
     */
    public static PageBounds buildDefaultIns(Map<String, Object> param){
        PageBounds pageBounds = new PageBounds();
        if (param != null) {
            String pageNo = (String) param.get(PageParam.KEY.KEY_PAGE_NO);
            String pageSize = (String) param.get(PageParam.KEY.KEY_PAGE_SIZE);
            if (StringUtil.isEmpty(pageNo)) {
                pageNo = "1";
            }
            if (StringUtil.isEmpty(pageSize)) {
                pageSize = "20";
            }
            pageBounds.getPage().setPageNo(Integer.parseInt(pageNo));
            pageBounds.getPage().setPageSize(Integer.parseInt(pageSize));
        }
        return pageBounds;
    }

    /**
     * 获取分页数据
     * @return 分页数据
     */
    public Page getPage() {
        return page;
    }

    /**
     * 设置分页数据
     * @param page 分页数据
     */
    public void setPage(Page page) {
        this.page = page;
    }
}
