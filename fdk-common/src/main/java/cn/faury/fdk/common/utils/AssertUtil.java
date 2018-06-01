/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 6/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.common.utils;

/**
 * 参数判断
 */
public class AssertUtil {
    public AssertUtil() {
    }

    /**
     * 检查表达式是否成立
     *
     * @param expression 表达式
     * @param message    不成立时异常信息
     */
    public static void check(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 检查表达式是否成立
     *
     * @param expression 表达式
     * @param message    不成立时异常信息格式
     * @param args       格式化参数列表
     */
    public static void check(boolean expression, String message, Object... args) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, args));
        }
    }

    /**
     * 检查对象不为空
     *
     * @param obj     对象
     * @param message 不成立时异常信息
     */
    public static void notNull(Object obj, String message) {
        check(obj != null, message);
    }

    /**
     * 检查字符串不为空
     *
     * @param str     字符串
     * @param message 不成立时异常信息
     */
    public static void notEmpty(String str, String message) {
        check(StringUtil.isNotEmpty(str), message);
    }
}
