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

	private static final String appid = "wx09adf502d19bf421";	    //?????????????????????appid??????????????????
	private static final String secret = "5bc57353b29a4862327d2d2f19b064ab";	//????????????????????????????????????????????????
	private static final String grant_type = "authorization_code";

//	private static final String applets_appid = "wx09adf502d19bf421";	    //????????????appid
//	private static final String applets_secret = "3d1449aedc049746173103927f711e4b";	//?????????????????????

	@Override
	public Json wxPay(String openid, HttpServletRequest request){
		Json json = new Json();
		try{
			//????????????????????????
			String nonce_str = StringUtils.getRandomStringByLength(32);
			//????????????
			String body = "test";
			//???????????????ip??????
			String spbill_create_ip = IpUtils.getIpAddr(request);

			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmsssss");
			String orderNo = sdf.format(date);
			String money = "1";//????????????????????????????????????????????????????????????????????????????????????????????????

			Map<String, String> packageParams = new HashMap<String, String>();
			packageParams.put("appid", WechatPay.appid);
			packageParams.put("body", body);
			packageParams.put("mch_id", WechatPay.mch_id);
			packageParams.put("nonce_str", nonce_str);
			packageParams.put("notify_url", WechatPay.notify_url);
			packageParams.put("openid", openid);
			packageParams.put("out_trade_no", orderNo);//???????????????
			packageParams.put("spbill_create_ip", spbill_create_ip);
			packageParams.put("total_fee", money);//?????????????????????????????????????????????????????????????????????????????????
			packageParams.put("trade_type", WechatPay.TRADETYPE);

			SortedMap<String, String> packageParams2 = new TreeMap<String, String>();
			packageParams2.put("appid", WechatPay.appid);
			packageParams2.put("body", body);
			packageParams2.put("mch_id", WechatPay.mch_id);
			packageParams2.put("nonce_str", nonce_str);
			packageParams2.put("notify_url", WechatPay.notify_url);
			packageParams2.put("openid", openid);
			packageParams2.put("out_trade_no", orderNo);//???????????????
			packageParams2.put("spbill_create_ip", spbill_create_ip);
			packageParams2.put("total_fee", money);//?????????????????????????????????????????????????????????????????????????????????
			packageParams2.put("trade_type", WechatPay.TRADETYPE);

			// ???????????????????????????????????????
			packageParams = PayUtil.paraFilter(packageParams);
			String prestr = PayUtil.createLinkString(packageParams); // ???????????????????????????????????????=???????????????????????????&???????????????????????????
			//MD5??????????????????????????????????????????????????????????????????????????????
			String mysign = PayUtil.sign(prestr, WechatPay.key, "utf-8").toUpperCase();
			logger.info("=======================spbill_create_ip???" + spbill_create_ip + "=====================");
			logger.info("=======================???????????????1???" + mysign + "=====================");
			//?????????????????????????????????xml?????????????????????????????????????????????????????????
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

			System.out.println("????????????_?????????????????? ??????XML?????????" + xml);

			//???????????????????????????????????????????????????
			String result = PayUtil.httpRequest(WechatPay.pay_url, "POST", xml);

			System.out.println("????????????_?????????????????? ??????XML??????1???" + result);

			// ????????????????????????HashMap???
			Map map = PayUtil.doXMLParse(result);

			String return_code = (String) map.get("return_code");//???????????????

			//?????????????????????????????????
			Map<String, Object> response = new HashMap<String, Object>();
			if(return_code == "SUCCESS" || return_code.equals(return_code)){
				// ????????????
				String prepay_id = (String) map.get("prepay_id");//????????????????????????
				response.put("nonceStr", nonce_str);
				response.put("package", "prepay_id=" + prepay_id);
				response.put("signType", WechatPay.SIGNTYPE);
				Long timeStamp = System.currentTimeMillis() / 1000;
				response.put("timeStamp", timeStamp + "");//???????????????????????????????????????????????????????????????????????????wx.requestPayment????????????????????????

				String stringSignTemp = "appId=" + WechatPay.appid + "&nonceStr=" + nonce_str + "&package=prepay_id=" + prepay_id+ "&signType=" + WechatPay.SIGNTYPE + "&timeStamp=" + timeStamp;
				//???????????????????????????????????????????????????wx.requesetPayment??????
				String paySign = PayUtil.sign(stringSignTemp, WechatPay.key, "utf-8").toUpperCase();
				logger.info("=======================??????????????????" + paySign + "=====================");

				response.put("paySign", paySign);

				//??????????????????
				//??????????????????
			}

			response.put("appid", WechatPay.appid);

			json.setSuccess(true);
			json.setData(response);
		}catch(Exception e){
			e.printStackTrace();
			json.setSuccess(false);
			json.setMsg("????????????");
		}
		return json;
	}
	@Override
    public HttpResponse getOpenId(String code, HttpServletRequest request) throws WeixinException, IOException {
		HttpResponse hr = new HttpResponse();
        if (code == null || code.equals("")) {
            throw new WeixinException("invalid null, code is null.");
        }
        //????????????
        String param = "?grant_type=" + grant_type + "&appid=" + appid + "&secret=" + secret + "&js_code=" + code;
        //??????????????????
        HttpsClient http = new HttpsClient();
        //????????????access_token??????
        Response res = http.get("https://api.weixin.qq.com/sns/jscode2session" + param);
        //?????????????????????????????????????????????
        JSONObject jsonObj = res.asJSONObject();
        if (jsonObj != null) {
            Object errcode = jsonObj.get("errcode");
            if (errcode != null) {
                //??????????????????
                return hr.error(9999,jsonObj.get("errmsg").toString());
            }
            ObjectMapper mapper = new ObjectMapper();
            OAuthJsToken oauthJsToken = mapper.readValue(jsonObj.toJSONString(),OAuthJsToken.class);
			hr.setData(oauthJsToken);
        }
        return hr;
    }
	@Override
	public String getUnionId(String access_token, String openId) throws WeixinException {
		HttpResponse hr = new HttpResponse();
		if (access_token == null || access_token.equals("")) {
			System.out.println("+++++++++++++++++++");
			System.out.println("access_token ?????????????????????????????????????????????????????????????????????");
		}
		//????????????
		String param = "?access_token=" + access_token + "&openid=" + openId;
		//??????????????????
		HttpsClient http = new HttpsClient();
		//????????????access_token??????
		Response res = http.get("https://api.weixin.qq.com/sns/userinfo" + param);
		//?????????????????????????????????????????????
		JSONObject jsonObj = res.asJSONObject();
		if (jsonObj != null) {
			Object obj = jsonObj.get("unionid");
			return obj.toString();
		}else {
			return null;
		}
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
		//sb??????????????????xml
		String notityXml = sb.toString();
		String resXml = "";
		System.out.println("?????????????????????" + notityXml);

		Map map = PayUtil.doXMLParse(notityXml);

		String returnCode = (String) map.get("return_code");
		if("SUCCESS".equals(returnCode)){
			//????????????????????????
			if(PayUtil.verify(PayUtil.createLinkString(map), (String)map.get("sign"), WechatPay.key, "utf-8")){
				/**???????????????????????????????????????start**/


				/**???????????????????????????????????????end**/

				//???????????????????????????????????????
				resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
						+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
			}
		}else{
			resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
					+ "<return_msg><![CDATA[????????????]]></return_msg>" + "</xml> ";
		}
		System.out.println(resXml);
		System.out.println("??????????????????????????????");

		BufferedOutputStream out = new BufferedOutputStream(
				response.getOutputStream());
		out.write(resXml.getBytes());
		out.flush();
		out.close();
	}

	//???????????????(????????????)
	@Override
	public HttpResponse sweepPay(Integer total_fee, String body, String auth_code,
								 HttpServletRequest request, String hotelCode) throws Exception {
		HttpResponse hr = new HttpResponse();
		Map<String, Object> map = new HashMap<>();
		//????????????????????????
		String nonce_str = StringUtils.getRandomStringByLength(32);
		//????????????
//		String body = body;
		String attach = "??????????????????";
		//???????????????ip??????
		String spbill_create_ip = IpUtils.getIpAddr(request);

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmsssss");
		String orderNo = sdf.format(date);
		Integer money = total_fee;//????????????????????????????????????????????????????????????????????????????????????????????????
        WechatMerchants wm = wechatMerchantsDao.findByHotelCode(hotelCode);
		if(wm == null){
			return hr.error("???????????????????????????????????????");
		}
		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appid", wm.getAppid());
		packageParams.put("auth_code", auth_code);
		packageParams.put("body", body);
		packageParams.put("mch_id", wm.getMchId());
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("out_trade_no", orderNo);//???????????????
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("total_fee", money.toString());//?????????????????????????????????????????????????????????????????????????????????

		// ???????????????????????????????????????
		packageParams = PayUtil.paraFilter(packageParams);
		String prestr = PayUtil.createLinkString(packageParams); // ???????????????????????????????????????=???????????????????????????&???????????????????????????
		//MD5??????????????????????????????????????????????????????????????????????????????
		String mysign = PayUtil.sign(prestr, wm.getSecretKey(), "utf-8").toUpperCase();
		logger.info("=======================spbill_create_ip???" + spbill_create_ip + "=====================");
		logger.info("=======================???????????????1???" + mysign + "=====================");
        packageParams.put("sign", mysign);
		//?????????????????????????????????xml?????????????????????????????????????????????????????????
		String xml = XMLBeanUtil.map2XmlString(packageParams);
		System.out.println("????????????_?????????????????? ??????XML?????????" + xml);
		//???????????????????????????????????????????????????
		String result = PayUtil.httpRequest(WechatPay.pay_url, "POST", xml);
		System.out.println("????????????_?????????????????? ??????XML??????1???" + result);
		// ????????????????????????HashMap???
		map = PayUtil.doXMLParse(result);
		String resultCode = MapUtils.getString(map, "result_code");
		if("SUCCESS".equals(resultCode)){
			//????????????
		}else {
			//????????????,???????????????????????????????????????????????????transaction_id
			map.put("out_trade_no", orderNo);
		}
		Map<String, Object> mapw = ChangeCharUtil.humpToLineMap(map);
		WechatPayRecord wpr = BeanToMapUtils.toBean(WechatPayRecord.class, mapw);
		wpr.setTotalFee(total_fee.toString());
		wpr.setHotelCode(hotelCode);
		wpr.setBody(body);
		wechatPayRecordDao.saveAndFlush(wpr);//????????????
		hr.addData(map);
		return hr;
	}
	//???????????????(?????????)
	@Override
	public HttpResponse sweepPayService(Integer total_fee, String body, String auth_code,
										HttpServletRequest request, String hotelCode) throws Exception {
		HttpResponse hr = new HttpResponse();
		Map<String, Object> map = new HashMap<>();
		//????????????????????????
		String nonce_str = StringUtils.getRandomStringByLength(32);
		//????????????
//		String body = body;
		String attach = "??????????????????";
		//???????????????ip??????
		String spbill_create_ip = IpUtils.getIpAddr(request);

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmsssss");
		String orderNo = sdf.format(date);
		String rd = StringUtils.getRandom1(15);
		String out_trade_no = rd + orderNo;
		Integer money = total_fee;//????????????????????????????????????????????????????????????????????????????????????????????????
		WechatMerchants wm = wechatMerchantsDao.findByHotelCode(hotelCode);
		if(wm == null){
			return hr.error("???????????????????????????????????????");
		}
		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appid", wm.getAppid());
		packageParams.put("auth_code", auth_code);
		packageParams.put("mch_id", wm.getMchId());
		packageParams.put("sub_mch_id", wm.getSubMchId());
		packageParams.put("body", body);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("out_trade_no", out_trade_no);//???????????????
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("total_fee", money.toString());//?????????????????????????????????????????????????????????????????????????????????

		// ???????????????????????????????????????
		packageParams = PayUtil.paraFilter(packageParams);
		String prestr = PayUtil.createLinkString(packageParams); // ???????????????????????????????????????=???????????????????????????&???????????????????????????
		//MD5??????????????????????????????????????????????????????????????????????????????
		String mysign = PayUtil.sign(prestr, wm.getSecretKey(), "utf-8").toUpperCase();
		logger.info("=======================spbill_create_ip???" + spbill_create_ip + "=====================");
		logger.info("=======================???????????????1???" + mysign + "=====================");
		packageParams.put("sign", mysign);
		//?????????????????????????????????xml?????????????????????????????????????????????????????????
		String xml = XMLBeanUtil.map2XmlString(packageParams);
		System.out.println("????????????_?????????????????? ??????XML?????????" + xml);
		//???????????????????????????????????????????????????
		String result = PayUtil.httpRequest(WechatPay.pay_url, "POST", xml);
		System.out.println("????????????_?????????????????? ??????XML??????1???" + result);
		// ????????????????????????HashMap???
		map = PayUtil.doXMLParse(result);
		String resultCode = MapUtils.getString(map, "result_code");
		if("SUCCESS".equals(resultCode)){
			//????????????
		}else {
			//????????????,???????????????????????????????????????????????????transaction_id
			map.put("out_trade_no", out_trade_no);
		}
		Map<String, Object> mapw = ChangeCharUtil.humpToLineMap(map);
		WechatPayRecord wpr = BeanToMapUtils.toBean(WechatPayRecord.class, mapw);
		wpr.setTotalFee(total_fee.toString());
		wpr.setHotelCode(hotelCode);
		wpr.setBody(body);
		wechatPayRecordDao.saveAndFlush(wpr);//????????????
		hr.addData(map);
		return hr;
	}

	//?????????????????????????????????????????????
	@Override
	public HttpResponse refund(String refund_fee, String transaction_id, String hotelCode) throws Exception {
		HttpResponse hr = new HttpResponse();
//		WechatPayRecord wpr = new WechatPayRecord();
		WechatPayRecord wpr = wechatPayRecordDao.findByTransactionId(transaction_id);
		String totalFee = null;
		if(wpr == null){//??????????????????????????????????????????????????????transaction_id
			HttpResponse h = orderquery(null, transaction_id, hotelCode);
			Map<String, Object> map = (Map<String, Object>) h.getData();
			String trade_state = MapUtils.getString(map, "trade_state");
			if("SUCCESS".equals(trade_state)){
				totalFee = MapUtils.getString(map, "total_fee");
			}else {
				return hr.error("????????????????????????????????????????????????");
			}
		}else {
			totalFee = wpr.getTotalFee();
		}
		Map<String, Object> map = new HashMap<>();
		//????????????????????????
		String nonce_str = StringUtils.getRandomStringByLength(32);

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmsssss");
		String out_refund_no = sdf.format(date);

        WechatMerchants wm = wechatMerchantsDao.findByHotelCode(hotelCode);
        if(wm == null){
        	return hr.error("???????????????????????????????????????");
		}
		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appid", wm.getAppid());
		packageParams.put("mch_id", wm.getMchId());
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("transaction_id", transaction_id);//???????????????
//		packageParams.put("out_trade_no", wpr.getOut_trade_no());//???????????????(?????????????????? ?????????)
		packageParams.put("out_refund_no", out_refund_no);//??????????????????
		packageParams.put("total_fee", totalFee);//?????????????????????????????????????????????????????????????????????????????????
		if(refund_fee != null || !"".equals(refund_fee)){
			packageParams.put("refund_fee", refund_fee);//?????????????????????????????????
		}else {
			packageParams.put("refund_fee", totalFee);//???????????????????????????
		}

		// ???????????????????????????????????????
		packageParams = PayUtil.paraFilter(packageParams);
		String prestr = PayUtil.createLinkString(packageParams); // ???????????????????????????????????????=???????????????????????????&???????????????????????????
		//MD5??????????????????????????????????????????????????????????????????????????????
		String mysign = PayUtil.sign(prestr, wm.getSecretKey(), "utf-8").toUpperCase();
		logger.info("=======================???????????????1???" + mysign + "=====================");
        packageParams.put("sign", mysign);
        String xml = XMLBeanUtil.map2XmlString(packageParams);
		//???????????????????????????????????????????????????
//		String result = PayUtil.httpRequest(WechatPay.refund_url, "POST", xml);
		String path = wm.getCerPath();
		String result = doRefund(wm.getMchId(), WechatPay.refund_url, xml, path, hotelCode);
		System.out.println("????????????_?????????????????? ??????XML??????1???" + result);
		// ????????????????????????HashMap???
		map = PayUtil.doXMLParse(result);
		String resultCode = MapUtils.getString(map, "result_code");
		if("SUCCESS".equals(resultCode)){
			//????????????
		}else {
			//????????????
		}
		hr.addData(map);
		return hr;
	}
	//???????????????????????????
	@Override
	public HttpResponse refundService(String refund_fee, String transaction_id, String hotelCode) throws Exception {
		HttpResponse hr = new HttpResponse();
//		WechatPayRecord wpr = new WechatPayRecord();
		WechatPayRecord wpr = wechatPayRecordDao.findByTransactionId(transaction_id);
		String totalFee = null;
		if(wpr == null){//??????????????????????????????????????????????????????transaction_id
			HttpResponse h = orderquery(null, transaction_id, hotelCode);
			Map<String, Object> map = (Map<String, Object>) h.getData();
			String trade_state = MapUtils.getString(map, "trade_state");
			if("SUCCESS".equals(trade_state)){
				totalFee = MapUtils.getString(map, "total_fee");
			}else {
				return hr.error("????????????????????????????????????????????????");
			}
		}else {
			totalFee = wpr.getTotalFee();
		}
		Map<String, Object> map = new HashMap<>();
		//????????????????????????
		String nonce_str = StringUtils.getRandomStringByLength(32);

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmsssss");
		String orderNo = sdf.format(date);
		String rd = StringUtils.getRandom1(15);
		String out_refund_no = rd + orderNo;

		WechatMerchants wm = wechatMerchantsDao.findByHotelCode(hotelCode);
		if(wm == null){
			return hr.error("???????????????????????????????????????");
		}
		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appid", wm.getAppid());
		packageParams.put("mch_id", wm.getMchId());
		packageParams.put("sub_mch_id", wm.getSubMchId());
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("transaction_id", transaction_id);//???????????????
//		packageParams.put("out_trade_no", wpr.getOut_trade_no());//???????????????(?????????????????? ?????????)
		packageParams.put("out_refund_no", out_refund_no);//??????????????????
		packageParams.put("total_fee", totalFee);//?????????????????????????????????????????????????????????????????????????????????
		if(refund_fee != null || !"".equals(refund_fee)){
			packageParams.put("refund_fee", refund_fee);//?????????????????????????????????
		}else {
			packageParams.put("refund_fee", totalFee);//???????????????????????????
		}

		// ???????????????????????????????????????
		packageParams = PayUtil.paraFilter(packageParams);
		String prestr = PayUtil.createLinkString(packageParams); // ???????????????????????????????????????=???????????????????????????&???????????????????????????
		//MD5??????????????????????????????????????????????????????????????????????????????
		String mysign = PayUtil.sign(prestr, wm.getSecretKey(), "utf-8").toUpperCase();
		logger.info("=======================???????????????1???" + mysign + "=====================");
		packageParams.put("sign", mysign);
		String xml = XMLBeanUtil.map2XmlString(packageParams);
		//???????????????????????????????????????????????????
//		String result = PayUtil.httpRequest(WechatPay.refund_url, "POST", xml);
		String path = wm.getCerPath();
		String result = doRefund(wm.getMchId(), WechatPay.refund_url, xml, path, hotelCode);
		System.out.println("????????????_?????????????????? ??????XML??????1???" + result);
		// ????????????????????????HashMap???
		map = PayUtil.doXMLParse(result);
		String resultCode = MapUtils.getString(map, "result_code");
		if("SUCCESS".equals(resultCode)){
			//????????????
		}else {
			//????????????
		}
		hr.addData(map);
		return hr;
	}
	//??????????????????????????????
	@Override
	public String doRefund(String mchId, String url, String data, String path, String hotelCode) throws Exception {
//		path = "E:\\certificate\\rooibook.p12";
		/**
		 * ??????PKCS12?????? ????????????????????????-???????????????-??? API?????? ????????????
		 */
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		//??????????????????????????????????????????????????????????????????????????????????????? FileInputStream??????????????????
		FileInputStream inputStream  = new FileInputStream(new File(path));
//        URL url1 = new URL("https://??????");
//        InputStream fin = url1.openStream();
		try {
			//???????????????..???????????????MCHID
			keyStore.load(inputStream, mchId.toCharArray());
		} finally {
			inputStream.close();
		}
		SSLContext sslcontext = custom()
				//????????????????????????
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
				//?????????????????????
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

	//????????????(????????????)
	@Override
	public HttpResponse orderquery(String out_trade_no, String transaction_id, String hotelCode) throws Exception {
		HttpResponse hr = new HttpResponse();
		Map<String, Object> map = new HashMap<>();
		//????????????????????????
		String nonce_str = StringUtils.getRandomStringByLength(32);

        WechatMerchants wm = wechatMerchantsDao.findByHotelCode(hotelCode);
        if(wm == null){
        	return hr.error("???????????????????????????????????????");
		}
		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appid", wm.getAppid());
		packageParams.put("mch_id", wm.getMchId());
		packageParams.put("nonce_str", nonce_str);
		if(transaction_id != null && !"".equals(transaction_id)){
			packageParams.put("transaction_id", transaction_id);//???????????????
		}else if(out_trade_no != null && !"".equals(out_trade_no)){
			packageParams.put("out_trade_no", out_trade_no);//???????????????
		}
		// ???????????????????????????????????????
		packageParams = PayUtil.paraFilter(packageParams);
		String prestr = PayUtil.createLinkString(packageParams); // ???????????????????????????????????????=???????????????????????????&???????????????????????????
		//MD5??????????????????????????????????????????????????????????????????????????????
		String mysign = PayUtil.sign(prestr, wm.getSecretKey(), "utf-8").toUpperCase();
		logger.info("=======================???????????????1???" + mysign + "=====================");
		packageParams.put("sign", mysign);
		//?????????????????????????????????xml?????????????????????????????????????????????????????????
		String xml = XMLBeanUtil.map2XmlString(packageParams);
		System.out.println("????????????_?????????????????? ??????XML?????????" + xml);
		//???????????????????????????????????????????????????
		String result = PayUtil.httpRequest(WechatPay.orderquery_url, "POST", xml);
		System.out.println("????????????_?????????????????? ??????XML??????1???" + result);
		// ????????????????????????HashMap???
		map = PayUtil.doXMLParse(result);
		hr.addData(map);
		return hr;
	}
	//????????????(?????????)
	@Override
	public HttpResponse orderqueryService(String out_trade_no, String transaction_id, String hotelCode) throws Exception {
		HttpResponse hr = new HttpResponse();
		Map<String, Object> map = new HashMap<>();
		//????????????????????????
		String nonce_str = StringUtils.getRandomStringByLength(32);

		WechatMerchants wm = wechatMerchantsDao.findByHotelCode(hotelCode);
		if(wm == null){
			return hr.error("???????????????????????????????????????");
		}
		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appid", wm.getAppid());
		packageParams.put("mch_id", wm.getMchId());
		packageParams.put("sub_mch_id", wm.getSubMchId());
		packageParams.put("nonce_str", nonce_str);
		if(transaction_id != null && !"".equals(transaction_id)){
			packageParams.put("transaction_id", transaction_id);//???????????????
		}else if(out_trade_no != null && !"".equals(out_trade_no)){
			packageParams.put("out_trade_no", out_trade_no);//???????????????
		}
		// ???????????????????????????????????????
		packageParams = PayUtil.paraFilter(packageParams);
		String prestr = PayUtil.createLinkString(packageParams); // ???????????????????????????????????????=???????????????????????????&???????????????????????????
		//MD5??????????????????????????????????????????????????????????????????????????????
		String mysign = PayUtil.sign(prestr, wm.getSecretKey(), "utf-8").toUpperCase();
		logger.info("=======================???????????????1???" + mysign + "=====================");
		packageParams.put("sign", mysign);
		//?????????????????????????????????xml?????????????????????????????????????????????????????????
		String xml = XMLBeanUtil.map2XmlString(packageParams);
		System.out.println("????????????_?????????????????? ??????XML?????????" + xml);
		//???????????????????????????????????????????????????
		String result = PayUtil.httpRequest(WechatPay.orderquery_url, "POST", xml);
		System.out.println("????????????_?????????????????? ??????XML??????1???" + result);
		// ????????????????????????HashMap???
		map = PayUtil.doXMLParse(result);
		if(out_trade_no != null && !"".equals(out_trade_no)){
			String trade_state = MapUtils.getString(map, "trade_state");
			if("SUCCESS".equals(trade_state)){
				//????????????
				WechatPayRecord wpr = wechatPayRecordDao.findByOutTradeNoAndHotelCode(out_trade_no, hotelCode);
				wpr.setTradeState(trade_state);
				wpr.setTransactionId(MapUtils.getString(map, "transaction_id"));
				wpr.setOpenid(MapUtils.getString(map, "openid"));
				wpr.setResultCode("SUCCESS");
				wpr.setTradeStateDesc(MapUtils.getString(map, "trade_state_desc"));
				wechatPayRecordDao.saveAndFlush(wpr);//????????????
			}
		}
		hr.addData(map);
		return hr;
	}

	//????????????
	@Override
	public HttpResponse reverse(String transaction_id, String hotelCode) throws Exception {
		HttpResponse hr = new HttpResponse();
		Map<String, Object> map = new HashMap<>();
		//????????????????????????
		String nonce_str = StringUtils.getRandomStringByLength(32);

        WechatMerchants wm = wechatMerchantsDao.findByHotelCode(hotelCode);
		if(wm == null){
			return hr.error("???????????????????????????????????????");
		}
		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appid", wm.getAppid());
		packageParams.put("mch_id", wm.getMchId());
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("transaction_id", transaction_id);//???????????????
		// ???????????????????????????????????????
		packageParams = PayUtil.paraFilter(packageParams);
		String prestr = PayUtil.createLinkString(packageParams); // ???????????????????????????????????????=???????????????????????????&???????????????????????????
		//MD5??????????????????????????????????????????????????????????????????????????????
		String mysign = PayUtil.sign(prestr, wm.getSecretKey(), "utf-8").toUpperCase();
		logger.info("=======================???????????????1???" + mysign + "=====================");
		packageParams.put("sign", mysign);
		//?????????????????????????????????xml?????????????????????????????????????????????????????????
		String xml = XMLBeanUtil.map2XmlString(packageParams);
		System.out.println("????????????_?????????????????? ??????XML?????????" + xml);
		//???????????????????????????????????????????????????
		String result = PayUtil.httpRequest(WechatPay.reverse_url, "POST", xml);
		System.out.println("????????????_?????????????????? ??????XML??????1???" + result);
		// ????????????????????????HashMap???
		map = PayUtil.doXMLParse(result);
		hr.addData(map);
		return hr;
	}
	//????????????(????????????)
	@Override
	public HttpResponse refundquery(String refund_id, String hotelCode) throws Exception {
		HttpResponse hr = new HttpResponse();
		Map<String, Object> map = new HashMap<>();
		//????????????????????????
		String nonce_str = StringUtils.getRandomStringByLength(32);

        WechatMerchants wm = wechatMerchantsDao.findByHotelCode(hotelCode);
		if(wm == null){
			return hr.error("???????????????????????????????????????");
		}
		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appid", wm.getAppid());
		packageParams.put("mch_id", wm.getMchId());
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("refund_id", refund_id);//???????????????
		// ???????????????????????????????????????
		packageParams = PayUtil.paraFilter(packageParams);
		String prestr = PayUtil.createLinkString(packageParams); // ???????????????????????????????????????=???????????????????????????&???????????????????????????
		//MD5??????????????????????????????????????????????????????????????????????????????
		String mysign = PayUtil.sign(prestr, wm.getSecretKey(), "utf-8").toUpperCase();
		logger.info("=======================???????????????1???" + mysign + "=====================");
		packageParams.put("sign", mysign);
		//?????????????????????????????????xml?????????????????????????????????????????????????????????
		String xml = XMLBeanUtil.map2XmlString(packageParams);
		System.out.println("????????????_?????????????????? ??????XML?????????" + xml);
		//???????????????????????????????????????????????????
		String result = PayUtil.httpRequest(WechatPay.refundquery_url, "POST", xml);
		System.out.println("????????????_?????????????????? ??????XML??????1???" + result);
		// ????????????????????????HashMap???
		map = PayUtil.doXMLParse(result);
		hr.addData(map);
		return hr;
	}
	//????????????(?????????)
	@Override
	public HttpResponse refundqueryService(String refund_id, String hotelCode) throws Exception {
		HttpResponse hr = new HttpResponse();
		Map<String, Object> map = new HashMap<>();
		//????????????????????????
		String nonce_str = StringUtils.getRandomStringByLength(32);

		WechatMerchants wm = wechatMerchantsDao.findByHotelCode(hotelCode);
		if(wm == null){
			return hr.error("???????????????????????????????????????");
		}
		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appid", wm.getAppid());
		packageParams.put("mch_id", wm.getMchId());
		packageParams.put("sub_mch_id", wm.getSubMchId());
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("refund_id", refund_id);//???????????????
		// ???????????????????????????????????????
		packageParams = PayUtil.paraFilter(packageParams);
		String prestr = PayUtil.createLinkString(packageParams); // ???????????????????????????????????????=???????????????????????????&???????????????????????????
		//MD5??????????????????????????????????????????????????????????????????????????????
		String mysign = PayUtil.sign(prestr, wm.getSecretKey(), "utf-8").toUpperCase();
		logger.info("=======================???????????????1???" + mysign + "=====================");
		packageParams.put("sign", mysign);
		//?????????????????????????????????xml?????????????????????????????????????????????????????????
		String xml = XMLBeanUtil.map2XmlString(packageParams);
		System.out.println("????????????_?????????????????? ??????XML?????????" + xml);
		//???????????????????????????????????????????????????
		String result = PayUtil.httpRequest(WechatPay.refundquery_url, "POST", xml);
		System.out.println("????????????_?????????????????? ??????XML??????1???" + result);
		// ????????????????????????HashMap???
		map = PayUtil.doXMLParse(result);
		hr.addData(map);
		return hr;
	}

}
