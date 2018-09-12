package cn.faury.fdk.pay.tenpay.service;

import cn.faury.fdk.pay.common.HttpClient;
import cn.faury.fdk.pay.tenpay.TenpayConfig;
import cn.faury.fdk.pay.tenpay.XMLParser;
import cn.faury.fdk.pay.tenpay.listener.TenBillListener;
import cn.faury.fdk.pay.tenpay.protocol.TenBillRequest;
import cn.faury.fdk.pay.tenpay.protocol.TenBillResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TenpayServiceImpl implements TenpayService {
	
	private static final Logger log = LoggerFactory.getLogger(TenpayServiceImpl.class);
	
	private static HttpClient httpClient;

	@Override
	public void doDownloadBill(TenBillRequest request,
			TenBillListener resultListener) throws Exception {
		
		if (httpClient == null) {
			httpClient = HttpClient.newInstance(HttpClient.Aim.Ten);
		}
		
		String result = httpClient.doPost(TenpayConfig.DOWNLOAD_BILL_API, request);
		
		TenBillResponse response;

        try {
            //注意，这里失败的时候是返回xml数据，成功的时候反而返回非xml数据
            response = (TenBillResponse) XMLParser.getObjectFromXML(result, TenBillResponse.class);

            if (response == null || response.getReturn_code() == null) {
            	log.error("Case1:ERROR/对账单API请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问");
                resultListener.onFailByReturnCodeError(response);
                return;
            }
            if (response.getReturn_code().equals("FAIL")) {
                //注意：一般这里返回FAIL是出现系统级参数错误，请检测Post给API的数据是否规范合法
            	log.error("Case2:ERROR/对账单API系统返回失败，请检测Post给API的数据是否规范合法");
                resultListener.onFailByReturnCodeFail(response);
            }
        } catch (Exception e) {
            //注意，这里成功的时候是直接返回纯文本的对账单文本数据，非XML格式
            if (result.equals(null) || result.equals("")) {
            	log.error("Case4:ERROR/对账单API系统返回数据为空");
                resultListener.onDownloadBillFail(result);
            } else {
            	log.info("Case3:INFO/对账单API系统成功返回数据");
                resultListener.onDownloadBillSuccess(result);
            }
        } 
	}
	
}
