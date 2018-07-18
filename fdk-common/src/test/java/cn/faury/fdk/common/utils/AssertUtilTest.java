package cn.faury.fdk.common.utils;

import cn.faury.fdk.common.exception.TipsException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AssertUtilTest {

    @Test
    public void assertTrue() throws Exception {
        AssertUtil.assertTrue(true,new RuntimeException("这个异常不会出现"));
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("这个异常会出现");
        AssertUtil.assertTrue(false,new IllegalArgumentException("这个异常会出现"));
    }

    @Test
    public void assertTrue1() throws Exception {
        AssertUtil.assertTrue(true,"500","这个异常不会出现");
        thrown.expect(TipsException.class);
        thrown.expectMessage("这个异常会出现(code=500)");
        AssertUtil.assertTrue(false,"500","这个异常会出现");
    }

    @Test
    public void assertTrue2() throws Exception {
        AssertUtil.assertTrue(true,"这个异常不会出现");
        thrown.expect(TipsException.class);
        thrown.expectMessage("这个异常会出现(code=402)");
        AssertUtil.assertTrue(false,"这个异常会出现");
    }

    @Test
    public void assertFalse() throws Exception {
        AssertUtil.assertFalse(false,new RuntimeException("这个异常不会出现"));
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("这个异常会出现");
        AssertUtil.assertFalse(true,new IllegalArgumentException("这个异常会出现"));
    }

    @Test
    public void assertFalse1() throws Exception {
        AssertUtil.assertFalse(false,"500","这个异常不会出现");
        thrown.expect(TipsException.class);
        thrown.expectMessage("这个异常会出现(code=500)");
        AssertUtil.assertFalse(true,"500","这个异常会出现");
    }

    @Test
    public void assertFalse2() throws Exception {
        AssertUtil.assertFalse(false,"这个异常不会出现");
        thrown.expect(TipsException.class);
        thrown.expectMessage("这个异常会出现(code=402)");
        AssertUtil.assertFalse(true,"这个异常会出现");
    }

    @Test
    public void assertNotNull() throws Exception {
        AssertUtil.assertNotNull(new Object(),"这个异常不会出现");
        thrown.expect(TipsException.class);
        thrown.expectMessage("这个异常会出现(code=402)");
        AssertUtil.assertNotNull(null,"这个异常会出现");
    }

    @Test
    public void assertNotNull1assaaaaasdas2d() throws Exception {
        AssertUtil.assertNotNull(new Object(),"500","这个异常不会出现");
        thrown.expect(TipsException.class);
        thrown.expectMessage("这个异常会出现(code=500)");
        AssertUtil.assertNotNull(null,"500","这个异常会出现");
    }

    @Test
    public void assertNotEmpty() throws Exception {
        AssertUtil.assertNotEmpty("string","这个异常不会出现");
        thrown.expect(TipsException.class);
        thrown.expectMessage("这个异常会出现(code=402)");
        AssertUtil.assertNotEmpty("","这个异常会出现");
    }

    @Test
    public void assertNotEmpty1() throws Exception {
        AssertUtil.assertNotEmpty("string","500","这个异常不会出现");
        thrown.expect(TipsException.class);
        thrown.expectMessage("这个异常会出现(code=500)");
        AssertUtil.assertNotEmpty("","500","这个异常会出现");
    }
    @Test
    public void assertEmpty() throws Exception {
        AssertUtil.assertEmpty("","这个异常不会出现");
        thrown.expect(TipsException.class);
        thrown.expectMessage("这个异常会出现(code=402)");
        AssertUtil.assertEmpty("string","这个异常会出现");
    }

    @Test
    public void assertEmpty1() throws Exception {
        AssertUtil.assertEmpty("","500","这个异常不会出现");
        thrown.expect(TipsException.class);
        thrown.expectMessage("这个异常会出现(code=500)");
        AssertUtil.assertEmpty("string","500","这个异常会出现");
    }

    @Rule
    public ExpectedException thrown= ExpectedException.none();

}