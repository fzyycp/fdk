package cn.faury.fdk.shiro.controller;

import cn.faury.fdk.common.entry.RestResultCode;
import cn.faury.fdk.common.entry.RestResultEntry;
import cn.faury.fdk.common.utils.JsonUtil;
import cn.faury.fdk.common.utils.StringUtil;
import cn.faury.fdk.shiro.core.FdkWebSessionManager;
import cn.faury.fdk.shiro.filter.captcha.FdkCaptchaConst;
import cn.faury.fdk.shiro.utils.SessionUtil;
import cn.faury.fdk.shiro.utils.ShiroUtil;
import cn.faury.fwmf.module.api.user.bean.UserInfoBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

/**
 * 登录控制器
 */
@RestController
@RequestMapping("/")
@Api(value = "Shiro主控制器", tags = {"Shiro主控制器接口"})
public class FdkShiroLoginController {

    // 日志
    private static final Logger logger = LoggerFactory.getLogger(FdkShiroLoginController.class);

    /**
     * 登录失败接口
     *
     * @return 登录结果JSON
     */
    @RequestMapping(value = "/login",method = {RequestMethod.GET,RequestMethod.POST})
    @ApiOperation(value = "登录结果返回", notes = "登录失败后返回登录结果")
    public RestResultEntry login(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        // 已登录
        if (ShiroUtil.authenticated()) {
            return loginSuccess(httpServletRequest, httpServletResponse);
        }

        RestResultEntry resultEntry = RestResultEntry.createErrorResult(RestResultCode.CODE401);
        try {
            // 未登录时，看看是否有错误信息
            Object errorMsg = httpServletRequest.getAttribute(FdkCaptchaConst.ATTRIBUTE_KEY_FAILURE_MSG);
            if (errorMsg instanceof String && StringUtil.isNotEmpty((String) errorMsg)) {
                resultEntry.setMessage((String) errorMsg);
            }
            Object errorTips = httpServletRequest.getAttribute(FdkCaptchaConst.ATTRIBUTE_KEY_FAILURE_MSG);
            if (errorMsg instanceof String && StringUtil.isNotEmpty((String) errorTips)) {
                resultEntry.setTips((String) errorTips);
            }
            logger.debug("登录失败：{}", resultEntry);
        } catch (Exception e) {
            logger.error("异常：{}", e.getMessage(), e);
            resultEntry.setMessage(e.getMessage());
        }
        return resultEntry;
    }

    /**
     * 登录成功接口
     *
     * @return 登录结果JSON
     */
    @RequestMapping(value = "/loginSuccess",method = {RequestMethod.GET,RequestMethod.POST})
    @ApiOperation(value = "登录结果验证", notes = "检查是否已登录")
    public RestResultEntry loginSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResultEntry resultEntry;
        try {
            UserInfoBean userInfoBean = SessionUtil.getCurrentUserInfo(UserInfoBean.class);
            Map<String, Object> result = JsonUtil.jsonToMap(JsonUtil.objectToJson(userInfoBean));
            String sessionId = SessionUtil.getSession().getId().toString();
            result.put("S", sessionId);
            resultEntry = RestResultEntry.createSuccessResult(Collections.singletonList(result));
            httpServletResponse.setHeader(FdkWebSessionManager.AUTHORIZATION_HEADER, sessionId);
            logger.debug("登录成功：{}", resultEntry);
        } catch (IncorrectCredentialsException e) {
            logger.error("异常：{}", e.getMessage(), e);
            resultEntry = RestResultEntry.createErrorResult("用户名密码不匹配", "用户名密码不匹配");
        } catch (LockedAccountException e) {
            logger.error("异常：{}", e.getMessage(), e);
            resultEntry = RestResultEntry.createErrorResult("登录失败，该用户已被冻结", "登录失败，该用户已被冻结");
        } catch (AuthenticationException e) {
            logger.error("异常：{}", e.getMessage(), e);
            resultEntry = RestResultEntry.createErrorResult("该用户不存在", "该用户不存在");
        } catch (Exception e) {
            logger.error("异常：{}", e.getMessage(), e);
            resultEntry = RestResultEntry.createErrorResult("未知错误" + e.getMessage(), "未知错误");
        }
        return resultEntry;
    }

    /**
     * 退出登录
     *
     * @return 登录结果JSON
     */
    @PostMapping(value = "/logout")
    @ApiOperation(value = "退出登录", notes = "退出当前登录的用户")
    public RestResultEntry logout() {
        SecurityUtils.getSubject().logout();
        return RestResultEntry.createSuccessResult(null);
    }

}
