package com.kry.pms.service.msg.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.msg.MsgAccountDao;
import com.kry.pms.model.persistence.msg.MsgAccount;
import com.kry.pms.service.msg.MsgAccountService;
import com.kry.pms.util.HttpClientUtil;
import com.kry.pms.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MsgAccountServiceImpl implements MsgAccountService {
	@Autowired
	MsgAccountDao msgAccountDao;
	 
	@Override
	public MsgAccount add(MsgAccount msgAccount) {
	 	String hotleCode = msgAccount.getHotelCode();
		return msgAccountDao.saveAndFlush(msgAccount);
	}
	@Override
	public HttpResponse addTo(MsgAccount msgAccount) {
		HttpResponse hr = new HttpResponse();
		String hotelCode = msgAccount.getHotelCode();
		if(hotelCode == null){
			return hr.error("酒店编码不能为空");
		}else {
			List<MsgAccount> list = msgAccountDao.findByHotelCode(hotelCode);
			if(list != null && !list.isEmpty()){
				return hr.error("该酒店账号已经设置，不能重复添加");
			}else{
				msgAccountDao.saveAndFlush(msgAccount);
			}
		}
		return hr.ok();
	}

	@Override
	public void delete(String id) {
		MsgAccount msgAccount = msgAccountDao.findById(id).get();
		if (msgAccount != null) {
			msgAccount.setDeleted(Constants.DELETED_TRUE);
		}
		msgAccountDao.saveAndFlush(msgAccount);
	}

	@Override
	public MsgAccount modify(MsgAccount department) {
		return msgAccountDao.saveAndFlush(department);
	}

	@Override
	public MsgAccount findById(String id) {
		return msgAccountDao.getOne(id);
	}

	@Override
	public List<MsgAccount> getAllByHotelCode(String code) {
		return null;//默认不实现
//		return msgAccountDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<MsgAccount> listPage(PageRequest<MsgAccount> prq) {
		Example<MsgAccount> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(msgAccountDao.findAll(ex, req));
	}

	@Override
	public HttpResponse getBalance(String hotelCode){
		HttpResponse hr = new HttpResponse();
		List<MsgAccount> ma = msgAccountDao.findByHotelCode(hotelCode);
		MsgAccount msgAccount = null;
		if(ma == null || ma.isEmpty()){
			return hr.error("酒店未配置短信账号");
		}else {
			msgAccount = ma.get(0);
		}
		String username = msgAccount.getOrganId()+":"+msgAccount.getUsername();
		String password = msgAccount.getPassword();
		String params = "?username="+username+"&password="+password;
		String result = HttpClientUtil.doGet("http://gateway.iems.net.cn/GeneralSMS/GetAccountBalance"+params);
		hr.setData(result);
		return hr;
	}
	 
	 
}
