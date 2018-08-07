package cn.faury.fdk.mobile.controller;

import cn.faury.fdk.common.entry.RestResultEntry;
import cn.faury.fdk.common.exception.TipsException;
import cn.faury.fdk.common.utils.StringUtil;
import cn.faury.fdk.mobile.core.FdkMobileInterfaceManager;
import cn.faury.fdk.mobile.exception.IntefaceInvokeException;
import cn.faury.fdk.mobile.annotation.IMobileService;
import cn.faury.fdk.shiro.core.FdkWebSessionManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 手机端首页Action
 */
@RestController
@RequestMapping("/mobile")
public class FdkMobileController extends MobileBaseController {

    /**
     * 日志记录
     */
    private Logger log = LoggerFactory.getLogger(this.getClass());

    /** 常量 ： 方法名 */
    public static final String P_METHOD = "m";
    /** 常量 ： 安卓版本号 */
    public static final String P_ANDROID_VERSION = "androidver";
    /** 常量 ： IOS版本号 */
    public static final String P_IOS_VERSION = "iosver";

    private ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();
    private ThreadLocal<HttpServletResponse> responseHolder = new ThreadLocal<>();

    /**
     * exec:(请求方法接口).
     */
    @RequestMapping("exec")
    public RestResultEntry exec(HttpServletRequest request, HttpServletResponse response) {
        requestHolder.set(request);
        responseHolder.set(response);

        String method = this.getRequestMethod();
        RestResultEntry result;
        // 验证方法
        if (StringUtil.isEmpty(method)) {
            result = this.rendNoSuchMethodError();
        } else {
            try {
                result = this.invokeMethod();
                if (result == null) {
                    throw new IntefaceInvokeException("接口返回结果为null", "无返回结果");
                }
            } catch (TipsException e){
                log.error("调用接口错误！", e);
                result = RestResultEntry.createErrorResult(e);
            } catch (Exception e) {
                log.error("调用接口异常！", e);
                result = this.rendSystemError();
            }
        }
        if (FdkMobileInterfaceManager.Config.MODE_PRO.equalsIgnoreCase(FdkMobileInterfaceManager.getConfigMode())) {
            result.setMessage(result.getTips());
        }
        response.setHeader("Access-Control-Allow-Origin", "*");
        return result;
    }

    /**
     * 执行方法调用
     *
     * @return 返回结果
     */
    private RestResultEntry invokeMethod() {
        String method = getRequestMethod();
        IMobileService ser = FdkMobileInterfaceManager.getMobileService(method);
        Boolean isAuthc = FdkMobileInterfaceManager.isAuthc(method);

        // 接口不存在
        if (ser == null) {
            return this.rendNoSuchMethodError();
        }

        // 非开发者模式，则进行版本过时验证
        if (!FdkMobileInterfaceManager.Config.MODE_DEV.equalsIgnoreCase(FdkMobileInterfaceManager.getConfigMode())
                && isVersionObsolete(method)) {
            return this.rendVersionObsoleteError();
        }

        // 配置参数设置需要权限验证
        if (FdkMobileInterfaceManager.getConfigAuth()) {
            // 需要登录验证
            if (isAuthc && !this.authenticationCookie()) {
                return this.rendNoLoginError();
            }
        }

        // 执行
        return ser.execute(requestHolder.get());
    }

    /**
     * 接口版本验证是否过时
     *
     * @param method 接口名称
     * @return 验证结果
     */
    private Boolean isVersionObsolete(String method) {
        // 验证接口是否过期 case1：配置为[obsolete]的接口
        if (FdkMobileInterfaceManager.isObsolete(method)) {
            return Boolean.TRUE;
        }
        // 是否配置接口过期验证
        Float reqAndroidVer = FdkMobileInterfaceManager.getAndroidVersion(method);
        Float reqIosVer = FdkMobileInterfaceManager.getIosVersion(method);
        if (reqAndroidVer != null || reqIosVer != null) {
            // 获取传入的安卓版本号或者IOS版本号
            Float androidVer = this.getRequestAndroidVersion();
            Float iosVer = this.getRequestIosVersion();
            if (androidVer == null && iosVer == null) {
                // case2：手机端没有传入相应的版本号
                // 由于没有传入相关参数无法判断是安卓还是IOS请求，
                // 所有如果只验证安卓接口，而ios请求没有传入版本号也会提示过期
                return Boolean.TRUE;
            }

            // 存在安卓版本号配置
            if (androidVer != null) {
                // 传入的版本号小于需求的版本号
                Float config = FdkMobileInterfaceManager.getAndroidVersion(method);
                if (config != null && androidVer < config) {
                    return Boolean.TRUE;
                }
            } else if (iosVer != null) {
                // 存在IOS版本号
                Float config = FdkMobileInterfaceManager.getIosVersion(method);
                if (config != null && iosVer < config) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 获取请求方法的值
     *
     * @return 请求方法的值
     */
    protected String getRequestMethod() {
        return requestHolder.get().getParameter(P_METHOD);
    }

    /**
     * 获取请求的Android版本号
     *
     * @return 安卓版本号
     */
    protected Float getRequestAndroidVersion() {
        try {
            String ver = requestHolder.get().getParameter(P_ANDROID_VERSION);
            // 如果没有传入版本号则从Session中尝试获取
            if (StringUtil.isNotEmpty(ver)) {
                return Float.parseFloat(ver);
            } else {
                // 获取SessionID
                String sessionId = getSessionId();
                if (StringUtil.isNotEmpty(sessionId)) {
                    Subject sub = new Subject.Builder().sessionId(sessionId).buildSubject();
                    Session session = sub.getSession(false);
                    // 存在Session则尝试获取
                    if (session != null && session.getAttribute(P_ANDROID_VERSION) != null
                            && session.getAttribute(P_ANDROID_VERSION) instanceof String) {
                        return Float.parseFloat((String) session.getAttribute(P_ANDROID_VERSION));
                    }
                }
            }
        } catch (RuntimeException re) {
            log.debug("获取安卓版本号异常:" + re.getMessage(), re);
        }
        return null;
    }

    /**
     * 获取请求的Ios版本号
     *
     * @return Ios版本号
     */
    protected Float getRequestIosVersion() {
        try {
            String ver = requestHolder.get().getParameter(P_IOS_VERSION);
            if (StringUtil.isNotEmpty(ver)) {
                return Float.parseFloat(ver);
            } else {
                // 获取SessionID
                String sessionId = getSessionId();
                if (StringUtil.isNotEmpty(sessionId)) {
                    Subject sub = new Subject.Builder().sessionId(sessionId).buildSubject();
                    Session session = sub.getSession(false);
                    // 存在Session则尝试获取
                    if (session != null && session.getAttribute(P_IOS_VERSION) != null
                            && session.getAttribute(P_IOS_VERSION) instanceof String) {
                        return Float.parseFloat((String) session.getAttribute(P_IOS_VERSION));
                    }
                }
            }
        } catch (RuntimeException re) {
            log.debug("获取IOS版本号异常:" + re.getMessage(), re);
        }
        return null;
    }

    @Override
    protected String getSessionId() {
        return FdkWebSessionManager.resolveSessionId(requestHolder.get());
    }
}
