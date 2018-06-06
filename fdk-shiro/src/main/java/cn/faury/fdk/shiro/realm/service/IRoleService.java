package cn.faury.fdk.shiro.realm.service;

import cn.faury.fdk.common.anotation.permission.Read;
import cn.faury.fdk.shiro.realm.bean.IRoleInfoBean;

import java.util.List;

public interface IRoleService {

    /**
     * 根据用户ID获取指定系统下的角色信息，只查询启用状态的角色和业务系统
     *
     * @param systemCode
     *            业务系统编码
     * @param userId
     *            用户ID
     * @return 用户角色列表
     */
    @Read
    <T extends IRoleInfoBean> List<T> getUserRolesByUserId(final String systemCode, final Long userId);

    /**
     * 根据用户ID获取指定系统下的角色授权信息
     *
     * @param systemCode
     *            业务系统编码
     * @param userId
     *            用户ID
     * @return 用户授权列表
     */
    @Read
    List<String> getUserRolePermsByUserId(final String systemCode, final Long userId);
}
