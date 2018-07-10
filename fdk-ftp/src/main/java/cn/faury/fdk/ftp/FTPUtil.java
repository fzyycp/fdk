package cn.faury.fdk.ftp;

import java.io.*;
import java.util.Calendar;
import java.util.Date;


import cn.faury.fdk.common.utils.PropertiesUtil;
import cn.faury.fdk.common.utils.StringUtil;
import cn.faury.fdk.ftp.constants.UploadStatus;
import cn.faury.fdk.ftp.pool.FTPPool;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * FTP工具类
 */
public class FTPUtil {

    /**
     * 日志记录器
     */
    private static Logger log = LoggerFactory.getLogger(FTPUtil.class);

    /**
     * 连接池
     */
    private static FTPPool pool = null;

    /**
     * 初始化
     *
     * @param configFile 配置文件路径
     */
    public static void init(File configFile) {
        log.error("{}", "=====初始化FTP缓冲池组件 Start=====");
        if (pool == null) {
            if (configFile == null || !configFile.exists()) {
                log.error("{}", "》配置文件不可以为空=====");
                return;
            }
            synchronized (FTPUtil.class) {
                if (pool == null) {
                    log.error("》开始读取配置文件[{}]=====", configFile);
                    // 初始化配置文件
                    PropertiesUtil cfg = PropertiesUtil.createPropertyInstance(configFile);
                    // 验证正确性
                    if (StringUtil.isEmpty(cfg.getProperty("ftp.hostname"), cfg.getProperty("ftp.port"),
                            cfg.getProperty("ftp.username"), cfg.getProperty("ftp.password"))) {
                        log.error("Properties file [{}] error", configFile);
                        throw new RuntimeException("Properties file [ftppool.properties] error");
                    }

                    String hostname = cfg.getProperty("ftp.hostname");
                    int port = cfg.getPropertyToInt("ftp.port");
                    String username = cfg.getProperty("ftp.username");
                    String password = cfg.getProperty("ftp.password");
                    boolean passiveModeConf = cfg.getPropertyToBoolean("ftp.passiveModeConf", true);

                    log.error("》初始化账户参数：{}",
                            String.format("{hostname=%s,port=%s,username=%s,password=***,passiveModeConf=%s}",
                                    hostname, port, username, passiveModeConf));

                    GenericObjectPoolConfig config = new GenericObjectPoolConfig();
                    // 能从池中借出的对象的最大数目。如果这个值不是正数，表示没有限制。
                    config.setMaxTotal(cfg.getPropertyToInt("ftp.pool.maxActive", 10));
                    /*
                     * 在池中借出对象的数目已达极限的情况下，调用它的borrowObject方法时的行为。 可以选用的值有：
					 * GenericObjectPool.WHEN_EXHAUSTED_BLOCK，表示等待
					 * GenericObjectPool
					 * .WHEN_EXHAUSTED_GROW，表示创建新的实例（maxActive参数失去意义）；
					 * GenericObjectPool
					 * .WHEN_EXHAUSTED_FAIL，表示抛出一个java.util.NoSuchElementException异常
					 * 。
					 */
                    config.setBlockWhenExhausted(true);
                    // 若在对象池空时调用borrowObject方法的行为被设定成等待，最多等待多少毫秒。
                    config.setMaxWaitMillis(cfg.getPropertyToInt("ftp.pool.maxWait", 30000));
                    config.setMaxIdle(cfg.getPropertyToInt("ftp.pool.maxIdle", 10));
                    config.setMinIdle(cfg.getPropertyToInt("ftp.pool.minIdle", 5));
                    // 设定在借出对象时是否进行有效性检查。
                    config.setTestOnBorrow(cfg.getPropertyToBoolean("ftp.pool.testOnBorrow", true));
                    // 设定在还回对象时是否进行有效性检查。
                    config.setTestOnReturn(cfg.getPropertyToBoolean("ftp.pool.testOnReturn", true));
                    // 设定间隔每过多少毫秒进行一次后台对象清理的行动。如果不是正数，则不进行清理。
                    config.setTimeBetweenEvictionRunsMillis(cfg.getPropertyToInt("ftp.pool.timeBetweenEvictionRunsMillis", 30000));
                    // 设定在进行后台对象清理时，每次检查几个对象。
                    config.setNumTestsPerEvictionRun(cfg.getPropertyToInt("ftp.pool.numTestsPerEvictionRun", 5));
                    // 设定在进行后台对象清理时，视休眠时间超过了多少毫秒的对象为过期。过期的对象将被回收。
                    config.setMinEvictableIdleTimeMillis(cfg.getPropertyToInt("ftp.pool.minEvictableIdleTimeMillis", 60000));
                    // 设定在进行后台对象清理时，是否还对没有过期的池内对象进行有效性检查。
                    config.setTestWhileIdle(cfg.getPropertyToBoolean("ftp.pool.testWhileIdle", false));
                    config.setSoftMinEvictableIdleTimeMillis(cfg.getPropertyToInt("ftp.pool.softMinEvictableIdleTimeMillis", 60000));
                    // 先进先出
                    config.setLifo(cfg.getPropertyToBoolean("ftp.pool.lifo", true));

                    log.error("》初始化池参数：{}",
                            String.format(
                                    "{maxActive=%s,whenExhaustedAction=%s,maxWait=%s,testOnBorrow=%s,testOnReturn=%s}",
                                    config.getMaxTotal(), config.getBlockWhenExhausted(), config.getMaxWaitMillis(), config.getTestOnBorrow(),
                                    config.getTestOnReturn()));

                    pool = new FTPPool(config, hostname, port, username, password, passiveModeConf);
                    log.error("{}", "》FTP缓冲池组件初始化 Success=====");
                } else {
                    log.error("{}", "》FTP缓冲池组件已经存在无需重复初始化=====");
                }
            }
        } else {
            log.error("{}", "》FTP缓冲池组件已经存在无需重复初始化=====");
        }
        log.error("{}", "=====初始化FTP缓冲池组件 Finish=====");
    }

    /**
     * 销毁
     */
    public static void destory() {
        log.error("{}", "=====销毁FTP缓冲池组件 Start=====");
        if (pool != null) {
            pool.destory();
        }
        log.error("{}", "=====销毁FTP缓冲池组件 End=====");
    }

    /**
     * 初始化Check
     */
    protected static void checkInit() {
        if (pool == null) {
            log.debug("{}", "checkInit error[FTPPool not init]!");
            throw new IllegalArgumentException("FTPPool not init！");
        }
    }

    /**
     * 获取FTP连接
     *
     * @return FTP连接
     */
    public static FTPClient borrowInstance() {
        checkInit();
        return pool.getResource();
    }

    /**
     * 归还FTP连接
     *
     * @return FTP连接
     */
    public static void returnInstance(FTPClient ins) {
        checkInit();
        pool.returnResource(ins);
    }

    /**
     * 获取服务器上某个文件的大小
     *
     * @param remote FTP服务器文件路径
     * @return 文件大小
     */
    public static int getRemoteFileSize(String remote) {
        // 去除第一个/,不允许从根目录开始
        if (StringUtil.isNotEmpty(remote) && remote.startsWith("/")) {
            remote = remote.substring(1);
        }

        FTPClient ftpClient = null;
        try {
            ftpClient = borrowInstance();
            FTPFile[] files = ftpClient.listFiles(remote);
            if (files.length < 1) {
                log.error("FTP获取文件大小失败->找不到文件：{},{}", remote, ftpClient.getReplyString());
                return 0;
            }
            Long size = files[0].getSize();
            return size.intValue();
        } catch (IOException ex) {
            log.error("FTP获取文件大小失败->获取文件出错：{},{}", remote, ftpClient.getReplyString(), ex);
            return 0;
        } finally {
            returnInstance(ftpClient);
        }
    }

    /**
     * 获取服务器上某个文件的时间
     *
     * @param remote FTP服务器文件路径
     * @return 文件最后修改日期
     */
    public static Date getRemoteFileDate(String remote) {
        // 去除第一个/,不允许从根目录开始
        if (StringUtil.isNotEmpty(remote) && remote.startsWith("/")) {
            remote = remote.substring(1);
        }

        FTPClient ftpClient = null;
        try {
            ftpClient = borrowInstance();
            FTPFile[] files = ftpClient.listFiles(remote);
            if (files.length < 1) {
                log.error("FTP获取文件时间失败->找不到文件：{},{}", remote, ftpClient.getReplyString());
                return null;
            }
            Calendar date = files[0].getTimestamp();
            return date.getTime();
        } catch (IOException ex) {
            log.error("FTP获取文件时间失败->获取文件出错：{},{}", remote, ftpClient.getReplyString(), ex);
            return null;
        } finally {
            returnInstance(ftpClient);
        }
    }

    /**
     * 上传文件到FTP服务器，支持断点续传
     *
     * @param request  请求
     * @param remote   目标文件路径，使用/home/directory1/subdirectory/file.ext
     *                 按照Linux上的路径指定方式，支持多级目录嵌套，支持递归创建不存在的目录结构
     * @param override 是否覆盖已存在文件
     * @return 上传结果
     */
    public static UploadStatus upload(HttpServletRequest request, String remote, boolean override) {
        UploadStatus result = UploadStatus.Upload_New_File_Failed;
        // 验证输入参数
        if (request != null) {
            try {
                result = upload(request.getInputStream(), remote, 0, override);
            } catch (IOException e) {
                log.error("{}", "FTP上传文件失败：读取文件流失败", e);
                result = UploadStatus.Upload_New_File_Failed;
            }
        } else {
            log.error("{}", "FTP上传文件失败：输入参数request为空");
        }
        return result;
    }

    /**
     * 上传文件到FTP服务器【如目标文件已存在则上传失败】
     *
     * @param request 请求
     * @param remote  目标文件路径，使用/home/directory1/subdirectory/file.ext
     *                按照Linux上的路径指定方式，支持多级目录嵌套，支持递归创建不存在的目录结构
     * @return 上传结果
     */
    public static UploadStatus upload(HttpServletRequest request, String remote) {
        return upload(request, remote, false);
    }

    /**
     * 以流的方式上传文件
     *
     * @param stream   文件流
     * @param remote   目标文件路径，使用/home/directory1/subdirectory/file.ext
     *                 按照Linux上的路径指定方式，支持多级目录嵌套，支持递归创建不存在的目录结构
     * @param offset   从流的哪个地方开始读取
     * @param override 是否覆盖
     * @return 上传结果
     */
    public static UploadStatus upload(InputStream stream, String remote, int offset, boolean override) {
        // 去除第一个/,不允许从根目录开始
        if (StringUtil.isNotEmpty(remote) && remote.startsWith("/")) {
            remote = remote.substring(1);
        }

        log.debug("开始上传文件到：{}", remote);
        FTPClient ftpClient = null;
        try {
            ftpClient = borrowInstance();
            if (_isFileExist(ftpClient, remote) && !override) {
                throw new RuntimeException("目标文件已存在");
            }
            UploadStatus result = UploadStatus.Upload_New_File_Failed;
            // 设置PassiveMode传输
            ftpClient.enterLocalPassiveMode();
            // 设置以二进制流的方式传输
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            // 对远程目录的处理
            if (remote.contains("/")) {
                if (!_createDir(ftpClient, remote)) {
                    result = UploadStatus.Create_Directory_Fail;
                    return result;
                }
            }
            // 检查远程是否存在文件
            FTPFile[] files = ftpClient.listFiles(remote);
            log.debug("检测目标文件是否存在：{}", files.length);
            if (files.length == 1) {
                log.debug("{}", "文件已存在，进行断点续传......");
                result = _breakPointUpload(ftpClient, stream, remote, offset);
            } else {
                log.debug("{}", "上传新文件......");
                if (ftpClient.storeFile(remote, stream)) {
                    log.debug("{}", "上传新文件成功");
                    result = UploadStatus.Upload_New_File_Success;
                } else {
                    log.debug("{}", "上传新文件失败");
                    result = UploadStatus.Upload_New_File_Failed;
                }
            }
            return result;
        } catch (RuntimeException | IOException ex) {
            log.error("FTP上传文件失败：{},{}", remote, ftpClient.getReplyString(), ex);
            return UploadStatus.Upload_New_File_Failed;
        } finally {
            log.debug("结束上传文件到：{}", remote);
            try {
                stream.close();
            } catch (IOException ignored) {
            }
            returnInstance(ftpClient);
        }
    }

    /**
     * 以流的方式上传文件【如目标文件已存在则上传失败】
     *
     * @param stream 文件流
     * @param remote 目标文件路径，使用/home/directory1/subdirectory/file.ext
     *               按照Linux上的路径指定方式，支持多级目录嵌套，支持递归创建不存在的目录结构
     * @param offset 从流的哪个地方开始读取
     * @return 上传结果
     */
    public static UploadStatus upload(InputStream stream, String remote, int offset) {
        return upload(stream, remote, offset, false);
    }

    /**
     * 以流的方式上传文件【如目标文件已存在则上传失败】
     *
     * @param stream 文件流
     * @param remote 目标文件路径，使用/home/directory1/subdirectory/file.ext
     *               按照Linux上的路径指定方式，支持多级目录嵌套，支持递归创建不存在的目录结构
     * @return 上传结果
     */
    public static UploadStatus upload(InputStream stream, String remote) {
        return upload(stream, remote, 0, false);
    }

    /**
     * 创建目录
     *
     * @param remote 远程文件目录
     * @return 是否成功
     */
    public static boolean createDir(String remote) {
        // 去除第一个/,不允许从根目录开始
        if (StringUtil.isNotEmpty(remote) && remote.startsWith("/")) {
            remote = remote.substring(1);
        }

        FTPClient ftpClient = null;
        try {
            ftpClient = borrowInstance();
            return _createDir(ftpClient, remote);
        } finally {
            returnInstance(ftpClient);
        }
    }

    /**
     * 从FTP服务器下载文件
     *
     * @param remote 远程文件路径。例如：ftp/301/App_1323/1040/eclipse-SDK-3.7-win32.zip
     *               第一个不用带“/”。这是跟上传的区别
     * @return 是否成功
     */
    public static byte[] download(String remote) {
        // 去除第一个/,不允许从根目录开始
        if (StringUtil.isNotEmpty(remote) && remote.startsWith("/")) {
            remote = remote.substring(1);
        }

        FTPClient ftpClient = null;
        try {
            ftpClient = borrowInstance();

            if (!_isFileExist(ftpClient, remote)) {
                throw new RuntimeException("目标文件不存在");
            }

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ftpClient.retrieveFile(remote, buffer);
            return buffer.toByteArray();
        } catch (RuntimeException | IOException ex) {
            log.error("FTP下载文件失败：{},{}", remote, ftpClient.getReplyString(), ex);
        } finally {
            returnInstance(ftpClient);
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return 是否成功
     */
    public static boolean deleteFile(String filePath) {
        // 去除第一个/,不允许从根目录开始
        if (StringUtil.isNotEmpty(filePath) && filePath.startsWith("/")) {
            filePath = filePath.substring(1);
        }

        FTPClient ftpClient = null;
        try {
            ftpClient = borrowInstance();
            if (!_isFileExist(ftpClient, filePath)) {
                throw new RuntimeException("目标文件不存在！");
            }
            return ftpClient.deleteFile(filePath);
        } catch (RuntimeException | IOException ex) {
            log.error("FTP删除文件失败：{},{}", filePath, ftpClient.getReplyString(), ex);
        } finally {
            returnInstance(ftpClient);
        }
        return false;
    }

    /**
     * 移动文件
     *
     * @param fromPath 将要移动的文件
     * @param toPath   移动至服务器的路径
     * @param override 是否覆盖
     * @return 是否成功
     */
    public static boolean moveFile(String fromPath, String toPath, boolean override) {
        // 去除第一个/,不允许从根目录开始
        if (StringUtil.isNotEmpty(fromPath) && fromPath.startsWith("/")) {
            fromPath = fromPath.substring(1);
        }
        // 去除第一个/,不允许从根目录开始
        if (StringUtil.isNotEmpty(toPath) && toPath.startsWith("/")) {
            toPath = toPath.substring(1);
        }

        FTPClient ftpClient = null;
        try {
            ftpClient = borrowInstance();

            // 验证源文件
            if (false == _isFileExist(ftpClient, fromPath)) {
                throw new RuntimeException("源文件不存在");
            }

            // 验证目标位置文件
            if (_isFileExist(ftpClient, toPath)) {
                if (override) {
                    ftpClient.deleteFile(toPath);
                } else {
                    throw new RuntimeException("目标文件已存在");
                }
            } else {
                // 创建目标目录
                _createDir(ftpClient, toPath);
            }

            return ftpClient.rename(fromPath, toPath);
        } catch (RuntimeException ex) {
            log.error("FTP移动文件失败：{} -> {},{}", fromPath, toPath, ftpClient.getReplyString(), ex);
        } catch (IOException e) {
            log.error("FTP移动文件失败：{} -> {},{}", fromPath, toPath, ftpClient.getReplyString(), e);
        } finally {
            returnInstance(ftpClient);
        }
        return false;
    }

    /**
     * 移动文件【目标文件存在则移动失败】
     *
     * @param fromPath 源路径
     * @param toPath   目标路径
     * @return 是否移动成功
     */
    public static boolean moveFile(String fromPath, String toPath) {
        return moveFile(fromPath, toPath, false);
    }

    /**
     * 复制文件
     *
     * @param fromPath 将要复制的文件路径
     * @param toPath   复制至服务器的路径
     * @param override 是否覆盖
     * @return 是否成功
     */
    public static boolean copyFile(String fromPath, String toPath, boolean override) {
        // 去除第一个/,不允许从根目录开始
        if (StringUtil.isNotEmpty(fromPath) && fromPath.startsWith("/")) {
            fromPath = fromPath.substring(1);
        }
        // 去除第一个/,不允许从根目录开始
        if (StringUtil.isNotEmpty(toPath) && toPath.startsWith("/")) {
            toPath = toPath.substring(1);
        }

        FTPClient ftpClient = null;
        try {
            ftpClient = borrowInstance();

            // 验证源文件
            if (false == _isFileExist(ftpClient, fromPath)) {
                throw new RuntimeException("源文件不存在");
            }

            // 验证目标位置文件
            if (_isFileExist(ftpClient, toPath)) {
                if (override) {
                    ftpClient.deleteFile(toPath);
                } else {
                    throw new RuntimeException("目标文件已存在");
                }
            } else {
                // 创建目标目录
                _createDir(ftpClient, toPath);
            }

            // 复制文件
            InputStream fromIS = ftpClient.retrieveFileStream(fromPath);
            if (false == ftpClient.completePendingCommand()) {
                throw new RuntimeException("读取源文件流异常");
            }
            if (fromIS == null) {
                throw new RuntimeException("读取源文件流为null");
            }

            return ftpClient.storeFile(toPath, fromIS);
        } catch (RuntimeException ex) {
            log.error("FTP复制文件失败：{} -> {},{}", fromPath, toPath, ftpClient.getReplyString(), ex);
        } catch (IOException e) {
            log.error("FTP复制文件失败：{} -> {},{}", fromPath, toPath, ftpClient.getReplyString(), e);
        } finally {
            returnInstance(ftpClient);
        }
        return false;
    }

    /**
     * 复制文件【目标文件存在则移动失败】
     *
     * @param fromPath 将要复制的源文件路径
     * @param toPath   复制至服务器的路径
     * @return 是否成功
     */
    public static boolean copyFile(String fromPath, String toPath) {
        return copyFile(fromPath, toPath, false);
    }

    /**
     * 打印缓冲池
     *
     * @return 缓冲池信息
     */
    public static String printPool() {
        if (pool == null) {
            return "null";
        } else {
            return String.format("{NumActive=%s,getNumIdle=%s}", pool.getNumActive(), pool.getNumIdle());
        }
    }

    /**
     * 断点续传文件
     *
     * @param ftpClient FTP连接
     * @param remote    远程地址
     * @param stream    文件流
     * @param offset    偏移量
     * @return 上传结果
     */
    private static UploadStatus _breakPointUpload(FTPClient ftpClient, InputStream stream, String remote, int offset) {
        try {
            log.debug("{}", "开始断点续传......");
            UploadStatus status = UploadStatus.Upload_From_Break_Success;
            OutputStream out = ftpClient.appendFileStream(remote);
            if (null == out) {
                log.debug("断点续传失败：{}", ftpClient.getReplyString());
                return UploadStatus.Upload_From_Break_Failed;
            }
            byte[] bytes = new byte[1024];
            int c;
            stream.skip(offset);
            while ((c = stream.read(bytes, 0, bytes.length)) != -1) {
                out.write(bytes, 0, c);
            }
            out.flush();
            out.close();
            boolean result = ftpClient.completePendingCommand();
            if (!result) {
                log.debug("断点续传失败：{}", ftpClient.getReplyString());
                return UploadStatus.Upload_From_Break_Failed;
            }
            log.debug("{}", "断点续传完成");
            return status;
        } catch (RuntimeException ex) {
            log.error("FTP上传文件失败：{},{}", remote, ftpClient.getReplyString(), ex);
            return UploadStatus.Upload_From_Break_Failed;
        } catch (IOException e) {
            log.error("FTP上传文件失败：{},{}", remote, ftpClient.getReplyString(), e);
            return UploadStatus.Upload_From_Break_Failed;
        }
    }

    private static int _gotoRootDir(FTPClient ftpClient) throws IOException {
        int res = ftpClient.cwd("/");
        log.debug("回到根目录：{}", ftpClient.getReplyString());
        return res;
    }

    /**
     * 创建目录
     *
     * @param ftpClient FTP连接
     * @param remote    目标地址
     * @return 是否成功
     */
    private static boolean _createDir(FTPClient ftpClient, String remote) {
        log.debug("准备创建目录：{}", remote);
        try {
            String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
            _gotoRootDir(ftpClient);
            if (!directory.equalsIgnoreCase("/") && !ftpClient.changeWorkingDirectory(directory)) {
                // 如果远程目录不存在，则递归创建远程服务器目录
                int start = 0;
                int end = 0;
                if (directory.startsWith("/")) {
                    start = 1;
                } else {
                    start = 0;
                }
                end = directory.indexOf("/", start);
                while (true) {
                    String subDirectory = remote.substring(start, end);
                    log.debug("进入目录：{}", subDirectory);
                    if (!ftpClient.changeWorkingDirectory(subDirectory)) {
                        log.debug("进入目录失败：{}{}", subDirectory, ftpClient.getReplyString());
                        int rs = ftpClient.mkd(subDirectory);
                        if (FTPReply.isPositiveCompletion(rs)) {
                            ftpClient.changeWorkingDirectory(subDirectory);
                        } else {
                            log.debug("创建目录失败:{},{}", subDirectory, ftpClient.getReplyString());
                            return false;
                        }
                    }

                    start = end + 1;
                    end = directory.indexOf("/", start);

                    // 检查所有目录是否创建完毕
                    if (end <= start) {
                        break;
                    }
                }
            }
            log.debug("创建目录成功：{}", directory);
            _gotoRootDir(ftpClient);
            return true;
        } catch (RuntimeException | IOException ex) {
            log.error("FTP创建目录失败：{},{}", remote, ftpClient.getReplyString(), ex);
            return false;
        }
    }

    /**
     * 目标文件是否存在
     *
     * @param ftpClient FTP连接
     * @param remote    目标路径
     * @return 是否存在
     */
    private static boolean _isFileExist(FTPClient ftpClient, String remote) {
        FTPFile[] files = new FTPFile[0];
        try {
            files = ftpClient.listFiles(remote);
        } catch (IOException e) {
            log.error("查看文件是否存在失败：{},{}", remote, ftpClient.getReplyString(), e);
        }
        return files.length > 0;
    }
}
