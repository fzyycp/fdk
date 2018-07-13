package cn.faury.fdk.mobile;

import cn.faury.fdk.common.entry.RestResultCode;
import cn.faury.fdk.common.exception.TipsException;
import cn.faury.fdk.common.utils.StringUtil;
import cn.faury.fdk.mobile.core.FdkMobileInterfaceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;

import java.io.File;

/**
 * 手机框架初始化
 */
public class MobileFrameworkInitializer implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 日志记录器
     */
    private Logger log = LoggerFactory.getLogger(MobileFrameworkInitializer.class);
    // 默认的配置文件名
    public static final String DEFAULT_SERVICE_CONFIG_FILE = "fdk.mobile.service.properties";

    // 配置文件
    private String properties;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.error("{}","=====初始化手机接口管理器=====init");
        try {
            properties = StringUtil.emptyDefault(properties, DEFAULT_SERVICE_CONFIG_FILE);
            File configFile = (new ClassPathResource(properties)).getFile();
            // 初始化手机接口管理器
            FdkMobileInterfaceManager.init(event, configFile);
            log.error("{}","=====初始化手机接口管理器=====success");
        } catch (TipsException te) {
            throw te;
        } catch (Exception e) {
            log.error("{}","=====初始化手机接口管理器=====Exception", e);
            throw new TipsException(RestResultCode.CODE500.getCode(), "初始化框架异常");
        } finally {
            log.error("{}","=====初始化手机接口管理器=====finish");
        }
    }

    /**
     * 获取properties
     *
     * @return properties
     */
    public String getProperties() {
        return properties;
    }

    /**
     * 设置properties
     *
     * @param properties 值
     */
    public void setProperties(String properties) {
        this.properties = properties;
    }
}
