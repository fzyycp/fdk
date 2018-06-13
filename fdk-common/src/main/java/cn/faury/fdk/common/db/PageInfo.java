package cn.faury.fdk.common.db;

import cn.faury.fdk.common.utils.JsonUtil;

import java.util.List;

/**
 * 分页对象信息
 */
public class PageInfo<T> extends com.github.pagehelper.PageInfo {
    public PageInfo() {
    }

    public PageInfo(List list) {
        super(list);
    }

    public PageInfo(List list, int navigatePages) {
        super(list, navigatePages);
    }

    public String toString() {
        return JsonUtil.objectToJson(this);
    }
}
