package cn.faury.fdk.pay.common;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 工具集
 */
public class Util {
	
	/**
	 * 根据查询参数名称按字典顺序构建查询字符串，排除空置
	 * @param queryParams 查询参数Map
	 * @return 返回查询字符串，如key1=value1&key2=value2&...&keyN=valueN
	 */
	public static String buildQueryString(Map<String, String> queryParams){
		return buildQueryString(queryParams, null, null);
	}
	
	/**
	 * 根据查询参数名称按字典顺序构建查询字符串，排除空置
	 * @param queryParams 查询参数Map
	 * @param append 最后附加的参数
	 * @return 返回查询字符串，如key1=value1&key2=value2&...&keyN=valueN
	 */
	public static String buildQueryString(Map<String, String> queryParams, String append){
		return buildQueryString(queryParams, null, append);
	}
	
	/**
	 * 根据查询参数名称按字典顺序构建查询字符串，排除空置
	 * @param queryParams 查询参数Map
	 * @param excludes 排除参数名称
	 * @return 返回查询字符串，如key1=value1&key2=value2&...&keyN=valueN
	 */
	public static String buildQueryString(Map<String, String> queryParams, String[] excludes){
		return buildQueryString(queryParams, excludes, null);
	}
	
	/**
	 * 根据查询参数名称按字典顺序构建查询字符串，排除空置
	 * @param queryParams 查询参数Map
	 * @param excludes 排除参数名称
	 * @param append 最后附加的参数
	 * @return 返回查询字符串，如key1=value1&key2=value2&...&keyN=valueN
	 */
	public static String buildQueryString(Map<String, String> queryParams, String[] excludes, String append){
		
		StringBuilder queryString = new StringBuilder();

		if (queryParams != null) {

			List<String> keys = new ArrayList<String>(queryParams.keySet());
			Collections.sort(keys);

			for (int i = 0; i < keys.size(); i++) {
				String key = keys.get(i);
				String value = queryParams.get(key);
				if (!exists(key, excludes) && value != null && !"".equals(value)) {
					queryString
						.append(i > 0 ? "&" : "")
						.append(key)
						.append("=")
						.append(value);
				}
			}
		}
		if (append != null) {
			if (queryString.length() > 0) {
				queryString.append("&");
			}
			queryString.append(append);
		}

		return queryString.toString();
	}
	
	/**
	 * 检查指定对象是否存在于对象数组中
	 * @param which 指定对象
	 * @param depos 数组
	 * @return 是否存在，如果对象which为null判断depos中是否有null值,否则使用which.equals()判断depos中是否有相等的值。
	 */
	public static boolean exists(Object which, Object[] depos) {
		if (depos != null && depos.length > 0) {
			if (which == null) {
				for (Object o : depos) {
					if (null == o) {
						return true;
					}
				}
			} else {
				for (Object o : depos) {
					if (which.equals(o)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static InputStream getStringStream(String sInputString) throws UnsupportedEncodingException {
        ByteArrayInputStream tInputStringStream = null;
        if (sInputString != null && !sInputString.trim().equals("")) {
            tInputStringStream = new ByteArrayInputStream(sInputString.getBytes("UTF-8"));
        }
        return tInputStringStream;
    }
}
