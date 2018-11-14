package cn.faury.fdk.common.db;

/**
 * 带主键的Bean
 */
public interface PrimaryKeyEnableBean<P> {

    /**
     * 获取表主键数据库字段名
     *
     * @return 字段名
     */
    default public String getPrimaryKeyName() {
        return null;
    }

    /**
     * 获取主键Bean的值
     *
     * @return 字段值
     */
    default public P getPrimaryKeyValue() {
        return this.getPrimaryKey();
    }

    /**
     * 名字容易歧义，修改名字
     *
     * @return 主键值
     * @see #getPrimaryKeyValue()
     */
    @Deprecated
    default public P getPrimaryKey() {
        return null;
    }
}
