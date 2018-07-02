package cn.faury.fdk.pay.tenpay;

import cn.faury.fdk.pay.common.Configs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static cn.faury.fdk.pay.alipay.AlipayConfig.*;

public class TenpayConfig {

    /**
     * 日志记录器
     */
    private static Logger logger = LoggerFactory.getLogger(TenpayConfig.class);

    //sdk的版本号
    public static final String sdkVersion = "java sdk 1.0.1";

    //这个就是自己要保管好的私有Key了（切记只能放在自己的后台代码里，不能放在任何可能被看到源代码的客户端程序中）
    //每次自己Post数据给API的时候都要用这个key来对所有字段进行签名，生成的签名会放在Sign这个字段，
    //API收到Post数据的时候也会用同样的签名算法对Post过来的数据进行签名和验证
    //收到API的返回的时候也要用这个key来对返回的数据算下签名，跟API的Sign数据进行比较，如果值不一致，有可能数据被第三方给篡改
    //PARTNER_KEY
    public static String key = "";

    //微信分配的公众号ID（开通公众号之后可以获取到）
    //APP_ID
    public static String appID = "";

    //微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
    //PARTNER
    public static String mchID = "";

    //受理模式下给子商户分配的子商户号
    public static String subMchID = "";

    //HTTPS证书的本地路径
    public static String certLocalPath = "";

    //HTTPS证书密码，默认密码等于商户号MCHID
    public static String certPassword = "";

//    public static String NOTIFY_URL = ""; // 支付完成后的回调处理页面,*替换成notify_url.asp所在路径
//    public static String LOGING_DIR = ""; // 日志保存路径
//    public static String signType = "SHA1";
//    public static String bank_type = "WX";
//    public static String fee_type = "1";
//    public static String sign_type = "MD5";
//    public static String service_version = "1.1";
//    public static String sign_key_index = "1";
//    public static String op_user_id = "";
//    public static String op_user_passwd = "";

    //cft_signtype:0:默认值，不需要财付通签名，效率最高；其他保留
//    public static String cft_signtype = "0";
    //字符编码格式 目前支持 gbk 或 utf-8
//  public static String input_charset = "utf-8";
//  public static String trade_type_NATIVE = "NATIVE";
//  public static String trade_type_JSAPI = "JSAPI";


    // 
//    public static String submit_url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    // 关闭订单接口
//    public static String close_url = "https://api.mch.weixin.qq.com/pay/closeorder";
    //以下是几个API的路径：
    //1）被扫支付API
    public static String PAY_API = "https://api.mch.weixin.qq.com/pay/micropay";
    //2）被扫支付查询API - query_url
    public static String PAY_QUERY_API = "https://api.mch.weixin.qq.com/pay/orderquery";
    //3）退款API - refund_url
    public static String REFUND_API = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    //4）退款查询API - refundquery_url
    public static String REFUND_QUERY_API = "https://api.mch.weixin.qq.com/pay/refundquery";
    //5）撤销API
    public static String REVERSE_API = "https://api.mch.weixin.qq.com/secapi/pay/reverse";
    //6）下载对账单API - tradeReport_url
    public static String DOWNLOAD_BILL_API = "https://api.mch.weixin.qq.com/pay/downloadbill";
    //7) 统计上报API
    public static String REPORT_API = "https://api.mch.weixin.qq.com/payitil/report";

    static {
        init();
    }

    /**
     * 初始化
     */
    public static void init() {
        logger.error("========== TenpayConfig Config Start ==========");
        try {
            Configs configs = Configs.me();
            mchID = configs.getProperty("tenpay.partner", mchID);
            key = configs.getProperty("tenpay.key", key);
            appID = configs.getProperty("tenpay.appid", appID);
            certLocalPath = configs.getProperty("tenpay.certLocalPath", certLocalPath);
            certPassword = configs.getProperty("tenpay.certPassword", certPassword);
//        input_charset = configs.getProperty("tenpay.input_charset", "utf-8");
//        NOTIFY_URL = configs.getProperty("tenpay.NOTIFY_URL", "");
            logger.debug("=====mchID=" + mchID);
            logger.debug("=====key=" + key);
            logger.debug("=====appID=" + appID);
            logger.debug("=====certLocalPath=" + certLocalPath);
            logger.debug("=====certPassword=***");
            logger.error("========== TenpayConfig Config Success ==========");
        } catch (RuntimeException e) {
            logger.error("========== TenpayConfig Config Exception ==========", e);
        } finally {
            logger.error("========== TenpayConfig Config End ==========");
        }
    }


}
