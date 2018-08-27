package cn.faury.fdk.pay.autoconfigure;

import cn.faury.fdk.pay.tenpay.TenpayConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FdkPayTencentProperties.class)
public class FdkPayAutoConfiguration {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired(required = false)
    private FdkPayTencentProperties fdkPayTencentProperties;

    @Bean
    public TenpayConfig tenpayConfig() {
        TenpayConfig tenpayConfig = new TenpayConfig();
        {
            logger.error("{}", "========== TenpayConfig Config Start ==========");
            try {
                TenpayConfig.mchID = fdkPayTencentProperties.getPartner();
                TenpayConfig.key = fdkPayTencentProperties.getKey();
                TenpayConfig.appID = fdkPayTencentProperties.getAppId();
                TenpayConfig.certLocalPath = fdkPayTencentProperties.getCertLocalPath();
                TenpayConfig.certPassword = fdkPayTencentProperties.getCertPassword();
                TenpayConfig.inputCharset = fdkPayTencentProperties.getInputCharset();
                TenpayConfig.notifyUrl = fdkPayTencentProperties.getNotifyUrl();
                logger.debug("mchID={},key={},appID={},certLocalPath={},certPassword=**,input_charset={},NOTIFY_URL={}"
                        , TenpayConfig.mchID, TenpayConfig.key, TenpayConfig.appID, TenpayConfig.certLocalPath, TenpayConfig.inputCharset, TenpayConfig.notifyUrl);
                logger.error("{}", "========== TenpayConfig Config Success ==========");
            } catch (RuntimeException e) {
                logger.error("{}", "========== TenpayConfig Config Exception ==========", e);
            } finally {
                logger.error("{}", "========== TenpayConfig Config End ==========");
            }
        }
        return tenpayConfig;
    }

}
