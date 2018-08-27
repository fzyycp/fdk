package cn.faury.fdk.captcha.autoconfigure;

import cn.faury.fdk.captcha.config.FdkCaptchaConfig;
import cn.faury.fdk.common.utils.JsonUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = FdkCaptchaProperties.PROPERTIES_PREFIX)
public class FdkCaptchaProperties extends FdkCaptchaConfig {
    /**
     * 配置文件前缀
     */
    public static final String PROPERTIES_PREFIX = "fdk.captcha";

    @Override
    public String toString() {
        return JsonUtil.objectToJson(this);
    }
}
