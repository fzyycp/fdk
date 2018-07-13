package cn.faury.fdk.mobile.shiro.filter.oauth2;

import cn.faury.fdk.common.entry.RestResultCode;
import cn.faury.fdk.common.exception.TipsException;
import cn.faury.fdk.common.utils.StringUtil;
import cn.faury.fwmf.module.api.user.bean.UserOAuthInfoBean;
import cn.faury.fwmf.module.api.user.config.OAuthFrom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 手机端OAuth2登录验证过滤器：微信公众平台
 */
public class WeixinMPOAuth2Handler extends WeixinOAuth2Handler {
    /**
     * 日志记录器
     */
    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Key键
     */
    public static final String HANDLER_KEY = "WEIXIN_MP";

    /**
     * 放弃UnionId机制
     */
    private String nounionid;

    /**
     * 通过code获取access_token
     */
    public static final String MP_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token";

    /**
     * 获取用户个人信息（UnionID机制）
     */
    public static final String MP_USERINFO = "https://api.weixin.qq.com/cgi-bin/user/info";

    /**
     * 请求jsapi_ticket地址
     */
    public static final String JSAPI_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";

    /**
     * 缓存jsapi_ticket：accessToken作为key
     */
    private static final Map<String, Map<String, Object>> BUF_MP_JSAPI_TICKET = new LinkedHashMap<>();

    /**
     * 缓存AccessToken
     */
    private static final Map<String, Map<String, Object>> BUF_MP_ACCESS_TOKEN = new LinkedHashMap<>();

    /**
     * 刷新获取公众号授权access_token
     *
     * @param appCode APP编码
     * @return 得到的access_token相关信息
     */
    public Map<String, Object> refreshMpAccessToken(String appCode) {
        // 获取公众号ACCESS_TOKEN
        String[] cfgs = getAppIdAndSecret(appCode);
        String url = String.format("%s?appid=%s&secret=%s&grant_type=client_credential"
                , MP_ACCESS_TOKEN, cfgs[0], cfgs[1]);
        Map<String, Object> accessToken = getUrlJsonToMap(url);

        if (accessToken != null) {
            if (StringUtil.isNotEmpty((String) accessToken.get("access_token"))) {
                Long expires_in = getExpiresIn(accessToken, "expires_in");
                accessToken.put("expires_in", System.currentTimeMillis() + expires_in * 1000);
                BUF_MP_ACCESS_TOKEN.put(appCode, accessToken);
            } else {
                String errmsg = (String) accessToken.get("errmsg");
                throw new TipsException(RestResultCode.CODE500.getCode(), "获取微信公众号授权凭证失败", StringUtil.emptyDefault(errmsg, "获取微信公众号授权凭证失败"));
            }
        }
        return accessToken;
    }

    /**
     * 获取公众号授权access_token
     *
     * @param appCode APP编码
     * @return 得到的access_token相关信息
     */
    public Map<String, Object> getMpAccessToken(String appCode) {
        // 检查缓存
        Map<String, Object> buf = BUF_MP_ACCESS_TOKEN.get(appCode);
        if (buf != null) {
            Long expires_in = getExpiresIn(buf, "expires_in");
            if (expires_in < System.currentTimeMillis()) {
                refreshMpAccessToken(appCode);
            }
        } else {
            refreshMpAccessToken(appCode);
        }

        return BUF_MP_ACCESS_TOKEN.get(appCode);
    }

    /**
     * 通过授权Code获取access_token
     *
     * @param appCode APP编码
     * @param code    授权Code
     * @return 得到的access_token相关信息
     */
    @Override
    public Map<String, Object> getAccessToken(String appCode, String code) {
        // 获取用户aceess_token
        Map<String, Object> userAccess = super.getAccessToken(appCode, code);
        if (userAccess == null || StringUtil.isEmpty((String) userAccess.get("openid"))) {
            userAccess = new LinkedHashMap<>();
        }

        // 获取公众号授权access_token
        Map<String, Object> mpAccess = getMpAccessToken(appCode);
        if (mpAccess != null && StringUtil.isNotEmpty((String) mpAccess.get("access_token"))) {

            // 获取公众号授权jsapi_ticket
            Map<String, Object> jsapiTicket = getJsapiTicket((String) mpAccess.get("access_token"));
            if (jsapiTicket != null) {
                userAccess.putAll(jsapiTicket);
            }

            userAccess.putAll(mpAccess);
        }


        return userAccess;
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
        String url = String.format("%s?access_token=%s&openid=%s&lang=zh_CN"
                , MP_USERINFO, accessToken, openid);
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
        UserOAuthInfoBean bean = super.getUserOAuthInfoBean(appCode, code);
        if (bean != null && getNounionidList().contains(appCode)) {// 不使用unionid机制,用openid代替
            bean.setUnionid(bean.getOpenid());
        }
        return bean;
    }

    /**
     * 获取当前OAuth类型
     *
     * @return OAuth类型
     */
    @Override
    public OAuthFrom getOAuthType() {
        return OAuthFrom.WEIXIN_MP;
    }

    /**
     * 获取jsapi_ticket
     *
     * @param accessToken 公众号授权access_token
     * @return jsapi_ticket结果
     */
    public Map<String, Object> getJsapiTicket(String accessToken) {
        // 清理过时ticket
        removeExpiresTicket();
        // 检查缓存
        Map<String, Object> buf = BUF_MP_JSAPI_TICKET.get(accessToken);
        if (buf != null) {
            Long expires_in = getExpiresIn(buf, "expires_in");
            if (expires_in < System.currentTimeMillis()) {
                refreshJsapiTicket(accessToken);
            }
        } else {
            refreshJsapiTicket(accessToken);
        }
        return BUF_MP_JSAPI_TICKET.get(accessToken);
    }

    /**
     * 刷新jsapi_ticket
     *
     * @param accessToken 授权access
     */
    public Map<String, Object> refreshJsapiTicket(String accessToken) {
        String url = String.format("%s?access_token=%s&type=jsapi"
                , JSAPI_TICKET, accessToken);
        Map<String, Object> jsapiTicket = getUrlJsonToMap(url);
        if (jsapiTicket != null) {
            String errcode = String.valueOf(jsapiTicket.get("errcode"));
            if ("0".equals(errcode)) {
                Long expires_in = getExpiresIn(jsapiTicket, "expires_in");
                jsapiTicket.put("expires_in", System.currentTimeMillis() + expires_in * 1000);
                BUF_MP_JSAPI_TICKET.put(accessToken, jsapiTicket);
            } else {
                String errmsg = (String) jsapiTicket.get("errmsg");
                throw new TipsException(RestResultCode.CODE500.getCode(), "获取微信公众号授权凭证失败",StringUtil.emptyDefault(errmsg,"获取微信公众号授权凭证失败"));
            }
        }
        return jsapiTicket;
    }

    /**
     * 清理过时的ticket
     */
    public void removeExpiresTicket() {
        if (BUF_MP_JSAPI_TICKET != null && BUF_MP_JSAPI_TICKET.size() > 0) {
            for (Map.Entry<String, Map<String, Object>> entry : BUF_MP_JSAPI_TICKET.entrySet()) {
                if (entry.getValue() != null) {
                    Long expires_in = getExpiresIn(entry.getValue(), "expires_in");
                    if (expires_in < System.currentTimeMillis()) {
                        BUF_MP_JSAPI_TICKET.remove(entry.getKey());
                    }
                }
            }
        }
    }

    /**
     * 获取不使用unionid机制的App编码
     */
    public List<String> getNounionidList() {
        if (StringUtil.isNotEmpty(getNounionid())) {
            return Arrays.asList(getNounionid().split(","));
        }
        return new ArrayList<>();
    }

    public String getNounionid() {
        return nounionid;
    }

    public void setNounionid(String nounionid) {
        this.nounionid = nounionid;
    }
}
