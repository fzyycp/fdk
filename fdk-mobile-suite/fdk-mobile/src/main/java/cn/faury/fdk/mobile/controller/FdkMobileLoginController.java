package cn.faury.fdk.mobile.controller;

import cn.faury.fdk.common.entry.RestResultCode;
import cn.faury.fdk.common.entry.RestResultEntry;
import cn.faury.fdk.common.utils.SerializeUtil;
import cn.faury.fdk.mobile.shiro.filter.FdkOAuth2MobileFormAuthenticationFilter;
import cn.faury.fdk.shiro.controller.FdkShiroLoginController;
import cn.faury.fdk.shiro.utils.ShiroUtil;
import cn.faury.fwmf.module.api.user.bean.UserOAuthInfoBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static cn.faury.fdk.mobile.shiro.filter.FdkOAuth2MobileFormAuthenticationFilter.ATTRIBUTE_KEY_OAUTH2_INFO;

/**
 * 手机端登录控制器(扩展OAuth2登录和游客登录)
 */
@RestController
@RequestMapping("/")
@Api(value = "手机端登录主控制器", tags = {"手机端登录主控制器接口"})
public class FdkMobileLoginController {

    @Autowired
    private FdkShiroLoginController fdkShiroLoginController;

    /**
     * OAuth2登录失败接口
     *
     * @return 登录结果JSON
     */
    @RequestMapping(value = FdkOAuth2MobileFormAuthenticationFilter.DEFAULT_OAUTH2_LOGIN_URL
            ,method = {RequestMethod.GET,RequestMethod.POST})
    @ApiOperation(value = "OAuth2登录结果返回", notes = "OAuth2登录结果返回")
    public RestResultEntry oauth2Login(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        // 已登录
        if (ShiroUtil.authenticated()) {
            return oauth2LoginSuccess(httpServletRequest, httpServletResponse);
        }

        return fdkShiroLoginController.login(httpServletRequest, httpServletResponse);
    }

    /**
     * 游客登录失败接口
     *
     * @return 登录结果JSON
     */
    @RequestMapping(value = FdkOAuth2MobileFormAuthenticationFilter.DEFAULT_GUEST_LOGIN_URL
            ,method = {RequestMethod.GET,RequestMethod.POST})
    @ApiOperation(value = "游客登录结果返回", notes = "游客登录结果返回")
    public RestResultEntry guestLogin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return fdkShiroLoginController.login(httpServletRequest,httpServletResponse);
    }

    /**
     * 登录成功接口
     *
     * @return 登录结果JSON
     */
    @RequestMapping(value = "/oauth2LoginSuccess",method = {RequestMethod.GET,RequestMethod.POST})
    @ApiOperation(value = "登录结果验证", notes = "检查是否已登录")
    public RestResultEntry oauth2LoginSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        // 获取登录成功基础信息
        RestResultEntry resultEntry = fdkShiroLoginController.loginSuccess(httpServletRequest, httpServletResponse);
        // 扩展OAuth2登录信息
        if (resultEntry!=null){
            try {
                byte[] oauth2InfoBytes = (byte[]) httpServletRequest.getAttribute(ATTRIBUTE_KEY_OAUTH2_INFO);
                if (oauth2InfoBytes != null) {
                    UserOAuthInfoBean bean = SerializeUtil.deserialize(oauth2InfoBytes, UserOAuthInfoBean.class);
                    Map<String, Object> result = (Map<String, Object>) resultEntry.getData().get(0);
                    result.put("oauth",bean);
                }
            }catch (RuntimeException re){
                resultEntry = RestResultEntry.createErrorResult(re.getMessage(),RestResultCode.CODE500.getTips());
            }
        }
        return resultEntry;
    }
}
