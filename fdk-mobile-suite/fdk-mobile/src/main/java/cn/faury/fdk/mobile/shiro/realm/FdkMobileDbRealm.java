package cn.faury.fdk.mobile.shiro.realm;

import cn.faury.fdk.common.utils.DateUtil;
import cn.faury.fdk.common.utils.StringUtil;
import cn.faury.fdk.mobile.shiro.token.AppUsernamePasswordToken;
import cn.faury.fdk.shiro.exception.HasExpiredAccountException;
import cn.faury.fdk.shiro.exception.NotEffectAccountException;
import cn.faury.fdk.shiro.exception.UnauthorizedAppException;
import cn.faury.fdk.shiro.utils.ShiroUtil;
import cn.faury.fwmf.module.api.app.bean.AppInfoBean;
import cn.faury.fwmf.module.api.app.bean.ShopRAppInfoBean;
import cn.faury.fwmf.module.api.app.bean.UserRAppInfoBean;
import cn.faury.fwmf.module.api.app.service.AppInfoService;
import cn.faury.fwmf.module.api.app.service.ShopRAppInfoService;
import cn.faury.fwmf.module.api.app.service.UserRAppInfoService;
import cn.faury.fwmf.module.api.role.bean.RoleInfoBean;
import cn.faury.fwmf.module.api.role.service.RoleService;
import cn.faury.fwmf.module.api.shop.bean.ShopInfoBean;
import cn.faury.fwmf.module.api.shop.service.ShopInfoService;
import cn.faury.fwmf.module.api.user.bean.UserInfoBean;
import cn.faury.fwmf.module.api.user.bean.UserPasswordBean;
import cn.faury.fwmf.module.api.user.config.UserType;
import cn.faury.fwmf.module.api.user.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * 手机端登录用户校验
 */
public class FdkMobileDbRealm extends AuthorizingRealm {

    /**
     * 用户信息服务
     */
    private UserService userService;

    /**
     * 用户授权App服务
     */
    private UserRAppInfoService userRAppInfoService;

    /**
     * 商店授权APP服务
     */
    private ShopRAppInfoService shopRAppInfoService;

    /**
     * 商店信息服务
     */
    private ShopInfoService shopInfoService;

    /**
     * APP信息服务
     */
    private AppInfoService appInfoService;

    /**
     * APP编码
     */
    private String appCode;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upt = (UsernamePasswordToken) token;

        // 获取用户信息
        UserInfoBean user = userService.getUserInfoByLoginName(upt.getUsername());

        if (user != null) {
            // 执行有效性验证
            validateStatus(user, token);
            // 执行授权验证
            validateAuth(user, token);
            UserPasswordBean pwd = userService.getUserPasswordByUserId(user.getUserId());
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
        if (!StringUtil.whetherYes(user.getIsEnable())) {
            // 账号未启用
            throw new DisabledAccountException(String.format("当前账号[%s]已禁用!", user.getLoginName()));
        }

        // 获取当前时间
        Calendar now = Calendar.getInstance();
        Date currentDate = DateUtil.getCurrentDate();
        // 验证账号是否生效
        Date startDate = user.getEfctYmd();
        if (startDate.compareTo(currentDate) > 0) {
            // 账号生效日期未到
            throw new NotEffectAccountException(String.format("当前账号[%s]尚未生效!", user.getLoginName()));
        }

        // 验证账号是否失效
        Date endDate = user.getExprYmd();
        if (endDate.compareTo(currentDate) < 0) {
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

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {
        // 手机APP不存在角色授权
        return null;
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
    protected boolean preValidateAuth(UserInfoBean user,
                                      AuthenticationToken token) throws AuthenticationException {
        // 是否有权限
        boolean hasAuth = false;
        // 如果启用商店关联业务系统服务验证，则进行判断用户所在商店是否有授权
        if (user != null && shopRAppInfoService != null) {
            List<ShopRAppInfoBean> apps = shopRAppInfoService
                    .getShopRAppInfoListByUserId(user.getUserId());
            if (apps != null && apps.size() > 0) {
                for (ShopRAppInfoBean ura : apps) {
                    if (this.appCode != null
                            && this.appCode.equals(ura.getAppCode())) {
                        // 进一步验证商店是否禁用、删除
                        if (shopInfoService != null && ura.getShopId() != null) {
                            ShopInfoBean sib = shopInfoService.getShopInfoById(ura.getShopId());
                            // 商店信息存在且未删除且启用状态则可以登录，否则无权限登录
                            if (sib != null && "1".equals(sib.getShopState()) && !StringUtil.whetherYes(sib.getDelFlag())) {
                                hasAuth = true;
                            }
                        } else {
                            // 不验证商店状态
                            hasAuth = true;
                        }
                        break;
                    }
                }
            }
        }

        // 没有权限则继续下推验证
        return (false == hasAuth);
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
    protected void doValidateAuth(UserInfoBean user, AuthenticationToken token)
            throws AuthenticationException {
        if (userRAppInfoService == null) {
            throw new UnauthorizedAppException("用户关联APP服务未配置");
        } else if (false == hasAppAuth(user, token)) {
            throw new UnauthorizedAppException();
        }
    }

    /**
     * 判断用户是否有权限登录该APP
     *
     * @param user  用户信息
     * @param token 身份
     * @throws UnauthorizedAppException
     */
    protected boolean hasAppAuth(UserInfoBean user, AuthenticationToken token) {
        boolean hasAuth = false;
        if (user != null && userRAppInfoService != null) {
            UserType userType = UserType.parse(user.getUserType());
            AppInfoBean appInfoBean = null;
            if (appInfoService != null) {
                appInfoBean = appInfoService.getAppInfoByAppCode(null, this.getAppCode());
            }
            switch (userType) {
                case GUEST:
                    // 检查APP是否设置为拒绝游客用户直接登录
                    if (appInfoBean != null && !StringUtil.whetherYes(appInfoBean.getRejectGuestUser())) {
                        hasAuth = true;
                    }
                    break;
                case SHOPPING:
                    // 检查APP是否设置为拒绝购物用户直接登录
                    if (appInfoBean != null && !StringUtil.whetherYes(appInfoBean.getRejectShoppingUser())) {
                        hasAuth = true;
                    }
                    break;
                case FWMF:
                case SYSTEM:
                    if (appInfoBean != null && StringUtil.whetherYes(appInfoBean.getAllowBackgroundUser())) {
                        hasAuth = true;
                    }
                    break;
            }
            // 没有统一放行权限，则验证单个用户APP授权信息
            if (!hasAuth) {
                // 获取用户授权的APP列表
                List<UserRAppInfoBean> apps = userRAppInfoService
                        .getUserRAppInfoList(Arrays.asList(user.getUserId()));
                if (apps != null && apps.size() > 0) {
                    for (UserRAppInfoBean ura : apps) {
                        if (this.appCode != null
                                && this.appCode.equals(ura.getAppCode())) {
                            hasAuth = true;
                            break;
                        }
                    }
                }
            }
        }
        return hasAuth;
    }

    /**
     * 获取userService
     *
     * @return userService
     */
    public UserService getUserService() {
        return userService;
    }

    /**
     * 设置userService
     *
     * @param userService 值
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 获取userRAppInfoService
     *
     * @return userRAppInfoService
     */
    public UserRAppInfoService getUserRAppInfoService() {
        return userRAppInfoService;
    }

    /**
     * 设置userRAppInfoService
     *
     * @param userRAppInfoService 值
     */
    public void setUserRAppInfoService(UserRAppInfoService userRAppInfoService) {
        this.userRAppInfoService = userRAppInfoService;
    }

    /**
     * 获取shopRAppInfoService
     *
     * @return shopRAppInfoService
     */
    public ShopRAppInfoService getShopRAppInfoService() {
        return shopRAppInfoService;
    }

    /**
     * 设置shopRAppInfoService
     *
     * @param shopRAppInfoService 值
     */
    public void setShopRAppInfoService(ShopRAppInfoService shopRAppInfoService) {
        this.shopRAppInfoService = shopRAppInfoService;
    }

    /**
     * 获取shopInfoService
     *
     * @return shopInfoService
     */
    public ShopInfoService getShopInfoService() {
        return shopInfoService;
    }

    /**
     * 设置shopInfoService
     *
     * @param shopInfoService 值
     */
    public void setShopInfoService(ShopInfoService shopInfoService) {
        this.shopInfoService = shopInfoService;
    }

    /**
     * 获取appCode
     *
     * @return appCode
     */
    public String getAppCode() {
        return appCode;
    }

    /**
     * 设置appCode
     *
     * @param appCode 值
     */
    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    /**
     * 获取appInfoService
     *
     * @return appInfoService
     */
    public AppInfoService getAppInfoService() {
        return appInfoService;
    }

    /**
     * 设置appInfoService
     *
     * @param appInfoService 值
     */
    public void setAppInfoService(AppInfoService appInfoService) {
        this.appInfoService = appInfoService;
    }
}
