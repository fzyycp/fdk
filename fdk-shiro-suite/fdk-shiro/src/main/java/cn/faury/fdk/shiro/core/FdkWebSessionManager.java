package cn.faury.fdk.shiro.core;

import cn.faury.fdk.common.utils.AssertUtil;
import cn.faury.fdk.common.utils.StringUtil;
import cn.faury.fdk.shiro.utils.SessionUtil;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义Session管理器
 */
public class FdkWebSessionManager extends DefaultWebSessionManager {

    // 校验头
    public static final String AUTHORIZATION_HEADER = "FdkAuthorization";

    // 请求参数
    public static final String AUTHORIZATION_PARAM = "S";

    // sessionId来源: header
    public static final String REFERENCED_SESSION_ID_SOURCE_HEADER = "Fdk stateless request header";
    // sessionId来源: cookie
    public static final String REFERENCED_SESSION_ID_SOURCE_COOKIE = "Fdk stateless request cookie";
    // sessionId来源: parameter
    public static final String REFERENCED_SESSION_ID_SOURCE_PARAM = "Fdk stateless request parameter";

    // 自定义获取SessionID
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        String sessionId = resolveSessionId(httpServletRequest);

        return StringUtil.isEmpty(sessionId)? super.getSessionId(request,response) : sessionId;
    }

    /**
     * 从请求中解析Session Id
     *
     * <pre>
     *     1，先从Header中读取FdkAuthorization字段作为Session ID
     *     2，Header中不存在，则从Cookie中读取FdkAuthorization字段作为Session ID
     *     3，Cookie中不存在，则从request请求参数中读取S参数作为Session ID
     *     4，以上情况都没有时，返回null
     * </pre>
     *
     * @param httpServletRequest request请求
     * @return session id
     */
    public static String resolveSessionId(HttpServletRequest httpServletRequest) {
        AssertUtil.assertNotNull(httpServletRequest,"request请求不可以为空");

        String sessionId = httpServletRequest.getHeader(FdkWebSessionManager.AUTHORIZATION_HEADER);
        String source = FdkWebSessionManager.REFERENCED_SESSION_ID_SOURCE_HEADER;
        // Header中没有，从Cookie中获取
        if (StringUtil.isEmpty(sessionId) && httpServletRequest.getCookies() != null) {
            Cookie[] cookies = httpServletRequest.getCookies();
            List<Cookie> cookieList = Arrays.stream(cookies)
                    .filter(cookie -> FdkWebSessionManager.AUTHORIZATION_HEADER.equals(cookie.getName()))
                    .collect(Collectors.toList());
            if (cookieList != null && cookieList.size() > 0) {
                sessionId = cookieList.get(0).getValue();
                source = FdkWebSessionManager.REFERENCED_SESSION_ID_SOURCE_COOKIE;
            }
        }
        // Header和Cookie中都没有，从request中获取
        if (StringUtil.isEmpty(sessionId)) {
            sessionId = httpServletRequest.getParameter(FdkWebSessionManager.AUTHORIZATION_PARAM);
            source = FdkWebSessionManager.REFERENCED_SESSION_ID_SOURCE_PARAM;
        }

        if (StringUtil.isNotEmpty(sessionId)) {
            httpServletRequest.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, source);
            httpServletRequest.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, sessionId);
            httpServletRequest.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return sessionId;
        }
        return null;
    }
}
