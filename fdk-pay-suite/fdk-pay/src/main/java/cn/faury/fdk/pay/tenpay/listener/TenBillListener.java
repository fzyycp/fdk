package cn.faury.fdk.pay.tenpay.listener;

import cn.faury.fdk.pay.tenpay.protocol.TenBillResponse;

/**
 * 下载对账单响应监听接口
 */
public interface TenBillListener {
	//API返回ReturnCode不合法，支付请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问
    void onFailByReturnCodeError(TenBillResponse downloadBillResData);

    //API返回ReturnCode为FAIL，支付API系统返回失败，请检测Post给API的数据是否规范合法
    void onFailByReturnCodeFail(TenBillResponse downloadBillResData);

    //下载对账单失败
    void onDownloadBillFail(String response);

    //下载对账单成功
    void onDownloadBillSuccess(String response);
}
