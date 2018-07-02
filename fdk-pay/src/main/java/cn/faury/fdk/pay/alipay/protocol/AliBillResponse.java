package cn.faury.fdk.pay.alipay.protocol;

import cn.faury.fdk.pay.alipay.AliError;

/**
 * 对账单响应封装
 */
public class AliBillResponse {
	
	private String is_success; //是否成功
	private String error; //错误代码
	private String sign; //签名
	private String sign_type; //签名类型
	
	
	private AliBillResponse(String is_success, String error, String sign,
			String sign_type) {
		super();
		this.is_success = is_success;
		this.error = error;
		this.sign = sign;
		this.sign_type = sign_type;
	}

	/**
	 * 构建对账单响应
	 * @param xml
	 * @return
	 */
	public static AliBillResponse build(String xml) {
		if (xml == null || "".equals(xml.trim()))
			return null;
		String is_success = getTagContent("is_success", xml);
		String error = getTagContent("error", xml);
		String sign = getTagContent("sign", xml);
		String sign_type = getTagContent("sign_type", xml);
		return new AliBillResponse(is_success, error, sign, sign_type);
	}

	private static String getTagContent(String tag, String xml) {
		int index_s = xml.indexOf("<" + tag + ">");
		if (index_s == -1)
			return null;
		int index_e = xml.indexOf("</" + tag + ">");
		if (index_e == -1)
			return null;
		return xml.substring(index_s + tag.length() + 2, index_e);

	}

	public String getIs_success() {
		return is_success;
	}

	public void setIs_success(String is_success) {
		this.is_success = is_success;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}
	
	/**
	 * 获取错误代码的含义
	 * @return
	 */
	public String getErrorMessage(){
		return AliError.getErrorMessage(error);
	}
}
