package cn.faury.fdk.mybatis.config;

import cn.faury.fdk.common.utils.AssertUtil;
import cn.faury.fdk.mybatis.anotation.AutoScannedMapper;
import cn.faury.fdk.mybatis.dao.CommonDao;
import cn.faury.fdk.mybatis.dao.impl.CommonDaoImpl;
import cn.faury.fdk.mybatis.page.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.config.AnnotationDrivenBeanDefinitionParser;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Mybaits自动装配
 */
@Configuration
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class FdkMybatisAutoConfiguration {
    // 日志记录器
    private final Logger logger = LoggerFactory.getLogger(FdkMybatisAutoConfiguration.class);

    // 数据源（外部提供，必须有，此次用false只是关闭语法提示错误）
    @Autowired(required = false)
    private DataSource dataSource;

    // 数据库类型
    @Value("${fdk.mybatis.databaseType:mysql}")
    private String databaseType;

    // mapper基类
    @Value("${fdk.mybatis.basePackage:}")
    private String basePackage;

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean() {
        logger.debug("{}", "=====开始初始化SqlSessionFactoryBean");
        logger.debug("{}", "databaseType=" + databaseType);
        logger.debug("{}", "dataSource=" + dataSource);

        AssertUtil.notNull(dataSource,"缺少数据源[dataSource]支持，初始化失败");
        AssertUtil.notEmpty(databaseType,"数据库类型[databaseType]不可以为空");

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        // 装配插件
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("databaseType", databaseType);
        pageInterceptor.setProperties(properties);
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageInterceptor});
        logger.debug("{}", "=====完成初始化SqlSessionFactoryBean");
        return sqlSessionFactoryBean;
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate() {
        try {
            return new SqlSessionTemplate(sqlSessionFactoryBean().getObject());
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * Mapper自动扫描注册
     */
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        logger.debug("{}", "=====开始初始化MapperScannerConfigurer");
        logger.debug("{}", "basePackage=" + basePackage);
        AssertUtil.notEmpty(basePackage,"Mapper扫描的基类[fdk.mybatis.basePackage]不可以为空");

        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setAnnotationClass(AutoScannedMapper.class);
        mapperScannerConfigurer.setSqlSessionTemplateBeanName("sqlSessionTemplate");
        mapperScannerConfigurer.setBasePackage(basePackage);
        logger.debug("{}", "=====完成初始化MapperScannerConfigurer");
        return mapperScannerConfigurer;
    }

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 通用数据库操作器
     */
    @Bean
    public CommonDao commonDao() {
        CommonDaoImpl commonDao = new CommonDaoImpl();
        commonDao.setSqlSessionTemplate(sqlSessionTemplate());
        return commonDao;
    }
}