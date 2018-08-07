package cn.faury.fdk.shiro.filter.captcha;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

/**
 * 共通常量
 */
public interface FdkCaptchaConst {

	/**
	 * 验证码验证失败后存储到Request的属性名
	 */
	String ATTRIBUTE_KEY_FAILURE = FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME;
//
//	/**
//	 * 验证码验证失败后错误信息存储到Request的属性名
//	 */
//	String ATTRIBUTE_KEY_FAILURE_MSG = "fdkCaptchaErrorMsg";

	/**
	 * 错误信息
	 */
	String ATTRIBUTE_KEY_FAILURE_MSG = "shiroLoginFailureMsg";

	/**
	 * 用户提示信息
	 */
	String ATTRIBUTE_KEY_FAILURE_MSG_TIPS = "shiroLoginFailureMsgTips";

	/**
	 * 验证码验证失败后存储到Request的属性值
	 */
	String ATTRIBUTE_VALUE_FAILURE = FdkCaptchaValidateException.class.getName();
	
	/**
	 * 验证码是否启用存储到Request的属性名
	 */
	String ATTRIBUTE_KEY_CAPTCHA_ENABLED = "fdkCaptchaEnabled";
}
