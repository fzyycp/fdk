package cn.faury.fdk.mobile.exception;


import cn.faury.fdk.common.entry.RestResultCode;
import cn.faury.fdk.common.exception.TipsException;

/**
 * 重复的接口名异常
 */
public class DuplicateIntefaceException extends TipsException {

	/**
     * 
     */
    public DuplicateIntefaceException() {
	    super(RestResultCode.CODE500);
    }

	/**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public DuplicateIntefaceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
	    super(RestResultCode.CODE500.getCode(),RestResultCode.CODE500.getTips(),message, cause, enableSuppression, writableStackTrace);
    }

	/**
     * @param message
     * @param cause
     */
    public DuplicateIntefaceException(String message, Throwable cause) {
	    super(RestResultCode.CODE500.getCode(),RestResultCode.CODE500.getTips(),message, cause);
    }

	/**
     * @param message
     */
    public DuplicateIntefaceException(String message) {
	    super(RestResultCode.CODE500.getCode(),RestResultCode.CODE500.getTips(),message);
    }

	/**
     * @param cause
     */
    public DuplicateIntefaceException(Throwable cause) {
	    super(RestResultCode.CODE500.getCode(),RestResultCode.CODE500.getTips(),cause);
    }

}
