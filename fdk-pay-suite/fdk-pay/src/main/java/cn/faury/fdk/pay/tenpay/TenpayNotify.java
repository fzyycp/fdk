package cn.faury.fdk.pay.tenpay;

import cn.faury.fdk.pay.common.Util;
import cn.faury.fdk.pay.common.sign.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 财付通回调签名验证工具
 */
public class TenpayNotify {

	private static Logger log = LoggerFactory.getLogger(TenpayNotify.class);

	/**
	 * 签名校验
	 * @param queryParams 签名校验参数集
	 * @return 签名校验是否通过
	 */
    public static boolean verify(Map<String, String> queryParams) {

//    	签名校验
    	boolean pass = signVeryfy(queryParams);
    	
    	if (!pass) {
    		log.debug("【回调校验】【Tenpay】 - sign 校验结果：" + (pass ? "通过" : "未通过"));
            return false;
        }
    	
        return pass;
    }
    
    private static boolean signVeryfy(Map<String, String> queryParams){
    	
    	String orgsign = queryParams.get("sign");
    	if (orgsign == null || "".equals(orgsign)) {
    		log.debug("【回调校验】【Tenpay】 - 请求参数中签名（sign）参数为空");
            return false;
        }
    	
		String[] excludes = { "sign" };
		String append = "key=" + TenpayConfig.key;
    	
    	String queryString = Util.buildQueryString(queryParams, excludes, append);
    	
    	String sign = MD5.MD5Encode(queryString);
    	
    	return orgsign.equalsIgnoreCase(sign);
    }

}
