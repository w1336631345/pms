package com.kry.pms.service.pay;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.http.response.pay.Json;
import org.weixin4j.WeixinException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface WechatPayService {

    Json wxPay(String openid, HttpServletRequest request);

    HttpResponse getOpenId(String code, HttpServletRequest request) throws WeixinException, IOException;

    HttpResponse sweepPay(Integer total_fee, String body, String auth_code, HttpServletRequest request, String hotelCode) throws Exception;

    String getUnionId(String access_token, String openId) throws WeixinException;

    void wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception;

    //付款码支付(服务商)
    HttpResponse sweepPayService(Integer total_fee, String body, String auth_code,
                                 HttpServletRequest request, String hotelCode) throws Exception;

    HttpResponse refund(String refund_fee, String transaction_id, String hotelCode) throws Exception;

    //申请退款（服务商）
    HttpResponse refundService(String refund_fee, String transaction_id, String hotelCode) throws Exception;

    String doRefund(String mchId, String url, String data, String path, String hotelCode) throws Exception;

    HttpResponse orderquery(String out_trade_no, String transaction_id, String hotelCode) throws Exception;

    //查询订单(服务商)
    HttpResponse orderqueryService(String out_trade_no, String transaction_id, String hotelCode) throws Exception;

    //撤销订单
    HttpResponse reverse(String transaction_id, String hotelCode) throws Exception;

    HttpResponse refundquery(String refund_id, String hotelCode) throws Exception;

    //退款查询(服务商)
    HttpResponse refundqueryService(String refund_id, String hotelCode) throws Exception;
}