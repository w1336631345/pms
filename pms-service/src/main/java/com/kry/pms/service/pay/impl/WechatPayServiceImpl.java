package com.kry.pms.service.pay.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.dao.pay.WechatMerchantsDao;
import com.kry.pms.dao.pay.WechatPayRecordDao;
import com.kry.pms.dao.pay.WechatRefundRecordDao;
import com.kry.pms.model.http.response.pay.Json;
import com.kry.pms.model.http.response.pay.OAuthJsToken;
import com.kry.pms.model.persistence.pay.WechatMerchants;
import com.kry.pms.model.persistence.pay.WechatPay;
import com.kry.pms.model.persistence.pay.WechatPayRecord;
import com.kry.pms.model.persistence.pay.WechatRefundRecord;
import com.kry.pms.pay.IpUtils;
import com.kry.pms.pay.StringUtils;
import com.kry.pms.pay.XMLBeanUtil;
import com.kry.pms.pay.weixin.PayUtil;
import com.kry.pms.service.pay.WechatPayService;
import com.kry.pms.util.BeanToMapUtils;
import com.kry.pms.util.ChangeCharUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.weixin4j.WeixinException;
import org.weixin4j.WeixinSupport;
import org.weixin4j.http.HttpsClient;
import org.weixin4j.http.Response;

import javax.net.ssl.SSLContext;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.http.conn.ssl.SSLContexts.*;

@Service
public class WechatPayServiceImpl extends WeixinSupport implements WechatPayService  {

	@Autowired
	WechatPayRecordDao wechatPayRecordDao;
	@Autowired
	WechatRefundRecordDao wechatRefundRecordDao;
	@Autowired
    WechatMerchantsDao wechatMerchantsDao;

	private Logger logger = LoggerFactory.getLogger(getClass());

	private static final String appid = "wx09adf502d19bf421";	    //微小程序appid
	private static final String secret = "3d1449aedc049746173103927f711e4b";	//微信小程序密钥
	private static final String grant_type = "authorization_code";

	@Override
	public Json wxPay(String openid, HttpServletRequest request){
		Json json = new Json();
		try{
			//生成的随机字符串
			String nonce_str = StringUtils.getRandomStringByLength(32);
			//商品名称
			String body = "test";
			//获取本机的ip地址
			String spbill_create_ip = IpUtils.getIpAddr(request);

			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmsssss");
			String orderNo = sdf.format(date);
			String money = "1";//支付金额，单位：分，这边需要转成字符串类型，否则后面的签名会失败

			Map<String, String> packageParams = new HashMap<String, String>();
			packageParams.put("appid", WechatPay.appid);
			packageParams.put("body", body);
			packageParams.put("mch_id", WechatPay.mch_id);
			packageParams.put("nonce_str", nonce_str);
			packageParams.put("notify_url", WechatPay.notify_url);
			packageParams.put("openid", openid);
			packageParams.put("out_trade_no", orderNo);//商户订单号
			packageParams.put("spbill_create_ip", spbill_create_ip);
			packageParams.put("total_fee", money);//支付金额，这边需要转成字符串类型，否则后面的签名会失败
			packageParams.put("trade_type", WechatPay.TRADETYPE);

			SortedMap<String, String> packageParams2 = new TreeMap<String, String>();
			packageParams2.put("appid", WechatPay.appid);
			packageParams2.put("body", body);
			packageParams2.put("mch_id", WechatPay.mch_id);
			packageParams2.put("nonce_str", nonce_str);
			packageParams2.put("notify_url", WechatPay.notify_url);
			packageParams2.put("openid", openid);
			packageParams2.put("out_trade_no", orderNo);//商户订单号
			packageParams2.put("spbill_create_ip", spbill_create_ip);
			packageParams2.put("total_fee", money);//支付金额，这边需要转成字符串类型，否则后面的签名会失败
			packageParams2.put("trade_type", WechatPay.TRADETYPE);

			// 除去数组中的空值和签名参数
			packageParams = PayUtil.paraFilter(packageParams);
			String prestr = PayUtil.createLinkString(packageParams); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
			//MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
			String mysign = PayUtil.sign(prestr, WechatPay.key, "utf-8").toUpperCase();
			logger.info("=======================spbill_create_ip：" + spbill_create_ip + "=====================");
			logger.info("=======================第一次签名1：" + mysign + "=====================");
			//拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
			String xml = "<xml>" + "<appid>" + WechatPay.appid + "</appid>"
					+ "<body>" + body + "</body>"
					+ "<mch_id>" + WechatPay.mch_id + "</mch_id>"
					+ "<nonce_str>" + nonce_str + "</nonce_str>"
					+ "<notify_url>" + WechatPay.notify_url + "</notify_url>"
					+ "<openid>" + openid + "</openid>"
					+ "<out_trade_no>" + orderNo + "</out_trade_no>"
					+ "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>"
					+ "<total_fee>" + money + "</total_fee>"
					+ "<trade_type>" + WechatPay.TRADETYPE + "</trade_type>"
					+ "<sign>" + mysign + "</sign>"
					+ "</xml>";

			System.out.println("调试模式_统一下单接口 请求XML数据：" + xml);

			//调用统一下单接口，并接受返回的结果
			String result = PayUtil.httpRequest(WechatPay.pay_url, "POST", xml);

			System.out.println("调试模式_统一下单接口 返回XML数据1：" + result);

			// 将解析结果存储在HashMap中
			Map map = PayUtil.doXMLParse(result);

			String return_code = (String) map.get("return_code");//返回状态码

			//返回给移动端需要的参数
			Map<String, Object> response = new HashMap<String, Object>();
			if(return_code == "SUCCESS" || return_code.equals(return_code)){
				// 业务结果
				String prepay_id = (String) map.get("prepay_id");//返回的预付单信息
				response.put("nonceStr", nonce_str);
				response.put("package", "prepay_id=" + prepay_id);
				response.put("signType", WechatPay.SIGNTYPE);
				Long timeStamp = System.currentTimeMillis() / 1000;
				response.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误

				String stringSignTemp = "appId=" + WechatPay.appid + "&nonceStr=" + nonce_str + "&package=prepay_id=" + prepay_id+ "&signType=" + WechatPay.SIGNTYPE + "&timeStamp=" + timeStamp;
				//再次签名，这个签名用于小程序端调用wx.requesetPayment方法
				String paySign = PayUtil.sign(stringSignTemp, WechatPay.key, "utf-8").toUpperCase();
				logger.info("=======================第二次签名：" + paySign + "=====================");

				response.put("paySign", paySign);

				//更新订单信息
				//业务逻辑代码
			}

			response.put("appid", WechatPay.appid);

			json.setSuccess(true);
			json.setData(response);
		}catch(Exception e){
			e.printStackTrace();
			json.setSuccess(false);
			json.setMsg("发起失败");
		}
		return json;
	}
	@Override
    public HttpResponse getOpenId(String code, HttpServletRequest request) throws WeixinException, IOException {
		HttpResponse hr = new HttpResponse();
        if (code == null || code.equals("")) {
            throw new WeixinException("invalid null, code is null.");
        }
        //拼接参数
        String param = "?grant_type=" + grant_type + "&appid=" + appid + "&secret=" + secret + "&js_code=" + code;
        //创建请求对象
        HttpsClient http = new HttpsClient();
        //调用获取access_token接口
        Response res = http.get("https://api.weixin.qq.com/sns/jscode2session" + param);
        //根据请求结果判定，是否验证成功
        JSONObject jsonObj = res.asJSONObject();
        if (jsonObj != null) {
            Object errcode = jsonObj.get("errcode");
            if (errcode != null) {
                //返回异常信息
                return hr.error(9999,jsonObj.get("errmsg").toString());
            }
            ObjectMapper mapper = new ObjectMapper();
            OAuthJsToken oauthJsToken = mapper.readValue(jsonObj.toJSONString(),OAuthJsToken.class);
			hr.setData(oauthJsToken);
        }
        return hr;
    }
	@Override
	public void wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream()));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while((line = br.readLine())!=null){
			sb.append(line);
		}
		br.close();
		//sb为微信返回的xml
		String notityXml = sb.toString();
		String resXml = "";
		System.out.println("接收到的报文：" + notityXml);

		Map map = PayUtil.doXMLParse(notityXml);

		String returnCode = (String) map.get("return_code");
		if("SUCCESS".equals(returnCode)){
			//验证签名是否正确
			if(PayUtil.verify(PayUtil.createLinkString(map), (String)map.get("sign"), WechatPay.key, "utf-8")){
				/**此处添加自己的业务逻辑代码start**/


				/**此处添加自己的业务逻辑代码end**/

				//通知微信服务器已经支付成功
				resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
						+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
			}
		}else{
			resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
					+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
		}
		System.out.println(resXml);
		System.out.println("微信支付回调数据结束");

		BufferedOutputStream out = new BufferedOutputStream(
				response.getOutputStream());
		out.write(resXml.getBytes());
		out.flush();
		out.close();
	}

	//付款码支付
	@Override
	public HttpResponse sweepPay(Integer total_fee, String body, String auth_code,
								 HttpServletRequest request, String hotelCode) throws Exception {
		HttpResponse hr = new HttpResponse();
		Map<String, Object> map = new HashMap<>();
		//生成的随机字符串
		String nonce_str = StringUtils.getRandomStringByLength(32);
		//商品描述
//		String body = body;
		String attach = "订单额外描述";
		//获取本机的ip地址
		String spbill_create_ip = IpUtils.getIpAddr(request);

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmsssss");
		String orderNo = sdf.format(date);
		Integer money = total_fee;//支付金额，单位：分，这边需要转成字符串类型，否则后面的签名会失败
        WechatMerchants wm = wechatMerchantsDao.findByHotelCode(hotelCode);
		if(wm == null){
			return hr.error("请检查微信商户信息是否录入");
		}
		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appid", wm.getAppid());
		packageParams.put("auth_code", auth_code);
		packageParams.put("body", body);
		packageParams.put("mch_id", wm.getMchId());
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("out_trade_no", orderNo);//商户订单号
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("total_fee", money.toString());//支付金额，这边需要转成字符串类型，否则后面的签名会失败

		// 除去数组中的空值和签名参数
		packageParams = PayUtil.paraFilter(packageParams);
		String prestr = PayUtil.createLinkString(packageParams); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		//MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
		String mysign = PayUtil.sign(prestr, wm.getSecretKey(), "utf-8").toUpperCase();
		logger.info("=======================spbill_create_ip：" + spbill_create_ip + "=====================");
		logger.info("=======================第一次签名1：" + mysign + "=====================");
        packageParams.put("sign", mysign);
		//拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
		String xml = XMLBeanUtil.map2XmlString(packageParams);
		System.out.println("调试模式_统一下单接口 请求XML数据：" + xml);
		//调用统一下单接口，并接受返回的结果
		String result = PayUtil.httpRequest(WechatPay.pay_url, "POST", xml);
		System.out.println("调试模式_统一下单接口 返回XML数据1：" + result);
		// 将解析结果存储在HashMap中
		map = PayUtil.doXMLParse(result);
		String resultCode = MapUtils.getString(map, "result_code");
		if("SUCCESS".equals(resultCode)){
			//支付成功
		}else {
			//支付失败,有可能提示用户输入密码，就不会返回transaction_id
			map.put("out_trade_no", orderNo);
		}
		Map<String, Object> mapw = ChangeCharUtil.humpToLineMap(map);
		WechatPayRecord wpr = BeanToMapUtils.toBean(WechatPayRecord.class, mapw);
		wpr.setTotalFee(total_fee.toString());
		wpr.setHotelCode(hotelCode);
		wpr.setBody(body);
		wechatPayRecordDao.saveAndFlush(wpr);//保存记录
		hr.addData(map);
		return hr;
	}

	//申请退款（参数整理）
	@Override
	public HttpResponse refund(String refund_fee, String transaction_id, String hotelCode) throws Exception {
		HttpResponse hr = new HttpResponse();
//		WechatPayRecord wpr = new WechatPayRecord();
		WechatPayRecord wpr = wechatPayRecordDao.findByTransactionId(transaction_id);
		String totalFee = null;
		if(wpr == null){//如果是要用户输入密码，数据库没有记录transaction_id
			HttpResponse h = orderquery(null, transaction_id, hotelCode);
			Map<String, Object> map = (Map<String, Object>) h.getData();
			String trade_state = MapUtils.getString(map, "trade_state");
			if("SUCCESS".equals(trade_state)){
				totalFee = MapUtils.getString(map, "total_fee");
			}else {
				return hr.error("该订单用户并未成功支付，无法退款");
			}
		}else {
			totalFee = wpr.getTotalFee();
		}
		Map<String, Object> map = new HashMap<>();
		//生成的随机字符串
		String nonce_str = StringUtils.getRandomStringByLength(32);

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmsssss");
		String out_refund_no = sdf.format(date);

        WechatMerchants wm = wechatMerchantsDao.findByHotelCode(hotelCode);
        if(wm == null){
        	return hr.error("请检查微信商户信息是否录入");
		}
		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appid", wm.getAppid());
		packageParams.put("mch_id", wm.getMchId());
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("transaction_id", transaction_id);//微信订单号
//		packageParams.put("out_trade_no", wpr.getOut_trade_no());//商户订单号(与微信订单号 二选一)
		packageParams.put("out_refund_no", out_refund_no);//商户退款单号
		packageParams.put("total_fee", totalFee);//支付金额，这边需要转成字符串类型，否则后面的签名会失败
		if(refund_fee != null || !"".equals(refund_fee)){
			packageParams.put("refund_fee", refund_fee);//退款金额，可以部分退款
		}else {
			packageParams.put("refund_fee", totalFee);//退款金额，全部退款
		}

		// 除去数组中的空值和签名参数
		packageParams = PayUtil.paraFilter(packageParams);
		String prestr = PayUtil.createLinkString(packageParams); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		//MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
		String mysign = PayUtil.sign(prestr, wm.getSecretKey(), "utf-8").toUpperCase();
		logger.info("=======================第一次签名1：" + mysign + "=====================");
        packageParams.put("sign", mysign);
        String xml = XMLBeanUtil.map2XmlString(packageParams);
		//调用统一下单接口，并接受返回的结果
//		String result = PayUtil.httpRequest(WechatPay.refund_url, "POST", xml);
		String path = wm.getCerPath();
		String result = doRefund(wm.getMchId(), WechatPay.refund_url, xml, path, hotelCode);
		System.out.println("调试模式_统一下单接口 返回XML数据1：" + result);
		// 将解析结果存储在HashMap中
		map = PayUtil.doXMLParse(result);
		String resultCode = MapUtils.getString(map, "result_code");
		if("SUCCESS".equals(resultCode)){
			//支付成功
		}else {
			//支付失败
		}
		hr.addData(map);
		return hr;
	}
	//申请退款（接口调用）
	@Override
	public String doRefund(String mchId, String url, String data, String path, String hotelCode) throws Exception {
//		path = "E:\\certificate\\apiclient_cert.p12";
		/**
		 * 注意PKCS12证书 是从微信商户平台-》账户设置-》 API安全 中下载的
		 */
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		//这里自行实现我是使用数据库配置将证书上传到了服务器可以使用 FileInputStream读取本地文件
		FileInputStream inputStream  = new FileInputStream(new File(path));
//        URL url1 = new URL("https://···");
//        InputStream fin = url1.openStream();
		try {
			//这里写密码..默认是你的MCHID
			keyStore.load(inputStream, mchId.toCharArray());
		} finally {
			inputStream.close();
		}
		SSLContext sslcontext = custom()
				//这里也是写密码的
				.loadKeyMaterial(keyStore, mchId.toCharArray())
				.build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslcontext,
				SSLConnectionSocketFactory.getDefaultHostnameVerifier());
		CloseableHttpClient httpclient = HttpClients.custom()
				.setSSLSocketFactory(sslsf)
				.build();
		try {
			HttpPost httpost = new HttpPost(url);
			httpost.setEntity(new StringEntity(data, "UTF-8"));
			CloseableHttpResponse response = httpclient.execute(httpost);
			try {
				HttpEntity entity = response.getEntity();
				//接受到返回信息
				String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
				EntityUtils.consume(entity);
				Map<String, Object> map = PayUtil.doXMLParse(jsonStr);
				Map<String, Object> mapw = ChangeCharUtil.humpToLineMap(map);
				WechatRefundRecord wrr = BeanToMapUtils.toBean(WechatRefundRecord.class, mapw);
				wechatRefundRecordDao.saveAndFlush(wrr);
				return jsonStr;
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	}

	//查询订单
	@Override
	public HttpResponse orderquery(String out_trade_no, String transaction_id, String hotelCode) throws Exception {
		HttpResponse hr = new HttpResponse();
		Map<String, Object> map = new HashMap<>();
		//生成的随机字符串
		String nonce_str = StringUtils.getRandomStringByLength(32);

        WechatMerchants wm = wechatMerchantsDao.findByHotelCode(hotelCode);
        if(wm == null){
        	return hr.error("请检查微信商户信息是否录入");
		}
		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appid", wm.getAppid());
		packageParams.put("mch_id", wm.getMchId());
		packageParams.put("nonce_str", nonce_str);
		if(transaction_id != null && !"".equals(transaction_id)){
			packageParams.put("transaction_id", transaction_id);//微信订单号
		}else if(out_trade_no != null && !"".equals(out_trade_no)){
			packageParams.put("out_trade_no", out_trade_no);//微信订单号
		}
		// 除去数组中的空值和签名参数
		packageParams = PayUtil.paraFilter(packageParams);
		String prestr = PayUtil.createLinkString(packageParams); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		//MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
		String mysign = PayUtil.sign(prestr, wm.getSecretKey(), "utf-8").toUpperCase();
		logger.info("=======================第一次签名1：" + mysign + "=====================");
		packageParams.put("sign", mysign);
		//拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
		String xml = XMLBeanUtil.map2XmlString(packageParams);
		System.out.println("调试模式_统一下单接口 请求XML数据：" + xml);
		//调用统一下单接口，并接受返回的结果
		String result = PayUtil.httpRequest(WechatPay.orderquery_url, "POST", xml);
		System.out.println("调试模式_统一下单接口 返回XML数据1：" + result);
		// 将解析结果存储在HashMap中
		map = PayUtil.doXMLParse(result);
		hr.addData(map);
		return hr;
	}

	//撤销订单
	@Override
	public HttpResponse reverse(String transaction_id, String hotelCode) throws Exception {
		HttpResponse hr = new HttpResponse();
		Map<String, Object> map = new HashMap<>();
		//生成的随机字符串
		String nonce_str = StringUtils.getRandomStringByLength(32);

        WechatMerchants wm = wechatMerchantsDao.findByHotelCode(hotelCode);
		if(wm == null){
			return hr.error("请检查微信商户信息是否录入");
		}
		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appid", wm.getAppid());
		packageParams.put("mch_id", wm.getMchId());
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("transaction_id", transaction_id);//微信订单号
		// 除去数组中的空值和签名参数
		packageParams = PayUtil.paraFilter(packageParams);
		String prestr = PayUtil.createLinkString(packageParams); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		//MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
		String mysign = PayUtil.sign(prestr, wm.getSecretKey(), "utf-8").toUpperCase();
		logger.info("=======================第一次签名1：" + mysign + "=====================");
		packageParams.put("sign", mysign);
		//拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
		String xml = XMLBeanUtil.map2XmlString(packageParams);
		System.out.println("调试模式_统一下单接口 请求XML数据：" + xml);
		//调用统一下单接口，并接受返回的结果
		String result = PayUtil.httpRequest(WechatPay.reverse_url, "POST", xml);
		System.out.println("调试模式_统一下单接口 返回XML数据1：" + result);
		// 将解析结果存储在HashMap中
		map = PayUtil.doXMLParse(result);
		hr.addData(map);
		return hr;
	}
	//退款查询
	@Override
	public HttpResponse refundquery(String refund_id, String hotelCode) throws Exception {
		HttpResponse hr = new HttpResponse();
		Map<String, Object> map = new HashMap<>();
		//生成的随机字符串
		String nonce_str = StringUtils.getRandomStringByLength(32);

        WechatMerchants wm = wechatMerchantsDao.findByHotelCode(hotelCode);
		if(wm == null){
			return hr.error("请检查微信商户信息是否录入");
		}
		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appid", wm.getAppid());
		packageParams.put("mch_id", wm.getMchId());
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("refund_id", refund_id);//微信订单号
		// 除去数组中的空值和签名参数
		packageParams = PayUtil.paraFilter(packageParams);
		String prestr = PayUtil.createLinkString(packageParams); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		//MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
		String mysign = PayUtil.sign(prestr, wm.getSecretKey(), "utf-8").toUpperCase();
		logger.info("=======================第一次签名1：" + mysign + "=====================");
		packageParams.put("sign", mysign);
		//拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
		String xml = XMLBeanUtil.map2XmlString(packageParams);
		System.out.println("调试模式_统一下单接口 请求XML数据：" + xml);
		//调用统一下单接口，并接受返回的结果
		String result = PayUtil.httpRequest(WechatPay.refundquery_url, "POST", xml);
		System.out.println("调试模式_统一下单接口 返回XML数据1：" + result);
		// 将解析结果存储在HashMap中
		map = PayUtil.doXMLParse(result);
		hr.addData(map);
		return hr;
	}

}
