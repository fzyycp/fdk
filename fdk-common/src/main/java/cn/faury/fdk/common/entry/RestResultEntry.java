/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 6/2018
 # @author faury
 #############################################################################*/

package cn.faury.fdk.common.entry;

import cn.faury.fdk.common.exception.TipsException;

import java.util.ArrayList;
import java.util.List;

/**
 * Rest接口返回实体
 */
public class RestResultEntry {

    /**
     * 消息
     */
    String message;

    /**
     * 用户提示信息
     */
    String tips;

    /**
     * 是否成功
     */
    boolean success;

    /**
     * 返回码
     */
    String code;

    /**
     * 返回数据结构
     */
    List<?> data;

    /**
     * 构造函数
     */
    public RestResultEntry() {
        RestResultEntry.createSuccessResult(null);
    }

    /**
     * 构造函数
     *
     * @param success 是否成功
     * @param code    返回码
     * @param message 消息
     * @param data    数据
     */
    public RestResultEntry(boolean success, String code, String message, List<?> data) {
        this.setCode(code);
        this.setData(data);
        this.setMessage(message);
        this.setSuccess(success);
        this.setTips(message);
    }

    /**
     * 构造函数
     *
     * @param success 是否成功
     * @param code    返回码
     * @param message 消息
     * @param tips    用户提示信息
     * @param data    数据
     */
    public RestResultEntry(boolean success, String code, String message, String tips, List<?> data) {
        this.setCode(code);
        this.setData(data);
        this.setMessage(message);
        this.setTips(tips);
        this.setSuccess(success);
    }

    /**
     * 根据输入参数生成一个返回结果集
     *
     * @param success 是否成功
     * @param code    返回码
     * @param message 消息
     * @param tips    用户提示信息
     * @param data    数据
     * @return 结果对象
     */
    public static RestResultEntry createResult(boolean success, String code, String message, String tips, List<?> data) {
        return new RestResultEntry(success, code, message, tips, data);
    }

    /**
     * 根据输入参数生成一个返回结果集
     *
     * @param data 数据
     * @return 结果对象
     */
    public static RestResultEntry createSuccessResult(List<?> data) {
        return new RestResultEntry(true, RestResultCode.CODE200.getCode(), RestResultCode.CODE200.getMessage(), data);
    }

    /**
     * 根据输入参数生成一个返回结果集
     *
     * @param te 执行异常
     * @return 结果对象
     */
    public static RestResultEntry createErrorResult(TipsException te) {
        return createErrorResult(te.getMessage(), te.getTips());
    }

    /**
     * 根据输入参数生成一个返回结果集
     *
     * @param message 消息
     * @param tips    用户提示信息
     * @return 结果对象
     */
    public static RestResultEntry createErrorResult(String message, String tips) {
        return new RestResultEntry(false, RestResultCode.CODE500.getCode(), message, tips, null);
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message == null ? "" : message;
    }

    /**
     * @return the tips
     */
    public String getTips() {
        return tips;
    }

    /**
     * @param tips the tips to set
     */
    public void setTips(String tips) {
        this.tips = tips;
    }

    /**
     * @return the success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @param success the success to set
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code == null ? RestResultCode.CODE200.getCode() : code;
    }

    /**
     * @return the data
     */
    public List<?> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(List<?> data) {
        this.data = data == null ? new ArrayList<>() : data;
    }

}
