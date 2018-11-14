/*
 * 电商
 */
package cn.faury.fdk.common.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CrudBaseService<T,P> {
    /**
     * 新增一条记录
     *
     * @param bean 对象Bean
     * @return 插入后的ID
     */
    public P insert(T bean);

    /**
     * 新增多条记录
     *
     * @param beans 对象Bean列表
     * @return 插入后的ID类别
     */
    public List<P> insertBatch(List<T> beans);

    /**
     * 更新一条记录
     *
     * @param bean 对象Bean
     * @return 成功更新条数
     */
    public int update(T bean);

    /**
     * 删除一条记录
     *
     * @param bean 对象Bean
     * @return 成功删除条数
     */
    public int delete(T bean);

    /**
     * 根据记录ID删除一条记录
     *
     * @param id 要删除记录的ID
     * @return 成功删除条数
     */
    public int deleteById(P id);

    /**
     * 根据记录ID删除多条记录
     *
     * @param ids 要删除记录的ID列表
     * @return 成功删除条数
     */
    public int deleteByIdBatch(List<P> ids);

    /**
     * 根据ID获取对象Bean
     *
     * @param id 唯一标识
     * @return 实体对象
     */
    public T getBeanById(P id);

    /**
     * 根据ID列表获取多个对象Bean
     *
     * @param ids 唯一标识
     * @return 实体对象类别
     */
    public List<T> getBeanByIdBatch(List<P> ids);

    /**
     * 根据所属上级主键ID，获取对象列表
     * @param belongPrimaryId 所属上级主键ID
     * @return 对象列表
     */
    default public List<T> getBeansByBelongPrimaryId(Object belongPrimaryId){
        PageParam pageParam = new PageParam(1,Integer.MAX_VALUE);
        PageInfo<T> page = getBeansByBelongPrimaryId(belongPrimaryId,pageParam);
        return (List<T>) page.getList();
    }

    /**
     * 根据所属上级主键ID，获取对象列表
     * @param belongPrimaryId 所属上级主键ID
     * @return 对象列表
     */
    default public PageInfo<T> getBeansByBelongPrimaryId(Object belongPrimaryId,PageParam pageParam){
        throw new UnsupportedOperationException("current service do not support belong primary");
    }

    /**
     * 根据参数查询记录
     *
     * @param param 查询参数
     * @return 查询结果
     */
    default public List<T> query(final Map<String, Object> param) {
        Map<String, Object> _param = new HashMap<>();
        if (param != null) {
            _param.putAll(param);
        }
        _param.put(PageParam.KEY.KEY_PAGE_NO, 1);
        _param.put(PageParam.KEY.KEY_PAGE_SIZE, Integer.MAX_VALUE);
        PageInfo<T> page = search(_param);
        return (List<T>) page.getList();
    }

    /**
     * 根据参数分页查询结果
     *
     * @param param 查询参数
     * @return 查询结果
     */
    public PageInfo<T> search(Map<String, Object> param);
}
