package cn.faury.fdk.shiro.filter.captcha;

import org.apache.shiro.authc.AuthenticationException;


/**
 * 验证码失败异常
 */
public class FdkCaptchaValidateException extends AuthenticationException {

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = -66565762239417245L;

	/**
	 * Creates a new JCaptchaValidateException.
	 */
	public FdkCaptchaValidateException() {
		super();
	}

	/**
	 * Constructs a new JCaptchaValidateException.
	 *
	 * @param message
	 *            the reason for the exception
	 */
	public FdkCaptchaValidateException(String message) {
		super(message);
	}

	/**
	 * Constructs a new JCaptchaValidateException.
	 *
	 * @param cause
	 *            the underlying Throwable that caused this exception to be
	 *            thrown.
	 */
	public FdkCaptchaValidateException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new JCaptchaValidateException.
	 *
	 * @param message
	 *            the reason for the exception
	 * @param cause
	 *            the underlying Throwable that caused this exception to be
	 *            thrown.
	 */
	public FdkCaptchaValidateException(String message, Throwable cause) {
		super(message, cause);
	}
}
