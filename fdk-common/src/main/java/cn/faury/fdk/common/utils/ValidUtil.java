/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 5/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.common.utils;

import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * 常用的验证工具类
 */
public class ValidUtil {

    /**
     * 二代身份证正则表达式
     */
    public static final String PATTERN_ID_NO = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([\\d|x|X]{1})$";

    /**
     * 是否二代身份证号是否合法
     *
     * @param idNo 身份证号
     * @return 是否合法
     */
    public static boolean isValidIDNo(String idNo) {
        return StringUtil.isNotEmpty(idNo)
                && idNo.length() == 18
                && Pattern.matches(PATTERN_ID_NO, idNo)
                && isValidProvinceCode(idNo.substring(0, 2))
                && isValidDate(idNo.substring(6, 14), DateUtil.FORMAT_DATE_SHORT)
                && checkIdNoLastNum(idNo);
    }

    /**
     * 检查省份编号是否合法
     *
     * @param provinceCode 省份编号
     * @return 是否合法
     */
    public static boolean isValidProvinceCode(String provinceCode) {
        //省(直辖市)码表
        return Arrays.stream(new String[]{"11", "12", "13", "14", "15", "21", "22",
                "23", "31", "32", "33", "34", "35", "36", "37", "41", "42", "43",
                "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63",
                "64", "65", "71", "81", "82", "91"}).anyMatch(provinceCode::equals);
    }

    /**
     * 判断日期是否有效
     *
     * @param dateStr 日期字符串
     * @param pattern 日期模式
     * @return 日期是否合法
     */
    public static boolean isValidDate(String dateStr, String pattern) {
        if (StringUtil.isEmpty(dateStr, pattern)) {
            return false;
        }
        Date date = DateUtil.parse(dateStr, pattern, false);
        return date != null;
    }

    // 校验身份证第18位是否正确(只适合18位身份证)
    private static boolean checkIdNoLastNum(String idNo) {
        if (idNo.length() != 18) {
            return false;
        }
        char[] tmp = idNo.toCharArray();
        int[] cardidArray = new int[tmp.length - 1];
        int i = 0;
        for (i = 0; i < tmp.length - 1; i++) {
            cardidArray[i] = Integer.parseInt(tmp[i] + "");
        }
        String checkCode = sumPower(cardidArray);
        String lastNum = tmp[tmp.length - 1] + "";
        if (lastNum.equals("x")) {
            lastNum = lastNum.toUpperCase();
        }
        return checkCode.equals(lastNum);
    }

    // 计算身份证的第十八位校验码
    private static String sumPower(int[] cardIdArray) {
        //身份证前17位每位加权因子
        int[] power = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        //身份证第18位校检码
        String[] refNumber = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
        int result = 0;
        for (int i = 0; i < power.length; i++) {
            result += power[i] * cardIdArray[i];
        }
        return refNumber[(result % 11)];
    }
}
