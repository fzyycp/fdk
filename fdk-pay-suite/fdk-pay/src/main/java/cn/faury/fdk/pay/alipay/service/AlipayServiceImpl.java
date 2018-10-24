package cn.faury.fdk.pay.alipay.service;

import cn.faury.fdk.pay.alipay.AlipayConfig;
import cn.faury.fdk.pay.alipay.listener.AliBillListener;
import cn.faury.fdk.pay.alipay.protocol.AliBillRequest;
import cn.faury.fdk.pay.alipay.protocol.AliBillResponse;
import cn.faury.fdk.pay.common.FdkPayHttpClient;
import cn.faury.fdk.pay.common.Util;
import cn.faury.fdk.pay.tenpay.service.TenpayServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlipayServiceImpl implements AlipayService {

	private static final Logger log = LoggerFactory.getLogger(TenpayServiceImpl.class);
	
	private static FdkPayHttpClient fdkPayHttpClient;
	
	@Override
	public void doDownloadBill(AliBillRequest request, AliBillListener listener) throws Exception {
		
		if (fdkPayHttpClient == null) {
			fdkPayHttpClient = FdkPayHttpClient.newInstance(FdkPayHttpClient.Aim.Ali);
		}
		
		String url = AlipayConfig.HTTPS_GATEWAY + "?_input_charset=" + AlipayConfig.inputCharset;
		
		String queryString = Util.buildQueryString(request.getQueryParams());

		String result = fdkPayHttpClient.doPost(url, queryString);

		AliBillResponse response = AliBillResponse.build(result);

		if (response == null) {
			listener.onDownloadBillFail(result);
			log.error("Case3:对账单API系统返回数据为空");
		} else if("T".equals(response.getIs_success())) {
			listener.onDownloadBillSuccess(result);
			log.info("Case1:对账单API系统成功返回数据");
		} else {
			listener.onFailByReturnCodeFail(response);
			log.error("Case2:对账单API系统返回错误码(is_success = "
					+ response.getIs_success() + ", error:<"+ response.getError() 
					+ ", " + response.getErrorMessage()	+ ">)");
		}
	}

}
