/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 6/2018
 # @author faury
 #############################################################################*/

package cn.faury.fdk.common.entry;

import cn.faury.fdk.common.anotation.NonNull;
import cn.faury.fdk.common.exception.TipsException;
import cn.faury.fdk.common.utils.JsonUtil;
import cn.faury.fdk.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
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
     * @param data 数据
     * @return 结果对象
     */
    public static <T> RestResultEntry createSuccessResult(T data) {
        return createSuccessResult(Collections.singletonList(data));
    }

    /**
     * 根据输入参数生成一个返回结果集
     *
     * @param te 执行异常
     * @return 结果对象
     */
    public static RestResultEntry createErrorResult(TipsException te) {
        return createErrorResult(te.getCode(), te.getMessage(), te.getTips());
    }

    /**
     * 根据输入参数生成一个返回结果集
     *
     * @param message 消息
     * @param tips    用户提示信息
     * @return 结果对象
     */
    public static RestResultEntry createErrorResult(String message, String tips) {
        return createErrorResult(RestResultCode.CODE500.getCode(), message, tips);
    }

    /**
     * 根据输入参数生成一个返回结果集
     *
     * @param tips    用户提示信息
     * @return 结果对象
     */
    public static RestResultEntry createErrorResult(String tips) {
        return createErrorResult(tips, tips);
    }

    /**
     * 根据输入参数生成一个返回结果集
     *
     * @param code    错误码
     * @param message 消息
     * @param tips    用户提示信息
     * @return 结果对象
     */
    public static RestResultEntry createErrorResult(String code, String message, String tips) {
        return new RestResultEntry(false, StringUtil.emptyDefault(code, RestResultCode.CODE500.getCode()), message, tips, null);
    }

    /**
     * 根据输入参数生成一个返回结果集
     *
     * @param resultCode 错误码
     * @return 结果对象
     */
    public static RestResultEntry createErrorResult(@NonNull RestResultCode resultCode) {
        return new RestResultEntry(false, resultCode.getCode(), resultCode.getMessage(), resultCode.getTips(), null);
    }

    /**
     * 获取message
     *
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置message
     *
     * @param message 值
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取tips
     *
     * @return tips
     */
    public String getTips() {
        return tips;
    }

    /**
     * 设置tips
     *
     * @param tips 值
     */
    public void setTips(String tips) {
        this.tips = tips;
    }

    /**
     * 获取success
     *
     * @return success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * 设置success
     *
     * @param success 值
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * 获取code
     *
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置code
     *
     * @param code 值
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取data
     *
     * @return data
     */
    public List<?> getData() {
        return data;
    }

    /**
     * 设置data
     *
     * @param data 值
     */
    public void setData(List<?> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JsonUtil.objectToJson(this);
    }
}
