/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 5/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

/**
 * 加密工具
 */
public class RandomUtil {

    /**
     * 加密算法：MD5
     */
    public static final String ALGORITHM_MD5 = "MD5";

    /**
     * 加密算法：SHA256
     */
    public static final String ALGORITHM_SHA256 = "SHA-256";

    /**
     * 获取UUID字符串
     *
     * @return uuid字符串
     */
    public static String getUUIDString() {
        return UUID.randomUUID().toString().replace("-", StringUtil.EMPTY_STR);
    }

    /**
     * 获取报文摘要
     *
     * @param str       要加密的字符串
     * @param algorithm 算法
     * @return 加密结果
     * @throws NoSuchAlgorithmException 算法不存在异常
     */
    public static String getDigestString(String str, String algorithm)
            throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] bs = str.getBytes(StringUtil.ASCII);
        byte[] hashed = md.digest(bs);
        return StringUtil.byteToHexString(hashed);
    }

    /**
     * 获取MD5摘要
     *
     * @param str 要加密的字符串
     * @return 加密结果
     */
    public static String getMD5String(String str) {
        String hashed = str;
        try {
            hashed = getDigestString(str, ALGORITHM_MD5);
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hashed;
    }

    /**
     * 获取SHA256加密字符串
     *
     * @param str 要加密的字符串
     * @return 加密结果
     */
    public static String getSHA256String(String str) {
        String hashed = str;
        try {
            hashed = getDigestString(str, ALGORITHM_SHA256);
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hashed;

    }

    /**
     * 随机获取几位数字
     * @param digital 数字位数
     * @return 字符串数字
     */
    public static String getRandomNumber(int digital) {
        StringBuilder number = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < digital; i++) {
            int nextInt = random.nextInt(10);
            number.append(nextInt);
        }
        return number.toString();
    }
}
