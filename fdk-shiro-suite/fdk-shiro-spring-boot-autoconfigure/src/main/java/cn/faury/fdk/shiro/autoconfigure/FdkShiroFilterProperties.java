package cn.faury.fdk.shiro.autoconfigure;

import cn.faury.fdk.common.utils.JsonUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置文件
 */
@ConfigurationProperties(prefix = FdkShiroFilterProperties.PROPERTIES_PREFIX)
public class FdkShiroFilterProperties {
    /**
     * 配置文件前缀
     */
    public static final String PROPERTIES_PREFIX = "fdk.shiro.filter";

    // 登录URL
    private String loginUrl = "/login";
    //登录成功URL
    private String successUrl = "/login";
    //未授权URL
    private String unauthorizedUrl = "/login";
    //过滤规则
    private List<String> chain = new ArrayList<>();

    /**
     * 获取loginUrl
     *
     * @return loginUrl
     */
    public String getLoginUrl() {
        return loginUrl;
    }

    /**
     * 设置loginUrl
     *
     * @param loginUrl 值
     */
    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    /**
     * 获取successUrl
     *
     * @return successUrl
     */
    public String getSuccessUrl() {
        return successUrl;
    }

    /**
     * 设置successUrl
     *
     * @param successUrl 值
     */
    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    /**
     * 获取unauthorizedUrl
     *
     * @return unauthorizedUrl
     */
    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    /**
     * 设置unauthorizedUrl
     *
     * @param unauthorizedUrl 值
     */
    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }

    /**
     * 获取chain
     *
     * @return chain
     */
    public List<String> getChain() {
        return chain;
    }

    /**
     * 设置chain
     *
     * @param chain 值
     */
    public void setChain(List<String> chain) {
        this.chain = chain;
    }

    @Override
    public String toString() {
        return JsonUtil.objectToJson(this);
    }
}
