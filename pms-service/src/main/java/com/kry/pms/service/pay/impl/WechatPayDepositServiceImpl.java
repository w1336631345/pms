package com.kry.pms.service.pay.impl;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.dao.pay.WechatMerchantsDao;
import com.kry.pms.dao.pay.WechatPayRecordDao;
import com.kry.pms.dao.pay.WechatRefundRecordDao;
import com.kry.pms.model.persistence.marketing.SalesMen;
import com.kry.pms.model.persistence.pay.WechatMerchants;
import com.kry.pms.model.persistence.pay.WechatPay;
import com.kry.pms.model.persistence.pay.WechatPayRecord;
import com.kry.pms.model.persistence.pay.WechatRefundRecord;
import com.kry.pms.pay.IpUtils;
import com.kry.pms.pay.StringUtils;
import com.kry.pms.pay.XMLBeanUtil;
import com.kry.pms.pay.weixin.PayUtil;
import com.kry.pms.service.pay.WechatPayDepositService;
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
import org.weixin4j.WeixinSupport;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.apache.http.conn.ssl.SSLContexts.custom;

@Service
public class WechatPayDepositServiceImpl extends WeixinSupport implements WechatPayDepositService {

	@Autowired
    WechatMerchantsDao wechatMerchantsDao;
	@Autowired
	WechatPayRecordDao wechatPayRecordDao;
	@Autowired
	WechatRefundRecordDao wechatRefundRecordDao;

	private Logger logger = LoggerFactory.getLogger(getClass());
	//支付押金
	@Override
	public HttpResponse micropay(String auth_code, Integer total_fee, String hotelCode, HttpServletRequest request) throws Exception {
		HttpResponse hr = new HttpResponse();
		WechatMerchants wm = wechatMerchantsDao.findByHotelCode(hotelCode);
		if(wm == null){
			return hr.error("请检查微信商户信息是否录入");
		}
		Map<String, Object> map = new HashMap<>();
		//生成的随机字符串
		String nonce_str = StringUtils.getRandomStringByLength(32);
		//商品描述
		String body = wm.getHotelName()+"-押金";
//		String body = "asdf";
		//获取本机的ip地址
		String spbill_create_ip = IpUtils.getIpAddr(request);
//		String spbill_create_ip = "8.8.8.8";

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmsssss");
		String orderNo = sdf.format(date);
		String rd = StringUtils.getRandom1(15);
		String out_trade_no = rd + orderNo;
		Integer money = total_fee;//支付金额，单位：分，这边需要转成字符串类型，否则后面的签名会失败

		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appid", wm.getAppid());
		packageParams.put("auth_code", auth_code);
		packageParams.put("body", body);
		packageParams.put("deposit", "Y");//是否押金支付，Y-是,N-普通付款码支付
		packageParams.put("fee_type", "CNY");
		packageParams.put("mch_id", wm.getMchId());
		packageParams.put("nonce_str", nonce_str);
//		packageParams.put("nonce_str", "p9vi20tjmwu7wkcp9eazgn84pnaihil8");
		packageParams.put("out_trade_no", out_trade_no);//商户订单号
//		packageParams.put("out_trade_no", "87029761740714020200708172700034");//商户订单号
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("sub_mch_id", wm.getSubMchId());
		packageParams.put("total_fee", money.toString());//支付金额，这边需要转成字符串类型，否则后面的签名会失败
		packageParams.put("sign_type", wm.getDepositSignType());

		// 除去数组中的空值和签名参数
		packageParams = PayUtil.paraFilter2(packageParams);
		String prestr = PayUtil.createLinkString(packageParams); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		//MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
		String mysign = PayUtil.signHMAC_SHA256(prestr, wm.getSecretKey());
		logger.info("=======================第一次签名1：" + mysign + "=====================");
		packageParams.put("sign", mysign);

		//拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
		String xml = XMLBeanUtil.map2XmlString(packageParams);
		//调用统一下单接口，并接受返回的结果
		String result = PayUtil.httpRequest(WechatPay.deposit_url, "POST", xml);
		System.out.println("调试模式_统一下单接口 返回XML数据1：" + result);
		// 将解析结果存储在HashMap中
		map = PayUtil.doXMLParse(result);
		String resultCode = MapUtils.getString(map, "result_code");
		if("SUCCESS".equals(resultCode)){
			//支付成功
		}else {
			//支付失败,有可能提示用户输入密码，就不会返回transaction_id
			map.put("out_trade_no", out_trade_no);
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
	//查询订单
	@Override
	public HttpResponse orderquery(String transaction_id, String out_trade_no, String hotelCode) throws Exception {
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
		packageParams.put("sub_mch_id", wm.getSubMchId());
		packageParams.put("nonce_str", nonce_str);
		if(transaction_id != null && !"".equals(transaction_id)){
			packageParams.put("transaction_id", transaction_id);//微信订单号
		}else if(out_trade_no != null && !"".equals(out_trade_no)){
			packageParams.put("out_trade_no", out_trade_no);//微信订单号
		}
		packageParams.put("sign_type", wm.getDepositSignType());
		// 除去数组中的空值和签名参数
		packageParams = PayUtil.paraFilter2(packageParams);
		String prestr = PayUtil.createLinkString(packageParams); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		//HMAC_SHA256运算生成签名，这里是第一次签名，用于调用统一下单接口
		String mysign = PayUtil.signHMAC_SHA256(prestr, wm.getSecretKey());
		logger.info("=======================第一次签名1：" + mysign + "=====================");
		packageParams.put("sign", mysign);

		//拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
		String xml = XMLBeanUtil.map2XmlString(packageParams);
		//调用统一下单接口，并接受返回的结果
		String result = PayUtil.httpRequest(WechatPay.deposit_orderquery_url, "POST", xml);
		System.out.println("调试模式_统一下单接口 返回XML数据1：" + result);
		// 将解析结果存储在HashMap中
		map = PayUtil.doXMLParse(result);
		hr.addData(map);
		return hr;
	}
	//撤销订单（全额退押金）
	@Override
	public HttpResponse reverse(String transaction_id, String hotelCode) throws Exception {
		HttpResponse hr = new HttpResponse();
		WechatPayRecord wpr = wechatPayRecordDao.findByTransactionId(transaction_id);
		if(wpr == null){//如果是要用户输入密码，数据库没有记录transaction_id
			HttpResponse h = orderquery(transaction_id, null, hotelCode);
			Map<String, Object> map = (Map<String, Object>) h.getData();
			String trade_state = MapUtils.getString(map, "trade_state");
			if("SUCCESS".equals(trade_state)){
			}else {
				return hr.error("该订单用户并未成功支付，无法退款");
			}
		}else {
		}
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
		packageParams.put("sub_mch_id", wm.getSubMchId());
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("transaction_id", transaction_id);//微信订单号
		packageParams.put("sign_type", wm.getDepositSignType());

		// 除去数组中的空值和签名参数
		packageParams = PayUtil.paraFilter2(packageParams);
		String prestr = PayUtil.createLinkString(packageParams); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		//HMAC_SHA256运算生成签名，这里是第一次签名，用于调用统一下单接口
		String mysign = PayUtil.signHMAC_SHA256(prestr, wm.getSecretKey());
		logger.info("=======================第一次签名1：" + mysign + "=====================");
		packageParams.put("sign", mysign);

		String xml = XMLBeanUtil.map2XmlString(packageParams);
		//调用统一下单接口，并接受返回的结果
		String path = wm.getCerPath();
		String result = postSSL(wm.getMchId(), WechatPay.deposit_reverse_url, xml, path, hotelCode);
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
	//消费订单
	@Override
	public HttpResponse consume(String transaction_id, Integer consume_fee, String hotelCode) throws Exception {
		HttpResponse hr = new HttpResponse();
		WechatPayRecord wpr = wechatPayRecordDao.findByTransactionId(transaction_id);
		String totalFee = null;
		if(wpr == null){//如果是要用户输入密码，数据库没有记录transaction_id
			HttpResponse h = orderquery(transaction_id, null, hotelCode);
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

		WechatMerchants wm = wechatMerchantsDao.findByHotelCode(hotelCode);
		if(wm == null){
			return hr.error("请检查微信商户信息是否录入");
		}
		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appid", wm.getAppid());
		packageParams.put("mch_id", wm.getMchId());
		packageParams.put("sub_mch_id", wm.getSubMchId());
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("transaction_id", transaction_id);//微信订单号
//		packageParams.put("out_trade_no", wpr.getOut_trade_no());//商户订单号(与微信订单号 二选一)
		packageParams.put("total_fee", totalFee);//支付金额，这边需要转成字符串类型，否则后面的签名会失败
		packageParams.put("consume_fee", consume_fee.toString());//支付金额，这边需要转成字符串类型，否则后面的签名会失败
		packageParams.put("fee_type", "CNY");
		packageParams.put("sign_type", wm.getDepositSignType());

		// 除去数组中的空值和签名参数
		packageParams = PayUtil.paraFilter2(packageParams);
		String prestr = PayUtil.createLinkString(packageParams); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		//HMAC_SHA256运算生成签名，这里是第一次签名，用于调用统一下单接口
		String mysign = PayUtil.signHMAC_SHA256(prestr, wm.getSecretKey());
		logger.info("=======================第一次签名1：" + mysign + "=====================");
		packageParams.put("sign", mysign);

		String xml = XMLBeanUtil.map2XmlString(packageParams);
		//调用统一下单接口，并接受返回的结果
//		String result = PayUtil.httpRequest(WechatPay.refund_url, "POST", xml);
		String path = wm.getCerPath();
		String result = postSSL(wm.getMchId(), WechatPay.deposit_consume_url, xml, path, hotelCode);
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
	//申请退款（押金）
	@Override
	public HttpResponse refund( String refund_fee, String transaction_id, String hotelCode) throws Exception {
		HttpResponse hr = new HttpResponse();
//		WechatPayRecord wpr = new WechatPayRecord();
		WechatPayRecord wpr = wechatPayRecordDao.findByTransactionId(transaction_id);
		String totalFee = null;
		if(wpr == null){//如果是要用户输入密码，数据库没有记录transaction_id
			HttpResponse h = orderquery(transaction_id, null, hotelCode);
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
		String orderNo = sdf.format(date);
		String rd = StringUtils.getRandom1(15);
		String out_refund_no = rd + orderNo;

		WechatMerchants wm = wechatMerchantsDao.findByHotelCode(hotelCode);
		if(wm == null){
			return hr.error("请检查微信商户信息是否录入");
		}
		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appid", wm.getAppid());
		packageParams.put("mch_id", wm.getMchId());
		packageParams.put("sub_mch_id", wm.getSubMchId());
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
		packageParams.put("sign_type", wm.getDepositSignType());

		// 除去数组中的空值和签名参数
		packageParams = PayUtil.paraFilter2(packageParams);
		String prestr = PayUtil.createLinkString(packageParams); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		//HMAC_SHA256运算生成签名，这里是第一次签名，用于调用统一下单接口
		String mysign = PayUtil.signHMAC_SHA256(prestr, wm.getSecretKey());
		logger.info("=======================第一次签名1：" + mysign + "=====================");
		packageParams.put("sign", mysign);

		String xml = XMLBeanUtil.map2XmlString(packageParams);
		//调用统一下单接口，并接受返回的结果
		String path = wm.getCerPath();
		String result = postSSL(wm.getMchId(), WechatPay.deposit_refund_url, xml, path, hotelCode);
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
	//退款查询
	@Override
	public HttpResponse refundquery(String refund_id, String out_refund_no, String hotelCode) throws Exception {
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
		packageParams.put("sub_mch_id", wm.getSubMchId());
		packageParams.put("nonce_str", nonce_str);
		if(refund_id != null && !"".equals(refund_id)){
			packageParams.put("refund_id", refund_id);//微信订单号
		}else if(out_refund_no != null && !"".equals(out_refund_no)){
			packageParams.put("out_refund_no", out_refund_no);//微信订单号
		}
		packageParams.put("sign_type", wm.getDepositSignType());
		// 除去数组中的空值和签名参数
		packageParams = PayUtil.paraFilter2(packageParams);
		String prestr = PayUtil.createLinkString(packageParams); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		//HMAC_SHA256运算生成签名，这里是第一次签名，用于调用统一下单接口
		String mysign = PayUtil.signHMAC_SHA256(prestr, wm.getSecretKey());
		logger.info("=======================第一次签名1：" + mysign + "=====================");
		packageParams.put("sign", mysign);
		//拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
		String xml = XMLBeanUtil.map2XmlString(packageParams);
		//调用统一下单接口，并接受返回的结果
		String result = PayUtil.httpRequest(WechatPay.deposit_refundquery_url, "POST", xml);
		System.out.println("调试模式_统一下单接口 返回XML数据1：" + result);
		// 将解析结果存储在HashMap中
		map = PayUtil.doXMLParse(result);
		hr.addData(map);
		return hr;
	}

	@Override
	public String postSSL(String mchId, String url, String data, String path, String hotelCode) throws Exception {
//		path = "E:\\certificate\\rooibook.p12";
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
}
