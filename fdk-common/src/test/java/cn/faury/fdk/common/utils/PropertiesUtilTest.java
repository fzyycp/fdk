package cn.faury.fdk.common.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class PropertiesUtilTest {

    @Test
    public void createWebPropertyInstance() throws Exception {
        // need web environment
        System.out.println(PathUtil.getWebRootPath());
    }

    @Test
    public void createPropertyInstance() throws Exception {
        PropertiesUtil configUtil = PropertiesUtil.createPropertyInstance("pro.properties");
        Assert.assertNotNull("fail: property config create null",configUtil);
    }

    @Test
    public void getProperty() throws Exception {
        PropertiesUtil configUtil = PropertiesUtil.createPropertyInstance("pro.properties");
        Assert.assertTrue("fail: property name is error","faury".equals(configUtil.getProperty("name")));
    }

    @Test
    public void getProperty1() throws Exception {
        PropertiesUtil configUtil = PropertiesUtil.createPropertyInstance("pro.properties");
        Assert.assertTrue("fail: property default value is error","notExist".equals(configUtil.getProperty("name1","notExist")));
    }

    @Test
    public void getPropertyToInt() throws Exception {
        PropertiesUtil configUtil = PropertiesUtil.createPropertyInstance("pro.properties");
        Assert.assertTrue("fail: property age is error",18==configUtil.getPropertyToInt("age"));
        Assert.assertNull("fail: property notExist is error",configUtil.getPropertyToInt("notExist"));
    }

    @Test
    public void getPropertyToInt1() throws Exception {
        PropertiesUtil configUtil = PropertiesUtil.createPropertyInstance("pro.properties");
        Assert.assertTrue("fail: property age is error",19==configUtil.getPropertyToInt("noExist",19));
    }

    @Test
    public void getPropertyToBoolean() throws Exception {
        PropertiesUtil configUtil = PropertiesUtil.createPropertyInstance("pro.properties");
        Assert.assertTrue("fail: property man is error",Boolean.TRUE==configUtil.getPropertyToBoolean("man"));
        Assert.assertNull("fail: property notExist is error",configUtil.getPropertyToBoolean("notExist"));
    }

    @Test
    public void getPropertyToBoolean1() throws Exception {
        PropertiesUtil configUtil = PropertiesUtil.createPropertyInstance("pro.properties");
        Assert.assertTrue("fail: property default value is error",Boolean.FALSE==configUtil.getPropertyToBoolean("noExist",Boolean.FALSE));
    }

    @Test
    public void getPropertyKeySet() throws Exception {
        PropertiesUtil configUtil = PropertiesUtil.createPropertyInstance("pro.properties");
        Set<String> keys = configUtil.getPropertyKeySet();
        System.out.println(keys);
        Assert.assertTrue(keys.size()==3);
    }

}