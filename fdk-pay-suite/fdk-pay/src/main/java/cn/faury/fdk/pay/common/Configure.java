package cn.faury.fdk.pay.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configure {

	private static Logger logger = LoggerFactory.getLogger(Configure.class);

	//请求代理服务器
	public static String proxyHost;
		
	//请求代理服务器端口
	public static int proxyPort = 80;
	
	static {
		init();
	}
	
	private static void init() {
		String proxy = Configs.me().getProperty("proxy");
		if (proxy != null && !"".equals(proxy = proxy.trim())) {
			int index = proxy.indexOf(':');
			if (index == -1) {
				proxyHost = proxy;
			} else if (index < 1) {
				logger.error("HTTP代理配置错误（proxy=proxyHost[:proxyPort]）");
				return;
			} else {
				proxyHost = proxy.substring(0, index);
				Integer port = null;
				try {
					port = Integer.valueOf(proxy.substring(index + 1));
				} catch (Exception e) {
				}
				if (port == null || port < 1 || port > 65535) {
					logger.error("HTTP代理端口配置错误（1-65535）");
					return;
				}
				proxyPort = port;
			}
		}
		if (proxyHost != null && "".equals(proxyHost.trim())) {
			proxyHost = null;
		}
	}
}

