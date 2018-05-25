package cn.faury.fdk.common.utils;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilTest {
    @Test
    public void dealNull() throws Exception {
        Assert.assertTrue("fail:dealNull with null",StringUtil.EMPTY_STR.equals(StringUtil.dealNull((String)null)));
        Assert.assertTrue("fail:dealNull with string","abc".equals(StringUtil.dealNull("abc")));
    }

    @Test
    public void toInt() throws Exception {
        Assert.assertTrue("fail:toInt with null",1 == StringUtil.toInt((String)null,1));
        Assert.assertTrue("fail:toInt with abc",1 == StringUtil.toInt("abc",1));
        Assert.assertTrue("fail:toInt with 2",2 == StringUtil.toInt("2",1));
    }

    @Test
    public void toFloat() throws Exception {
        Assert.assertTrue("fail:toFloat with null",1F == StringUtil.toFloat((String)null,1F));
        Assert.assertTrue("fail:toFloat with abc",1F == StringUtil.toFloat("abc",1F));
        Assert.assertTrue("fail:toFloat with 2",2F == StringUtil.toFloat("2",1F));
    }

    @Test
    public void toBoolean() throws Exception {
        Assert.assertTrue("fail:toBoolean with null",false == StringUtil.toBoolean((String)null,false));
        Assert.assertTrue("fail:toBoolean with abc",false == StringUtil.toBoolean("abc",false));
        Assert.assertTrue("fail:toBoolean with 2",true == StringUtil.toBoolean("true",false));
    }

    @Test
    public void mergeDuplicateArray() throws Exception {
        Assert.assertArrayEquals("fail:mergeDuplicateArray with null",null,StringUtil.mergeDuplicateArray((String[])null));
        Assert.assertArrayEquals("fail:mergeDuplicateArray with a,b,c",new String[]{"a","b","c"},StringUtil.mergeDuplicateArray(new String[]{"a","b","b","c","a"}));
    }

    @Test
    public void firstCharToLowerCase() throws Exception {
        Assert.assertTrue("fail:firstCharToLowerCase with null",null==StringUtil.firstCharToLowerCase((String)null));
        Assert.assertTrue("fail:firstCharToLowerCase with Faury","faury".equals(StringUtil.firstCharToLowerCase("faury")));
    }

    @Test
    public void firstCharToUpperCase() throws Exception {
        Assert.assertTrue("fail:firstCharToUpperCase with null",null==StringUtil.firstCharToUpperCase((String)null));
        Assert.assertTrue("fail:firstCharToUpperCase with faury","Faury".equals(StringUtil.firstCharToUpperCase("Faury")));
    }

    @Test
    public void isEmpty() throws Exception {
        Assert.assertTrue("fail:isEmpty with null",true==StringUtil.isEmpty((String)null));
        Assert.assertTrue("fail:isEmpty with faury",false == StringUtil.isEmpty("Faury"));
    }

    @Test
    public void isNotEmpty() throws Exception {
        Assert.assertTrue("fail:isNotEmpty with null",false==StringUtil.isNotEmpty((String)null));
        Assert.assertTrue("fail:isNotEmpty with faury",true == StringUtil.isNotEmpty("Faury"));
    }

    @Test
    public void isNotEmpty1() throws Exception {
        Assert.assertTrue("fail:isNotEmpty with null,\"\"",false==StringUtil.isNotEmpty((String)null,"faury"));
        Assert.assertTrue("fail:isNotEmpty with faury,fau",true == StringUtil.isNotEmpty("Faury","fau"));
    }

    @Test
    public void isEmpty1() throws Exception {
        Assert.assertTrue("fail:isEmpty with null,\"\"",true==StringUtil.isEmpty((String)null,"faury"));
        Assert.assertTrue("fail:isEmpty with faury,fau",false == StringUtil.isEmpty("Faury","fau"));
        Assert.assertTrue("fail:isEmpty with faury,fau",true == StringUtil.isEmpty("",(String)null));
    }

    @Test
    public void emptyReplace() throws Exception {
        Assert.assertTrue("fail:emptyReplace with null","faury".equals(StringUtil.emptyReplace((String)null,"faury")));
        Assert.assertTrue("fail:emptyReplace with \"\"","faury".equals(StringUtil.emptyReplace("","faury")));
        Assert.assertTrue("fail:emptyReplace with abc","abc".equals(StringUtil.emptyReplace("abc","faury")));
    }

}