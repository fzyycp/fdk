package cn.faury.fdk.common.utils;

import org.junit.Assert;
import org.junit.Test;

public class VersionUtilTest {
    @Test
    public void isVersionNo() throws Exception {
        String ver = "1.0.3";
        Assert.assertTrue("fail: 1.0.3 is a version no",VersionUtil.isVersionNo(ver));
        ver = "1.0.3e";
        Assert.assertTrue("fail: 1.0.3e is not a version no",false==VersionUtil.isVersionNo(ver));
    }

    @Test
    public void compareVersionNo() throws Exception {
        String ver1 = "1.0";
        String ver2 = "1.0.0";
        Assert.assertTrue("fail: 1.0 != 1.0.0",0==VersionUtil.compareVersionNo(ver1,ver2));
        ver1 = "1.0";
        ver2 = "1.0.0.1";
        Assert.assertTrue("fail: 1.0 < 1.0.0.1",-1==VersionUtil.compareVersionNo(ver1,ver2));
        ver1 = "1.2";
        ver2 = "1.10";
        Assert.assertTrue("fail: 1.2 < 1.10",-1==VersionUtil.compareVersionNo(ver1,ver2));
        ver1 = "2";
        ver2 = "1.10";
        Assert.assertTrue("fail: 2 > 1.10",1==VersionUtil.compareVersionNo(ver1,ver2));
        ver1 = "2a";
        ver2 = "1.10";
        Assert.assertTrue("fail: 2a can not compare to 1.10",-2==VersionUtil.compareVersionNo(ver1,ver2));
    }


}