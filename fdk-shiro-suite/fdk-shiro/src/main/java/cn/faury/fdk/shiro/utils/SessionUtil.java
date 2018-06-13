package cn.faury.fdk.shiro.utils;

import cn.faury.fdk.common.utils.SerializeUtil;
import cn.faury.fdk.common.utils.StringUtil;
import org.apache.shiro.session.Session;

import java.io.Serializable;
import java.util.List;

public class SessionUtil {

    /**
     * Session中KEY：用户信息
     */
    public static final String SKEY_USERBEAN = "SESSION_USER_BEAN";

    /**
     * Session中KEY：角色信息信息
     */
    public static final String SKEY_ROLESBEAN = "SESSION_ROLES_BEAN";

    /**
     * Session中KEY：错误信息或正确信息传递
     */
    public static final String SKEY_MESSAGE = "SESSION_MESSAGE";

    /**
     * 获取当前登录的用户登录名. <br/>
     *
     * @return 当前登录的用户登录名
     */
    public static String getCurrentLoginName() {
        String principal = null;
        try {
            principal = ShiroUtil.principal();
        } catch (Exception e) {
            return null;
        }
        if (StringUtil.isEmpty(principal)) {
            return null;
        }
        return principal;
    }

    /**
     * 设置当前登录的用户名. <br/>
     */
    public static void setCurrentUserName(String userName) {
        try {
            if(StringUtil.isEmpty(userName)){
                userName = SessionUtil.getCurrentLoginName();
            }
            SessionUtil.setSessionAtt("userName", userName);
        }catch (Exception e){

        }
    }

    /**
     * 获取当前登录的用户名. <br/>
     *
     * @return 当前登录的用户名
     */
    public static String getCurrentUserName() {
        String userName = null;
        try {
            userName = SessionUtil.getSessionAtt("userName");
        } catch (Exception e) {
            return null;
        }
        if (StringUtil.isEmpty(userName)) {
            return null;
        }
        return userName;
    }

    /**
     * 设置当前登录的用户ID. <br/>
     */
    public static void setCurrentUserId(Long userId) {
        try {
            SessionUtil.setSessionAtt("userId", userId);
        }catch (Exception e){
        }
    }

    /**
     * 获取当前登录的用户ID. <br/>
     *
     * @return 当前登录的用户ID
     */
    public static String getCurrentUserId() {
        String userId = null;
        try {
            userId = String.valueOf(SessionUtil.getSessionAtt("userId"));
        } catch (Exception e) {
            return null;
        }
        if (StringUtil.isEmpty(userId)) {
            return null;
        }
        return userId;
    }

    /**
     * 设置当前登录的用户Bean.
     *
     * @param  userInfo 用户信息Bean对象
     */
    public static void setCurrentUserInfo(Serializable userInfo) {
        byte[] data = SerializeUtil.serialize(userInfo);
        try {
            setSessionAtt(SKEY_USERBEAN,data);
        }catch (Exception e){}
    }

    /**
     * 获取当前登录的用户Bean.
     *
     * @return 当前登录的用户Bean
     */
    public static <T> T getCurrentUserInfo(Class<T> clazz) {
        if (getSessionAtt(SKEY_USERBEAN) != null) {// 检查Session中是否存在
            byte[] data = getSessionAtt(SKEY_USERBEAN);
            if (data != null) {
                return SerializeUtil.deserialize(data, clazz);
            }
        }

        return null;
    }

    /**
     * 设置当前登录的角色Bean列表.
     *
     * @param roles 角色列表
     */
    public static void setCurrentRolesInfo(List<? extends  Serializable> roles) {
        byte[] data = SerializeUtil.serialize(roles);
        try {
            setSessionAtt(SKEY_ROLESBEAN,data);
        }catch (Exception e){}
    }

    /**
     * 获取当前登录用户的角色Bean列表.
     *
     * @return 当前登录用户的角色列表
     */
    public static <T> T getCurrentRolesInfo(Class<T> clazz) {
        if (getSessionAtt(SKEY_ROLESBEAN) != null) {// 检查Session中是否存在
            byte[] data = getSessionAtt(SKEY_ROLESBEAN);
            if (data != null) {
                return SerializeUtil.deserialize(data, clazz);
            }
        }

        return null;
    }



    /**
     * 获取用户Session
     *
     * @return 用户Session
     */
    public static Session getSession() {
        return ShiroUtil.getShiroSession();
    }

    /**
     * 向Session中添加属性
     *
     * @param key
     *            键
     * @param value
     *            值
     */
    public static void setSessionAtt(String key, Object value) {
        ShiroUtil.setShiroSessionAttr(key, value);
    }

    /**
     * 获取Session中的值
     *
     * @param key
     *            键
     * @return 对应的值
     */
    public static <T> T getSessionAtt(String key) {
        return ShiroUtil.getShiroSessionAttr(key);
    }

    /**
     * 删除Session中的值
     *
     * @param key
     *            键
     */
    public static void removeSessionAtt(String key) {
        ShiroUtil.removeShiroSessionAttr(key);
    }

    /**
     * 获取Session中存放的信息
     *
     * @return 信息
     */
    public static String getSessionMessage() {
        return getSessionAtt(SKEY_MESSAGE);
    }

    /**
     * 删除Session中存放的信息
     *
     */
    public static void removeSessionMessage() {
        removeSessionAtt(SKEY_MESSAGE);
    }

    /**
     * 设置Session中存放的信息
     *
     * @param message
     *            信息
     */
    public static void setSessionMessage(String message) {
        setSessionAtt(SKEY_MESSAGE, message);
    }

    /**
     * 验证Session中是否有旧版本标识
     *
     * @return 是否访问旧版本
     */
    public static void setOldStyle() {
        try {
            SessionUtil.setSessionAtt("oldPage", "true");
        }catch (Exception e){
        }
    }

    /**
     * 验证Session中是否有旧版本标识
     *
     * @return 是否访问旧版本
     */
    public static boolean isOldStyle() {
        return "true".equals(SessionUtil.getSessionAtt("oldPage"));
    }

}
