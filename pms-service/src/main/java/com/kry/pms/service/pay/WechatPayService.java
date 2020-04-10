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

    Map<String, Object> sweepPay(String auth_code, HttpServletRequest request) throws Exception;

    void wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception;
}