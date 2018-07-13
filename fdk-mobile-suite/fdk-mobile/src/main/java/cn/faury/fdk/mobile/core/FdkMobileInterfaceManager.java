package cn.faury.fdk.mobile.core;

import cn.faury.fdk.common.utils.PropertiesUtil;
import cn.faury.fdk.common.utils.StringUtil;
import cn.faury.fdk.mobile.annotation.IMobile;
import cn.faury.fdk.mobile.annotation.IMobileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;

import java.io.File;
import java.util.DuplicateFormatFlagsException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 手机接口管理器
 */
public class FdkMobileInterfaceManager {

    /**
     * 日志记录器
     */
    private static Logger log = LoggerFactory.getLogger(FdkMobileInterfaceManager.class);

    /**
     * 默认配置文件字符串拆分：逗号
     */
    private static final String DEFAULT_CONFIG_SPLIT = ",";

    /**
     * 默认配置文件Key：模式
     */
    private static final String DEFAULT_CONFIG_KEY_MODE = "fdk.mobile.service.mode";

    /**
     * 默认配置文件Key：授权
     */
    private static final String DEFAULT_CONFIG_KEY_AUTH = "fdk.mobile.service.auth";

    /**
     * 默认配置文件值：接口过时标识
     */
    private static final String DEFAULT_CONFIG_VALUE_OBSOLETE = "[obsolete]";

    /**
     * 接口服务列表
     */
    private static Map<String, IMobileService> intefaceService = new ConcurrentHashMap<>();

    /**
     * 接口授权列表
     */
    private static Map<String, Boolean> intefaceAuthc = new ConcurrentHashMap<String, Boolean>();

    /**
     * 接口需求最低版本号
     */
    private static Map<String, Float[]> intefaceVesrion = new ConcurrentHashMap<String, Float[]>();

    /**
     * 过时接口列表
     */
    private static Map<String, String> intefaceObsolete = new ConcurrentHashMap<String, String>();

    /**
     * 手机框架配置
     */
    private static Config cfg = Config.defaultConfig();

    /**
     * 通过配置文件初始化接口框架
     *
     * @param event 事件
     * @param file  配置文件
     */
    public static void init(ContextRefreshedEvent event, File file) {
        if (event.getApplicationContext().getParent() == null) {
            log.debug("scan mobile inteface config file......start");
            try {
                // 没有配置文件则使用默认生产配置
                PropertiesUtil pcu = (file != null && file.exists()) ? PropertiesUtil.createPropertyInstance(file) : new PropertiesUtil();
                cfg.mode = pcu.getProperty(DEFAULT_CONFIG_KEY_MODE, cfg.mode);
                cfg.auth = pcu.getPropertyToBoolean(DEFAULT_CONFIG_KEY_AUTH, cfg.auth);
                log.debug(String.format("configure[mode=%s,auth=%s]", cfg.mode, cfg.auth));

                // 获取接口需要版本号
                Set<String> keys = pcu.getPropertyKeySet();
                for (String key : keys) {
                    // 过滤已知配置
                    if (StringUtil.isEmpty(key) || DEFAULT_CONFIG_KEY_MODE.equalsIgnoreCase(key)
                            || DEFAULT_CONFIG_KEY_AUTH.equalsIgnoreCase(key)) {
                        continue;
                    }

                    // 判断是否过时
                    String value = pcu.getProperty(key, "");
                    if (DEFAULT_CONFIG_VALUE_OBSOLETE.equals(value)) {
                        intefaceObsolete.put(key, key);
                        continue;
                    }

                    String[] versions = value.split(DEFAULT_CONFIG_SPLIT);
                    if (versions.length > 1) {
                        Float[] vs = new Float[2];
                        try {
                            vs[0] = Float.parseFloat(versions[0]);
                            vs[1] = Float.parseFloat(versions[1]);
                            intefaceVesrion.put(key, vs);
                            log.debug(String.format("configure[service=%s,version(andriod,ios)=%s]", key, value));
                        } catch (RuntimeException re) {
                            log.error(String.format("configure error[service=%s,version(andriod,ios)=%s]", key, value));
                        }
                    }
                }
            } catch (RuntimeException e) {
                cfg = Config.defaultConfig();
                log.error("scan mobile inteface config file......exception", e);
            } finally {
                log.debug("scan mobile inteface config file......end");
            }
            // 扫描手机接口服务
            log.debug("scan mobile inteface......start");
            try {
                // 扫描所有接口
                Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(IMobile.class);
                if (beans != null && beans.size() > 0) {
                    for (Object bean : beans.values()) {
                        if (bean instanceof IMobileService) {
                            IMobileService ims = (IMobileService) bean;
                            IMobile annotation = ims.getClass().getAnnotation(IMobile.class);
                            if (annotation == null) {
                                continue;
                            }
                            if (intefaceService.containsKey(annotation.method())) {
                                String c1 = intefaceService.get(annotation.method()).getClass().getName();
                                throw new DuplicateFormatFlagsException(String.format("接口名[%s]重复,类名：[%s][%s]",
                                        annotation.method(), c1, ims.getClass().getName()));
                            }
                            intefaceAuthc.put(annotation.method(), annotation.isAuthc());
                            intefaceService.put(annotation.method(), ims);
                            log.debug(String.format("[%s] checked by shiro for inteface [%s],service provide by [%s]",
                                    annotation.isAuthc() ? "authc" : "anno", annotation.method(), ims.getClass()
                                            .getName()));
                        }
                    }
                }
            } catch (RuntimeException e) {
                log.error("scan mobile inteface......exception", e);
            } finally {
                log.debug("scan mobile inteface......end");
            }
        }
    }

    /**
     * 获取接口返回服务
     *
     * @param method 接口名称
     * @return 接口服务
     */
    public static IMobileService getMobileService(String method) {
        return intefaceService.get(method);
    }

    /**
     * 是否需要权限验证
     *
     * @param method 接口名称
     * @return 是否需要验证
     */
    public static Boolean isAuthc(String method) {
        return intefaceAuthc.get(method);
    }

    /**
     * 接口是否过时
     *
     * @param method 接口名
     * @return 是否过时
     */
    public static Boolean isObsolete(String method) {
        return intefaceObsolete.containsKey(method);
    }

    /**
     * 获取安卓需要最低版本号，没有配置时返回null
     *
     * @param method 接口名称
     * @return 版本号
     */
    public static Float getAndroidVersion(String method) {
        if (StringUtil.isNotEmpty(method) && intefaceVesrion.get(method) != null && intefaceVesrion.get(method).length > 0) {
            return intefaceVesrion.get(method)[0];
        }
        return null;
    }

    /**
     * 获取苹果需要最低版本号，没有配置时返回null
     *
     * @param method 接口名称
     * @return 版本号
     */
    public static Float getIosVersion(String method) {
        if (StringUtil.isNotEmpty(method) && intefaceVesrion.get(method) != null && intefaceVesrion.get(method).length > 1) {
            return intefaceVesrion.get(method)[1];
        }
        return null;
    }

    /**
     * 获取配置的模式
     *
     * @return the mode
     */
    public static String getConfigMode() {
        return cfg.mode;
    }

    /**
     * 获取配置的权限验证
     *
     * @return the auth
     */
    public static Boolean getConfigAuth() {
        return cfg.auth;
    }

    /**
     * 手机框架配置对象
     *
     * @author faury
     */
    public static class Config {

        /**
         * 开发模式
         */
        public static final String MODE_DEV = "DEV";

        /**
         * 测试模式
         */
        public static final String MODE_TEST = "TEST";

        /**
         * 生产模式
         */
        public static final String MODE_PRO = "PRO";

        /**
         * 模式:默认PRO
         * <p>
         * <pre>
         * DEV：开发模式，输出错误信息和提示信息
         * TEST：测试模式，屏蔽错误信息，输出提示信息
         * PRO：生产模式，屏蔽错误信息，输出提示信息
         * </pre>
         */
        private String mode = MODE_PRO;

        /**
         * 是否进行权限验证：默认true
         * <p>
         * <pre>
         * false：关闭权限验证，
         * true：采用Service声明的权限验证方式
         * </pre>
         */
        private Boolean auth = Boolean.TRUE;

        public Config(String mode, Boolean auth) {
            this.mode = mode;
            this.auth = auth;
        }

        /**
         * 重置默认设置
         */
        public static Config defaultConfig() {
            return new Config(MODE_PRO, Boolean.TRUE);
        }
    }
}
