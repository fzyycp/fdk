package cn.faury.fdk.ftp.pool;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * FTP连接对象池
 */
public class FTPPool extends Pool<FTPClient> {
    // 缓冲池配置
    private GenericObjectPoolConfig poolConfig;
    // FTP连接地址
    private String hostname;
    // FTP端口号
    private int port;
    // FTP连接用户名
    private String username;
    // 是否被动模式
    private boolean passiveModeConf;

    /**
     * FTP连接池
     *
     * @param poolConfig      池配置
     * @param hostname        主机
     * @param port            端口
     * @param username        用户名
     * @param password        密码
     * @param passiveModeConf 被动池
     */
    public FTPPool(GenericObjectPoolConfig poolConfig, String hostname, int port, String username, String password,
                   boolean passiveModeConf) {
        super(poolConfig, new FTPPoolableObjectFactory(hostname, port, username, password, passiveModeConf));
        this.poolConfig = poolConfig;
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.passiveModeConf = passiveModeConf;
    }

    /**
     * 获取poolConfig
     *
     * @return poolConfig
     */
    public GenericObjectPoolConfig getPoolConfig() {
        return poolConfig;
    }

    /**
     * 获取hostname
     *
     * @return hostname
     */
    public String getHostname() {
        return hostname;
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
     * 获取username
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * 获取passiveModeConf
     *
     * @return passiveModeConf
     */
    public boolean isPassiveModeConf() {
        return passiveModeConf;
    }
}
