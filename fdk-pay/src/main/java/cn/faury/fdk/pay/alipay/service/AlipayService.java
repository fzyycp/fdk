package cn.faury.fdk.pay.alipay.service;


import cn.faury.fdk.pay.alipay.listener.AliBillListener;
import cn.faury.fdk.pay.alipay.protocol.AliBillRequest;

public interface AlipayService {
	
	/**
	 * 下载对账单
	 * @param request 请求数据封装
	 * @param resultListener 响应结果监听
	 * @throws Exception 
	 */
	public void doDownloadBill(AliBillRequest request, AliBillListener resultListener) throws Exception;

}
