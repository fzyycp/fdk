package cn.faury.fdk.datasource.dbcp.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class FdkDataSourceDbcpAutoConfiguration {
    // 日志类
    private final Logger logger = LoggerFactory.getLogger(FdkDataSourceDbcpAutoConfiguration.class);

    @Value("${fdk.datasource.dbcp.driverClassName:}")
    private String driverClassName;
    @Value("${fdk.datasource.dbcp.url:127.0.0.1}")
    private String url;
    @Value("${fdk.datasource.dbcp.username:}")
    private String username;
    @Value("${fdk.datasource.dbcp.password:}")
    private String password;
    @Value("${fdk.datasource.dbcp.initialSize:5}")
    private int initialSize;
    @Value("${fdk.datasource.dbcp.maxTotal:10}")
    private int maxTotal;
    @Value("${fdk.datasource.dbcp.minIdle:5}")
    private int minIdle;
    @Value("${fdk.datasource.dbcp.maxIdle:5}")
    private int maxIdle;
    @Value("${fdk.datasource.dbcp.maxWait:8000}")
    private long maxWait;

    @Value("${fdk.datasource.dbcp.removeAbandoned:true}")
    private boolean removeAbandoned;
    @Value("${fdk.datasource.dbcp.testWhileIdle:true}")
    private boolean testWhileIdle;
    @Value("${fdk.datasource.dbcp.timeBetweenEvictionRunsMillis:60000}")
    private int timeBetweenEvictionRunsMillis;
    @Value("${fdk.datasource.dbcp.numTestsPerEvictionRun:10}")
    private int numTestsPerEvictionRun;
    @Value("${fdk.datasource.dbcp.validationQuery:select 1}")
    private String validationQuery;
    @Value("${fdk.datasource.dbcp.validationQueryTimeout:1}")
    private int validationQueryTimeout;

    @Bean
    public BasicDataSource dataSource() {
        logger.debug("{}", "=====开始初始化dataSource");
        logger.debug("{}", "driverClassName=" + driverClassName);
        logger.debug("{}", "url=" + url);
        logger.debug("{}", "username=" + username);
        logger.debug("{}", "password=" + password);
        logger.debug("{}", "initialSize=" + initialSize);
        logger.debug("{}", "maxTotal=" + maxTotal);
        logger.debug("{}", "minIdle=" + minIdle);
        logger.debug("{}", "maxIdle=" + maxIdle);
        logger.debug("{}", "maxWait=" + maxWait);
        logger.debug("{}", "removeAbandoned=" + removeAbandoned);
        logger.debug("{}", "testWhileIdle=" + testWhileIdle);
        logger.debug("{}", "timeBetweenEvictionRunsMillis=" + timeBetweenEvictionRunsMillis);
        logger.debug("{}", "numTestsPerEvictionRun=" + numTestsPerEvictionRun);
        logger.debug("{}", "validationQuery=" + validationQuery);
        logger.debug("{}", "validationQueryTimeout=" + validationQueryTimeout);

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setInitialSize(initialSize);
        dataSource.setMaxTotal(maxTotal);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxIdle(maxIdle);
        dataSource.setMaxWaitMillis(maxWait);

        dataSource.setRemoveAbandonedOnBorrow(removeAbandoned);
        dataSource.setTestWhileIdle(testWhileIdle);
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        dataSource.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setValidationQueryTimeout(validationQueryTimeout);

        logger.debug("{}", "=====完成初始化dataSource");
        return dataSource;
    }
}
