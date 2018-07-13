package cn.faury.fdk.mobile.shiro.token;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * APP用户登录身份
 */
public class AppUsernamePasswordToken extends UsernamePasswordToken {

	/**
	 * APP编码
	 */
	private String appCode;

	/**
	 * 用户名是否进行解码
	 */
	private boolean notDecodeUername = true;

	/**
	 * 构造函数
	 * 
	 * @param appCode
	 *            APP编码
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param rememberMe
	 *            记住我
	 * @param host
	 *            主机
	 */
	public AppUsernamePasswordToken(String appCode, String username, String password, boolean rememberMe, String host) {
		super(username, password, rememberMe, host);
		this.setAppCode(appCode);
	}

	/**
	 * 构造函数
	 * 
	 * @param appCode
	 *            APP编码
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param rememberMe
	 *            记住我
	 * @param host
	 *            主机
	 * @param notDecodeUername
	 *            是否
	 */
	public AppUsernamePasswordToken(String appCode, String username, String password, boolean rememberMe, String host,
                                    boolean notDecodeUername) {
		super(username, password, rememberMe, host);
		this.setAppCode(appCode);
		this.setNotDecodeUername(notDecodeUername);
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
	 * 获取notDecodeUername
	 *
	 * @return notDecodeUername
	 */
	public boolean isNotDecodeUername() {
		return notDecodeUername;
	}

	/**
	 * 设置notDecodeUername
	 *
	 * @param notDecodeUername 值
	 */
	public void setNotDecodeUername(boolean notDecodeUername) {
		this.notDecodeUername = notDecodeUername;
	}
}
