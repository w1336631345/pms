package com.kry.pms.service.pay;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.http.response.pay.Json;
import org.weixin4j.WeixinException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface WechatPayDepositService {

    HttpResponse micropay(String auth_code, Integer total_fee, String hotelCode) throws Exception;

    HttpResponse orderquery(String transaction_id, String out_trade_no, String hotelCode) throws Exception;

    HttpResponse reverse(String transaction_id, String hotelCode);

    HttpResponse consume(String transaction_id, Integer consume_fee, String hotelCode) throws Exception;

    HttpResponse refund(String refund_fee, String transaction_id, String hotelCode) throws Exception;

    HttpResponse refundquery(String refund_id, String out_refund_no, String hotelCode) throws Exception ;

    String postSSL(String mchId, String url, String data, String path, String hotelCode) throws Exception;
}