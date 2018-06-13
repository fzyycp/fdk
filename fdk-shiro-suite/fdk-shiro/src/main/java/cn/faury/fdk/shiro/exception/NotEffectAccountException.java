package cn.faury.fdk.shiro.exception;

import org.apache.shiro.authc.DisabledAccountException;


/**
 * 账号未生效异常
 */
public class NotEffectAccountException extends DisabledAccountException implements IMessageAccountException {
	/**
	 * 异常信息
	 */
	public static final String DFT_MESSAGE = "用户账号未生效";

	/**
	 * 构造函数
	 */
	public NotEffectAccountException() {
		this(DFT_MESSAGE);
	}

	/**
	 * 构造函数
	 * 
	 * @param message
	 *            错误消息
	 */
	public NotEffectAccountException(String message) {
		super(message);
	}

	/**
	 * 构造函数
	 * 
	 * @param cause
	 *            异常原因
	 */
	public NotEffectAccountException(Throwable cause) {
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
	public NotEffectAccountException(String message, Throwable cause) {
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
