package cn.faury.fdk.common.utils;

import org.junit.Test;

public class PathUtilTest {
    @Test
    public void getWebRootPath() throws Exception {
        System.out.println(PathUtil.getWebRootPath());
    }

    @Test
    public void getPackagePath() throws Exception {
        System.out.println(PathUtil.getPackagePath(null));
        System.out.println(PathUtil.getPackagePath(this));
    }

    @Test
    public void getRootClassPath() throws Exception {
        System.out.println(PathUtil.getClassRootPath());
    }

    @Test
    public void getPath() throws Exception {
        System.out.println(PathUtil.getPath((Class)null));
        System.out.println(PathUtil.getPath(PathUtil.class));
    }

    @Test
    public void getPath1() throws Exception {
        System.out.println(PathUtil.getPath((Object)null));
        System.out.println(PathUtil.getPath(this));
    }

}