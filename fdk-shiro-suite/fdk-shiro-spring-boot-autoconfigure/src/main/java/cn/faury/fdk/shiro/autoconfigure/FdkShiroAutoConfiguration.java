package cn.faury.fdk.shiro.autoconfigure;

import cn.faury.fdk.captcha.autoconfigure.FdkCaptchaAutoConfiguration;
import cn.faury.fdk.captcha.autoconfigure.FdkCaptchaProperties;
import cn.faury.fdk.common.utils.JsonUtil;
import cn.faury.fdk.shiro.core.*;
import cn.faury.fdk.shiro.filter.FdkCaptchaValidateFilter;
import cn.faury.fdk.shiro.filter.LoginSuccessFilter;
import cn.faury.fdk.shiro.realm.FdkShiroRealm;
import cn.faury.fwmf.module.api.role.service.RoleService;
import cn.faury.fwmf.module.api.user.service.UserService;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@AutoConfigureAfter(FdkCaptchaAutoConfiguration.class)
@EnableConfigurationProperties({FdkShiroProperties.class, FdkShiroFilterProperties.class, FdkCaptchaProperties.class})
public class FdkShiroAutoConfiguration {
    // 日志
    private Logger logger = LoggerFactory.getLogger(FdkShiroAutoConfiguration.class);

    // 配置文件，不可以为空
    @Autowired(required = false)
    private FdkShiroProperties fdkShiroProperties;

    @Autowired(required = false)
    private FdkShiroFilterProperties fdkShiroFilterProperties;

    // 验证码配置文件
    @Autowired(required = false)
    private FdkCaptchaProperties fdkCaptchaProperties;

    // Session管理器由外部jar包提供，不可以为空
    @Autowired(required = false)
    private ShiroSessionRepository shiroSessionRepository;

    // 用户服务，不可以为空
    @Autowired(required = false)
    private UserService userService;

    // 角色服务，不可以为空
    @Autowired(required = false)
    private RoleService roleService;

    @Bean
    public ShiroFilterFactoryBean shirFilter() {
        logger.debug("{}", "=====开始初始化ShiroFilterFactoryBean=====");
        logger.debug("{}", "fdkShiroProperties=" + fdkShiroProperties);
        logger.debug("{}", "fdkShiroFilterProperties=" + fdkShiroFilterProperties);
        logger.debug("{}", "saCode=" + fdkShiroProperties.getSaCode());
        logger.debug("{}", "sessionTimeout=" + fdkShiroProperties.getSessionTimeout());
        logger.debug("{}", "userService=" + userService);
        logger.debug("{}", "roleService=" + roleService);

        Map<String, String> filterChainDefinitionMap = new HashMap<>();
        // 添加验证码过滤
        filterChainDefinitionMap.put(FdkCaptchaProperties.REQUEST_URL, "anon");
        filterChainDefinitionMap.put(fdkShiroFilterProperties.getLoginUrl(), "captcha,login");
        filterChainDefinitionMap.put("logout", "logout");
        // properties文件中列表，以分号隔开，前面为过滤规则，后面为匹配uri规则
        if (fdkShiroFilterProperties.getChain() != null) {
            fdkShiroFilterProperties.getChain().forEach(value -> {
                String[] chains = value.split(":");
                if (chains.length > 1) {
                    filterChainDefinitionMap.put(chains[1], chains[0]);
                }
            });
        }
        logger.debug("{}", "filterChainDefinitionMap=" + JsonUtil.objectToJson(filterChainDefinitionMap));

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());

        //配置shiro默认登录界面地址，前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据
        shiroFilterFactoryBean.setLoginUrl(fdkShiroFilterProperties.getLoginUrl());
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl(fdkShiroFilterProperties.getSuccessUrl());
        //未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl(fdkShiroFilterProperties.getUnauthorizedUrl());
        Map<String, Filter> filters = new HashMap<>();
        filters.put("captcha", fdkCaptchaValidateFilter().getFilter());
        filters.put("login", loginSuccessFilter().getFilter());
        shiroFilterFactoryBean.setFilters(filters);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        logger.debug("{}", "=====完成初始化ShiroFilterFactoryBean=====");
        return shiroFilterFactoryBean;
    }

    @Bean
    public FilterRegistrationBean fdkCaptchaValidateFilter() {
        FdkCaptchaValidateFilter filter = new FdkCaptchaValidateFilter(fdkCaptchaProperties);
        FilterRegistrationBean<FdkCaptchaValidateFilter> registrationBean = new FilterRegistrationBean<>(filter);
        // 关闭在spring chain中注册filter
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean loginSuccessFilter() {
        LoginSuccessFilter filter = new LoginSuccessFilter(userService, roleService, fdkShiroProperties.getSaCode());
        FilterRegistrationBean<LoginSuccessFilter> registrationBean = new FilterRegistrationBean<>(filter);
        // 关闭在spring chain中注册filter
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm());
        securityManager.setSessionManager(webSessionManager());
        return securityManager;
    }

    @Bean
    public FdkShiroRealm shiroRealm() {
        FdkShiroRealm shiroRealm = new FdkShiroRealm();
        shiroRealm.setUserService(userService);
        shiroRealm.setRoleService(roleService);
        shiroRealm.setSaCode(fdkShiroProperties.getSaCode());
        shiroRealm.setCredentialsMatcher(shiroCredentialsMatcher());
        return shiroRealm;
    }

    public FdkShiroCredentialsMatcher shiroCredentialsMatcher() {
        return new FdkShiroCredentialsMatcher();
    }

    @Bean
    public FdkWebSessionManager webSessionManager() {
        FdkWebSessionManager webSessionManager = new FdkWebSessionManager();
        webSessionManager.setGlobalSessionTimeout(fdkShiroProperties.getSessionTimeout());
        webSessionManager.setSessionListeners(Collections.singletonList(sessionListener()));
        webSessionManager.setDeleteInvalidSessions(true);
        webSessionManager.setSessionDAO(sessionDAO());
        webSessionManager.setSessionIdCookieEnabled(true);
        webSessionManager.setSessionIdCookie(sessionIdCookie());
        return webSessionManager;
    }

    @Bean
    public SessionListener sessionListener() {
        return new SessionExtListener();
    }

    @Bean
    public SessionDAO sessionDAO() {
        return new ShiroSessionDAO(shiroSessionRepository);
    }

    @Bean
    public Cookie sessionIdCookie() {
        Cookie cookie = new SimpleCookie("S");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(fdkShiroProperties.getSessionTimeout());
        return cookie;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }
}
