package cn.faury.fdk.mobile.autoconfigure;

import cn.faury.fdk.common.utils.JsonUtil;
import cn.faury.fdk.common.utils.PropertiesUtil;
import cn.faury.fdk.common.utils.StringUtil;
import cn.faury.fdk.mobile.MobileFrameworkInitializer;
import cn.faury.fdk.mobile.shiro.filter.FdkOAuth2MobileFormAuthenticationFilter;
import cn.faury.fdk.mobile.shiro.filter.oauth2.*;
import cn.faury.fdk.mobile.shiro.realm.FdkMobileDbRealm;
import cn.faury.fdk.shiro.core.*;
import cn.faury.fwmf.module.api.app.service.AppInfoService;
import cn.faury.fwmf.module.api.app.service.ShopRAppInfoService;
import cn.faury.fwmf.module.api.app.service.UserRAppInfoService;
import cn.faury.fwmf.module.api.role.service.RoleInfoService;
import cn.faury.fwmf.module.api.shop.service.ShopInfoService;
import cn.faury.fwmf.module.api.user.service.UserInfoService;
import cn.faury.fwmf.module.api.user.service.UserOAuthService;
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
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.*;

@Configuration
@EnableConfigurationProperties({FdkMobileProperties.class, FdkMobileServiceProperties.class})
public class FdkMobileAutoConfiguration {
    // 日志
    private Logger logger = LoggerFactory.getLogger(FdkMobileAutoConfiguration.class);

    @Autowired(required = false)
    private FdkMobileProperties fdkMobileProperties;

    @Autowired(required = false)
    private FdkMobileServiceProperties fdkMobileServiceProperties;

    // Session管理器由外部jar包提供，不可以为空
    @Autowired(required = false)
    private ShiroSessionRepository shiroSessionRepository;

    // 用户服务，不可以为空
    @Autowired(required = false)
    private UserInfoService userInfoService;

    // 角色服务，不可以为空
    @Autowired(required = false)
    private RoleInfoService roleInfoService;
    // 用户第三方授权服务
    @Autowired(required = false)
    private UserOAuthService userOAuthService;
    // APP服务
    @Autowired(required = false)
    private AppInfoService appInfoService;
    // 用户授权App服务
    @Autowired(required = false)
    private UserRAppInfoService userRAppInfoService;
    // 商店授权APP服务
    @Autowired(required = false)
    private ShopRAppInfoService shopRAppInfoService;
    // 商店信息服务
    @Autowired(required = false)
    private ShopInfoService shopInfoService;

    @Bean
    public MobileFrameworkInitializer mobileFrameworkInitializer() {
        MobileFrameworkInitializer initializer = new MobileFrameworkInitializer();
        Properties properties = new Properties();
        if (fdkMobileServiceProperties != null) {
            properties.setProperty(FdkMobileServiceProperties.PROPERTIES_PREFIX + ".mode", fdkMobileServiceProperties.getMode());
            properties.setProperty(FdkMobileServiceProperties.PROPERTIES_PREFIX + ".auth", String.valueOf(fdkMobileServiceProperties.isAuth()));
            Map<String, String> incompatible = fdkMobileServiceProperties.getIncompatible();
            if (incompatible != null) {
                properties.putAll(incompatible);
            }
        }
        initializer.setPropertiesUtil(PropertiesUtil.createPropertyInstance(properties));
        return initializer;
    }

    @Bean
    public WeixinOAuth2Handler weixinOAuth2Handler() {
        WeixinOAuth2Handler handler = new WeixinOAuth2Handler();
        if (fdkMobileProperties != null) {
            handler.setKeys(fdkMobileProperties.getWeixinOAuth2Keys());
        }
        logger.debug("weixinOAuth2Handler:keys={}",handler.getKeys());
        return handler;
    }

    @Bean
    public WeixinMPOAuth2Handler weixinMPOAuth2Handler() {
        WeixinMPOAuth2Handler handler = new WeixinMPOAuth2Handler();
        if (fdkMobileProperties != null) {
            handler.setKeys(fdkMobileProperties.getWeixinmpOAuth2Keys());
            handler.setNounionid(fdkMobileProperties.getWeixinmpNoUnionId());
        }
        logger.debug("weixinMPOAuth2Handler:keys={}",handler.getKeys());
        logger.debug("weixinMPOAuth2Handler:nounionid={}",handler.getNounionid());
        logger.debug("weixinMPOAuth2Handler:nounionidList={}",handler.getNounionidList());
        return handler;
    }

    @Bean
    public QqOAuth2Handler qqOAuth2Handler() {
        QqOAuth2Handler handler = new QqOAuth2Handler();
        if (fdkMobileProperties != null) {
            handler.setKeys(fdkMobileProperties.getQqOAuth2Keys());
        }
        logger.debug("qqOAuth2Handler:keys={}",handler.getKeys());
        return handler;
    }

    @Bean
    public SinaWeiboOAuth2Handler sinaWeiboOAuth2Handler() {
        SinaWeiboOAuth2Handler handler = new SinaWeiboOAuth2Handler();
        if (fdkMobileProperties != null) {
            handler.setKeys(fdkMobileProperties.getSinaweiboOAuth2Keys());
        }
        logger.debug("sinaWeiboOAuth2Handler:keys={}",handler.getKeys());
        return handler;
    }

    @Bean
    public FilterRegistrationBean fdkOAuth2MobileFormAuthenticationFilter() {
        logger.debug("{}", "=====开始初始化FdkOAuth2MobileFormAuthenticationFilter=====");
        logger.debug("fdkMobileProperties={}", fdkMobileProperties);
        logger.debug("userInfoService={}", userInfoService);
        logger.debug("roleInfoService={}", roleInfoService);
        logger.debug("userOAuthService={}", userOAuthService);
        logger.debug("appInfoService={}", appInfoService);
        FdkOAuth2MobileFormAuthenticationFilter filter = new FdkOAuth2MobileFormAuthenticationFilter();
        filter.setUserInfoService(userInfoService);
        filter.setRoleInfoService(roleInfoService);
        filter.setUserOAuthService(userOAuthService);
        filter.setAppInfoService(appInfoService);

        Map<String, OAuth2Handler> oAuth2HandlerMap = new HashMap<>();
        oAuth2HandlerMap.put(WeixinOAuth2Handler.HANDLER_KEY, weixinOAuth2Handler());
        oAuth2HandlerMap.put(WeixinMPOAuth2Handler.HANDLER_KEY, weixinMPOAuth2Handler());
        oAuth2HandlerMap.put(QqOAuth2Handler.HANDLER_KEY, qqOAuth2Handler());
        oAuth2HandlerMap.put(SinaWeiboOAuth2Handler.HANDLER_KEY, sinaWeiboOAuth2Handler());
        filter.setOauth2(oAuth2HandlerMap);
        logger.debug("{}", "=====完成初始化FdkOAuth2MobileFormAuthenticationFilter=====");

        // 关闭在spring chain中注册filter
        FilterRegistrationBean<OncePerRequestFilter> filterRegistrationBean = new FilterRegistrationBean<>(filter);
        filterRegistrationBean.setEnabled(false);
        return filterRegistrationBean;
    }

    @Bean
    public ShiroFilterFactoryBean shirFilter() {
        logger.debug("{}", "=====开始初始化ShiroFilterFactoryBean=====");
        logger.debug("fdkMobileProperties={}", fdkMobileProperties);
        logger.debug("{}", "userInfoService=" + userInfoService);
        logger.debug("{}", "roleInfoService=" + roleInfoService);

        Map<String, String> filterChainDefinitionMap = new HashMap<>();
        // 接口请求
        filterChainDefinitionMap.put("/mobile/exec", "anon");
        List<String> anon = fdkMobileProperties.getAnon();
        // properties文件中列表，以分号隔开，前面为过滤规则，后面为匹配uri规则
        if (anon != null) {
            anon.forEach(value -> {
                if (StringUtil.isNotEmpty(value)) {
                    filterChainDefinitionMap.put(value, "anon");
                }
            });
        }
        filterChainDefinitionMap.put("/**", "mobile");
        logger.debug("{}", "filterChainDefinitionMap=" + JsonUtil.objectToJson(filterChainDefinitionMap));

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());

        //配置shiro默认登录界面地址，前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/login");
        //未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/login");
        Map<String, Filter> filters = new HashMap<>();
        filters.put("mobile", fdkOAuth2MobileFormAuthenticationFilter().getFilter());
        shiroFilterFactoryBean.setFilters(filters);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        logger.debug("{}", "=====完成初始化ShiroFilterFactoryBean=====");
        return shiroFilterFactoryBean;
    }

    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(fdkMobileDbRealm());
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    @Bean
    public FdkMobileDbRealm fdkMobileDbRealm() {
        FdkMobileDbRealm fdkMobileDbRealm = new FdkMobileDbRealm();
        fdkMobileDbRealm.setAppCode(fdkMobileProperties.getAppCode());
        fdkMobileDbRealm.setShopInfoService(shopInfoService);
        fdkMobileDbRealm.setShopRAppInfoService(shopRAppInfoService);
        fdkMobileDbRealm.setUserRAppInfoService(userRAppInfoService);
        fdkMobileDbRealm.setAppInfoService(appInfoService);
        fdkMobileDbRealm.setUserInfoService(userInfoService);
        fdkMobileDbRealm.setCredentialsMatcher(shiroCredentialsMatcher());
        return fdkMobileDbRealm;
    }

    @Bean
    public FdkShiroCredentialsMatcher shiroCredentialsMatcher() {
        return new FdkShiroCredentialsMatcher();
    }

    @Bean
    public FdkWebSessionManager sessionManager() {
        FdkWebSessionManager fdkWebSessionManager = new FdkWebSessionManager();
        fdkWebSessionManager.setGlobalSessionTimeout(fdkMobileProperties.getSessionTimeout());
        fdkWebSessionManager.setSessionListeners(Collections.singletonList(sessionListener()));
        fdkWebSessionManager.setDeleteInvalidSessions(true);
        fdkWebSessionManager.setSessionValidationSchedulerEnabled(true);
        fdkWebSessionManager.setSessionDAO(sessionDAO());
        fdkWebSessionManager.setSessionIdCookieEnabled(true);
        fdkWebSessionManager.setSessionIdCookie(sessionIdCookie());

        return fdkWebSessionManager;
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
        Cookie cookie = new SimpleCookie(StringUtil.emptyDefault(fdkMobileProperties.getCookieName(), "S"));
        cookie.setHttpOnly(true);
        cookie.setMaxAge(fdkMobileProperties.getSessionTimeout());
        return cookie;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }

}
