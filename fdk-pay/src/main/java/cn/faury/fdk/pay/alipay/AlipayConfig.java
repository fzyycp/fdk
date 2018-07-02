package cn.faury.fdk.pay.alipay;

import cn.faury.fdk.pay.common.Configs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlipayConfig {

    /**
     * 日志记录器
     */
    private static Logger logger = LoggerFactory.getLogger(AlipayConfig.class);

    public static String partner = "";

    public static String key = "";

    public static String input_charset = "utf-8";

    public static final String sign_type = "MD5";

    public static String privateKey = "";

    public static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

    public static String seller_email = "";

    public static final String HTTPS_GATEWAY = "https://mapi.alipay.com/gateway.do";

    //支付宝消息验证地址
    public static final String HTTPS_VERIFY_URL = HTTPS_GATEWAY + "?service=notify_verify";

    static {
        init();
    }

    public static void init() {
        logger.error("========== AlipayConfig Config Start ==========");
        try {
            Configs configs = Configs.me();

            partner = configs.getProperty("alipay.partner", partner);
            key = configs.getProperty("alipay.key", key);
            privateKey = configs.getProperty("alipay.privateKey", privateKey);
            publicKey = configs.getProperty("alipay.publicKey", publicKey);
            input_charset = configs.getProperty("alipay.input_charset", input_charset);
            seller_email = configs.getProperty("alipay.seller_email", seller_email);
            logger.debug("=====partner=" + partner);
            logger.debug("=====key=" + key);
            logger.debug("=====privateKey=******");
            logger.debug("=====publicKey=" + publicKey);
            logger.debug("=====input_charset=" + input_charset);
            logger.debug("=====seller_email=" + seller_email);
            logger.error("========== AlipayConfig Config Success ==========");
        } catch (RuntimeException e) {
            logger.error("========== AlipayConfig Config Exception ==========", e);
        } finally {
            logger.error("========== AlipayConfig Config End ==========");
        }
    }

}
