package cn.faury.fdk.pay.alipay.listener;

import cn.faury.fdk.pay.alipay.protocol.AliBillResponse;

/**
 * 对账单下载监听接口
 */
public interface AliBillListener {
	//下载对账单失败
    void onDownloadBillFail(String response);

    //下载对账单成功
    void onDownloadBillSuccess(String response);
    
    //下载对账单返回状态码不是T
    void onFailByReturnCodeFail(AliBillResponse aliBillResponse);
    
}	
