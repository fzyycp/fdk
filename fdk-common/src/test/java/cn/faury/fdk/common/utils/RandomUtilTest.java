package cn.faury.fdk.common.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class RandomUtilTest {
    @Test
    public void getRandomNumber() throws Exception {
        int i=100;
        while (i-->0){
            System.out.println(RandomUtil.getRandomNumber(3));
        }
    }

    @Test
    public void getUUIDString() throws Exception {
        System.out.println(RandomUtil.getUUIDString());
    }

    @Test
    public void getDigestString() throws Exception {
        Assert.assertTrue("4f4dab7a1ea61e58acb63592d13cd99c".equals(RandomUtil.getDigestString("faury",RandomUtil.ALGORITHM_MD5)));
    }

    @Test
    public void getMD5String() throws Exception {
        Assert.assertTrue("4f4dab7a1ea61e58acb63592d13cd99c".equals(RandomUtil.getMD5String("faury")));
    }

    @Test
    public void getSHA256String() throws Exception {
        Assert.assertTrue("12cd87f02af916c9d0d706f24817ad22519498bbdb4d5e83ecb5166271af63c6".equals(RandomUtil.getSHA256String("faury")));
    }

}