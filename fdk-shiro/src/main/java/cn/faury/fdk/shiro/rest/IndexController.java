package cn.faury.fdk.shiro.rest;

import cn.faury.fdk.common.entry.RestResultEntry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录控制器
 */
@RestController
@RequestMapping("/")
@Api(value = "Shiro主控制器", tags = {"Shiro主控制器接口"})
public class IndexController {

    // 日志
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    /**
     * 登录接口
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录结果JSON
     */
    @PostMapping("/login")
    @ApiOperation(value = "登录结果验证", notes = "检查是否已登录")
    public RestResultEntry login(@ApiParam(value = "用户名",required = true) @RequestParam String username
            , @ApiParam(value = "密码",required = true) @RequestParam String password) {
        Map<String, String> jsonObject = new HashMap<>();
        try {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken upt = new UsernamePasswordToken(username, password);
            subject.login(upt);
        } catch (IncorrectCredentialsException e) {
            jsonObject.put("msg", "密码错误");
        } catch (LockedAccountException e) {
            jsonObject.put("msg", "登录失败，该用户已被冻结");
        } catch (AuthenticationException e) {
            jsonObject.put("msg", "该用户不存在");
        } catch (Exception e) {
            jsonObject.put("msg", "该用户不存在");
        }
        return RestResultEntry.createSuccessResult(Collections.singletonList(jsonObject));
    }

    /**
     * 退出登录
     *
     * @return 登录结果JSON
     */
    @PostMapping("/logout")
    @ApiOperation(value = "退出登录", notes = "退出当前登录的用户")
    public RestResultEntry logout() {
        return RestResultEntry.createSuccessResult(null);
    }
}
