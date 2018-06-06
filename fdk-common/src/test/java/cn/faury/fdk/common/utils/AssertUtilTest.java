package cn.faury.fdk.common.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AssertUtilTest {
    final int a = 10;

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Test
    public void check() throws Exception {
        AssertUtil.check(a > 0, "这个异常不会出现的");
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("这个异常时会出现的，因为a<100");
        AssertUtil.check(a > 10, "这个异常时会出现的，因为a<100");
    }

    @Test
    public void check1() throws Exception {
        AssertUtil.check(a > 0, "这个异常[%s]会出现的","不");
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("这个异常[是]会出现的");
        AssertUtil.check(a > 10, "这个异常[%s]会出现的","是");
    }

    @Test
    public void notNull() throws Exception {
        AssertUtil.notNull(new Object(),"这个异常不会出现的");
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("这个异常会出现的");
        AssertUtil.notNull(null,"这个异常会出现的");
    }

    @Test
    public void notEmpty() throws Exception {
        AssertUtil.notEmpty("a","这个异常不会出现的");
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("这个异常会出现的");
        AssertUtil.notEmpty("","这个异常会出现的");
    }

}