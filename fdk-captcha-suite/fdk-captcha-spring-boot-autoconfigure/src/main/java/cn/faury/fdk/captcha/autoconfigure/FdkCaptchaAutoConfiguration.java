package cn.faury.fdk.captcha.autoconfigure;

import cn.faury.fdk.captcha.FdkCaptcha;
import com.google.code.kaptcha.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@ConditionalOnClass(FdkCaptcha.class)
@EnableConfigurationProperties(FdkCaptchaProperties.class)
public class FdkCaptchaAutoConfiguration {
    // 日志
    private Logger logger = LoggerFactory.getLogger(FdkCaptchaAutoConfiguration.class);

    @Autowired
    FdkCaptchaProperties fdkCaptchaProperties;

    @Bean
    FdkCaptcha fdkCaptcha() {
        logger.debug("{}", "=====开始初始化FdkCaptcha=====");
        logger.debug("{}", "fdkCaptchaProperties=" + fdkCaptchaProperties);
        FdkCaptcha fdkCaptcha = new FdkCaptcha();
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", fdkCaptchaProperties.getBorder());
        properties.setProperty("kaptcha.border.color", fdkCaptchaProperties.getBorderColor());
        properties.setProperty("kaptcha.textproducer.font.color", fdkCaptchaProperties.getFontColor());
        properties.setProperty("kaptcha.image.width", fdkCaptchaProperties.getImageWidth());
        properties.setProperty("kaptcha.image.height", fdkCaptchaProperties.getImageHeight());
        properties.setProperty("kaptcha.session.key", fdkCaptchaProperties.getSessionKey());
        properties.setProperty("kaptcha.textproducer.char.length", fdkCaptchaProperties.getCharLength());
        properties.setProperty("kaptcha.textproducer.font.names", fdkCaptchaProperties.getFontNames());
        Config config = new Config(properties);
        fdkCaptcha.setConfig(config);
        logger.debug("{}", "=====完成初始化FdkCaptcha=====");
        return fdkCaptcha;
    }
}
