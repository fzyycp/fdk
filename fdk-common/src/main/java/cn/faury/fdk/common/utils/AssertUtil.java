/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 6/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.common.utils;

import cn.faury.fdk.common.entry.RestResultCode;
import cn.faury.fdk.common.exception.TipsException;

/**
 * 参数判断
 */
public class AssertUtil {
    /**
     * 构造函数
     */
    protected AssertUtil() {
    }

    /**
     * 检查表达式是否成立，不成立抛出异常
     *
     * @param expression 表达式
     * @param re         异常信息
     */
    public static void assertTrue(boolean expression, RuntimeException re) {
        if (!expression) {
            throw re;
        }
    }

    /**
     * 检查表达式是否成立，不成立抛出异常
     *
     * @param expression 表达式
     * @param code       错误码
     * @param tips       不成立时异常信息
     */
    public static void assertTrue(boolean expression, String code, String tips) {
        assertTrue(expression, new TipsException(code, tips));
    }

    /**
     * 检查表达式是否成立，不成立抛出异常
     *
     * @param expression 表达式
     * @param tips       不成立时异常信息
     */
    public static void assertTrue(boolean expression, String tips) {
        assertTrue(expression, RestResultCode.CODE402.getCode(), tips);
    }

    /**
     * 检查表达式是否不成立，成立抛出异常
     *
     * @param expression 表达式
     * @param re         异常信息
     */
    public static void assertFalse(boolean expression, RuntimeException re) {
        assertTrue(!expression, re);
    }

    /**
     * 检查表达式是否不成立，成立抛出异常
     *
     * @param expression 表达式
     * @param code       错误码
     * @param tips       不成立时异常信息
     */
    public static void assertFalse(boolean expression, String code, String tips) {
        assertFalse(expression, new TipsException(code, tips));
    }

    /**
     * 检查表达式是否不成立，成立抛出异常
     *
     * @param expression 表达式
     * @param tips       不成立时异常信息
     */
    public static void assertFalse(boolean expression, String tips) {
        assertFalse(expression, RestResultCode.CODE402.getCode(), tips);
    }

    /**
     * 检查对象不为空，为空抛出异常
     *
     * @param obj  对象
     * @param code 错误码
     * @param tips 不成立时异常信息
     */
    public static void assertNotNull(Object obj, String code, String tips) {
        assertTrue(obj != null, code, tips);
    }

    /**
     * 检查对象不为空，为空抛出异常
     *
     * @param obj  对象
     * @param tips 不成立时异常信息
     */
    public static void assertNotNull(Object obj, String tips) {
        assertTrue(obj != null, RestResultCode.CODE402.getCode(), tips);
    }

    /**
     * 检查字符串不为空，为空抛出异常
     *
     * @param str  字符串
     * @param code 错误码
     * @param tips 不成立时异常信息
     */
    public static void assertNotEmpty(String str, String code, String tips) {
        assertTrue(StringUtil.isNotEmpty(str), code, tips);
    }

    /**
     * 检查字符串不为空，为空抛出异常
     *
     * @param str  字符串
     * @param tips 不成立时异常信息
     */
    public static void assertNotEmpty(String str, String tips) {
        assertTrue(StringUtil.isNotEmpty(str), RestResultCode.CODE402.getCode(), tips);
    }
}
