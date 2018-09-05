package cn.faury.fdk.mybatis.autoconfigure;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.junit.Test;

public class JasyptTest {

    // 加密算法
    public static final String algorithm = "PBEWithMD5AndDES";
    // 项目配置的加密种子
    public static final String salt = "C5hFGlINY7";


    // 加密
    @Test
    public void jasyptPBEStringEncryptionTest(){
        // 输入原文
        String url="jdbc:mysql://128.0.9.252:3306/eb";
        String username="fanyc";
        String password = "#EDCrfv4";

        System.out.println(url + " : " + encrypt(url));
        System.out.println(username + " : " + encrypt(username));
        System.out.println(password + " : " + encrypt(password));
    }

    // 解密
    @Test
    public void jasyptPBEStringDecryptionTest(){
        // 输入密文
        String url="JZkH5bu2/qQpVokeEyZbsmZ0bFuuBzlUnm7e54uKRJxbx2Ue9dW0kYvdY3y1JOXiWUqJGDNRiNfpUj8QzVl1KWMtXYmR8uAlUV0FB0hZ7sY=";
        String username="uK7aeM4tK/cXOksE1Yu0zA==";
        String password = "2xwWu5xTB5qFOI3OBRKfmA==";

        //解密
        System.out.println(url + " : " + decrypt(url));
        System.out.println(username + " : " + decrypt(username));
        System.out.println(password + " : " + decrypt(password));
    }

    private String  encrypt(String password){
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
        config.setAlgorithm(algorithm);
        config.setPassword(salt);
        encryptor.setConfig(config);
        return encryptor.encrypt(password);
    }
    private String decrypt(String encPassword){
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
        config.setAlgorithm(algorithm);
        config.setPassword(salt);
        encryptor.setConfig(config);
        return encryptor.decrypt(encPassword);
    }
}
