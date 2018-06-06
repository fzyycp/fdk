package cn.faury.fdk.mybatis;

import cn.faury.fdk.mybatis.config.FdkMybatisAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"cn.faury"})
public class FdkMybatisApplication {
    public static void main(String[] args) {
        SpringApplication.run(FdkMybatisAutoConfiguration.class, args);
    }
}
