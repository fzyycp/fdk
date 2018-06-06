package cn.faury.fdk.shiro.exception;

import org.apache.shiro.authc.CredentialsException;

/**
 * 未授权的业务系统异常
 */
public class UnauthorizedSystemException extends CredentialsException implements IMessageAccountException {

	/**
	 * 异常信息
	 */
	private static final String DFT_MESSAGE = "用户未授权登录该业务系统";

	/**
	 * 构造函数
	 */
	public UnauthorizedSystemException() {
		this(DFT_MESSAGE);
	}

	/**
	 * 构造函数
	 * 
	 * @param message
	 *            异常信息
	 */
	public UnauthorizedSystemException(String message) {
		super(message);
	}

	/**
	 * 构造函数
	 * 
	 * @param cause
	 *            异常原因
	 */
	public UnauthorizedSystemException(Throwable cause) {
		super(cause);
	}

	/**
	 * 构造函数
	 * 
	 * @param message
	 *            异常信息
	 * @param cause
	 *            异常原因
	 */
	public UnauthorizedSystemException(String message, Throwable cause) {
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
