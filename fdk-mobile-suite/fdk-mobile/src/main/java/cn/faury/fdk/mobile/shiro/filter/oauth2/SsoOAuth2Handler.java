package cn.faury.fdk.mobile.shiro.filter.oauth2;

import cn.faury.fdk.common.utils.AssertUtil;
import cn.faury.fdk.mobile.shiro.filter.FdkMobileFormAuthenticationFilter;
import cn.faury.fwmf.module.api.app.bean.AppInfoBean;
import cn.faury.fwmf.module.api.app.service.AppInfoService;
import cn.faury.fwmf.module.api.user.bean.UserInfoBean;
import cn.faury.fwmf.module.api.user.bean.UserOAuthInfoBean;
import cn.faury.fwmf.module.api.user.config.UserType;
import cn.faury.fwmf.module.api.user.service.UserInfoService;
import cn.faury.fwmf.module.api.user.service.UserOAuthService;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;

/**
 * 手机端OAuth2登录验证过滤器：SSO
 */
public abstract class SsoOAuth2Handler extends AdapterOAuth2Handler {

    /**
     * 日志记录器
     */
    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 用户服务协议
     */
    protected UserInfoService userInfoService;

    /**
     * 用户授权服务协议
     */
    protected UserOAuthService userOAuthService;

    /**
     * app注册服务协议
     */
    protected AppInfoService appRegisterService;

    /**
     * 预处理操作
     *
     * @param request 预处理参数
     */
    @Override
    public void beforeHandler(ServletRequest request) {
        String appCode = WebUtils.getCleanParam(request, FdkMobileFormAuthenticationFilter.DEFAULT_APP_CODE_PARAM);
        UserOAuthInfoBean bean = createUserOAuthInfoBean(request);
        log.debug("获取到第三方登录信息：{}", bean);
        AssertUtil.assertNotNull(bean, "第三方登录授权信息获取失败");
        AssertUtil.assertNotEmpty(bean.getUnionid(), "第三方登录授权信息获取失败");
        AssertUtil.assertNotEmpty(bean.getOpenid(), "第三方登录授权信息获取失败");

        String unionid = bean.getUnionid();

        // 检查用户统一标识是否已登录过
        UserInfoBean userInfo = userInfoService.getUserInfoByLoginName(unionid);
        UserOAuthInfoBean userOAuthInfo = userOAuthService.getUserOAuthInfoByUnionId(unionid);
        Long userId = -1L;
        // 用户未注册过，注册一下
        if (userInfo == null) {
            AppInfoBean appInfo = appRegisterService.getAppInfoBySystemCode(null, appCode);
            AssertUtil.assertNotNull(appInfo,"APP不存在或已停用");
            userId = userInfoService.insertUserInfo(unionid, unionid, unionid, appInfo.getSystemId(), UserType.ENDUSER,0L,"register",0L,"register");
        } else {
            userId = userInfo.getUserId();
        }

        // 第三方登录信息不存在，重新添加
        if (userOAuthInfo == null) {
            bean.setUserId(userId);
            userOAuthService.insertUserOAuthInfo(bean);
        } else {
            // 已存在则更新第三方授权信息
            userOAuthService.updateUserOAuthInfoByUnionId(bean);
        }
    }

    /**
     * 创建用户授权信息对象
     *
     * @param request 请求
     * @return 授权信息对象
     */
    protected abstract UserOAuthInfoBean createUserOAuthInfoBean(ServletRequest request);

    /**
     * 获取userService
     *
     * @return userInfoService
     */
    public UserInfoService getUserInfoService() {
        return userInfoService;
    }

    /**
     * 设置userService
     *
     * @param userInfoService 值
     */
    public void setUserInfoService(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    /**
     * 获取userOAuthService
     *
     * @return userOAuthService
     */
    public UserOAuthService getUserOAuthService() {
        return userOAuthService;
    }

    /**
     * 设置userOAuthService
     *
     * @param userOAuthService 值
     */
    public void setUserOAuthService(UserOAuthService userOAuthService) {
        this.userOAuthService = userOAuthService;
    }

    /**
     * 获取appRegisterService
     *
     * @return appInfoService
     */
    public AppInfoService getAppRegisterService() {
        return appRegisterService;
    }

    /**
     * 设置appRegisterService
     *
     * @param appRegisterService 值
     */
    public void setAppRegisterService(AppInfoService appRegisterService) {
        this.appRegisterService = appRegisterService;
    }
}
