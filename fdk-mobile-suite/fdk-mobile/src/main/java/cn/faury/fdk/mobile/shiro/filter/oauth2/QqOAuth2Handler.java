/**
 * 业务平台Mobile：ssk-platform-webapp
 *
 * @date 2017年02月24日
 * @author yc.fan
 * <p>
 * 版权所有：南京扫扫看数字科技有限公司
 */
package cn.faury.fdk.mobile.shiro.filter.oauth2;

import cn.faury.fdk.common.entry.RestResultCode;
import cn.faury.fdk.common.exception.TipsException;
import cn.faury.fdk.common.utils.AssertUtil;
import cn.faury.fwmf.module.api.user.bean.UserOAuthInfoBean;
import cn.faury.fwmf.module.api.user.config.OAuthFrom;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;

/**
 * 手机端OAuth2登录验证过滤器：QQ
 */
public class QqOAuth2Handler extends SsoOAuth2Handler {

    /**
     * Key键
     */
    public static final String HANDLER_KEY = "QQ";

    /**
     * 创建用户授权信息对象
     *
     * @param request 请求
     * @return 授权信息对象
     */
    @Override
    protected UserOAuthInfoBean createUserOAuthInfoBean(ServletRequest request) {
        String unionid = WebUtils.getCleanParam(request, DEFAULT_OAUTH2_UNIONID_PARAM);
        String accessToken = WebUtils.getCleanParam(request, DEFAULT_OAUTH2_ACCESS_TOKEN_PARAM);
        AssertUtil.assertNotEmpty(unionid,new TipsException(RestResultCode.CODE500.getCode(),"第三方登录授权信息获取失败","输入参数unionid不可以为空"));
        AssertUtil.assertNotEmpty(accessToken,new TipsException(RestResultCode.CODE500.getCode(),"第三方登录授权信息获取失败","输入参数access_token不可以为空"));

        UserOAuthInfoBean bean = new UserOAuthInfoBean();
        bean.setUnionid(unionid);
        bean.setOpenid(unionid);
        bean.setAccessToken(accessToken);
        bean.setRefreshToken("");
        bean.setOauthFrom(getOAuthType().getValue());
        return bean;
    }

    /**
     * 获取当前OAuth类型
     *
     * @return OAuth类型
     */
    @Override
    public OAuthFrom getOAuthType() {
        return OAuthFrom.QQ;
    }
}
