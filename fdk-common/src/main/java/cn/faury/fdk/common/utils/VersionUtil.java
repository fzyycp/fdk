/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 5/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.common.utils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 版本号操作工具
 */
public class VersionUtil {

    /**
     * 判断字符串是否为合法的版本号
     * <pre>
     *  判断规则：以.隔开，每个分割区都是由0~9组成
     * <pre/>
     * @param ver 版本号
     * @return 是否合法
     */
    public static boolean isVersionNo(String ver) {
        String regex = "[0-9]+(\\.[0-9]+)*";
        return Pattern.matches(regex, ver);
    }

    /**
     * 比较版本号大小
     *
     * @param ver1 版本号1
     * @param ver2 版本号2
     * @return 1表示ver1大，0表示相等，-1表示ver1小，-2表示没有可比性
     */
    public static int compareVersionNo(String ver1, String ver2) {
        int is = -2;
        if (StringUtil.isNotEmpty(ver1, ver2) && isVersionNo(ver1) && isVersionNo(ver2)) {
            List<String> ver1s = Arrays.asList(ver1.split("\\."));
            List<String> ver2s = Arrays.asList(ver2.split("\\."));

            // 用0将版本位数长度凑齐
            int i = 0;
            do {
                String ver1Part = "0";
                if (i < ver1s.size()) {
                    ver1Part = ver1s.get(i);
                }
                String ver2Part = "0";
                if (i < ver2s.size()) {
                    ver2Part = ver2s.get(i);
                }
                int ver1PartInt = Integer.parseInt(ver1Part);
                int ver2PartInt = Integer.parseInt(ver2Part);
                if (ver1PartInt > ver2PartInt) {
                    is = 1;
                    break;
                } else if (ver1PartInt < ver2PartInt) {
                    is = -1;
                    break;
                } else {
                    is = 0;
                }
                i++;
            } while (i < ver1s.size() || i < ver2s.size());
        }
        return is;
    }
}
