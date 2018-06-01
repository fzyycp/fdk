package cn.faury.fdk.common.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;

import static org.junit.Assert.*;

public class SerializeUtilTest implements Serializable {
    @Test
    public void serialize() throws Exception {
        User user = new User("faury",18);
        byte[] bytes = SerializeUtil.serialize(user);
        System.out.println(StringUtil.byteToBase64(bytes));
    }

    @Test
    public void deserialize() throws Exception {
        String base64="rO0ABXNyADBjbi5mYXVyeS5mZGsuY29tbW9uLnV0aWxzLlNlcmlhbGl6ZVV0aWxUZXN0JFVzZXJU3o5EM26C6gIAA0kAA2FnZUwABG5hbWV0ABJMamF2YS9sYW5nL1N0cmluZztMAAZ0aGlzJDB0AC1MY24vZmF1cnkvZmRrL2NvbW1vbi91dGlscy9TZXJpYWxpemVVdGlsVGVzdDt4cAAAABJ0AAVmYXVyeXNyACtjbi5mYXVyeS5mZGsuY29tbW9uLnV0aWxzLlNlcmlhbGl6ZVV0aWxUZXN0oytJ2spzHwYCAAB4cA==";
        User user = SerializeUtil.deserialize(StringUtil.base64ToByte(base64),User.class);
        System.out.println(JsonUtil.objectToJson(user));
        Assert.assertNotNull(user);

    }

    class User implements Serializable{
        String name;
        int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
}