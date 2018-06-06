package cn.faury.fdk.shiro.realm.bean;

/**
 * 用户信息
 */
public interface IUserInfoBean {

    /**
     * 获取用户ID
     */
    Long getUserId();

    /**
     * 获取用户登录名
     */
    String getLoginName();

    /**
     * 获取生效日期
     */
    String getEfctYmd();

    /**
     * 获取过期日期
     */
    String getExprYmd();

    /**
     * 是否启用
     */
    String getIsEnable();
}
