package cn.faury.fdk.mobile.shiro.filter;

import cn.faury.fdk.common.entry.RestResultCode;
import cn.faury.fdk.common.exception.TipsException;
import cn.faury.fdk.common.utils.AssertUtil;
import cn.faury.fdk.common.utils.SerializeUtil;
import cn.faury.fdk.common.utils.StringUtil;
import cn.faury.fdk.mobile.shiro.filter.oauth2.OAuth2Handler;
import cn.faury.fwmf.module.api.app.bean.AppInfoBean;
import cn.faury.fwmf.module.api.user.bean.UserInfoBean;
import cn.faury.fwmf.module.api.user.bean.UserOAuthInfoBean;
import cn.faury.fwmf.module.api.user.config.UserType;
import cn.faury.fwmf.module.api.user.service.UserOAuthService;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 手机端OAuth2登录验证过滤器
 */
public class FdkOAuth2MobileFormAuthenticationFilter extends FdkMobileFormAuthenticationFilter {

    /**
     * 日志记录器
     */
    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 第三方登录用户信息
     */
    public static final String ATTRIBUTE_KEY_OAUTH2_INFO = "shiroOAuth2LoginInfo";

    /**
     * 默认OAuth2登录URL
     */
    public static final String DEFAULT_OAUTH2_LOGIN_URL = "/oauth2Login";

    /**
     * 默认OAuth2类型参数名
     */
    public static final String DEFAULT_OAUTH2_TYPE_PARAM = "oauth";

    /**
     * 默认OAuth2授权码参数名
     */
    public static final String DEFAULT_OAUTH2_CODE_PARAM = "code";

    /**
     * 默认OAuth2授权统一标识参数名
     */
    public static final String DEFAULT_OAUTH2_UNIONID_PARAM = "unionid";

    /**
     * OAuth2类型参数变量名
     */
    private String oAuth2TypeParam = DEFAULT_OAUTH2_TYPE_PARAM;

    /**
     * OAuth2授权码参数变量名
     */
    private String oAuth2CodeParam = DEFAULT_OAUTH2_CODE_PARAM;

    /**
     * OAuth2授权统一标识参数变量名
     */
    private String oAuth2UnionIdParam = DEFAULT_OAUTH2_UNIONID_PARAM;

    /**
     * OAuth2登录校验器
     */
    private Map<String, OAuth2Handler> oauth2 = new LinkedHashMap();

    /**
     * 用户第三方授权服务
     */
    private UserOAuthService userOAuthService;

    @Override
    protected String getUsername(ServletRequest request) {
        // 获取已注册的第三方登录授权信息
        byte[] oauth2InfoBytes = (byte[]) request.getAttribute(ATTRIBUTE_KEY_OAUTH2_INFO);
        if (oauth2InfoBytes != null) {
            UserOAuthInfoBean bean = SerializeUtil.deserialize(oauth2InfoBytes, UserOAuthInfoBean.class);
            return bean.getUnionid();
        }
        return super.getUsername(request);
    }

    @Override
    protected String getPassword(ServletRequest request) {
        // 获取已注册的第三方登录授权信息
        byte[] oauth2InfoBytes = (byte[]) request.getAttribute(ATTRIBUTE_KEY_OAUTH2_INFO);
        if (oauth2InfoBytes != null) {
            UserOAuthInfoBean bean = SerializeUtil.deserialize(oauth2InfoBytes, UserOAuthInfoBean.class);
            return bean.getUnionid();
        }
        return super.getPassword(request);
    }

    /**
     * 获取登录终端类型：APP、WEIXIN、QQ、SINA_WEIBO等
     *
     * @param request 请求
     * @return 终端类型
     */
    @Override
    protected String getClientType(ServletRequest request) {
        // 如果登录类型不为空，则传输
        String clientType = getOAuth2Type(request);
        return StringUtil.emptyDefault(clientType, super.getClientType(request));
    }

    /**
     * 拦截是否OAuth登录
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isOAuth2Login(request) && isOAuth2LoginSubmission(request, response)) {
            // 如果OAuth2用户不存在，则先创建再执行登录
            if (userService != null && appInfoService != null) {
                try {
                    oauth2Handler(request);

                    // 执行用户登录
                    return executeLogin(request, response);
                } catch (TipsException e) {
                    log.debug("第三方登录登录异常:{}", e.getMessage(), e);
                    request.setAttribute(ATTRIBUTE_KEY_FAILURE_MSG, e.getMessage());
                    request.setAttribute(ATTRIBUTE_KEY_FAILURE_MSG_TIPS, StringUtil.emptyDefault(e.getTips(), "第三方登录登录失败"));
                    return true;
                }
            } else {
                request.setAttribute(ATTRIBUTE_KEY_FAILURE_MSG, "系统不支持第三方登录");
                request.setAttribute(ATTRIBUTE_KEY_FAILURE_MSG_TIPS, "系统不支持第三方登录");
                return true;
            }
        }
        return super.onAccessDenied(request, response);
    }

    /**
     * 是否为OAuth2登录URL
     *
     * @param request 请求
     * @return 校验结果
     */
    private boolean isOAuth2Login(ServletRequest request) {
        return pathsMatch(DEFAULT_OAUTH2_LOGIN_URL, request);
    }

    /**
     * 是否为POST提交
     *
     * @param request  请求
     * @param response 响应
     * @return 检查结果
     */
    private boolean isOAuth2LoginSubmission(ServletRequest request, ServletResponse response) {
        return (request instanceof HttpServletRequest)
                && WebUtils.toHttp(request).getMethod().equalsIgnoreCase(POST_METHOD);
    }

    /**
     * 获取OAuth2类型
     *
     * @param request 请求
     * @return 返回类型字符串
     */
    private String getOAuth2Type(ServletRequest request) {
        return WebUtils.getCleanParam(request, this.getoAuth2TypeParam());
    }

    /**
     * 获取OAuth2授权码
     *
     * @param request 请求
     * @return 返回类型字符串
     */
    private String getOAuth2Code(ServletRequest request) {
        return WebUtils.getCleanParam(request, this.getoAuth2CodeParam());
    }

    /**
     * 获取OAuth2授权统一标识
     *
     * @param request 请求
     * @return 返回类型字符串
     */
    private String getOAuth2UnionId(ServletRequest request) {
        return WebUtils.getCleanParam(request, this.getoAuth2UnionIdParam());
    }

    /**
     * 根据配置的OAuth2类型，处理对应的请求
     *
     * @param request 请求
     */
    protected void oauth2Handler(ServletRequest request) {
        String code = getOAuth2Code(request);
        String unionid = getOAuth2UnionId(request);
        String oauth2Type = getOAuth2Type(request);

        // 根据第三方登录类型，获取处理器
        OAuth2Handler value = getOauth2().get(oauth2Type);
        if (value == null) {
            if (log.isErrorEnabled()) {
                log.error("第三方登录类型错误：oauth2Type={}", oauth2Type);
            }
            throw new TipsException(RestResultCode.CODE500.getCode(), "不支持的第三方登录类型", "第三方登录类型错误");
        }

        try {
            value.beforeHandler(request);
            doOauth2Handler(request);
        } catch (TipsException pe) {
            throw pe;
        } catch (RuntimeException re) {
            log.error("第三方登录异常:" + re.getMessage(), re);
            throw new TipsException(RestResultCode.CODE500.getCode(), "不支持的第三方登录类型", "第三方登录异常:" + re.getMessage());
        } finally {
            try {
                value.afterHandler(unionid);
            } catch (TipsException pe) {
                throw pe;
            } catch (RuntimeException ignored) {
                throw new TipsException(RestResultCode.CODE500,ignored);
            }
        }
    }

    /**
     * 处理登录
     *
     * @param request 请求
     */
    private void doOauth2Handler(ServletRequest request) {
        UserOAuthInfoBean userOAuthFromCode = null;
        String code = getOAuth2Code(request);
        String unionid = getOAuth2UnionId(request);
        // 存在授权统一标识符，则使用自动登录功能
        if (StringUtil.isNotEmpty(unionid)) {
            oauth2HandlerUnionId(request);
        } else if (StringUtil.isNotEmpty(code)) {
            userOAuthFromCode = oauth2HandlerCode(request);
            unionid = userOAuthFromCode.getUnionid();
        } else {
            throw new TipsException(RestResultCode.CODE500.getCode(),RestResultCode.CODE500.getTips(),"输入参数错误[code和unionid不可以同时为空]");
        }

        // 重新获取用户第三方授权信息，并存储到请求中
        UserOAuthInfoBean userOAuthInfo = userOAuthService.getUserOAuthInfoByUnionId(unionid);
        if (userOAuthInfo == null) {
            throw new TipsException(RestResultCode.CODE500.getCode(),RestResultCode.CODE500.getTips(),"数据库保存数据失败");
        }
        if (userOAuthInfo.getExts() == null) {
            userOAuthInfo.setExts(new LinkedHashMap<>());
        }
        if (userOAuthFromCode != null && userOAuthFromCode.getExts() != null) {
            userOAuthInfo.getExts().putAll(userOAuthFromCode.getExts());
        }
        request.setAttribute(ATTRIBUTE_KEY_OAUTH2_INFO, SerializeUtil.serialize(userOAuthInfo));
    }

    /**
     * OAuth2处理通过code登录
     *
     * @param request http请求
     */
    protected UserOAuthInfoBean oauth2HandlerCode(ServletRequest request) {
        String appCode = this.getAppCode(request);
        String oauth2Type = getOAuth2Type(request);
        // 根据第三方登录类型，获取处理器
        OAuth2Handler value = getOauth2().get(oauth2Type);
        AssertUtil.assertNotNull(value,"不支持的第三方登录类型");
        UserOAuthInfoBean bean = value.getUserOAuthInfoBean(appCode, getOAuth2Code(request));
        log.debug("第三方登录授权信息获取失败：UserOAuthInfoBean={}",bean);
        AssertUtil.assertNotNull(bean,"第三方登录授权信息获取失败");
        AssertUtil.assertNotEmpty(bean.getUnionid(),"第三方登录授权信息获取失败");

        String unionid = bean.getUnionid();
        // 检查用户统一标识是否已登录过
        UserInfoBean userInfo = userService.getUserInfoByLoginName(unionid);
        UserOAuthInfoBean userOAuthInfo = userOAuthService.getUserOAuthInfoByUnionId(unionid);
        Long userId = -1L;
        // 用户未注册过，注册一下
        if (userInfo == null) {
            AppInfoBean appInfo = appInfoService.getAppInfoBySystemCode(null, appCode);
            AssertUtil.assertNotNull(appCode,"APP不存在或已停用");
            userId = userService.insertUserInfo(unionid, unionid, unionid, appInfo.getSystemId(), UserType.SHOPPING,userInfo.getCreatePerson(),"");
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
        return bean;
    }

    /**
     * 处理第三方登录成功后，由于Session超时，重新登录
     *
     * @param request
     */
    protected void oauth2HandlerUnionId(ServletRequest request) {
        String unionid = getOAuth2UnionId(request);

        // 检查用户统一标识是否已登录过
        UserOAuthInfoBean userOAuthInfo = userOAuthService.getUserOAuthInfoByUnionId(unionid);
        AssertUtil.assertNotNull(userOAuthInfo,"授权码[" + unionid + "]已过期或不存在");
    }

    public String getoAuth2TypeParam() {
        return oAuth2TypeParam;
    }

    public void setoAuth2TypeParam(String oAuth2TypeParam) {
        this.oAuth2TypeParam = oAuth2TypeParam;
    }

    public String getoAuth2CodeParam() {
        return oAuth2CodeParam;
    }

    public void setoAuth2CodeParam(String oAuth2CodeParam) {
        this.oAuth2CodeParam = oAuth2CodeParam;
    }

    public String getoAuth2UnionIdParam() {
        return oAuth2UnionIdParam;
    }

    public void setoAuth2UnionIdParam(String oAuth2UnionIdParam) {
        this.oAuth2UnionIdParam = oAuth2UnionIdParam;
    }

    public Map<String, OAuth2Handler> getOauth2() {
        return oauth2;
    }

    public void setOauth2(Map<String, OAuth2Handler> oauth2) {
        this.oauth2 = oauth2;
    }

    public UserOAuthService getUserOAuthService() {
        return userOAuthService;
    }

    public void setUserOAuthService(UserOAuthService userOAuthService) {
        this.userOAuthService = userOAuthService;
    }
}
