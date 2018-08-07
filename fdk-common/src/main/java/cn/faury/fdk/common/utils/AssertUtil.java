/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 6/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.common.utils;

import cn.faury.fdk.common.entry.RestResultCode;
import cn.faury.fdk.common.exception.TipsException;

import java.util.Collection;
import java.util.Map;

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
     * 检查对象为空，不为空抛出异常
     *
     * @param obj 对象
     * @param re  不为空时抛出的异常信息
     */
    public static void assertNull(Object obj, RuntimeException re) {
        assertTrue(obj == null, re);
    }

    /**
     * 检查对象为空，不为空抛出异常
     *
     * @param obj  对象
     * @param code 错误码
     * @param tips 不成立时异常信息
     */
    public static void assertNull(Object obj, String code, String tips) {
        assertTrue(obj == null, code, tips);
    }

    /**
     * 检查对象为空，不为空抛出异常
     *
     * @param obj  对象
     * @param tips 不成立时异常信息
     */
    public static void assertNull(Object obj, String tips) {
        assertTrue(obj == null, RestResultCode.CODE402.getCode(), tips);
    }

    /**
     * 检查对象不为空，为空抛出异常
     *
     * @param obj 对象
     * @param re  不成立时异常信息
     */
    public static void assertNotNull(Object obj, RuntimeException re) {
        assertTrue(obj != null, re);
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
     * @param str 字符串
     * @param re  不成立时异常信息
     */
    public static void assertNotEmpty(String str, RuntimeException re) {
        assertTrue(StringUtil.isNotEmpty(str), re);
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

    /**
     * 检查集合不为空，为空抛出异常
     *
     * @param collection 集合
     * @param re         不成立时异常信息
     */
    public static void assertNotEmpty(Collection collection, RuntimeException re) {
        assertTrue(collection != null && collection.size() > 0, re);
    }

    /**
     * 检查集合不为空，为空抛出异常
     *
     * @param collection 集合
     * @param code       错误码
     * @param tips       不成立时异常信息
     */
    public static void assertNotEmpty(Collection collection, String code, String tips) {
        assertTrue(collection != null && collection.size() > 0, code, tips);
    }

    /**
     * 检查集合不为空，为空抛出异常
     *
     * @param collection 集合
     * @param tips       不成立时异常信息
     */
    public static void assertNotEmpty(Collection collection, String tips) {
        assertTrue(collection != null && collection.size() > 0, RestResultCode.CODE402.getCode(), tips);
    }

    /**
     * 检查Map不为空，为空抛出异常
     *
     * @param map 集合
     * @param re  不成立时异常信息
     */
    public static void assertNotEmpty(Map map, RuntimeException re) {
        assertTrue(map != null && map.size() > 0, re);
    }

    /**
     * 检查Map不为空，为空抛出异常
     *
     * @param map  集合
     * @param code 错误码
     * @param tips 不成立时异常信息
     */
    public static void assertNotEmpty(Map map, String code, String tips) {
        assertTrue(map != null && map.size() > 0, code, tips);
    }

    /**
     * 检查Map不为空，为空抛出异常
     *
     * @param map  集合
     * @param tips 不成立时异常信息
     */
    public static void assertNotEmpty(Map map, String tips) {
        assertTrue(map != null && map.size() > 0, RestResultCode.CODE402.getCode(), tips);
    }

    /**
     * 检查字符串为空，不为空抛出异常
     *
     * @param str 字符串
     * @param re  不成立时异常信息
     */
    public static void assertEmpty(String str, RuntimeException re) {
        assertTrue(StringUtil.isEmpty(str), re);
    }

    /**
     * 检查字符串为空，不为空抛出异常
     *
     * @param str  字符串
     * @param code 错误码
     * @param tips 不成立时异常信息
     */
    public static void assertEmpty(String str, String code, String tips) {
        assertTrue(StringUtil.isEmpty(str), code, tips);
    }

    /**
     * 检查字符串为空，不为空抛出异常
     *
     * @param str  字符串
     * @param tips 不成立时异常信息
     */
    public static void assertEmpty(String str, String tips) {
        assertTrue(StringUtil.isEmpty(str), RestResultCode.CODE402.getCode(), tips);
    }

    /**
     * 检查集合为空，不为空抛出异常
     *
     * @param collection 集合
     * @param re         不成立时异常信息
     */
    public static void assertEmpty(Collection collection, RuntimeException re) {
        assertTrue(collection == null || collection.size() <= 0, re);
    }

    /**
     * 检查集合为空，不为空抛出异常
     *
     * @param collection 集合
     * @param code       错误码
     * @param tips       不成立时异常信息
     */
    public static void assertEmpty(Collection collection, String code, String tips) {
        assertTrue(collection == null || collection.size() <= 0, code, tips);
    }

    /**
     * 检查集合为空，不为空抛出异常
     *
     * @param collection 集合
     * @param tips       不成立时异常信息
     */
    public static void assertEmpty(Collection collection, String tips) {
        assertTrue(collection == null || collection.size() <= 0, RestResultCode.CODE402.getCode(), tips);
    }

    /**
     * 检查Map为空，不为空抛出异常
     *
     * @param map 集合
     * @param re  不成立时异常信息
     */
    public static void assertEmpty(Map map, RuntimeException re) {
        assertTrue(map == null || map.size() <= 0, re);
    }

    /**
     * 检查Map为空，不为空抛出异常
     *
     * @param map  集合
     * @param code 错误码
     * @param tips 不成立时异常信息
     */
    public static void assertEmpty(Map map, String code, String tips) {
        assertTrue(map == null || map.size() <= 0, code, tips);
    }

    /**
     * 检查Map为空，不为空抛出异常
     *
     * @param map  集合
     * @param tips 不成立时异常信息
     */
    public static void assertEmpty(Map map, String tips) {
        assertTrue(map == null || map.size() <= 0, RestResultCode.CODE402.getCode(), tips);
    }

    /**
     * 断言参数可以转换为Long值
     *
     * @param value 输入值
     * @param tips  失败提示
     */
    public static void assertIsLong(String value, String tips) {
        assertTrue(StringUtil.parseLong(value).isPresent(), tips);
    }

    /**
     * 断言参数可以转换为Long值
     *
     * @param value 输入值
     * @param code  错误码
     * @param tips  失败提示
     */
    public static void assertIsLong(String value, String code, String tips) {
        assertTrue(StringUtil.parseLong(value).isPresent(), code, tips);
    }

    /**
     * 断言参数可以转换为Long值
     *
     * @param value 输入值
     * @param re    异常对象
     */
    public static void assertIsLong(String value, RuntimeException re) {
        assertTrue(StringUtil.parseLong(value).isPresent(), re);
    }
}
