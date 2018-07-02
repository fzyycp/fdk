package cn.faury.fdk.pay.alipay.protocol;

import cn.faury.fdk.pay.alipay.AlipayConfig;
import cn.faury.fdk.pay.common.Util;
import cn.faury.fdk.pay.common.sign.MD5;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 对账单下载请求参数封装
 */
public class AliBillRequest {
	
	private String service = "account.page.query";
	private String partner = AlipayConfig.partner;
	private String _input_charset = AlipayConfig.input_charset;
	private String seller_email = AlipayConfig.seller_email;
	private String gmt_start_time;
	private String gmt_end_time;
	private String page_no;
	private String sign;
	private String sign_type = AlipayConfig.sign_type;
	
	/**
	 * 构造下载对账单请求参数
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @param pageNO 页数，大于0整数
	 * @throws UnsupportedEncodingException
	 */
	public AliBillRequest(Date startDate, Date endDate, int pageNO) {
		super();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.gmt_start_time = sdf.format(startDate);
		this.gmt_end_time = sdf.format(endDate);
		this.page_no = pageNO + "";
		String queryParams = Util.buildQueryString(getQueryParams(), new String[]{"sign", "sign_type"});
		sign = MD5.MD5Encode(queryParams + AlipayConfig.key, _input_charset);
	}
	
	/**
	 * 构造下载对账单请求参数
	 * @param startDate 开始日期{yyyy-MM-dd HH:mm:ss, 2015-07-29 00:00:00}
	 * @param endDate 结束日期{yyyy-MM-dd HH:mm:ss, 2015-07-30 00:00:00}
	 * @param pageNO 页数，大于0整数
	 * @throws UnsupportedEncodingException
	 */
	public AliBillRequest(String startDate, String endDate, String pageNO) {
		super();
		this.gmt_start_time = startDate;//new String(startDate.getBytes("ISO-8859-1"), "UTF-8");
		this.gmt_end_time = endDate;//new String(endDate.getBytes("ISO-8859-1"), "UTF-8");
		this.page_no = pageNO;
		String queryParams = Util.buildQueryString(getQueryParams(), new String[]{"sign", "sign_type"});
		sign = MD5.MD5Encode(queryParams + AlipayConfig.key, _input_charset);
	}
	
	public Map<String, String> getQueryParams() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("service", service);
		map.put("partner", partner);
		map.put("_input_charset", _input_charset);
		map.put("seller_email", seller_email);
		map.put("gmt_start_time", gmt_start_time);
		map.put("gmt_end_time", gmt_end_time);
		map.put("page_no", page_no);
		map.put("sign", sign);
		map.put("sign_type", sign_type);
		return map;
	}
	
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String get_input_charset() {
		return _input_charset;
	}

	public void set_input_charset(String _input_charset) {
		this._input_charset = _input_charset;
	}

	public String getSeller_email() {
		return seller_email;
	}

	public void setSeller_email(String seller_email) {
		this.seller_email = seller_email;
	}

	public String getGmt_start_time() {
		return gmt_start_time;
	}

	public void setGmt_start_time(String gmt_start_time) {
		this.gmt_start_time = gmt_start_time;
	}

	public String getGmt_end_time() {
		return gmt_end_time;
	}

	public void setGmt_end_time(String gmt_end_time) {
		this.gmt_end_time = gmt_end_time;
	}

	public String getPage_no() {
		return page_no;
	}

	public void setPage_no(String page_no) {
		this.page_no = page_no;
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
	
}
