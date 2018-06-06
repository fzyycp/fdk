package cn.faury.fdk.shiro.config;

import cn.faury.fdk.common.utils.JsonUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 配置文件
 */
@Component
@ConfigurationProperties(prefix = "fdk.shiro.filter")
public class FdkShiroFilterProperties {
    // 登录URL
    private String loginUrl;
    // 登录成功URL
    private String successUrl;
    // 未授权URL
    private String unauthorizedUrl;
    // 过滤规则
    private Map<String, String> chain;

    public String getLoginUrl() {
        return loginUrl;
    }

    public FdkShiroFilterProperties setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
        return this;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public FdkShiroFilterProperties setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
        return this;
    }

    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    public FdkShiroFilterProperties setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
        return this;
    }

    public Map<String, String> getChain() {
        return chain;
    }

    public FdkShiroFilterProperties setChain(Map<String, String> chain) {
        this.chain = chain;
        return this;
    }

    @Override
    public String toString() {
        return JsonUtil.objectToJson(this);
    }
}
