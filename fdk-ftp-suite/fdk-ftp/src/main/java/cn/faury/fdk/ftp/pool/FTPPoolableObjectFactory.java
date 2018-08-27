package cn.faury.fdk.ftp.pool;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * FTP池工厂
 */
public class FTPPoolableObjectFactory extends BasePooledObjectFactory<FTPClient> {
    /**
     * 主机
     */
    private String hostname;

    /**
     * 端口
     */
    private int port;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 被动池
     */
    private boolean passiveModeConf;

    /**
     * 构造函数
     *
     * @param hostname
     *            主机
     * @param port
     *            端口
     * @param username
     *            用户名
     * @param password
     *            密码
     */
    public FTPPoolableObjectFactory(String hostname, int port, String username, String password) {
        this(hostname, port, username, password, true);
    }

    /**
     * 构造函数
     *
     * @param hostname
     *            主机
     * @param port
     *            端口
     * @param username
     *            用户名
     * @param password
     *            密码
     * @param passiveModeConf
     *            被动池
     */
    public FTPPoolableObjectFactory(String hostname, int port, String username, String password, boolean passiveModeConf) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.passiveModeConf = passiveModeConf;
    }

    @Override
    public FTPClient create() throws Exception {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(this.hostname, this.port);
        ftpClient.setControlKeepAliveTimeout(60);
        ftpClient.login(this.username, this.password);
        if (this.passiveModeConf) {
            ftpClient.enterLocalPassiveMode();
        }
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        return ftpClient;
    }

    @Override
    public PooledObject<FTPClient> wrap(FTPClient ftpClient) {
        return new DefaultPooledObject<>(ftpClient);
    }
}
