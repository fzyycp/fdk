package cn.faury.fdk.pay.alipay;

import cn.faury.fdk.pay.common.HttpClient;
import cn.faury.fdk.pay.common.Util;
import cn.faury.fdk.pay.common.sign.MD5;
import cn.faury.fdk.pay.common.sign.RSA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 阿里回调签名校验工具类
 */
public class AlipayNotify {

    private static Logger log = LoggerFactory.getLogger(AlipayNotify.class);
    
    /**
     * 阿里回调签名校验
     * @param queryParams 回调所有请求参数集
     * @return 是否校验成功
     */
    public static boolean verfiy(Map<String, String> queryParams){
    	
    	log.debug("【回调校验】参数如下:[" + Util.buildQueryString(queryParams) + "]");
    	
    	//notify 校验
    	boolean pass = notifyVerfiy(queryParams);
    	
		log.debug("【回调校验】【Alipay】 - notify 校验结果：" + (pass ? "通过" : "未通过"));
    	
		if (!pass) {
			return false;
		}
		
		//签名校验
		boolean pass2 = signVerfiy(queryParams);
		
		log.debug("【回调校验】【Alipay】 - sign 校验结果：" + (pass2 ? "通过" : "未通过"));
    	
    	return pass2;
    }
    
    //notify_id 验证
    private static boolean notifyVerfiy(Map<String, String> queryParams){
		
		String notify_id = queryParams.get("notify_id");

		if (notify_id == null) {
			log.debug("【回调校验】【Alipay】 - 回调 notify_id 参数为 null，跳过 notify_id 验证");
			return true;
		}

		String veryfy_url = AlipayConfig.HTTPS_VERIFY_URL 
				+ "&partner=" + AlipayConfig.partner 
				+ "&notify_id=" + notify_id;
		log.debug("【回调校验】【Alipay】 - 校验 veryfy_url="+veryfy_url);

		String responseTxt = HttpClient.simpleRequstFirstLine(veryfy_url);
		log.debug("【回调校验】【Alipay】 - 校验 responseTxt="+responseTxt);

		return "true".equals(responseTxt);
    }
    
    //签名验证
    public static boolean signVerfiy(Map<String, String> queryParams){
    	
    	String signType = queryParams.get("sign_type");
    	if (signType == null || "".equals(signType)) {
			signType = AlipayConfig.sign_type;
		}
    	log.debug("【回调校验】【Alipay】 - 签名验证类型：" + signType);
    	
    	String orgsign = queryParams.get("sign");
    	if (orgsign == null || orgsign == "") {
    		log.debug("【回调校验】【Alipay】 - 签名参数为空");
    		return false;
        }
    	
    	String charset = AlipayConfig.input_charset;
    	
    	String[] excludes = new String[]{"sign", "sign_type"};
    	
    	String queryString = Util.buildQueryString(queryParams, excludes);
    	
    	boolean pass = false;
    	
    	if (signType.equals("MD5")) {
    		String sign = MD5.MD5Encode(queryString + AlipayConfig.key, charset);
    		pass = orgsign.equalsIgnoreCase(sign);
        } else if (signType.equals("RSA")) {
        	pass = RSA.verify(queryString, orgsign, AlipayConfig.publicKey, charset);
        }
    	
    	return pass;
    }
    
}
