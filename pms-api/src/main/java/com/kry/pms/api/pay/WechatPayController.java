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
     * @Param: [refund_fee,transaction_id]:退款金额，微信订单号
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/4/11 17:39
     */
    @RequestMapping("/refund")
    public HttpResponse refund(String refund_fee, String transaction_id) throws Exception {
        HttpResponse hr = new HttpResponse();
        if("".equals(transaction_id) || transaction_id == null){
            return hr.error("请传入微信订单号");
        }
        Map<String, Object> map = wechatPayService.refund(refund_fee, transaction_id);
        hr.addData(map);
        return hr;
    }

    /**
     * 功能描述: <br>查询订单
     * 〈〉
     * @Param: [transaction_id]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/4/13 14:11
     */
    @RequestMapping("/orderquery")
    public HttpResponse orderquery(String out_trade_no, String transaction_id) throws Exception {
        HttpResponse hr = new HttpResponse();
        if("".equals(transaction_id) || transaction_id == null){
            if("".equals(out_trade_no) || out_trade_no == null){
                return hr.error("请传入微信订单号");
            }
        }
        Map<String, Object> map = wechatPayService.orderquery(out_trade_no, transaction_id);
        hr.addData(map);
        return hr;
    }

    /**
     * 功能描述: <br> 撤销订单
     * 支付交易返回失败或支付系统超时，调用该接口撤销交易。如果此订单用户支付失败，微信支付系统会将此订单关闭；如果用户支付成功，微信支付系统会将此订单资金退还给用户。
     * 注意：7天以内的交易单可调用撤销，其他正常支付的单如需实现相同功能请调用申请退款API。提交支付交易后调用【查询订单API】，没有明确的支付结果再调用【撤销订单API】。
     * 〈〉调用支付接口后请勿立即调用撤销订单API，建议支付后至少15s后再调用撤销订单接口。
     * @Param: [transaction_id]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/4/13 14:25
     */
    @RequestMapping("/reverse")
    public HttpResponse reverse(String transaction_id) throws Exception {
        HttpResponse hr = new HttpResponse();
        if("".equals(transaction_id) || transaction_id == null){
            return hr.error("请传入微信订单号");
        }
        Map<String, Object> map = wechatPayService.reverse(transaction_id);
        hr.addData(map);
        return hr;
    }
    /**
     * 功能描述: <br>查询退款
     * 〈〉
     * @Param: [transaction_id]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/4/13 14:31
     */
    @RequestMapping("/refundquery")
    public HttpResponse refundquery(String refund_id) throws Exception {
        HttpResponse hr = new HttpResponse();
        if("".equals(refund_id) || refund_id == null){
            return hr.error("请传入微信订单号");
        }
        Map<String, Object> map = wechatPayService.refundquery(refund_id);
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
