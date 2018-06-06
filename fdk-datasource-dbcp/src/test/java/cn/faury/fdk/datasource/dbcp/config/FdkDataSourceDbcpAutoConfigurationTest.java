package cn.faury.fdk.datasource.dbcp.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication
public class FdkDataSourceDbcpAutoConfigurationTest {
    @Autowired
    FdkDataSourceDbcpAutoConfiguration configuration;

    public static void main(String[] args) {
        SpringApplication.run(FdkDataSourceDbcpAutoConfigurationTest.class, args);
    }

    @Test
    public void dataSource() throws Exception {
        System.out.println(configuration);
    }

}