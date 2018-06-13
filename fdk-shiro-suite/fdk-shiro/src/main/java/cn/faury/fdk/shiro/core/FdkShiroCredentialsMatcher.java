/*
 *
 * 基础类库 f-shiro
 *
 * @author faury
 *
 * 版权所有：秋刀鱼
 * Copyright (c) http://www.faury.cn
 *
 */

package cn.faury.fdk.shiro.core;

import cn.faury.fdk.common.utils.SigAESUtil;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;


/**
 * 密码加密器
 */
public class FdkShiroCredentialsMatcher extends SimpleCredentialsMatcher {

	@Override
	protected Object getCredentials(AuthenticationToken token) {
		Object cred = token.getCredentials();
		if (isByteSource(cred)) {
			String password = new String(toBytes(cred));
			return SigAESUtil.encryptPassWord(password);
		}
		return super.getCredentials(token);
	}
}
