package cn.faury.fdk.mobile.annotation;

import cn.faury.fdk.common.entry.RestResultEntry;

import javax.servlet.http.HttpServletRequest;

/**
 * 虚拟手机接口服务
 */
public interface IMobileService {

	/**
	 * 执行服务接口
	 * 
	 * @param request
	 *            web请求
	 * @return 返回结果
	 */
	public RestResultEntry execute(HttpServletRequest request);
}
