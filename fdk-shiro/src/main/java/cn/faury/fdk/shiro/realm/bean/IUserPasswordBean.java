package cn.faury.fdk.shiro.realm.bean;

/**
 * 用户密码信息
 */
public interface IUserPasswordBean {
    /**
     * 获取用户ID
     */
    Long getUserId();


    /**
     * 获取用户密码
     */
    String getPassword();
}
