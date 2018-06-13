package cn.faury.fdk.shiro.filter;

import cn.faury.fdk.shiro.exception.IMessageAccountException;
import cn.faury.fdk.shiro.filter.captcha.FdkCaptchaConst;
import cn.faury.fdk.shiro.filter.captcha.FdkCaptchaValidateException;
import org.apache.shiro.authc.*;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


/**
 * 验证码验证拦截
 */
public class FdkCaptchaValidateAuthenticationFilter extends FormAuthenticationFilter {

	/**
	 * 日志记录器
	 */
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		// 在执行登录的时候进行验证
		if (isLoginRequest(request, response) && isLoginSubmission(request, response)) {
			// 是否启用验证
			Object isEnabled = request.getAttribute(FdkCaptchaConst.ATTRIBUTE_KEY_CAPTCHA_ENABLED);
			if (isEnabled instanceof Boolean && ((Boolean) isEnabled).booleanValue()) {
				// 验证验证码是否正确
				Object validate = request.getAttribute(getFailureKeyAttribute());
				if ((validate instanceof Boolean && false == ((Boolean) validate).booleanValue())
				        || (validate instanceof String && FdkCaptchaConst.ATTRIBUTE_VALUE_FAILURE.equals(validate))) {
					// 验证失败
					this.setFailureMessage(request, response);
					return true;
				}
			}
		}
		boolean superDenied = super.onAccessDenied(request, response);
		this.setFailureMessage(request, response);
		return superDenied;
	}

	@Override
	protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
		log.error("登录异常：" + ae.getMessage(), ae);
		super.setFailureAttribute(request, ae);
	}

	/**
	 * 设置错误信息
	 * 
	 * @param request
	 *            请求
	 * @param response
	 *            响应
	 */
	protected void setFailureMessage(ServletRequest request, ServletResponse response) {
		// 检查是否有异常信息
		Object fail = request.getAttribute(getFailureKeyAttribute());
		if (fail == null) {
			return;
		}

		// 开始处理异常信息
		log.debug("登录异常：" + (String) fail);
		if (IncorrectCredentialsException.class.getName().equals(fail)) {
			request.setAttribute(FdkCaptchaConst.ATTRIBUTE_KEY_FAILURE_MSG, "用户名密码不匹配！");
		} else if (ExpiredCredentialsException.class.getName().equals(fail)) {
			request.setAttribute(FdkCaptchaConst.ATTRIBUTE_KEY_FAILURE_MSG, "用户密码已过期！");
		} else if (UnknownAccountException.class.getName().equals(fail)) {
			request.setAttribute(FdkCaptchaConst.ATTRIBUTE_KEY_FAILURE_MSG, "用户不存在或密码错误！");
		} else if (LockedAccountException.class.getName().equals(fail)) {
			request.setAttribute(FdkCaptchaConst.ATTRIBUTE_KEY_FAILURE_MSG, "用户账号已锁定！");
		} else if (DisabledAccountException.class.getName().equals(fail)) {
			request.setAttribute(FdkCaptchaConst.ATTRIBUTE_KEY_FAILURE_MSG, "用户账号已禁用！");
		} else if (FdkCaptchaValidateException.class.getName().equals(fail)) {
			request.setAttribute(FdkCaptchaConst.ATTRIBUTE_KEY_FAILURE_MSG, "验证码不正确！");
		} else if (AuthenticationException.class.getName().equals(fail)) {
			log.error("Shiro认证异常：" + (String) fail);
			request.setAttribute(FdkCaptchaConst.ATTRIBUTE_KEY_FAILURE_MSG, "用户认证失败！");
		} else {
			log.error("未知异常：" + (String) fail);
			request.setAttribute(FdkCaptchaConst.ATTRIBUTE_KEY_FAILURE_MSG, "登录失败！");

			// 尝试通过反射确认是否为自定义的异常信息，并获取默认消息返回
			try {
				Class<?> clazz = Class.forName((String) fail);
				Object obj = clazz.newInstance();
				if (obj instanceof IMessageAccountException) {
					IMessageAccountException exc = (IMessageAccountException) obj;
					if (exc != null && exc.getDefaultExceptionMessage() != null
					        && exc.getDefaultExceptionMessage().length() > 0) {
						request.setAttribute(FdkCaptchaConst.ATTRIBUTE_KEY_FAILURE_MSG,
						        exc.getDefaultExceptionMessage());
					}
				}
			} catch (Exception e) {
				log.error("反射处理异常", e);
			}
		}
	}
}
