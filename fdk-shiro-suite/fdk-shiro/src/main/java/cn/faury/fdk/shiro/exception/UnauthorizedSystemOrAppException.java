package cn.faury.fdk.shiro.exception;

import org.apache.shiro.authc.CredentialsException;

/**
 * 未授权的业务系统或者APP异常
 */
public class UnauthorizedSystemOrAppException extends CredentialsException implements IMessageAccountException {

	/**
	 * 异常信息
	 */
	private static final String DFT_MESSAGE = "用户未授权登录该业务系统或APP系统";

	/**
	 * 构造函数
	 */
	public UnauthorizedSystemOrAppException() {
		this(DFT_MESSAGE);
	}

	/**
	 * 构造函数
	 * 
	 * @param msg
	 *            异常信息
	 */
	public UnauthorizedSystemOrAppException(String msg) {
		super(msg);
	}

	/**
	 * 构造函数
	 * 
	 * @param message
	 *            异常信息
	 * @param cause
	 *            异常原因
	 */
	public UnauthorizedSystemOrAppException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 构造函数
	 * 
	 * @param cause
	 *            异常原因
	 */
	public UnauthorizedSystemOrAppException(Throwable cause) {
		super(cause);
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
