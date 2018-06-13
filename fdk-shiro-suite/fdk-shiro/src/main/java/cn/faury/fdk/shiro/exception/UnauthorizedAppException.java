package cn.faury.fdk.shiro.exception;

import org.apache.shiro.authc.CredentialsException;


/**
 * 未授权的APP异常
 */
public class UnauthorizedAppException extends CredentialsException implements IMessageAccountException {

	/**
	 * 异常信息
	 */
	private static final String DFT_MESSAGE = "用户未授权登录该APP系统";

	/**
	 * 构造函数
	 */
	public UnauthorizedAppException() {
		this(DFT_MESSAGE);
	}

	/**
	 * 构造函数
	 * 
	 * @param message
	 *            错误消息
	 */
	public UnauthorizedAppException(String message) {
		super(message);
	}

	/**
	 * 构造函数
	 * 
	 * @param cause
	 *            异常原因
	 */
	public UnauthorizedAppException(Throwable cause) {
		super(cause);
	}

	/**
	 * 构造函数
	 * 
	 * @param message
	 *            错误消息
	 * @param cause
	 *            异常原因
	 */
	public UnauthorizedAppException(String message, Throwable cause) {
		super(message, cause);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.wassk.framework.shiroplugin.exception.IMessageAccountException#
	 * getDefaultExceptionMessage()
	 */
	@Override
	public String getDefaultExceptionMessage() {
		return DFT_MESSAGE;
	}

}
