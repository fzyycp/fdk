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
     * 根据ID获取对象Bean
     *
     * @param id 唯一标志
     * @return 实体对象
     */
    public T getBeanById(P id);

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
