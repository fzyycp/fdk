/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 5/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.common.utils;

import cn.faury.fdk.common.anotation.NonNull;
import cn.faury.fdk.common.anotation.Nullable;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;
import java.util.*;

public class StringUtil {
    /**
     * 空字符串
     */
    public static final String EMPTY_STR = "";
    /**
     * 回车
     */
    public static final int CR = 13; // <US-ASCII CR, carriage return (13)>
    /**
     * 换行
     */
    public static final int LF = 10; // <US-ASCII LF, linefeed (10)>
    /**
     * 空格
     */
    public static final int SP = 32; // <US-ASCII SP, space (32)>
    /**
     * 水平制表符
     */
    public static final int HT = 9; // <US-ASCII HT, horizontal-tab (9)>

    /**
     * 字符集名称：UTF8
     */
    public static final String UTF8_NAME = "UTF-8";

    /**
     * 字符集名称：ASCII
     */
    public static final String ASCII_NAME = "US-ASCII";

    /**
     * 字符集名称：ISO8859
     */
    public static final String ISO8859_NAME = "ISO-8859-1";

    /**
     * 字符集UTF8
     */
    public static final Charset UTF_8 = Charset.forName(UTF8_NAME);
    /**
     * 字符集ASCII
     */
    public static final Charset ASCII = Charset.forName(ASCII_NAME);
    /**
     * 字符集ISO-8859-1
     */
    public static final Charset ISO_8859_1 = Charset.forName(ISO8859_NAME);

    /**
     * 是否字符表示时：是
     */
    public static final String WHETHER_YES = "Y";

    /**
     * 是否字符表示时：否
     */
    public static final String WHETHER_NO = "N";

    /**
     * 全静态方法
     */
    protected StringUtil() {
    }

    /**
     * 是否是是
     */
    public static boolean whetherYes(@NonNull String whether) {
        return StringUtil.isNotEmpty(whether) && WHETHER_YES.equals(whether.toUpperCase());
    }

    /**
     * 把null变成EMPTY_STR
     *
     * @param str 输入字符串
     * @return 规避null转为空字符串
     */
    @Nullable
    public static String dealNull(@Nullable String str) {
        return str == null ? EMPTY_STR : str;
    }

    /**
     * 转换为int型
     *
     * @param s            输入字符串
     * @param defaultValue 默认值
     * @return 数字
     */
    @Nullable
    public static int toInt(@Nullable String s, int defaultValue) {
        if (s == null)
            return defaultValue;

        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 字符串转换为float型数据
     *
     * @param s            输入字符串
     * @param defaultValue 默认值
     * @return float数据
     */
    @Nullable
    public static float toFloat(@Nullable String s, float defaultValue) {
        if (s == null)
            return defaultValue;

        try {
            return Float.parseFloat(s.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 字符串转换为bool型数据
     *
     * @param s            输入字符串
     * @param defaultValue 默认值
     * @return boolean数据
     */
    @Nullable
    public static boolean toBoolean(@Nullable String s, boolean defaultValue) {
        if (s == null)
            return defaultValue;

        try {
            return Boolean.parseBoolean(s.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 删除数组中相同的元素。<br>
     * 例如数组中元素为a b b c c c,合并重复元素后为a b c
     *
     * @param s 原始数组
     * @return 不含重复元素的数组。
     */
    @Nullable
    public static String[] mergeDuplicateArray(@Nullable String[] s) {
        if (s == null) {
            return null;
        }
        List<String> list = Arrays.asList(s);
        Set<String> set = new HashSet<String>(list);
        return set.toArray(new String[set.size()]);
    }

    /**
     * 首字母变小写
     *
     * @param str 输入字符串
     * @return 首字母小写
     */
    @Nullable
    public static String firstCharToLowerCase(@Nullable String str) {
        if (str == null) {
            return null;
        }
        Character firstChar = str.charAt(0);
        String tail = str.substring(1);
        str = Character.toLowerCase(firstChar) + tail;
        return str;
    }

    /**
     * 首字母变大写
     *
     * @param str 输入字符串
     * @return 首字母变大写
     */
    @Nullable
    public static String firstCharToUpperCase(@Nullable String str) {
        if (str == null) {
            return null;
        }
        Character firstChar = str.charAt(0);
        String tail = str.substring(1);
        str = Character.toUpperCase(firstChar) + tail;
        return str;
    }

    /**
     * 字符字节取得默认"UTF-8"
     *
     * @param str 输入字符串
     */
    public static byte[] getBytes(@NonNull String str) {
        return str == null ? new byte[0] : str.getBytes(UTF_8);
    }

    /**
     * 字节转字符串，默认"UTF-8"
     *
     * @param bytes 输入字符串
     */
    public static String getString(@NonNull byte[] bytes) {
        return bytes == null ? null : new String(bytes, UTF_8);
    }

    /**
     * 字符串为 null 或者为 "" 时返回 true
     *
     * @param str 输入字符串
     * @return 是否为空
     */
    public static boolean isEmpty(@Nullable String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 字符串不为 null 而且不为 EMPTY_STR 时返回 true
     *
     * @param str 输入字符串
     * @return 是否非空
     */
    public static boolean isNotEmpty(@Nullable String str) {
        return !isEmpty(str);
    }

    /**
     * 所以参数都非空
     *
     * @param strings 输入字符串
     * @return 都非空
     */
    public static boolean isNotEmpty(@Nullable String... strings) {
        if (strings == null) {
            return false;
        }
        for (String str : strings) {
            if (isEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否存在空
     *
     * @param strings 输入字符串
     * @return 存在空
     */
    public static boolean isEmpty(@Nullable String... strings) {
        return !isNotEmpty(strings);
    }

    /**
     * 如果为空则用默认值替换
     *
     * @param str        输入字符串
     * @param defaultStr 为空则替换为
     * @return 检查后字符串
     */
    @Nullable
    public static String emptyDefault(@Nullable String str, @Nullable String defaultStr) {
        return isEmpty(str) ? defaultStr : str;
    }

    /**
     * 字节转化为16进制字符串
     *
     * @param bs 字节
     * @return 16进制字符串
     */
    public static String byteToHexString(@NonNull byte[] bs) {
        StringBuilder sb = new StringBuilder(bs.length * 2);
        for (byte b : bs) {
            sb.append(Character.forDigit((b & 0xf0) >> 4, 16));
            sb.append(Character.forDigit((b & 0x0f), 16));
        }
        return sb.toString();
    }

    /**
     * 字节转base64码
     *
     * @param bs 字节
     * @return base64码
     */
    public static String byteToBase64(byte[] bs) {
        return new String(Base64.encodeBase64(bs), StringUtil.UTF_8);
    }

    /**
     * base64码转字节
     *
     * @param base base64码
     * @return 字节
     */
    public static byte[] base64ToByte(String base) {
        return Base64.decodeBase64(StringUtil.getBytes(base));
    }

    /**
     * 连接字符串
     *
     * @param values    要拼接的数组
     * @param delimiter 分隔符
     * @param prefix    前缀
     * @param suffix    后缀
     * @return 返回字符串
     */
    public static <T extends CharSequence> String join(List<T> values, CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
        StringJoiner joiner = new StringJoiner(delimiter, prefix, suffix);
        values.forEach(joiner::add);
        return joiner.toString();
    }

    /**
     * 连接字符串
     *
     * @param values    要拼接的数组
     * @param delimiter 分隔符
     * @param prefix    前缀
     * @param suffix    后缀
     * @return 返回字符串
     */
    public static <T extends CharSequence> String join(T[] values, CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
        StringJoiner joiner = new StringJoiner(delimiter, prefix, suffix);
        Arrays.stream(values).forEach(joiner::add);
        return joiner.toString();
    }

    /**
     * 连接字符串
     *
     * @param values    要拼接的数组
     * @param delimiter 分隔符
     * @return 返回字符串
     */
    public static <T extends CharSequence> String join(List<T> values, CharSequence delimiter) {
        return join(values, delimiter, "", "");
    }

    /**
     * 连接字符串
     *
     * @param values    要拼接的数组
     * @param delimiter 分隔符
     * @return 返回字符串
     */
    public static <T extends CharSequence> String join(T[] values, CharSequence delimiter) {
        return join(values, delimiter, "", "");
    }

    /**
     * 字符串转换为Long
     *
     * @param value 字符串值
     * @return 转换后的Long值
     */
    public static Optional<Long> parseLong(String value) {
        Optional<Long> result = Optional.empty();
        try {
            result = Optional.ofNullable(Long.parseLong(value));
        } catch (NumberFormatException ignored) {
        }
        return result;
    }

    /**
     * 字符串转换为Long
     *
     * @param value        字符串值
     * @param defaultValue 默认值
     * @return 转换后的Long值
     */
    public static Optional<Long> parseLong(String value, Long defaultValue) {
        Optional<Long> result = Optional.ofNullable(defaultValue);
        try {
            result = Optional.of(Long.valueOf(value));
        } catch (NumberFormatException ignored) {
        }
        return result;
    }
}
