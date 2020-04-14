package com.kry.pms.service.pay;

import com.kry.pms.model.http.response.pay.Json;
import org.weixin4j.WeixinException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public interface WechatPayService {

    Json wxPay(String openid, HttpServletRequest request);

    Map<String, Object> login(String code, HttpServletRequest request) throws WeixinException, IOException;

    Map<String, Object> sweepPay(Integer total_fee, String body, String auth_code, HttpServletRequest request, String hotelCode) throws Exception;

    void wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception;

    Map<String, Object> refund(String refund_fee, String transaction_id, String hotelCode) throws Exception;

    String doRefund(String mchId, String url, String data) throws Exception;

    Map<String, Object> orderquery(String out_trade_no, String transaction_id, String hotelCode) throws Exception;

    //撤销订单
    Map<String, Object> reverse(String transaction_id, String hotelCode) throws Exception;

    Map<String, Object> refundquery(String refund_id, String hotelCode) throws Exception;
}