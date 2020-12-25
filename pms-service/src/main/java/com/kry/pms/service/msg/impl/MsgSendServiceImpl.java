package com.kry.pms.service.msg.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.MemberInfoDao;
import com.kry.pms.dao.msg.MsgAccountDao;
import com.kry.pms.dao.msg.MsgRecordsDao;
import com.kry.pms.dao.msg.MsgTemplateDao;
import com.kry.pms.model.persistence.guest.MemberInfo;
import com.kry.pms.model.persistence.msg.MsgAccount;
import com.kry.pms.model.persistence.msg.MsgRecords;
import com.kry.pms.model.persistence.msg.MsgTemplate;
import com.kry.pms.service.msg.MsgSendService;
import com.kry.pms.service.msg.MsgTemplateService;
import com.kry.pms.util.HttpClientUtil;
import com.kry.pms.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MsgSendServiceImpl implements MsgSendService {

	@Autowired
	RedisTemplate<String,String> redisTemplate;
	@Autowired
	MsgAccountDao msgAccountDao;
	@Autowired
	MemberInfoDao memberInfoDao;
	@Autowired
	MsgRecordsDao msgRecordsDao;

	@Override
	public HttpResponse sendMsg(String id, String name, String phone, String content, String sendTime, String hotelCode){
		HttpResponse hr = new HttpResponse();
		List<MsgAccount> ma = msgAccountDao.findByHotelCode(hotelCode);
		MsgAccount msgAccount = null;
		if(ma == null || ma.isEmpty()){
			return hr.error("酒店未配置短信账号");
		}else {
			msgAccount = ma.get(0);
		}
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime time = LocalDateTime.now();
		try {
			String gbk = StringUtil.toGBK(content);
			String username = msgAccount.getOrganId()+":"+msgAccount.getUsername();
			String password = msgAccount.getPassword();
			String params = null;
			if(sendTime != null && !"".equals(sendTime)){
				time = LocalDateTime.parse(sendTime, df);
				String encodeTime = sendTime.replaceAll(" ", "+");
				params = "?username="+username+"&password="+password+"&to="+phone+"&content="+gbk+"&presendTime="+encodeTime;
			}else {
				params = "?username="+username+"&password="+password+"&to="+phone+"&content="+gbk;
			}
			String result = HttpClientUtil.doGet("http://gateway.iems.net.cn/GsmsHttp"+params);
			String ok = result.substring(0,2);
			String status = null;
			if("OK".equals(ok)){//发送短信成功
				hr.ok("短信发送成功");
				status = "SUCCESS";
			}else {
				status = "FAIL";
				hr.error("短信发送失败"+result);
			}
			String[] ids = id.split(",");
			for(int i=0; i<ids.length; i++){
				MemberInfo mi = memberInfoDao.getOne(ids[i]);
				MsgRecords msgRecords = new MsgRecords();
				msgRecords.setCustName(mi.getName());
				msgRecords.setMobile(mi.getCustomer().getMobile());
				msgRecords.setContent(content);
				msgRecords.setSendTime(time);
				msgRecords.setTotal(1);
				msgRecords.setResult(result);
				msgRecords.setStatus(status);
				msgRecordsDao.saveAndFlush(msgRecords);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return hr;
	}

	@Override
	public HttpResponse getVCode(String phone){
		HttpResponse hr = new HttpResponse();
		String vcode = StringUtil.getYzm();
		String content = "客如意验证码"+vcode;
		try {
			String gbk = StringUtil.toGBK(content);
			String result = HttpClientUtil.doGet("http://gateway.iems.net.cn/GsmsHttp?username=72343:admin&password=84119807&to="+phone+"&content="+gbk);
			String ok = result.substring(0,2);
			if("OK".equals(ok)){//发送短信成功
				redisTemplate.opsForValue().set(phone, vcode, 90*1000, TimeUnit.MILLISECONDS);
//                HttpSession session = request.getSession();
//                session.setAttribute(phone, vcode);
//                Timer timer=new Timer();
//                //一分钟后删除验证码
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        session.removeAttribute(phone);
//                        System.out.println("验证码删除成功");
//                        timer.cancel();
//                    }
//                },60*1000);
				hr.ok("验证码发送成功"+vcode);
			}else {
				hr.error("验证码发送失败"+result);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return hr;
	}

	@Override
	public HttpResponse verificationCode(String phone, String code){
		HttpResponse hr = new HttpResponse();
//        HttpSession session = request.getSession();
//        String vcode = (String) session.getAttribute(phone);
		String vcode = redisTemplate.opsForValue().get(phone).toString();
		if(code == null || "".equals(code)){
			hr.error("验证码不能为空");
		}else {
			if(code.equals(vcode)){
				hr.ok("验证通过");
			}else {
				hr.error("验证码错误");
			}
		}
		return hr;
	}
	 
}
