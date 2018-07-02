package cn.faury.fdk.shiro.realm;

import cn.faury.fdk.common.utils.DateUtil;
import cn.faury.fdk.common.utils.StringUtil;
import cn.faury.fdk.shiro.exception.HasExpiredAccountException;
import cn.faury.fdk.shiro.exception.NotEffectAccountException;
import cn.faury.fwmf.module.api.role.bean.RoleInfoBean;
import cn.faury.fwmf.module.api.role.service.RoleService;
import cn.faury.fwmf.module.api.user.bean.UserInfoBean;
import cn.faury.fwmf.module.api.user.bean.UserPasswordBean;
import cn.faury.fwmf.module.api.user.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.List;

public class FdkShiroRealm extends AuthorizingRealm {

    /**
     * 用户信息服务
     */
    protected UserService userService;

    /**
     * 用户信息服务
     */
    protected RoleService roleService;

    /**
     * 业务系统编码/APP编码
     */
    protected String saCode;

    /**
     * 获取授权信息
     *
     * @param principals 账号对象
     * @return 授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        SimpleAuthorizationInfo info;
//        // 首先从session中获取权限
//        SimpleAuthorizationInfo info = (SimpleAuthorizationInfo) ShiroKit.getShiroSessionAttr("perms");
//
//        if (info != null) {
//            return info;
//        }

        // 缓存中不存在，则从数据库查询
        String loginName = (String) principals.fromRealm(getName()).iterator().next();

        UserInfoBean acc = userService == null ? null : userService.getUserInfoByLoginName(loginName);

        if (acc != null) {

            info = new SimpleAuthorizationInfo();

            // 获取用户角色
            List<RoleInfoBean> roles = roleService == null ? null : roleService.getUserRolesByUserId(saCode, acc.getUserId());
            if (roles != null && roles.size() > 0) {
                for (RoleInfoBean role : roles) {
                    info.addRole(role.getRoleCode());
                }
            }

            // 获取用户授权字符串
            List<String> rolePerms = roleService == null ? null : roleService.getUserRolePermsByUserId(saCode, acc.getUserId());
            if (rolePerms != null && rolePerms.size() > 0) {
                info.addStringPermissions(rolePerms);
            }

//            ShiroKit.setShiroSessionAttr("perms", info);

            return info;
        } else {
            return null;
        }
    }

    /**
     * 获取账号信息
     *
     * @param token 登录对象
     * @return 账号信息
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upt = (UsernamePasswordToken) token;

        // 获取用户信息
        UserInfoBean user = userService == null ? null : userService.getUserInfoByLoginName(upt.getUsername());

        if (user != null) {
            // 执行有效性验证
            validateStatus(user, token);
            // 执行授权验证
            validateAuth(user, token);
            UserPasswordBean pwd = userService == null ? null : userService.getUserPasswordByUserId(user.getUserId());
            return new SimpleAuthenticationInfo(user.getLoginName(), pwd.getPassword(), getName());
        }
        throw new UnknownAccountException(String.format("当前登录用户[%s]不存在!", upt.getUsername()));
    }

    /**
     * 验证用户授权
     * <p>
     * <pre>
     * 不同的业务需要进行扩展验证
     * </pre>
     *
     * @param user  用户信息
     * @param token 身份
     * @throws AuthenticationException
     */
    protected void validateAuth(UserInfoBean user, AuthenticationToken token) throws AuthenticationException {
        if (preValidateAuth(user, token)) {
            doValidateAuth(user, token);
        }
        afterValidateAuth(user, token);
    }

    /**
     * 执行验证用户授权前操作
     * <p>
     * <pre>
     * 不同的业务需要进行扩展验证
     * </pre>
     *
     * @param user  用户信息
     * @param token 身份
     * @throws AuthenticationException
     */
    protected boolean preValidateAuth(UserInfoBean user, AuthenticationToken token) throws AuthenticationException {
        return true;
    }

    /**
     * 执行验证用户授权
     * <p>
     * <pre>
     * 不同的业务需要进行扩展验证
     * </pre>
     *
     * @param user  用户信息
     * @param token 身份
     * @throws AuthenticationException
     */
    protected void doValidateAuth(UserInfoBean user, AuthenticationToken token) throws AuthenticationException {
    }

    /**
     * 执行验证用户授权后
     * <p>
     * <pre>
     * 不同的业务需要进行扩展验证
     * </pre>
     *
     * @param user  用户信息
     * @param token 身份
     * @throws AuthenticationException
     */
    protected void afterValidateAuth(UserInfoBean user, AuthenticationToken token) throws AuthenticationException {
    }

    /**
     * 验证用户状态
     * <p>
     * <pre>
     * 对可以夸过状态验证的需要自己扩展
     * </pre>
     *
     * @param user  用户信息
     * @param token 身份
     * @throws AuthenticationException 验证异常
     */
    protected void validateStatus(UserInfoBean user, AuthenticationToken token) throws AuthenticationException {
        if (preValidateStatus(user, token)) {
            doValidateStatus(user, token);
        }
        afterValidateStatus(user, token);
    }

    /**
     * 执行验证用户状态前操作
     * <p>
     * <pre>
     * 不同的业务需要进行扩展验证
     * </pre>
     *
     * @param user  用户信息
     * @param token 身份
     * @throws AuthenticationException
     */
    protected boolean preValidateStatus(UserInfoBean user, AuthenticationToken token) throws AuthenticationException {
        return true;
    }

    /**
     * 执行验证用户状态
     * <p>
     * <pre>
     * 不同的业务需要进行扩展验证
     * </pre>
     *
     * @param user  用户信息
     * @param token 身份
     * @throws AuthenticationException
     */
    protected void doValidateStatus(UserInfoBean user, AuthenticationToken token) throws AuthenticationException {
        // 验证是否启用
        if (!"Y".equals(user.getIsEnable())) {
            // 账号未启用
            throw new DisabledAccountException(String.format("当前账号[%s]已禁用!", user.getLoginName()));
        }

        // 获取当前时间
        String currentDate = DateUtil.getCurrentDateStr();
        // 验证账号是否生效
        String startDate = DateUtil.formatDate(user.getEfctYmd());
        if (StringUtil.isNotEmpty(startDate) && startDate.compareToIgnoreCase(currentDate) > 0) {
            // 账号生效日期未到
            throw new NotEffectAccountException(String.format("当前账号[%s]尚未生效!", user.getLoginName()));
        }

        // 验证账号是否失效
        String endDate = DateUtil.formatDate(user.getExprYmd());
        if (StringUtil.isNotEmpty(endDate) && endDate.compareToIgnoreCase(currentDate) < 0) {
            // 账号失效日期已到
            throw new HasExpiredAccountException(String.format("当前账号[%s]已失效!", user.getLoginName()));
        }
    }

    /**
     * 执行验证用户状态后
     * <p>
     * <pre>
     * 不同的业务需要进行扩展验证
     * </pre>
     *
     * @param user  用户信息
     * @param token 身份
     * @throws AuthenticationException
     */
    protected void afterValidateStatus(UserInfoBean user, AuthenticationToken token) throws AuthenticationException {
    }

    /**
     * 获取用户服务
     *
     * @return 用户服务
     */
    public UserService getUserService() {
        return userService;
    }

    /**
     * 设置用户服务
     *
     * @param userService 用户服务
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public RoleService getRoleService() {
        return roleService;
    }

    public FdkShiroRealm setRoleService(RoleService roleService) {
        this.roleService = roleService;
        return this;
    }

    /**
     * 获取业务系统或APP的编码
     *
     * @return 业务系统或APP的编码
     */
    public String getSaCode() {
        return saCode;
    }

    /**
     * 设置业务系统或APP的编码
     *
     * @param saCode 业务系统或APP的编码
     */
    public void setSaCode(String saCode) {
        this.saCode = saCode;
    }
}
