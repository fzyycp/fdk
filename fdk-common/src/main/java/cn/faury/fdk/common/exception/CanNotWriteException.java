/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 6/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.common.exception;


import cn.faury.fdk.common.entry.RestResultCode;
import cn.faury.fdk.common.utils.StringUtil;

/**
 * 业务平台服务只写异常
 */
public class CanNotWriteException extends TipsException {

    /**
     * 错误消息常量
     */
    private final static String DEFAULT_MESSAGE = "This Service Can Not Write";

    /**
     * 构造函数
     */
    public CanNotWriteException() {
        super(RestResultCode.CODE500.getCode(), RestResultCode.CODE500.getTips(), DEFAULT_MESSAGE);
    }

    /**
     * 构造函数
     *
     * @param message 信息
     */
    public CanNotWriteException(String message) {
        super(RestResultCode.CODE500.getCode(), RestResultCode.CODE500.getTips(), StringUtil.emptyDefault(message, DEFAULT_MESSAGE));
    }

    /**
     * 构造函数
     *
     * @param cause 错误原因
     */
    public CanNotWriteException(Throwable cause) {
        super(RestResultCode.CODE500.getCode(), RestResultCode.CODE500.getTips(), DEFAULT_MESSAGE, cause);
    }

    /**
     * 构造函数
     *
     * @param message 信息
     * @param cause   错误原因
     */
    public CanNotWriteException(String message, Throwable cause) {
        super(RestResultCode.CODE500.getCode(), RestResultCode.CODE500.getTips(), StringUtil.emptyDefault(message, DEFAULT_MESSAGE), cause);
    }

    /**
     * 构造函数
     *
     * @param message            信息
     * @param cause              错误原因
     * @param enableSuppression  是否允许抛出
     * @param writableStackTrace 是否写入堆栈
     */
    public CanNotWriteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(RestResultCode.CODE500.getCode(), RestResultCode.CODE500.getTips(), StringUtil.emptyDefault(message, DEFAULT_MESSAGE), cause, enableSuppression, writableStackTrace);
    }
}
