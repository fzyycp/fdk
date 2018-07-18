package cn.faury.fdk.mobile.shiro.filter.oauth2;

import cn.faury.fdk.common.entry.RestResultCode;
import cn.faury.fdk.common.exception.TipsException;
import cn.faury.fdk.common.utils.AssertUtil;
import cn.faury.fdk.common.utils.StringUtil;
import cn.faury.fwmf.module.api.user.bean.UserOAuthInfoBean;
import cn.faury.fwmf.module.api.user.config.OAuthFrom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 手机端OAuth2登录验证过滤器：微信
 */
public class WeixinOAuth2Handler extends AdapterOAuth2Handler {

    /**
     * 日志记录器
     */
    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Key键
     */
    public static final String HANDLER_KEY = "WEIXIN";

    /**
     * 通过code获取access_token
     */
    public static final String ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";

    /**
     * 刷新或续期access_token使用
     */
    public static final String REFRESH_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token";

    /**
     * 检验授权凭证（access_token）是否有效
     */
    public static final String AUTH = "https://api.weixin.qq.com/sns/auth";

    /**
     * 获取用户个人信息（UnionID机制）
     */
    public static final String USERINFO = "https://api.weixin.qq.com/sns/userinfo";

    /**
     * 通过授权Code获取access_token
     *
     * @param appCode APP编码
     * @param code    授权Code
     * @return 得到的access_token相关信息
     */
    @Override
    public Map<String, Object> getAccessToken(String appCode, String code) {
        String[] cfgs = getAppIdAndSecret(appCode);
        String url = String.format("%s?appid=%s&secret=%s&code=%s&grant_type=authorization_code"
                , ACCESS_TOKEN, cfgs[0], cfgs[1], code);
        return getUrlJsonToMap(url);
    }

    /**
     * 刷新access_token
     *
     * @param appCode      APP编码
     * @param refreshToken 获取到的refreshToken
     * @return 刷新后的access_token
     */
    @Override
    public Map<String, Object> refreshAccessToken(String appCode, String refreshToken) {
        String[] cfgs = getAppIdAndSecret(appCode);
        String url = String.format("%s?appid=%s&grant_type=refresh_token&refresh_token=%s"
                , REFRESH_TOKEN, cfgs[0], refreshToken);
        return getUrlJsonToMap(url);
    }

    /**
     * 续期refresh_token
     *
     * @param appCode      APP编码
     * @param refreshToken 原refresh_token
     * @return 续期后的refresh_token
     */
    @Override
    public Map<String, Object> renewRefreshToken(String appCode, String refreshToken) {
        String[] cfgs = getAppIdAndSecret(appCode);
        String url = String.format("%s?appid=%s&grant_type=refresh_token&refresh_token=%s"
                , REFRESH_TOKEN, cfgs[0], refreshToken);
        return getUrlJsonToMap(url);
    }

    /**
     * 获取用户个人信息
     *
     * @param accessToken 调用凭证
     * @param openid      普通用户的标识，对当前开发者帐号唯一
     * @return 查询结果
     */
    @Override
    public Map<String, Object> userinfo(String accessToken, String openid) {
        String url = String.format("%s?access_token=%s&openid=%s"
                , USERINFO, accessToken, openid);
        return getUrlJsonToMap(url);
    }

    /**
     * 获取授权后统一标识
     *
     * @param appCode APP编码
     * @param code    授权码
     * @return 授权唯一标识
     */
    @Override
    public UserOAuthInfoBean getUserOAuthInfoBean(String appCode, String code) {
        AssertUtil.assertNotEmpty(code, "未获取到第三方授权码");

        // 通过授权Code获取access_token
        Map<String, Object> accessToken = this.getAccessToken(appCode, code);
        log.debug("获取微信授权凭证:accessToken={}", accessToken);
        if (accessToken == null) {
            throw new TipsException(RestResultCode.CODE500.getCode(), "获取微信授权凭证失败");
        } else if (StringUtil.isEmpty((String) accessToken.get("access_token"))
                || StringUtil.isEmpty((String) accessToken.get("refresh_token"))
                || StringUtil.isEmpty((String) accessToken.get("openid"))) {
            String errmsg = (String) accessToken.get("errmsg");
            errmsg = StringUtil.emptyDefault(errmsg,"获取微信授权凭证失败:access_token=null||refresh_token=null||openid=null");
            throw new TipsException(RestResultCode.CODE500.getCode(), "获取微信授权凭证失败",errmsg);
        }

        // 获取用户第三方登录统一标识
        String access_token = (String) accessToken.get("access_token");
        String refresh_token = (String) accessToken.get("refresh_token");
        String openid = (String) accessToken.get("openid");

        Map<String, Object> userinfo = this.userinfo(access_token, openid);
        log.debug("获取微信用户信息:userinfo={}", userinfo);
        if (userinfo == null) {
            throw new TipsException(RestResultCode.CODE500.getCode(),"获取微信用户信息失败");
        }

        Map<String, Serializable> exts = new LinkedHashMap<>();
        // 添加扩展信息
        for (Map.Entry<String, Object> entry : userinfo.entrySet()) {
            if (entry != null && entry.getValue() instanceof Serializable) {
                exts.put(entry.getKey(), (Serializable) entry.getValue());
            }
        }
        for (Map.Entry<String, Object> entry : accessToken.entrySet()) {
            if (entry != null && entry.getValue() instanceof Serializable) {
                exts.put(entry.getKey(), (Serializable) entry.getValue());
            }
        }

        UserOAuthInfoBean bean = new UserOAuthInfoBean();
        if (userinfo.containsKey("unionid") && userinfo.get("unionid") instanceof Serializable && StringUtil.isNotEmpty((String) userinfo.get("unionid"))) {
            bean.setUnionid((String) userinfo.get("unionid"));
        } else if (accessToken.containsKey("unionid") && accessToken.get("unionid") instanceof Serializable && StringUtil.isNotEmpty((String) accessToken.get("unionid"))) {
            bean.setUnionid((String) userinfo.get("unionid"));
        }
        bean.setOpenid(openid);
        bean.setAccessToken(access_token);
        bean.setRefreshToken(refresh_token);
        bean.setOauthFrom(getOAuthType().getValue());
        bean.setExts(exts);
        return bean;
    }

    /**
     * 获取当前OAuth类型
     *
     * @return OAuth类型
     */
    @Override
    public OAuthFrom getOAuthType() {
        return OAuthFrom.WEIXIN;
    }
}
