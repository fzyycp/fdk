package cn.faury.fdk.common.db;

/**
 * 存在上级所属关系的上级主键Bean
 *
 * <pre>
 *     例如：
 *     1，学校信息表、年级信息表，那么年级信息表中的学校ID字段即为上级所属主键
 *     2，订单信息表、订单商品表，那么订单商品表中的订单ID字段即为上级所属主键
 * </pre>
 */
public interface BelongPrimaryKeyEnableBean<P> {

    /**
     * 获取所属上级表主键数据库字段名
     * @return 字段名
     */
    public String getBelongPrimaryKeyName();

    /**
     * 获取所属上级表主键Bean的值
     * @return 字段值
     */
    public P getBelongPrimaryKeyValue();
}
