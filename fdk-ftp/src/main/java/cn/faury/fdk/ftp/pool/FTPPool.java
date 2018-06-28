package cn.faury.fdk.ftp.pool;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * FTP连接对象池
 */
public class FTPPool extends Pool<FTPClient> {

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
    }
}
