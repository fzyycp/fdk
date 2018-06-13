package cn.faury.fdk.shiro.repository.mongo.autoconfigure;

import cn.faury.fdk.common.utils.StringUtil;
import cn.faury.fdk.shiro.autoconfigure.FdkShiroAutoConfiguration;
import cn.faury.fdk.shiro.repository.mongo.MongoDBShiroSessionRepository;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@ConditionalOnClass(MongoDBShiroSessionRepository.class)
@AutoConfigureBefore(FdkShiroAutoConfiguration.class)
@EnableConfigurationProperties(ShiroSessionRepositoryMongoDBProperties.class)
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class ShiroSessionRepositoryMongoDBAutoConfiguration {

    @Autowired
    ShiroSessionRepositoryMongoDBProperties properties;

    @Bean
    public MongoDBShiroSessionRepository shiroSessionRepository() {
        MongoClient mongoClient;
        if (StringUtil.isEmpty(properties.getUsername(), properties.getPassword())) {
            mongoClient = new MongoClient(properties.getHost(), properties.getPort());
        } else {
            String uri = String.format("mongodb://%s:%s@%s:%s", properties.getUsername(), properties.getPassword()
                    , properties.getHost(), properties.getPort());
            mongoClient = new MongoClient(new MongoClientURI(uri));
        }
        return new MongoDBShiroSessionRepository(mongoClient, properties.getDbName(), properties.getCollName());
    }
}
