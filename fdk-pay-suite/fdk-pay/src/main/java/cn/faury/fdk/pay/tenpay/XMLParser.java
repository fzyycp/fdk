package cn.faury.fdk.pay.tenpay;

import cn.faury.fdk.pay.common.Util;
import com.thoughtworks.xstream.XStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * XML解析器
 * @author wei.wang
 *
 */
public class XMLParser {

    /*
     * 从RefunQueryResponseString里面解析出退款订单数据
     * @param refundQueryResponseString RefundQuery API返回的数据
     * @return 因为订单数据有可能是多个，所以返回一个列表
     
    public static List<RefundOrderData> getRefundOrderList(String refundQueryResponseString) throws IOException, SAXException, ParserConfigurationException {
        List<RefundOrderData> list = new ArrayList<RefundOrderData>();

        Map<String,Object> map = XMLParser.getMapFromXML(refundQueryResponseString);

       int count = Integer.parseInt((String) map.get("refund_count"));

        if(count<1){
            return list;
        }

        RefundOrderData refundOrderData;

        for(int i=0;i<count;i++){
            refundOrderData = new RefundOrderData();

            refundOrderData.setOutRefundNo(Util.getStringFromMap(map,"out_refund_no_" + i,""));
            refundOrderData.setRefundID(Util.getStringFromMap(map,"refund_id_" + i,""));
            refundOrderData.setRefundChannel(Util.getStringFromMap(map,"refund_channel_" + i,""));
            refundOrderData.setRefundFee(Util.getIntFromMap(map,"refund_fee_" + i));
            refundOrderData.setCouponRefundFee(Util.getIntFromMap(map,"coupon_refund_fee_" + i));
            refundOrderData.setRefundStatus(Util.getStringFromMap(map,"refund_status_" + i,""));
            list.add(refundOrderData);
        }

        return list;
    }
     */

    /*
     * 从payQueryResponseString里面解析出退款订单数据
     * @param payQueryResponseString RefundQuery API返回的数据
     * @return 因为订单数据有可能是多个，所以返回一个列表
     
    public static List<CouponData> getCouponDataList(String payQueryResponseString) throws IOException, SAXException, ParserConfigurationException {
    	List<CouponData> list = new ArrayList<CouponData>();

        Map<String,Object> map = XMLParser.getMapFromXML(payQueryResponseString);

        int count = Integer.parseInt((String) map.get("coupon_count"));

        if(count<1){
            return list;
        }

        CouponData couponData;

        for(int i=0;i<count;i++){
            couponData = new CouponData();
            couponData.setCoupon_batch_id(Util.getStringFromMap(map,"coupon_batch_id_" + i,""));
            couponData.setCoupon_id(Util.getStringFromMap(map,"coupon_id_" + i,""));
            couponData.setCoupon_fee(Util.getStringFromMap(map,"coupon_fee_" + i,""));
            list.add(couponData);
        }

        return list;
    }
	*/


    public static Map<String,Object> getMapFromXML(String xmlString) throws ParserConfigurationException, IOException, SAXException {

        //这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream is =  Util.getStringStream(xmlString);
        Document document = builder.parse(is);

        //获取到document里面的全部结点
        NodeList allNodes = document.getFirstChild().getChildNodes();
        Node node;
        Map<String, Object> map = new HashMap<String, Object>();
        int i=0;
        while (i < allNodes.getLength()) {
            node = allNodes.item(i);
            if(node instanceof Element){
                map.put(node.getNodeName(),node.getTextContent());
            }
            i++;
        }
        return map;

    }

    public static Object getObjectFromXML(String xml, Class<?> tClass) {
		if (tClass == null) {
			return null;
		}
        //将从API返回的XML数据映射到Java对象
        XStream xStreamForResponseData = new XStream();
        xStreamForResponseData.alias("xml", tClass);
        xStreamForResponseData.ignoreUnknownElements();//暂时忽略掉一些新增的字段
        return xStreamForResponseData.fromXML(xml);
    }

}