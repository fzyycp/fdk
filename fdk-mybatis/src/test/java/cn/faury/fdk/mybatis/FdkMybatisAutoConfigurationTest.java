package cn.faury.fdk.mybatis;

import cn.faury.fdk.mybatis.dao.CommonDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class FdkMybatisAutoConfigurationTest {

    @Autowired(required = false)
    CommonDao commonDao;

    @Autowired(required = false)
    DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(FdkMybatisAutoConfigurationTest.class,args);
    }

    @Test
    public void dataSource() throws Exception {
        System.out.println(dataSource);
    }

    @Test
    public void commonDao() throws Exception {
        System.out.println(commonDao);
    }

}