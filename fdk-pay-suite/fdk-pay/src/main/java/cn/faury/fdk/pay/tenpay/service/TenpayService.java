package cn.faury.fdk.pay.tenpay.service;


import cn.faury.fdk.pay.tenpay.listener.TenBillListener;
import cn.faury.fdk.pay.tenpay.protocol.TenBillRequest;

public interface TenpayService {
	
	/**
     * 请求对账单下载服务
     * @param request 这个数据对象里面包含了API要求提交的各种数据字段
     * @param resultListener 商户需要自己监听被扫支付业务逻辑可能触发的各种分支事件，并做好合理的响应处理
     * @return API返回的XML数据
     * @throws Exception
     */
    public void doDownloadBill(TenBillRequest request, TenBillListener resultListener) throws Exception;
        
}
