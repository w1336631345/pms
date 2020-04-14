package com.kry.pms.model.persistence.pay;

/**
 * 功能描述: <br>微信支付
 * 〈〉
 * @Author: huanghaibin
 * @Date: 2020/4/10 19:33
 */
public class WechatPay {

    //小程序appid
    public static final String appid = "wxb16fd80662516230";
    //微信支付的商户id
    public static final String mch_id = "1504506341";
    //微信支付的商户密钥
    public static final String key = "douLEGYEbB09gZEjm5TSH55qIgKoKp67";
    //支付成功后的服务器回调url
    public static final String notify_url = "http://127.0.0.1:8080/";
    //签名方式
    public static final String SIGNTYPE = "MD5";
    //交易类型
    public static final String TRADETYPE = "MICROPAY";
    //微信扫码收款接口地址
    public static final String pay_url = "https://api.mch.weixin.qq.com/pay/micropay";
//    public static final String pay_url = "https://api.mch.weixin.qq.com/sandboxnew/pay/micropay";//测试接口
    //微信扫码收款订单查询接口地址
    public static final String orderquery_url = "https://api.mch.weixin.qq.com/pay/orderquery";
    //微信扫码收款订单取消接口地址
    public static final String reverse_url = "https://api.mch.weixin.qq.com/secapi/pay/reverse";
    //微信退款申请接口地址
    public static final String refund_url = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    //微信退款申请查询接口地址
    public static final String refundquery_url = "https://api.mch.weixin.qq.com/pay/refundquery";
}
