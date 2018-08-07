package cn.faury.fdk.common.db;

/**
 * 带主键的Bean
 */
public interface PrimaryKeyEnableBean<P> {
    public P getPrimaryKey();
}
