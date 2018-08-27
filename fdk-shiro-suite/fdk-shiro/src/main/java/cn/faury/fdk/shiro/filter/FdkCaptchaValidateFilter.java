package cn.faury.fdk.shiro.filter;

import cn.faury.fdk.captcha.config.FdkCaptchaConfig;
import cn.faury.fdk.common.utils.StringUtil;
import cn.faury.fdk.shiro.filter.captcha.FdkCaptchaConst;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 图片验证码过滤器
 */
public class FdkCaptchaValidateFilter extends AccessControlFilter {

    // captcha配置属性
    private FdkCaptchaConfig fdkCaptchaConfig;

    public FdkCaptchaValidateFilter(FdkCaptchaConfig fdkCaptchaConfig) {
        this.fdkCaptchaConfig = fdkCaptchaConfig;
    }

    /*
         * (non-Javadoc)
         * @see org.apache.shiro.web.filter.AccessControlFilter#isAccessAllowed(javax.servlet.ServletRequest, javax.servlet.ServletResponse, java.lang.Object)
         */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws Exception {
        // 1、设置验证码是否开启属性，页面可以根据该属性来决定是否显示验证码
        request.setAttribute(FdkCaptchaConst.ATTRIBUTE_KEY_CAPTCHA_ENABLED, fdkCaptchaConfig == null || fdkCaptchaConfig.isEnable());

        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        // 2、判断验证码是否禁用 或不是表单提交（允许访问）
        if ((fdkCaptchaConfig != null && !fdkCaptchaConfig.isEnable())
                || !"post".equalsIgnoreCase(httpServletRequest.getMethod())) {
            return true;
        }
        // 3、此时是表单提交，验证验证码是否正确
        String captcha = (String) httpServletRequest.getSession().getAttribute(fdkCaptchaConfig.getSessionKey());
        String inputCaptcha = httpServletRequest.getParameter(fdkCaptchaConfig.getRequestName());
        return StringUtil.isNotEmpty(inputCaptcha) && inputCaptcha.trim().equalsIgnoreCase(captcha);
//		return JCaptcha.validateResponse(httpServletRequest, httpServletRequest.getParameter(jcaptchaParam));
    }

    /*
     * (non-Javadoc)
     * @see org.apache.shiro.web.filter.AccessControlFilter#onAccessDenied(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        // 如果验证码失败了，存储失败key属性
        request.setAttribute(FdkCaptchaConst.ATTRIBUTE_KEY_FAILURE, FdkCaptchaConst.ATTRIBUTE_VALUE_FAILURE);
        return true;
    }

}
