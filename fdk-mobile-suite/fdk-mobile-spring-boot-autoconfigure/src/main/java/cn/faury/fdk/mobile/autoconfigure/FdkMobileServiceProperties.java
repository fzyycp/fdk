package cn.faury.fdk.mobile.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置文件
 */
@ConfigurationProperties(prefix = FdkMobileServiceProperties.PROPERTIES_PREFIX)
public class FdkMobileServiceProperties {

    /**
     * 配置文件前缀
     */
    public static final String PROPERTIES_PREFIX = "fdk.mobile.service";

    /**
     * 模式:默认PRO
     * <p>
     * <pre>
     *      DEV：开发模式，输出错误信息和提示信息
     *      TEST：测试模式，输出错误信息和提示信息
     *      PRO：生产模式，屏蔽错误信息，输出提示信息
     * </pre>
     */
    private String mode = "PRO";

    /**
     * 是否进行权限验证：默认true
     * <p>
     * <pre>
     *      false：关闭权限验证，
     *      true：采用Service声明的权限验证方式
     * </pre>
     */
    private boolean auth = true;

    /**
     * 不兼容配置
     * <p>
     * <pre>
     * 保存所有不兼容配置的接口，兼容的无须配置
     * 		启动验证必须要非开发模式
     * 		如果一种客户端兼容另一种不兼容，则不兼容的配置版本号，兼容的为0
     * 		版本号为访问接口需要最低APP版本号
     * 		配置信息区分大小写
     * 		提示不兼容情况：
     *             case1：配置为[obsolete]的接口
     *             case2：手机端没有传入相应的版本号
     *             case3：传入的版本号小于最低版本号
     *
     * 不兼容升级配置：接口名称=Android版本号,IOS版本号
     *                        例：getUserInfo=1.5,2.5,0.0
     * 废弃的接口配置：接口名称=[obsolete]
     *                        例：getUseInfo=[obsolete]
     * </pre>
     */
    private Map<String, String> incompatible = new HashMap<>();

    /**
     * 获取mode
     *
     * @return mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * 设置mode
     *
     * @param mode 值
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * 获取auth
     *
     * @return auth
     */
    public boolean isAuth() {
        return auth;
    }

    /**
     * 设置auth
     *
     * @param auth 值
     */
    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    /**
     * 获取incompatible
     *
     * @return incompatible
     */
    public Map<String, String> getIncompatible() {
        return incompatible;
    }

    /**
     * 设置incompatible
     *
     * @param incompatible 值
     */
    public void setIncompatible(Map<String, String> incompatible) {
        this.incompatible = incompatible;
    }
}
