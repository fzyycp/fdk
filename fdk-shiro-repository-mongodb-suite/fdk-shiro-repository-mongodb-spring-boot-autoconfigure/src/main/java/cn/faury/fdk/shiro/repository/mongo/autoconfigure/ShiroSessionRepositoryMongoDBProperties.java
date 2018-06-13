package cn.faury.fdk.shiro.repository.mongo.autoconfigure;

import cn.faury.fdk.common.utils.JsonUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置文件读取
 */
@ConfigurationProperties(prefix = ShiroSessionRepositoryMongoDBProperties.PROPERTIES_PREFIX)
public class ShiroSessionRepositoryMongoDBProperties {
    /**
     * 配置文件前缀
     */
    public static final String PROPERTIES_PREFIX = "fdk.shiro.repository.mongodb";

    // 服务器地址
    private String host="127.0.0.1";
    // 服务端口号
    private int port=27017;
    // 服务器登录名
    private String username="";
    // 服务器密码
    private String password="";
    // 数据库名
    private String dbName="";
    // 集合名
    private String collName="";

    /**
     * 获取host
     *
     * @return host
     */
    public String getHost() {
        return host;
    }

    /**
     * 设置host
     *
     * @param host 值
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * 获取port
     *
     * @return port
     */
    public int getPort() {
        return port;
    }

    /**
     * 设置port
     *
     * @param port 值
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * 获取username
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置username
     *
     * @param username 值
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取password
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置password
     *
     * @param password 值
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取dbName
     *
     * @return dbName
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * 设置dbName
     *
     * @param dbName 值
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    /**
     * 获取collName
     *
     * @return collName
     */
    public String getCollName() {
        return collName;
    }

    /**
     * 设置collName
     *
     * @param collName 值
     */
    public void setCollName(String collName) {
        this.collName = collName;
    }

    @Override
    public String toString() {
        return JsonUtil.objectToJson(this);
    }
}
