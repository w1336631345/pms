package com.kry.pms.api.pay;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.http.response.pay.Json;
import com.kry.pms.model.persistence.room.Building;
import com.kry.pms.service.pay.WechatPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.weixin4j.WeixinException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 功能描述: <br>微信支付相关接口
 * 〈〉
 * @Author: huanghaibin
 * @Date: 2020/4/10 19:58
 */
@RestController
@RequestMapping("/api/v1/pay/wechatPay")
public class WechatPayController {
    @Autowired
    WechatPayService wechatPayService;

    /**
     * 功能描述: <br>小程序后台登录，向微信平台发送获取access_token请求，并返回openId
     * 〈〉
     * @Param: [code, request]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/4/10 19:47
     */
    @RequestMapping("/login")
    public HttpResponse login(String code, HttpServletRequest request) throws IOException, WeixinException {
        HttpResponse hr = new HttpResponse();
        Map<String, Object> map = wechatPayService.login(code, request);
        hr.addData(map);
        return hr;
    }

    /**
     * 功能描述: <br>(小程序)发起微信支付
     * 〈〉
     * @Param: [openid, request]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/4/10 19:47
     */
    @RequestMapping("/wxPay")
    public HttpResponse wxPay(String openid, HttpServletRequest request){
        HttpResponse hr = new HttpResponse();
        Json json = wechatPayService.wxPay(openid, request);
        hr.addData(json);
        return hr;
    }

    /**
     * 功能描述: <br>扫码枪支付(必须传入条形码code)
     * 〈〉
     * @Param: [code, request]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/4/10 19:48
     */
    @RequestMapping("/sweepPay")
    public HttpResponse sweepPay(Integer total_fee, String productName, String auth_code, HttpServletRequest request) throws Exception {
        HttpResponse hr = new HttpResponse();
        if("".equals(auth_code) || auth_code == null){
            return hr.error("未获取到支付条形码");
        }
        Map<String, Object> map = wechatPayService.sweepPay(total_fee,productName, auth_code, request);
        hr.addData(map);
        return hr;
    }
    /**
     * 功能描述: <br>退款申请
     * 〈〉
     * @Param: [transaction_id]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/4/11 17:39
     */
    @RequestMapping("/refund")
    public HttpResponse refund(String transaction_id) throws Exception {
        HttpResponse hr = new HttpResponse();
        if("".equals(transaction_id) || transaction_id == null){
            return hr.error("请传入微信订单号");
        }
        Map<String, Object> map = wechatPayService.refund(transaction_id);
        hr.addData(map);
        return hr;
    }
    /**
     * 功能描述: <br>微信支付
     * 〈〉
     * @Param: [code, request]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/4/10 19:50
     */
    @RequestMapping("/wxNotify")
    public HttpResponse wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpResponse hr = new HttpResponse();
        wechatPayService.wxNotify(request, response);
        return hr.ok();
    }

}
