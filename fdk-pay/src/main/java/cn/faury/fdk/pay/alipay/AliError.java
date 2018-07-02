package cn.faury.fdk.pay.alipay;

import java.util.HashMap;
import java.util.Map;

public class AliError {
	public static final Map<String, String> errors = new HashMap<String, String>();
	static {
		errors.put("ILLEGAL_SIGN", "签名不正确");
		errors.put("ILLEGAL_DYN_MD5_KEY", "动态密钥信息错误");
		errors.put("ILLEGAL_ENCRYPT", "加密不正确");
		errors.put("ILLEGAL_ARGUMENT", "参数不正确");
		errors.put("ILLEGAL_SERVICE", "接口名称不正确");
		errors.put("ILLEGAL_PARTNER", "合作伙伴ID不正确");
		errors.put("ILLEGAL_EXTERFACE", "接口配置不正确");
		errors.put("ILLEGAL_PARTNER_EXTERFACE", "合作伙伴接口信息不正确");
		errors.put("ILLEGAL_SECURITY_PROFILE", "未找到匹配的密钥配置");
		errors.put("ILLEGAL_AGENT", "代理ID不正确");
		errors.put("ILLEGAL_SIGN_TYPE", "签名类型不正确");
		errors.put("ILLEGAL_CHARSET", "字符集不合法");
		errors.put("ILLEGAL_CLIENT_IP", "客户端IP地址无权访问服务");
		errors.put("ILLEGAL_DIGEST_TYPE", "摘要类型不正确");
		errors.put("ILLEGAL_DIGEST", "文件摘要不正确");
		errors.put("ILLEGAL_FILE_FORMAT", "文件格式不正确");
		errors.put("ILLEGAL_ENCODING", "不支持该编码类型");
		errors.put("ILLEGAL_REQUEST_REFERER", "防钓鱼检查不支持该请求来源");
		errors.put("ILLEGAL_ANTI_PHISHING_KEY", "防钓鱼检查非法时间戳参数");
		errors.put("ANTI_PHISHING_KEY_TIMEOUT", "防钓鱼检查时间戳超时");
		errors.put("ILLEGAL_EXTER_INVOKE_IP", "防钓鱼检查非法调用IP");
		errors.put("ILLEGAL_NUMBER_FORMAT", "数字格式不合法");
		errors.put("ILLEGAL_INTEGER_FORMAT", "Int类型格式不合法");
		errors.put("ILLEGAL_MONEY_FORMAT", "金额格式不合法");
		errors.put("ILLEGAL_DATA_FORMAT", "日期格式错误");
		errors.put("REGEXP_MATCH_FAIL", "正则表达式匹配失败");
		errors.put("ILLEGAL_LENGTH", "参数值长度不合法");
		errors.put("PARAMTER_IS_NULL", "参数值为空");
		errors.put("HAS_NO_PRIVILEGE", "无权访问");
		errors.put("SYSTEM_ERROR", "支付宝系统错误");
		errors.put("SESSION_TIMEOUT", "session超时");
		errors.put("ILLEGAL_TARGET_SERVICE", "错误的target_service");
		errors.put("ILLEGAL_ACCESS_SWITCH_SYSTEM", "partner不允许访问该类型的系统");
		errors.put("ILLEGAL_SWITCH_SYSTEM", "切换系统异常");
		errors.put("EXTERFACE_IS_CLOSED", "接口已关闭");
	}
	
	public static String getErrorMessage(String errorCode){
		if(errorCode==null) return null;
		return errors.get(errorCode.trim().toUpperCase());
	}
	
}
