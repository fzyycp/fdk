package cn.faury.fdk.mobile.shiro.filter.oauth2;

import cn.faury.fdk.common.entry.RestResultCode;
import cn.faury.fdk.common.exception.TipsException;
import cn.faury.fwmf.module.api.user.bean.UserOAuthInfoBean;
import cn.faury.fwmf.module.api.user.config.OAuthFrom;

import javax.servlet.ServletRequest;
import java.util.Map;

public interface OAuth2Handler {

    /**
     * 默认OAuth2授权码参数名
     */
    public static final String DEFAULT_OAUTH2_CODE_PARAM = "code";

    /**
     * 默认OAuth2授权统一标识参数名
     */
    public static final String DEFAULT_OAUTH2_UNIONID_PARAM = "unionid";

    /**
     * 默认OAuth2授权标识token参数名
     */
    public static final String DEFAULT_OAUTH2_ACCESS_TOKEN_PARAM = "access_token";

    /**
     * 默认OAuth2授权刷新token参数名
     */
    public static final String DEFAULT_OAUTH2_REFRESH_TOKEN_PARAM = "refresh_token";

    /**
     * 通过授权Code获取access_token
     *
     * @param appCode APP编码
     * @param code    授权Code
     * @return 得到的access_token相关信息
     */
   default Map<String, Object> getAccessToken(String appCode, String code){
        throw new TipsException(RestResultCode.CODE500.getCode(), "不支持的第三方登录方式","后台不支持OAuth登录方式，请使用其他方式登录");
    }

    /**
     * 刷新access_token
     *
     * @param appCode      APP编码
     * @param refreshToken 获取到的refreshToken
     * @return 刷新后的access_token
     */
    default Map<String, Object> refreshAccessToken(String appCode, String refreshToken){
        throw new TipsException(RestResultCode.CODE500.getCode(), "不支持的第三方登录方式", "后台不支持OAuth登录方式，请使用其他方式登录");
    }

    /**
     * 续期refresh_token
     *
     * @param appCode      APP编码
     * @param refreshToken 原refresh_token
     * @return 续期后的refresh_token
     */
    default Map<String, Object> renewRefreshToken(String appCode, String refreshToken){
        throw new TipsException(RestResultCode.CODE500.getCode(), "不支持的第三方登录方式", "后台不支持OAuth登录方式，请使用其他方式登录");
    }

    /**
     * 获取用户个人信息
     *
     * @param accessToken 调用凭证
     * @param openid      普通用户的标识，对当前开发者帐号唯一
     * @return 查询结果
     */
    default Map<String, Object> userinfo(String accessToken, String openid){
        throw new TipsException(RestResultCode.CODE500.getCode(), "不支持的第三方登录方式", "后台不支持OAuth登录方式，请使用其他方式登录");
    }

    /**
     * 获取用户第三方授权信息
     *
     * @param appCode APP编码
     * @param code    授权码
     * @return 授权唯一标识
     */
    default UserOAuthInfoBean getUserOAuthInfoBean(String appCode, String code){
        throw new TipsException(RestResultCode.CODE500.getCode(), "不支持的第三方登录方式", "后台不支持OAuth登录方式，请使用其他方式登录");
    }

    /**
     * 获取当前OAuth类型
     * @return OAuth类型
     */
    OAuthFrom getOAuthType();

    /**
     * 处理OAuth2已授权过超时登录请求
     *
     * @param appCode      APP编码
     * @param refreshToken 刷新授权码
     */
    default void handlerRefresh(String appCode, String refreshToken){
        throw new TipsException(RestResultCode.CODE500.getCode(), "不支持的第三方登录方式", "后台不支持OAuth登录方式，请使用其他方式登录");
    }

    /**
     * 预处理操作
     *
     * @param request 请求
     */
    default void beforeHandler(ServletRequest request){
        // do nothing
    }


    /**
     * 处理后收尾
     *
     * @param unionid 统一用户标识
     */
    default void afterHandler(String unionid){
        // do nothing
    }
}
