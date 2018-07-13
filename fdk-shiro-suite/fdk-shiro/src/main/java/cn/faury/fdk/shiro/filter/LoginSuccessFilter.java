/*
 * Copyright (c)
 */

package cn.faury.fdk.shiro.filter;

import cn.faury.fdk.common.utils.StringUtil;
import cn.faury.fdk.shiro.utils.SessionUtil;
import cn.faury.fdk.shiro.utils.ShiroUtil;
import cn.faury.fwmf.module.api.role.bean.RoleInfoBean;
import cn.faury.fwmf.module.api.role.service.RoleService;
import cn.faury.fwmf.module.api.user.bean.UserInfoBean;
import cn.faury.fwmf.module.api.user.service.UserService;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.List;

/**
 * 登录成功后保存Session
 */
public class LoginSuccessFilter extends FdkCaptchaValidateAuthenticationFilter {

    // 用户服务
    private UserService userService = null;

    // 角色服务
    private RoleService roleService = null;

    // 系统编码
    private String saCode = "";

    /**
     * 构造函数
     *
     * @param userService 用户服务
     * @param roleService 角色服务
     * @param saCode      系统编码
     */
    public LoginSuccessFilter(UserService userService, RoleService roleService, String saCode) {
        this.userService = userService;
        this.roleService = roleService;
        this.saCode = saCode;
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        // 登录成功初始化:设置Session值，相当于初始化SessionUtil
        SessionUtil.setCurrentUserName(ShiroUtil.principal());
        if (userService != null) {
            UserInfoBean user = userService.getUserInfoByLoginName(SessionUtil.getCurrentLoginName());
            if (user != null) {
                SessionUtil.setCurrentUserName(user.getUserName());
                SessionUtil.setCurrentUserId(user.getUserId());
                SessionUtil.setCurrentUserInfo(user);

                if (roleService != null) {
                    List<RoleInfoBean> roles = roleService.getUserRolesByUserId(saCode, user.getUserId());
                    SessionUtil.setCurrentRolesInfo(roles);
                }
            }
        }
        // 登录成功页面和登录页面相同时，不进行302跳转
        String successUrl = this.getSuccessUrl();
        if (isLoginRequest(request, response) && isLoginSubmission(request, response) && StringUtil.isNotEmpty(successUrl) && successUrl.equals(this.getLoginUrl())) {
            return true;
        }

        return super.onLoginSuccess(token, subject, request, response);
    }
}
