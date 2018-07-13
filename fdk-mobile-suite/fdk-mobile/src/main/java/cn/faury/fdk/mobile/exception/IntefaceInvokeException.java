package cn.faury.fdk.mobile.exception;


import cn.faury.fdk.common.entry.RestResultCode;
import cn.faury.fdk.common.exception.TipsException;

/**
 * 接口执行异常
 */
public class IntefaceInvokeException extends TipsException {

	/**
	 * 构造函数
	 */
	public IntefaceInvokeException() {
		super(RestResultCode.CODE500.getCode(),RestResultCode.CODE500.getTips());
	}

	/**
	 * 构造函数
	 * 
	 * @param message
	 *            调试信息
	 * @param tips
	 *            用户提示信息
	 */
	public IntefaceInvokeException(String message, String tips) {
		super(RestResultCode.CODE500.getCode(),tips,message);
	}

	/**
	 * 构造函数
	 * 
	 * @param cause
	 *            错误原因
	 */
	public IntefaceInvokeException(Throwable cause) {
		super(RestResultCode.CODE500.getCode(),RestResultCode.CODE500.getTips(),cause);
	}

	/**
	 * 构造函数
	 * 
	 * @param message
	 *            错误信息
	 * @param tips
	 *            用户提示信息
	 * @param cause
	 *            错误原因
	 */
	public IntefaceInvokeException(String message, String tips, Throwable cause) {
		super(RestResultCode.CODE500.getCode(),tips,message, cause);
	}

	/**
	 * 构造函数
	 * 
	 * @param message
	 *            错误消息
	 * @param tips
	 *            用户提示信息
	 * @param cause
	 *            错误原因
	 * @param enableSuppression
	 *            whether or not suppression is enabled or disabled
	 * @param writableStackTrace
	 *            whether or not the stack trace should be writable
	 */
	public IntefaceInvokeException(String message, String tips, Throwable cause, boolean enableSuppression,
	        boolean writableStackTrace) {
		super(RestResultCode.CODE500.getCode(),tips,message, cause, enableSuppression, writableStackTrace);
	}
}
