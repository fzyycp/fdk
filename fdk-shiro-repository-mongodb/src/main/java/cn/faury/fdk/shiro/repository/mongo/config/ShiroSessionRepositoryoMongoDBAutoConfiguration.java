package cn.faury.fdk.shiro.repository.mongo.config;

import cn.faury.fdk.common.utils.StringUtil;
import cn.faury.fdk.shiro.repository.mongo.MongoDBShiroSessionRepository;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroSessionRepositoryoMongoDBAutoConfiguration {

    // 服务器地址
    @Value("${fdk.shiro.repository.mongodb.host:127.0.0.1}")
    private String host;
    // 服务端口号
    @Value("${fdk.shiro.repository.mongodb.port:27017}")
    private int port;
    // 服务器登录名
    @Value("${fdk.shiro.repository.mongodb.username:}")
    private String username;
    // 服务器密码
    @Value("${fdk.shiro.repository.mongodb.password:}")
    private String password;
    // 数据库名
    @Value("${fdk.shiro.repository.mongodb.dbName:}")
    private String dbName;
    // 集合名
    @Value("${fdk.shiro.repository.mongodb.collName:}")
    private String collName;

    @Bean
    public MongoDBShiroSessionRepository shiroSessionRepository() {
        MongoClient mongoClient;
        if(StringUtil.isEmpty(username,password)){
            mongoClient = new MongoClient(host,port);
        } else {
            String uri = String.format("mongodb://%s:%s@%s:%s", username, password, host, port);
            mongoClient = new MongoClient(new MongoClientURI(uri));
        }
        return new MongoDBShiroSessionRepository(mongoClient, dbName, collName);
    }
}
