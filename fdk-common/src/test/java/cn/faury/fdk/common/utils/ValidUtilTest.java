package cn.faury.fdk.common.utils;

import org.junit.Assert;
import org.junit.Test;

public class ValidUtilTest {
    @Test
    public void isValidIDNo() throws Exception {
        Assert.assertTrue("fail: 123456199205243265 is not a ID no",ValidUtil.isValidIDNo("123456199205243263"));
    }

    @Test
    public void isValidProvinceCode() throws Exception {
        Assert.assertFalse("fail: 16 is not a province code", ValidUtil.isValidProvinceCode("16"));
        Assert.assertTrue("fail: 12 is a province code", ValidUtil.isValidProvinceCode("12"));
    }

    @Test
    public void isValidDate() throws Exception {
        Assert.assertFalse("fail: 20180229 is not a date", ValidUtil.isValidDate("20180229", DateUtil.FORMAT_DATE_SHORT));
        Assert.assertTrue("fail: 20180228 is a date", ValidUtil.isValidDate("20180228", DateUtil.FORMAT_DATE_SHORT));
    }

}