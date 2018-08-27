package cn.faury.fdk.ftp.autoconfigure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * 配置文件
 */
@ConfigurationProperties(prefix = FdkFtpProperties.PROPERTIES_PREFIX)
public class FdkFtpProperties {

    /**
     * 配置文件前缀
     */
    public static final String PROPERTIES_PREFIX = "fdk.ftp";

    /**
     * FTP主机
     */
    private String hostname;
    /**
     * FTP端口
     */
    private int port = 21;
    /**
     * FTP用户名
     */
    private String username;
    /**
     * FTP密码
     */
    private String password;
    /**
     * FTP被动模式
     */
    private boolean passiveModeConf = true;

    /**
     * FTP缓冲池最大池容量
     */
    @Value("${" + PROPERTIES_PREFIX + ".pool.maxActive:10}")
    private int maxActive;

    /**
     * FTP缓冲池为空时取对象等待的最大毫秒数
     */
    @Value("${" + PROPERTIES_PREFIX + ".pool.maxWait:30000}")
    private int maxWait;

    /**
     * FTP缓冲池最大空闲数量
     */
    @Value("${" + PROPERTIES_PREFIX + ".pool.maxIdle:10}")
    private int maxIdle;

    /**
     * FTP缓冲池最小空闲数量
     */
    @Value("${" + PROPERTIES_PREFIX + ".pool.minIdle:5}")
    private int minIdle;

    /**
     * FTP缓冲池取出对象时验证(此处设置成验证ftp是否处于连接状态).
     */
    @Value("${" + PROPERTIES_PREFIX + ".pool.testOnBorrow:true}")
    private boolean testOnBorrow;

    /**
     * FTP缓冲池还回对象时验证(此处设置成验证ftp是否处于连接状态).
     */
    @Value("${" + PROPERTIES_PREFIX + ".pool.testOnReturn:true}")
    private boolean testOnReturn;

    /**
     * 设定间隔每过多少毫秒进行一次后台对象清理的行动。如果不是正数，则不进行清理
     */
    @Value("${" + PROPERTIES_PREFIX + ".pool.timeBetweenEvictionRunsMillis:30000}")
    private int timeBetweenEvictionRunsMillis;

    /**
     * 设定在进行后台对象清理时，每次检查几个对象
     */
    @Value("${" + PROPERTIES_PREFIX + ".pool.numTestsPerEvictionRun:5}")
    private int numTestsPerEvictionRun;

    /**
     * 定在进行后台对象清理时，视休眠时间超过了多少毫秒的对象为过期。过期的对象将被回收。
     */
    @Value("${" + PROPERTIES_PREFIX + ".pool.minEvictableIdleTimeMillis:60000}")
    private int minEvictableIdleTimeMillis;

    /**
     * 设定在进行后台对象清理时，是否还对没有过期的池内对象进行有效性检查。
     */
    @Value("${" + PROPERTIES_PREFIX + ".pool.testWhileIdle:false}")
    private boolean testWhileIdle;

    /**
     * 设定在进行后台对象清理时，是否还对没有过期的池内对象进行有效性检查。
     */
    @Value("${" + PROPERTIES_PREFIX + ".pool.softMinEvictableIdleTimeMillis:60000}")
    private int softMinEvictableIdleTimeMillis;

    /**
     * 先进先出
     */
    @Value("${" + PROPERTIES_PREFIX + ".pool.lifo:true}")
    private boolean lifo;

    /**
     * 获取hostname
     *
     * @return hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * 设置hostname
     *
     * @param hostname 值
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
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
     * 获取passiveModeConf
     *
     * @return passiveModeConf
     */
    public boolean isPassiveModeConf() {
        return passiveModeConf;
    }

    /**
     * 设置passiveModeConf
     *
     * @param passiveModeConf 值
     */
    public void setPassiveModeConf(boolean passiveModeConf) {
        this.passiveModeConf = passiveModeConf;
    }

    /**
     * 获取maxActive
     *
     * @return maxActive
     */
    public int getMaxActive() {
        return maxActive;
    }

    /**
     * 设置maxActive
     *
     * @param maxActive 值
     */
    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    /**
     * 获取maxWait
     *
     * @return maxWait
     */
    public int getMaxWait() {
        return maxWait;
    }

    /**
     * 设置maxWait
     *
     * @param maxWait 值
     */
    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    /**
     * 获取maxIdle
     *
     * @return maxIdle
     */
    public int getMaxIdle() {
        return maxIdle;
    }

    /**
     * 设置maxIdle
     *
     * @param maxIdle 值
     */
    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    /**
     * 获取minIdle
     *
     * @return minIdle
     */
    public int getMinIdle() {
        return minIdle;
    }

    /**
     * 设置minIdle
     *
     * @param minIdle 值
     */
    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    /**
     * 获取testOnBorrow
     *
     * @return testOnBorrow
     */
    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    /**
     * 设置testOnBorrow
     *
     * @param testOnBorrow 值
     */
    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    /**
     * 获取testOnReturn
     *
     * @return testOnReturn
     */
    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    /**
     * 设置testOnReturn
     *
     * @param testOnReturn 值
     */
    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    /**
     * 获取timeBetweenEvictionRunsMillis
     *
     * @return timeBetweenEvictionRunsMillis
     */
    public int getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    /**
     * 设置timeBetweenEvictionRunsMillis
     *
     * @param timeBetweenEvictionRunsMillis 值
     */
    public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    /**
     * 获取numTestsPerEvictionRun
     *
     * @return numTestsPerEvictionRun
     */
    public int getNumTestsPerEvictionRun() {
        return numTestsPerEvictionRun;
    }

    /**
     * 设置numTestsPerEvictionRun
     *
     * @param numTestsPerEvictionRun 值
     */
    public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
    }

    /**
     * 获取minEvictableIdleTimeMillis
     *
     * @return minEvictableIdleTimeMillis
     */
    public int getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    /**
     * 设置minEvictableIdleTimeMillis
     *
     * @param minEvictableIdleTimeMillis 值
     */
    public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    /**
     * 获取testWhileIdle
     *
     * @return testWhileIdle
     */
    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    /**
     * 设置testWhileIdle
     *
     * @param testWhileIdle 值
     */
    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    /**
     * 获取softMinEvictableIdleTimeMillis
     *
     * @return softMinEvictableIdleTimeMillis
     */
    public int getSoftMinEvictableIdleTimeMillis() {
        return softMinEvictableIdleTimeMillis;
    }

    /**
     * 设置softMinEvictableIdleTimeMillis
     *
     * @param softMinEvictableIdleTimeMillis 值
     */
    public void setSoftMinEvictableIdleTimeMillis(int softMinEvictableIdleTimeMillis) {
        this.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
    }

    /**
     * 获取lifo
     *
     * @return lifo
     */
    public boolean isLifo() {
        return lifo;
    }

    /**
     * 设置lifo
     *
     * @param lifo 值
     */
    public void setLifo(boolean lifo) {
        this.lifo = lifo;
    }
}
