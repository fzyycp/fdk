/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 5/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.common.exception;

import cn.faury.fdk.common.entry.RestResultCode;
import cn.faury.fdk.common.utils.StringUtil;

/**
 * 带提示的通用异常类
 */
public class TipsException extends RuntimeException {

    /**
     * 错误码
     */
    private String code = StringUtil.EMPTY_STR;

    /**
     * 用户提示信息
     */
    private String tips = StringUtil.EMPTY_STR;

    /**
     * 构造函数
     *
     * @param resultCode 错误码
     */
    public TipsException(RestResultCode resultCode) {
        this(resultCode.getCode(), resultCode.getTips(), resultCode.getMessage());
    }

    /**
     * 构造函数
     *
     * @param code 错误码
     * @param tips 用户提示信息
     */
    public TipsException(String code, String tips) {
        this(code, tips, String.format("code=%s,tips=%s", code, tips));
    }

    /**
     * 构造函数
     *
     * @param code  错误码
     * @param tips  用户提示信息
     * @param cause 错误原因
     */
    public TipsException(String code, String tips, Throwable cause) {
        this(code, tips, String.format("code=%s,tips=%s", code, tips), cause);
    }

    /**
     * 构造函数
     *
     * @param code    错误码
     * @param tips    用户提示信息
     * @param message 信息
     */
    public TipsException(String code, String tips, String message) {
        super(message);
        this.code = code;
        this.tips = tips;
    }

    /**
     * 构造函数
     *
     * @param code    错误码
     * @param tips    用户提示信息
     * @param message 信息
     * @param cause   错误原因
     */
    public TipsException(String code, String tips, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.tips = tips;
    }

    /**
     * 构造函数
     *
     * @param code               错误码
     * @param tips               用户提示信息
     * @param message            信息
     * @param cause              错误原因
     * @param enableSuppression  是否允许抛出
     * @param writableStackTrace 是否写入堆栈
     */
    public TipsException(String code, String tips, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
        this.tips = tips;
    }

    /**
     * 获取错误码
     *
     * @return 错误码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置错误码
     *
     * @param code 错误码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取用户提示信息
     *
     * @return 用户提示信息
     */
    public String getTips() {
        return tips;
    }

    /**
     * 设置用户提示信息
     *
     * @param tips 用户提示信息
     */
    public void setTips(String tips) {
        this.tips = tips;
    }

    @Override
    public String toString() {
        return "TipsException{" +
                "code='" + code + '\'' +
                ", tips='" + tips + '\'' +
                ", message='" + getMessage() + '\'' +
                '}';
    }
}
