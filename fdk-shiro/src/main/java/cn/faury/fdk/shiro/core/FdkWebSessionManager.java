package cn.faury.fdk.shiro.core;

import cn.faury.fdk.common.utils.StringUtil;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * 自定义Session管理器
 */
public class FdkWebSessionManager extends DefaultWebSessionManager {

    // 校验头
    private static final String AUTHORIZATION = "FDK_Authorization";

    // sessionId来源
    private static final String REFERENCED_SESSION_ID_SOURCE = "Fdk stateless request";

    // 自定义获取SessionID
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String sessionId = WebUtils.toHttp(request).getHeader(AUTHORIZATION);
        if(StringUtil.isNotEmpty(sessionId)){
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,REFERENCED_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID,sessionId);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID,Boolean.TRUE);
            return sessionId;
        }
        return super.getSessionId(request, response);
    }
}
