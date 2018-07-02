package cn.faury.fdk.ftp;

import cn.faury.fdk.ftp.constants.UploadStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.io.FileInputStream;
import java.util.Date;

public class FTPUtilTest {

    public static final String uploadLocalFile = "D:\\faury\\github\\fdk\\fdk-ftp\\src\\test\\resources\\testupload.txt";
    public static final String uploadRemotePath = "/ftpkit/test/testupload.txt";
    public static final String moveRemotePath = "/ftpkit/test/move/testupload.txt";

    @Before
    public void init() throws Exception {
        FTPUtil.init();
    }

    @After
    public void destory() throws Exception {
        FTPUtil.destory();
    }

//    @Test
    public void testAll() throws Exception {
        // 先删除文件
        FTPUtil.deleteFile(uploadRemotePath);
        FTPUtil.deleteFile(moveRemotePath);
        // 上传文件
        upload();
        // 获取文件大小和日期
        getRemoteFileSize();
        getRemoteFileDate();
        // 移动文件
        moveFile();
        // 复制回来
        copyFile();
        // 下载确认
        download();
    }

    public void upload() throws Exception {
        FileInputStream fileInputStream = new FileInputStream(uploadLocalFile);
        UploadStatus status = FTPUtil.upload(fileInputStream, uploadRemotePath,0,true);
        System.out.println(status);
        Assert.assertTrue(UploadStatus.Upload_New_File_Success == status || UploadStatus.Upload_From_Break_Success == status);
    }

    public void getRemoteFileSize() throws Exception {
        int size1 = FTPUtil.getRemoteFileSize(uploadRemotePath);
        System.out.println(String.format("s1=%s", size1));
    }


    public void getRemoteFileDate() throws Exception {
        Date date = FTPUtil.getRemoteFileDate(uploadRemotePath);
        System.out.println(date);
    }

    public void moveFile() throws Exception {
        boolean moved = FTPUtil.moveFile(uploadRemotePath, moveRemotePath);
        Assert.assertTrue(moved);
    }

    public void copyFile() throws Exception {
        boolean copyed = FTPUtil.copyFile(moveRemotePath, uploadRemotePath);
        Assert.assertTrue(copyed);
    }

    public void download() throws Exception {
        byte[] download = FTPUtil.download(uploadRemotePath);
        Assert.assertTrue(download.length > 0);
        String remote = new String(download);
        System.out.println(remote);
        download = FTPUtil.download(moveRemotePath);
        String move = new String(download);
        System.out.println(move);
        Assert.assertTrue(move.equals(remote));
    }

    public void printPool() throws Exception {
        System.out.println(FTPUtil.printPool());
    }
}