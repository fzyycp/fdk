package cn.faury.fdk.mobile;

import cn.faury.fdk.common.anotation.Properties;
import cn.faury.fdk.common.entry.RestResultCode;
import cn.faury.fdk.common.exception.TipsException;
import cn.faury.fdk.common.utils.PropertiesUtil;
import cn.faury.fdk.common.utils.StringUtil;
import cn.faury.fdk.mobile.core.FdkMobileInterfaceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.InputStream;

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
    private String configFile;

    // 配置属性
    private PropertiesUtil propertiesUtil;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.error("{}","=====初始化手机接口管理器=====init");
        try {
            if (propertiesUtil!=null){
                FdkMobileInterfaceManager.init(event,propertiesUtil);
            } else {
                configFile = StringUtil.emptyDefault(configFile, DEFAULT_SERVICE_CONFIG_FILE);
                InputStream inputStream = (new ClassPathResource(configFile)).getInputStream();
                // 初始化手机接口管理器
                FdkMobileInterfaceManager.init(event, inputStream);
            }
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
     * 获取configFile
     *
     * @return configFile
     */
    public String getConfigFile() {
        return configFile;
    }

    /**
     * 设置configFile
     *
     * @param configFile 值
     */
    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    /**
     * 获取propertiesUtil
     *
     * @return propertiesUtil
     */
    public PropertiesUtil getPropertiesUtil() {
        return propertiesUtil;
    }

    /**
     * 设置propertiesUtil
     *
     * @param propertiesUtil 值
     */
    public void setPropertiesUtil(PropertiesUtil propertiesUtil) {
        this.propertiesUtil = propertiesUtil;
    }
}
