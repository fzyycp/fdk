package cn.faury.fdk.common.utils;

import cn.faury.fdk.common.anotation.SerializeDate;
import cn.faury.fdk.common.anotation.SerializeExpose;
import cn.faury.fdk.common.anotation.SerializeHtmlEscaping;
import cn.faury.fdk.common.anotation.SerializeNulls;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.*;

public class JsonUtilTest {

    @Test
    public void jsonToObject1() throws Exception {
        String json = "{\"name\":\"faury\",\"age\":18}";
        Type type = (new TypeToken<Map<String, Object>>() {
        }).getType();
        Map<String, Object> map = JsonUtil.jsonToObject(json, type);
        System.out.println(map);
    }

    @Test
    public void mapToBean() throws Exception {
        Map<String, Object> o = new HashMap<>();
        o.put("name", "faury");
        o.put("age", 18);
        User user = JsonUtil.mapToObject(o, User.class);

        Assert.assertNotNull("fail: object is null", user);
        Assert.assertTrue("fail: object error", "faury".equals(user.name));
        Assert.assertTrue("fail: object error", 18 == user.age);
    }

    @Test
    public void jsonToMap() throws Exception {
        String json = "{\"name\":\"faury\",\"age\":18}";
        Map<String, Object> map = JsonUtil.jsonToMap(json);
        System.out.println(map);
    }

    @Test
    public void jsonToObject() throws Exception {
        String json = "{\"name\":\"faury\",\"age\":18}";
        Map<String, Object> map = JsonUtil.jsonToObject(json, Map.class);
        Assert.assertNotNull("fail: map is null", map);
        Assert.assertTrue("fail: map error", "faury".equals(map.get("name")));
        Assert.assertTrue("fail: map error", 18 == (Double) map.get("age"));

        User user = JsonUtil.jsonToObject(json, User.class);
        Assert.assertNotNull("fail: object is null", user);
        Assert.assertTrue("fail: object error", "faury".equals(user.name));
        Assert.assertTrue("fail: object error", 18 == user.age);

    }

    @Test
    public void objectToJson() throws Exception {
        Map<String, Object> o = new HashMap<>();
        o.put("name", "faury");
        o.put("age", 18);
        Assert.assertTrue("fail: json not equal map", "{\"name\":\"faury\",\"age\":18}".equals(JsonUtil.objectToJson(o)));

        User user = new User("faury", 18);
        user.date = DateUtil.parse("2018-01-02 03:04:05");
        System.out.println(JsonUtil.objectToJson(user));
        Assert.assertTrue("fail: json not equal user", "{\"name\":\"faury\",\"age\":18,\"date\":\"2018-01-02 03:04:05\"}".equals(JsonUtil.objectToJson(user)));

        List<User> users = new ArrayList<>();
        users.add(user);
        Assert.assertTrue("fail: json not equal list", "[{\"name\":\"faury\",\"age\":18,\"date\":\"2018-01-02 03:04:05\"}]".equals(JsonUtil.objectToJson(users)));
    }

    @Test
    public void objectToJsonDepth() throws Exception {
        Map<String, Object> o2 = new HashMap<>();
        o2.put("name", "faury");
        Map<String, Object> o3 = new HashMap<>();
        o3.put("name", "faury3");
        o2.put("map", o3);
        Map<String, Object> o4 = new HashMap<>();
        o4.put("name", "faury4");
        o3.put("map", o4);
        Map<String, Object> o5 = new HashMap<>();
        o5.put("name", "faury5");
        o4.put("map", o5);
        Map<String, Object> o6 = new HashMap<>();
        o6.put("name", "faury6");
        o5.put("map", o6);
        Map<String, Object> o7 = new HashMap<>();
        o7.put("name", "faury7");
        o6.put("map", o7);
        Map<String, Object> o8 = new HashMap<>();
        o8.put("name", "faury8");
        o7.put("map", o8);
        Map<String, Object> o9 = new HashMap<>();
        o9.put("name", "faury9");
        o8.put("map", o9);
        Map<String, Object> o10 = new HashMap<>();
        o10.put("name", "faury10");
        o9.put("map", o10);
        System.out.println(JsonUtil.objectToJson(o2));
    }

    @Test
    public void objectToJsonNullDate() throws Exception {
        UserNullDate user = new UserNullDate("faury", 18);
        System.out.println(JsonUtil.objectToJson(user));
        Assert.assertTrue("fail: json not equal user", "{\"name\":\"faury\",\"age\":18,\"date\":null}".equals(JsonUtil.objectToJson(user)));
        user.date = DateUtil.parse("2018-01-02");
        System.out.println(JsonUtil.objectToJson(user));
        Assert.assertTrue("fail: json not equal user", "{\"name\":\"faury\",\"age\":18,\"date\":\"2018-01-02\"}".equals(JsonUtil.objectToJson(user)));
    }

    @Test
    public void objectToJsonHtmlExpose() throws Exception {
        UserHtml user = new UserHtml("<h1>faury</h1>", "notExpose");
        System.out.println(JsonUtil.objectToJson(user));
        Assert.assertTrue("fail: json not equal escaping html user", "{\"html\":\"\\u003ch1\\u003efaury\\u003c/h1\\u003e\"}".equals(JsonUtil.objectToJson(user)));

        UserHtml2 user2 = new UserHtml2("<h1>faury</h1>");
        System.out.println(JsonUtil.objectToJson(user2));
        Assert.assertTrue("fail: json not equal html user", "{\"html\":\"<h1>faury</h1>\"}".equals(JsonUtil.objectToJson(user2)));
    }

    class User implements Serializable {
        String name;
        int age;
        Date date;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
            this.date = null;
        }
    }
    @SerializeNulls
    @SerializeDate(DateUtil.FORMAT_DATE)
    class UserNullDate implements Serializable {
        String name;
        int age;
        Date date;

        public UserNullDate(String name, int age) {
            this.name = name;
            this.age = age;
            this.date = null;
        }
    }

    @SerializeHtmlEscaping
    @SerializeExpose
    class UserHtml implements Serializable{
        @Expose
        String html;
        String notExpose;

        public UserHtml(String html, String notExpose) {
            this.html = html;
            this.notExpose = notExpose;
        }
    }
    @SerializeHtmlEscaping(false)
    class UserHtml2 implements Serializable{
        String html;

        public UserHtml2(String html) {
            this.html = html;
        }
    }
}