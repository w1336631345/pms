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
    public static final String notify_url = "http://127.0.0.1:8080/vasms-uche/weixin/wxNotify";
    //签名方式
    public static final String SIGNTYPE = "MD5";
    //交易类型
    public static final String TRADETYPE = "JSAPI";
    //微信统一下单接口地址
    public static final String pay_url = "https://api2.mch.weixin.qq.com/pay/micropay";
}
