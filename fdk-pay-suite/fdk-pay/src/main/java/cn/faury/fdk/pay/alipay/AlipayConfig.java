package cn.faury.fdk.pay.alipay;

public class AlipayConfig {

    public static String appId="";

    public static String partner = "";

    public static String privateKey = "";

    public static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

    public static String inputCharset = "utf-8";

    public static String signType = "RSA";

    public static String notifyUrl = "";

    public static final String HTTPS_GATEWAY = "https://mapi.alipay.com/gateway.do";

    // 支付宝的公钥，无需修改该值
    public static final String aliPublicKey  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

//    static {
//        init();
//    }
//
//    public static void init() {
//        logger.error("========== AlipayConfig Config Start ==========");
//        try {
//            Configs configs = Configs.me();
//
//            partner = configs.getProperty("alipay.partner", partner);
//            key = configs.getProperty("alipay.key", key);
//            privateKey = configs.getProperty("alipay.privateKey", privateKey);
//            publicKey = configs.getProperty("alipay.publicKey", publicKey);
//            input_charset = configs.getProperty("alipay.input_charset", input_charset);
//            seller_email = configs.getProperty("alipay.seller_email", seller_email);
//            logger.debug("=====partner=" + partner);
//            logger.debug("=====key=" + key);
//            logger.debug("=====privateKey=******");
//            logger.debug("=====publicKey=" + publicKey);
//            logger.debug("=====input_charset=" + input_charset);
//            logger.debug("=====seller_email=" + seller_email);
//            logger.error("========== AlipayConfig Config Success ==========");
//        } catch (RuntimeException e) {
//            logger.error("========== AlipayConfig Config Exception ==========", e);
//        } finally {
//            logger.error("========== AlipayConfig Config End ==========");
//        }
//    }

}
