package cn.faury.fdk.shiro.realm.service;


import cn.faury.fdk.common.anotation.permission.Read;
import cn.faury.fdk.shiro.realm.bean.IUserInfoBean;
import cn.faury.fdk.shiro.realm.bean.IUserPasswordBean;

/**
 * 用户服务协议
 */
public interface IUserService {

	/**
	 * 根据用户登录名获取用户信息
	 * 
	 * @param loginName
	 *            用户登录名
	 * @return 用户信息
	 */
	@Read
	<T extends IUserInfoBean> T getUserInfoByLoginName(final String loginName);

	/**
	 * 根据用户ID获取用户密码信息
	 * 
	 * @param userId
	 *            用户ID
	 * @return 用户密码信息
	 */
	@Read
	<T extends IUserPasswordBean> T getUserPasswordByUserId(final Long userId);

}
