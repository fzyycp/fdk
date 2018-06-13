package cn.faury.fdk.mybatis.autoconfigure;

import cn.faury.fdk.mybatis.dao.CommonDao;
import cn.faury.fdk.mybatis.dao.impl.CommonDaoImpl;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;

/**
 * Mybaits自动装配
 */
@Configuration
@ConditionalOnClass({DataSource.class,SqlSessionTemplate.class})
@EnableTransactionManagement
public class FdkMybatisAutoConfiguration implements TransactionManagementConfigurer {

    // 数据源（外部提供，必须有，此次用false只是关闭语法提示错误）
    @Autowired
    private DataSource dataSource;

    /**
     * 通用数据库操作器
     */
    @Bean
    public CommonDao commonDao(SqlSessionTemplate sqlSessionTemplate) {
        CommonDaoImpl commonDao = new CommonDaoImpl();
        commonDao.setSqlSessionTemplate(sqlSessionTemplate);
        return commonDao;
    }

    /**
     * 事务支持
     */
    @Bean
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}