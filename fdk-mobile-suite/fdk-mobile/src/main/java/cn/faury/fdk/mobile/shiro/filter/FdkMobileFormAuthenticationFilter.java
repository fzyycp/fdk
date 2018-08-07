package cn.faury.fdk.mobile.shiro.filter;

import cn.faury.fdk.common.entry.RestResultCode;
import cn.faury.fdk.common.exception.TipsException;
import cn.faury.fdk.common.utils.AssertUtil;
import cn.faury.fdk.common.utils.SigAESUtil;
import cn.faury.fdk.common.utils.StringUtil;
import cn.faury.fdk.mobile.shiro.token.AppUsernamePasswordToken;
import cn.faury.fdk.shiro.exception.IMessageAccountException;
import cn.faury.fdk.shiro.filter.captcha.FdkCaptchaConst;
import cn.faury.fdk.shiro.utils.SessionUtil;
import cn.faury.fdk.shiro.utils.ShiroUtil;
import cn.faury.fwmf.module.api.app.bean.AppInfoBean;
import cn.faury.fwmf.module.api.app.service.AppInfoService;
import cn.faury.fwmf.module.api.role.bean.RoleInfoBean;
import cn.faury.fwmf.module.api.role.service.RoleService;
import cn.faury.fwmf.module.api.user.bean.UserInfoBean;
import cn.faury.fwmf.module.api.user.config.UserType;
import cn.faury.fwmf.module.api.user.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 手机端登录验证过滤器
 */
public class FdkMobileFormAuthenticationFilter extends FormAuthenticationFilter {

    /**
     * 日志记录器
     */
    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 默认登录终端类型
     */
    public static final String DEFAULT_CLIENT_TYPE = "APP";

    /**
     * 默认App编码参数名
     */
    public static final String DEFAULT_APP_CODE_PARAM = "appCode";

    /**
     * 默认是否对用户名进行编码参数名
     */
    public static final String DEFAULT_NOT_DECODE_UERNAME_PARAM = "notDecodeUername";

    /**
     * 默认游客登录URL
     */
    public static final String DEFAULT_GUEST_LOGIN_URL = "/guestLogin";

    /**
     * 错误信息
     */
    public static final String ATTRIBUTE_KEY_FAILURE_MSG = FdkCaptchaConst.ATTRIBUTE_KEY_FAILURE_MSG;

    /**
     * 用户提示信息
     */
    public static final String ATTRIBUTE_KEY_FAILURE_MSG_TIPS = FdkCaptchaConst.ATTRIBUTE_KEY_FAILURE_MSG_TIPS;

    /**
     * 游客登录名check
     */
    public static final String GUEST_USERNAME_REGEX = "^YK.{1,62}$";

    /**
     * 用户服务
     */
    protected UserService userService;

    /**
     * 角色服务
     */
    protected RoleService roleService;

    /**
     * App注册服务
     */
    protected AppInfoService appInfoService;

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        try {
            return super.executeLogin(request, response);
        } catch (TipsException e) {
            log.debug("登录异常", e);
            request.setAttribute(ATTRIBUTE_KEY_FAILURE_MSG, StringUtil.emptyDefault(e.getTips(), e.getMessage()));
            return true;
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isGuestLogin(request) && isGuestLoginSubmission(request, response)) {
            // 如果游客用户不存在，则先创建再执行登录
            if (userService != null && appInfoService != null) {
                try {
                    AppInfoBean appInfo = appInfoService.getAppInfoBySystemCode(null, this.getAppCode(request));
                    AssertUtil.assertNotNull(appInfo, "APP不存在或已停用");
                    // 检查APP是否设置为拒绝游客登录
                    AssertUtil.assertFalse(StringUtil.whetherYes(appInfo.getRejectGuestUser()), "该APP拒绝游客登录");
                    AssertUtil.assertNotEmpty(this.getUsername(request), "游客登录名不能为空");
                    AssertUtil.assertTrue(Pattern.matches(GUEST_USERNAME_REGEX, this.getUsername(request)), "游客登录名格式不正确");
                    UserInfoBean ui = userService.getUserInfoByLoginName(this.getUsername(request));
                    if (ui == null) {
                        String loginName = this.getUsername(request);
                        String password = this.getPassword(request);

                        // 保存用户信息
                        Long userId = userService.insertUserInfo(loginName, loginName, password, appInfo.getSystemId(),
                                UserType.GUEST, "system-init", "");
                        AssertUtil.assertTrue(userId != null && userId > 0, "保存游客信息失败");
                    }

                    // 执行用户登录
                    return executeLogin(request, response);
                } catch (TipsException e) {
                    log.debug("游客登录异常", e);
                    request.setAttribute(ATTRIBUTE_KEY_FAILURE_MSG_TIPS, StringUtil.emptyDefault(e.getTips(), e.getMessage()));
                    return true;
                }
            } else {
                request.setAttribute(ATTRIBUTE_KEY_FAILURE_MSG, "系统不支持游客登录");
                request.setAttribute(ATTRIBUTE_KEY_FAILURE_MSG_TIPS, RestResultCode.CODE500.getTips());
                return true;
            }
        }
        return super.onAccessDenied(request, response);
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String appCode = getAppCode(request);
        String username = getUsername(request);
        String password = getPassword(request);
        boolean notDecodeUername = getNotDecodeUername(request);
        AssertUtil.assertNotEmpty(appCode, "APP编码不可以为空");
        AssertUtil.assertNotEmpty(username, "用户名不可以为空");
        AssertUtil.assertNotEmpty(password, "密码不可以为空");
        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);
        return new AppUsernamePasswordToken(appCode, username, password, rememberMe, host, notDecodeUername);
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
                                     ServletResponse response) throws Exception {
        // 记录用户信息到Session，安全执行
        // 登录成功初始化:设置Session值，相当于初始化SessionUtil
        SessionUtil.setCurrentUserName(ShiroUtil.principal());
        if (userService != null) {
            UserInfoBean user = userService.getUserInfoByLoginName(SessionUtil.getCurrentLoginName());
            if (user != null) {
                SessionUtil.setCurrentUserName(user.getUserName());
                SessionUtil.setCurrentUserId(user.getUserId());
                SessionUtil.setCurrentUserInfo(user);

                if (roleService != null) {
                    List<RoleInfoBean> roles = roleService.getUserRolesByUserId(getAppCode(request), user.getUserId());
                    SessionUtil.setCurrentRolesInfo(roles);
                }
            }
        }

        return true;
    }

    @Override
    protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
        log.error("登录异常：{}", ae.getMessage(), ae);
        super.setFailureAttribute(request, ae);
        this.setFailureMessage(request, ae);
    }

    @Override
    protected String getUsername(ServletRequest request) {
        String username = super.getUsername(request);
        // 是否需要转码
        if (StringUtil.isNotEmpty(username) && !getNotDecodeUername(request)) {
            try {
                username = URLDecoder.decode(username, "UTF-8");
            } catch (Exception ignored) {
            }
        }
        return username;
    }

    /**
     * 是否为POST提交
     *
     * @param request  请求
     * @param response 响应
     * @return 检查结果
     */
    protected boolean isGuestLoginSubmission(ServletRequest request, ServletResponse response) {
        return (request instanceof HttpServletRequest)
                && WebUtils.toHttp(request).getMethod().equalsIgnoreCase(POST_METHOD);
    }

    /**
     * 设置错误信息
     *
     * @param request 请求
     */
    protected void setFailureMessage(ServletRequest request, AuthenticationException ae) {
        String fail = ae.getClass().getName();

        // 开始处理异常信息
        log.debug("登录异常：" + fail);
        if (IncorrectCredentialsException.class.getName().equals(fail)) {
            request.setAttribute(ATTRIBUTE_KEY_FAILURE_MSG, "用户名密码不匹配");
            request.setAttribute(ATTRIBUTE_KEY_FAILURE_MSG_TIPS, "用户名密码不匹配");
        } else if (ExpiredCredentialsException.class.getName().equals(fail)) {
            request.setAttribute(ATTRIBUTE_KEY_FAILURE_MSG, "用户密码已过期");
            request.setAttribute(ATTRIBUTE_KEY_FAILURE_MSG_TIPS, "用户密码已过期");
        } else if (UnknownAccountException.class.getName().equals(fail)) {
            request.setAttribute(ATTRIBUTE_KEY_FAILURE_MSG, "用户不存在或密码错误");
            request.setAttribute(ATTRIBUTE_KEY_FAILURE_MSG_TIPS, "用户不存在或密码错误");
        } else if (LockedAccountException.class.getName().equals(fail)) {
            request.setAttribute(ATTRIBUTE_KEY_FAILURE_MSG, "用户账号已锁定");
            request.setAttribute(ATTRIBUTE_KEY_FAILURE_MSG_TIPS, "用户账号已锁定");
        } else if (DisabledAccountException.class.getName().equals(fail)) {
            request.setAttribute(ATTRIBUTE_KEY_FAILURE_MSG, "用户账号已禁用");
            request.setAttribute(ATTRIBUTE_KEY_FAILURE_MSG_TIPS, "用户账号已禁用");
        } else if (AuthenticationException.class.getName().equals(fail)) {
            log.error("Shiro认证异常：" + fail);
            request.setAttribute(ATTRIBUTE_KEY_FAILURE_MSG, "用户认证失败");
            request.setAttribute(ATTRIBUTE_KEY_FAILURE_MSG_TIPS, "用户认证失败");
        } else {
            log.error("未知异常：" + fail);
            request.setAttribute(ATTRIBUTE_KEY_FAILURE_MSG, "登录失败");
            request.setAttribute(ATTRIBUTE_KEY_FAILURE_MSG_TIPS, "登录失败");

            // 尝试通过反射确认是否为自定义的异常信息，并获取默认消息返回
            try {
                Class<?> clazz = Class.forName(fail);
                Object obj = clazz.newInstance();
                if (obj instanceof IMessageAccountException) {
                    IMessageAccountException exc = (IMessageAccountException) obj;
                    if (exc != null && exc.getDefaultExceptionMessage() != null
                            && exc.getDefaultExceptionMessage().length() > 0) {
                        request.setAttribute(ATTRIBUTE_KEY_FAILURE_MSG,
                                exc.getDefaultExceptionMessage());
                        request.setAttribute(ATTRIBUTE_KEY_FAILURE_MSG_TIPS,
                                exc.getDefaultExceptionMessage());
                    }
                }
            } catch (Exception e) {
                log.error("反射处理异常", e);
            }
        }
    }

    /**
     * 获取App编码
     *
     * @param request 请求
     * @return App编码
     */
    protected String getAppCode(ServletRequest request) {
        return WebUtils.getCleanParam(request, getAppCodeParam());
    }

    /**
     * 是否为游客登录URL
     *
     * @param request 请求
     * @return 校验结果
     */
    protected boolean isGuestLogin(ServletRequest request) {
        return pathsMatch(DEFAULT_GUEST_LOGIN_URL, request);
    }

    /**
     * 获取是否对用户名进行解码
     *
     * @param request 请求
     * @return 是否对用户名进行编码
     */
    public boolean getNotDecodeUername(ServletRequest request) {
        String notDecodeUername = WebUtils.getCleanParam(request, getNotDecodeUernameParam());
        return "true".equalsIgnoreCase(notDecodeUername);
    }

    /**
     * 获取App编码参数变量名
     *
     * @return App编码参数变量名
     */
    public String getAppCodeParam() {
        return DEFAULT_APP_CODE_PARAM;
    }

    /**
     * 获取是否对用户名解码参数
     *
     * @return the isEncodeUernameParam
     */
    public final String getNotDecodeUernameParam() {
        return DEFAULT_NOT_DECODE_UERNAME_PARAM;
    }

    /**
     * 获取登录终端类型：APP、WEIXIN、QQ、SINA_WEIBO等
     *
     * @param request 请求
     * @return 终端类型
     */
    protected String getClientType(ServletRequest request) {
        return DEFAULT_CLIENT_TYPE;
    }

    /**
     * 获取userService
     *
     * @return userService
     */
    public UserService getUserService() {
        return userService;
    }

    /**
     * 设置userService
     *
     * @param userService 值
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 获取roleService
     *
     * @return roleService
     */
    public RoleService getRoleService() {
        return roleService;
    }

    /**
     * 设置roleService
     *
     * @param roleService 值
     */
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 获取appRegisterService
     *
     * @return appInfoService
     */
    public AppInfoService getAppInfoService() {
        return appInfoService;
    }

    /**
     * 设置appRegisterService
     *
     * @param appInfoService 值
     */
    public void setAppInfoService(AppInfoService appInfoService) {
        this.appInfoService = appInfoService;
    }
}
