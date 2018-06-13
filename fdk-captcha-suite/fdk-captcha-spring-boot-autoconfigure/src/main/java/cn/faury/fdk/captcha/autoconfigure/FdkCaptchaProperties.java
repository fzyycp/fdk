package cn.faury.fdk.captcha.autoconfigure;

import cn.faury.fdk.common.utils.JsonUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = FdkCaptchaProperties.PROPERTIES_PREFIX)
public class FdkCaptchaProperties {
    /**
     * 配置文件前缀
     */
    public static final String PROPERTIES_PREFIX = "fdk.captcha";

    /**
     * 请求地址
     */
    public static final String REQUEST_URL = "/captcha.jpg";

    /**
     * 是否启用
     */
    private boolean enable = true;

    /**
     * 前台传入名称
     */
    private String requestName = "captchaCode";

    /**
     * 是否有边框
     */
    private String border = "yes";

    /**
     * 边框颜色
     */
    private String borderColor = "105,179,90";

    /**
     * 验证码字体颜色
     */
    private String fontColor = "blue";

    /**
     * 验证码字符个数
     */
    private int charLength = 4;

    /**
     * 验证码字体名字
     */
    private String fontNames = "宋体,楷体,微软雅黑";

    /**
     * 验证码图片宽度
     */
    private int imageWidth = 125;

    /**
     * 验证码图片高度
     */
    private int imageHeight = 45;

    /**
     * 验证码存储在Session中的key
     */
    private String sessionKey = "fdkCaptchaCode";

    /**
     * 获取enable
     *
     * @return enable
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * 设置enable
     *
     * @param enable 值
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * 获取requestName
     *
     * @return requestName
     */
    public String getRequestName() {
        return requestName;
    }

    /**
     * 设置requestName
     *
     * @param requestName 值
     */
    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    /**
     * 获取border
     *
     * @return border
     */
    public String getBorder() {
        return border;
    }

    /**
     * 设置border
     *
     * @param border 值
     */
    public void setBorder(String border) {
        this.border = border;
    }

    /**
     * 获取borderColor
     *
     * @return borderColor
     */
    public String getBorderColor() {
        return borderColor;
    }

    /**
     * 设置borderColor
     *
     * @param borderColor 值
     */
    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    /**
     * 获取fontColor
     *
     * @return fontColor
     */
    public String getFontColor() {
        return fontColor;
    }

    /**
     * 设置fontColor
     *
     * @param fontColor 值
     */
    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    /**
     * 获取charLength
     *
     * @return charLength
     */
    public int getCharLength() {
        return charLength;
    }

    /**
     * 设置charLength
     *
     * @param charLength 值
     */
    public void setCharLength(int charLength) {
        this.charLength = charLength;
    }

    /**
     * 获取fontNames
     *
     * @return fontNames
     */
    public String getFontNames() {
        return fontNames;
    }

    /**
     * 设置fontNames
     *
     * @param fontNames 值
     */
    public void setFontNames(String fontNames) {
        this.fontNames = fontNames;
    }

    /**
     * 获取imageWidth
     *
     * @return imageWidth
     */
    public int getImageWidth() {
        return imageWidth;
    }

    /**
     * 设置imageWidth
     *
     * @param imageWidth 值
     */
    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    /**
     * 获取imageHeight
     *
     * @return imageHeight
     */
    public int getImageHeight() {
        return imageHeight;
    }

    /**
     * 设置imageHeight
     *
     * @param imageHeight 值
     */
    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    /**
     * 获取sessionKey
     *
     * @return sessionKey
     */
    public String getSessionKey() {
        return sessionKey;
    }

    /**
     * 设置sessionKey
     *
     * @param sessionKey 值
     */
    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    @Override
    public String toString() {
        return JsonUtil.objectToJson(this);
    }
}
