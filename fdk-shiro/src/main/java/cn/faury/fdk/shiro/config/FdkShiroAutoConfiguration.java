package cn.faury.fdk.shiro.config;

import cn.faury.fdk.common.utils.JsonUtil;
import cn.faury.fdk.shiro.core.*;
import cn.faury.fdk.shiro.realm.FdkShiroRealm;
import cn.faury.fdk.shiro.realm.service.IRoleService;
import cn.faury.fdk.shiro.realm.service.IUserService;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class FdkShiroAutoConfiguration {
    // 日志
    private Logger logger = LoggerFactory.getLogger(FdkShiroAutoConfiguration.class);
    // Session超时
    @Value("${fdk.shiro.session.timeout:1800000}")
    private int sessionTimeout;

    @Autowired
    private FdkShiroFilterProperties fdkShiroFilterProperties;

    // Session管理器由外部jar包提供
    @Autowired
    private ShiroSessionRepository shiroSessionRepository;

    // 用户服务
    @Autowired(required = false)
    private IUserService userService;

    // 角色服务
    @Autowired(required = false)
    private IRoleService roleService;

    // 系统编码或APP编码
    @Value("${fdk.shiro.saCode}")
    private String saCode;

    @Bean
    public ShiroFilterFactoryBean shirFilter() {
        logger.debug("{}", "=====开始初始化ShiroFilterFactoryBean=====");
        logger.debug("{}", "=====已加载配置文件 Start=====");
        logger.debug("{}", fdkShiroFilterProperties);
        logger.debug("{}", "saCode=" + saCode);
        logger.debug("{}", "sessionTimeout=" + sessionTimeout);
        logger.debug("{}", "userService=" + userService);
        logger.debug("{}", "roleService=" + roleService);
        Map<String, String> filterChainDefinitionMap = new HashMap<>();
        // properties文件中key为过滤器名字，value为多个uri的连接，按照逗号隔开
        if (fdkShiroFilterProperties.getChain() != null) {
            fdkShiroFilterProperties.getChain().forEach((key, value) -> {
                String[] paths = value.split(",");
                Arrays.stream(paths).forEach(path -> {
                    filterChainDefinitionMap.put(path, key);
                });
            });
        }
        logger.debug("{}", "filterChainDefinitionMap=" + JsonUtil.objectToJson(filterChainDefinitionMap));
        logger.debug("{}", "=====已加载配置文件 End=====");

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());

        //配置shiro默认登录界面地址，前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据
        shiroFilterFactoryBean.setLoginUrl(fdkShiroFilterProperties.getLoginUrl());
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl(fdkShiroFilterProperties.getSuccessUrl());
        //未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl(fdkShiroFilterProperties.getUnauthorizedUrl());
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        logger.debug("{}", "=====完成初始化ShiroFilterFactoryBean=====");
        return shiroFilterFactoryBean;
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
        shiroRealm.setSaCode(saCode);
        shiroRealm.setCredentialsMatcher(shiroCredentialsMatcher());
        return shiroRealm;
    }

    public FdkShiroCredentialsMatcher shiroCredentialsMatcher() {
        return new FdkShiroCredentialsMatcher();
    }

    @Bean
    public FdkWebSessionManager webSessionManager() {
        FdkWebSessionManager webSessionManager = new FdkWebSessionManager();
        webSessionManager.setGlobalSessionTimeout(sessionTimeout);
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
        Cookie cookie = new SimpleCookie("JSESSIONID_S");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(sessionTimeout);
        return cookie;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }
}
