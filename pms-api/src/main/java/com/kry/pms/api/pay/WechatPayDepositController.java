package com.kry.pms.api.pay;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.http.response.pay.Json;
import com.kry.pms.service.pay.WechatPayDepositService;
import com.kry.pms.service.pay.WechatPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.weixin4j.WeixinException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 功能描述: <br>微信支付相关接口
 * 〈〉
 * @Author: huanghaibin
 * @Date: 2020/4/10 19:58
 */
@RestController
@RequestMapping("/api/v1/pay/wechatPayDeposit")
public class WechatPayDepositController extends BaseController {
    @Autowired
    WechatPayDepositService wechatPayDepositService;

    /**
     * 功能描述: <br>支付押金（付款码）
     * 〈〉
     * @Param: [code, request]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/4/10 19:48
     */
    @RequestMapping("/micropay")
    public HttpResponse micropay(Integer total_fee, String auth_code,  HttpServletRequest request) throws Exception {
        HttpResponse hr = new HttpResponse();
        if("".equals(auth_code) || auth_code == null){
            return hr.error("未获取到支付条形码");
        }
        hr = wechatPayDepositService.micropay(auth_code, total_fee, getCurrentHotleCode(), request);
        return hr;
    }

    /**
     * 功能描述: <br>查询订单
     * 〈〉
     * @Param: [total_fee, auth_code, request]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/7/6 15:14
     */
    @RequestMapping("/orderquery")
    public HttpResponse orderquery(String transaction_id, String out_trade_no) throws Exception {
        HttpResponse hr = new HttpResponse();
        hr = wechatPayDepositService.orderquery(transaction_id, out_trade_no, getCurrentHotleCode());
        return hr;
    }

    /**
     * 功能描述: <br>撤销订单（全额退押金）
     * 〈〉
     * @Param: [total_fee, auth_code, request]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/7/6 15:14
     */
    @RequestMapping("/reverse")
    public HttpResponse reverse(String transaction_id) throws Exception {
        HttpResponse hr = new HttpResponse();
        hr = wechatPayDepositService.reverse(transaction_id, getCurrentHotleCode());
        return hr;
    }

    /**
     * 功能描述: <br>消费押金
     * 〈〉
     * @Param: [total_fee, auth_code, request]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/7/6 15:14
     */
    @RequestMapping("/consume")
    public HttpResponse consume(Integer consume_fee, String transaction_id) throws Exception {
        HttpResponse hr = new HttpResponse();
        hr = wechatPayDepositService.consume(transaction_id, consume_fee, getCurrentHotleCode());
        return hr;
    }

    /**
     * 功能描述: <br>申请退款（押金）
     * 〈〉
     * @Param: [total_fee, auth_code, request]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/7/6 15:14
     */
    @RequestMapping("/refund")
    public HttpResponse refund(String refund_fee, String transaction_id) throws Exception {
        HttpResponse hr = new HttpResponse();
        hr = wechatPayDepositService.refund(refund_fee, transaction_id, getCurrentHotleCode());
        return hr;
    }

    /**
     * 功能描述: <br>查询押金退款
     * 〈〉
     * @Param: [total_fee, auth_code, request]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/7/6 15:14
     */
    @RequestMapping("/refundquery")
    public HttpResponse refundquery(String refund_id, String out_refund_no) throws Exception {
        HttpResponse hr = new HttpResponse();
        hr = wechatPayDepositService.refundquery(refund_id, out_refund_no, getCurrentHotleCode());
        return hr;
    }

}
