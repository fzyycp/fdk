package cn.faury.fdk.pay.tenpay;

import cn.faury.fdk.http.client.HttpClient;
import cn.faury.fdk.http.client.conf.HttpConfig;
import cn.faury.fdk.http.client.core.HttpRequest;
import cn.faury.fdk.http.client.core.HttpResponse;
import cn.faury.fdk.pay.common.sign.MD5;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 * 微信管理类
 */
public class WXManager {
    private static Logger logger = LoggerFactory.getLogger(WXManager.class);

    /**
     * 执行统一下单，并返回统一下单的部分返回值
     *
     * @param outTradeNo  商户订单号
     * @param orderAmount 订单金额
     * @param body        订单描述
     * @return 统一下单部分返回值
     */
    public static Map<String, String> unifiedorderForApp(String outTradeNo, double orderAmount, String body) {
        logger.debug("客户端统一下单输入参数：outTradeNo={}，orderAmount={}，body={}", outTradeNo, orderAmount, body);
        String entity = genAppUnifiedOrderArgs(outTradeNo, orderAmount, body);
        logger.debug("提交微信统一下单参数：entity={}", entity);

        HttpClient httpClient = new HttpClient(new HttpConfig.Builder().build());
        HttpRequest httpRequest = HttpRequest.Builder.createBytesRequest();
        httpRequest.setUri("https://api.mch.weixin.qq.com/pay/unifiedorder");
        httpRequest.setMethod(HttpPost.METHOD_NAME);
        httpRequest.setPostDatas(entity);
        HttpResponse httpResponse = httpClient.doRequest(httpRequest);
        String content = new String(httpResponse.getByteResult());
        return WXManager.decodeXml(content);
    }

    /**
     * 获取App端支付请求
     *
     * @param unifiedOrder 统一下单结果
     * @return App端请求对象
     */
    public static Map<String, String> createAppPayReq(Map<String, String> unifiedOrder) {
        String nonceStr = genNonceStr();
        String timeStamp = String.valueOf(genTimeStamp());
        List<NameValuePair> signParams = new LinkedList<>();
        signParams.add(new BasicNameValuePair("appid", unifiedOrder.get("appid")));
        signParams.add(new BasicNameValuePair("noncestr", nonceStr));
        signParams.add(new BasicNameValuePair("package", "Sign=WXPay"));
        signParams.add(new BasicNameValuePair("partnerid", unifiedOrder.get("mch_id")));
        signParams.add(new BasicNameValuePair("prepayid", unifiedOrder.get("prepay_id")));
        signParams.add(new BasicNameValuePair("timestamp", timeStamp));

        String sign = genPackageSign(signParams);

        Map<String, String> payReq = new HashMap<>();
        payReq.put("appId", unifiedOrder.get("appid"));
        payReq.put("partnerId", unifiedOrder.get("mch_id"));
        payReq.put("prepayId", unifiedOrder.get("prepay_id"));
        payReq.put("nonceStr", nonceStr);
        payReq.put("timeStamp", timeStamp);
        payReq.put("packageValue", "Sign=WXPay");
        payReq.put("sign", sign);
        return payReq;
    }

    /**
     * 获取统一下单提交的参数字符串
     *
     * @param outTradeNo  商户订单号
     * @param orderAmount 订单金额
     * @param body        订单描述
     * @return 参数字符串
     */
    public static String genAppUnifiedOrderArgs(String outTradeNo, double orderAmount, String body) {

        try {
            String nonceStr = genNonceStr();
            double fee = orderAmount * 100;
            List<NameValuePair> packageParams = new LinkedList<>();
            packageParams.add(new BasicNameValuePair("appid", TenpayConfig.appID));
            packageParams.add(new BasicNameValuePair("body", body));
            packageParams.add(new BasicNameValuePair("mch_id", TenpayConfig.mchID));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));

            packageParams.add(new BasicNameValuePair("notify_url", TenpayConfig.notifyUrl));
            packageParams.add(new BasicNameValuePair("out_trade_no", outTradeNo));
            packageParams.add(new BasicNameValuePair("spbill_create_ip", "127.0.0.1"));
            packageParams.add(new BasicNameValuePair("total_fee", ToYuan(fee)));
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));

            String sign = WXManager.genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));
            return toXml(packageParams);
//            return new String(xmlstring.getBytes(StringUtil.UTF_8), "ISO8859-1");//这句加上就可以了把xml转码下,否则中文不能转换

        } catch (Exception e) {
            logger.error("genAppUnifiedOrderArgs fail, ex = {}" + e.getMessage(), e);
            return null;
        }
    }

    /**
     * 对参数进行生成签名
     */
    public static String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        params.sort(Comparator.comparing(NameValuePair::getName));

        for (NameValuePair param : params) {
            sb.append(param.getName());
            sb.append('=');
            sb.append(param.getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(TenpayConfig.key);

        return MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
    }

    public static long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    public static String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    public static String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");
            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * 解析xml
     */
    public static Map<String, String> decodeXml(String content) {
        try {
            Map<String, String> xml = new HashMap<>();
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (!"xml".equals(nodeName)) {
                            //实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }
            return xml;
        } catch (Exception e) {
            logger.error("decodeXml-Exception:{}", e.toString(), e);
        }
        return null;
    }

    /**
     * 取整  去除小数点后一位
     */
    public static String ToYuan(double str) {
        NumberFormat nf = new DecimalFormat("#0");
        nf.setRoundingMode(RoundingMode.DOWN);

        return nf.format(str);
    }
}
