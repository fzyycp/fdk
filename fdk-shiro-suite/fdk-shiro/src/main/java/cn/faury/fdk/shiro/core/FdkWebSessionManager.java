package cn.faury.fdk.shiro.core;

import cn.faury.fdk.common.utils.StringUtil;
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

    // sessionId来源
    private static final String REFERENCED_SESSION_ID_SOURCE = "Fdk stateless request";

    // 自定义获取SessionID
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        String sessionId = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
        // Heather中没有，从Cookie中获取
        if (StringUtil.isEmpty(sessionId) && httpServletRequest.getCookies()!=null) {
            Cookie[] cookies = httpServletRequest.getCookies();
            List<Cookie> cookieList = Arrays.stream(cookies)
                    .filter(cookie -> AUTHORIZATION_HEADER.equals(cookie.getName()))
                    .collect(Collectors.toList());
            if (cookieList != null && cookieList.size() > 0) {
                sessionId = cookieList.get(0).getValue();
            }
        }
        if (StringUtil.isNotEmpty(sessionId)) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, sessionId);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return sessionId;
        }
        return super.getSessionId(request, response);
    }
}
