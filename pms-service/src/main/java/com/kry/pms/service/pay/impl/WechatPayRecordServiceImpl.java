package com.kry.pms.service.pay.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.org.CorporationDao;
import com.kry.pms.dao.pay.WechatPayRecordDao;
import com.kry.pms.model.persistence.org.Corporation;
import com.kry.pms.model.persistence.pay.WechatPayRecord;
import com.kry.pms.service.org.CorporationService;
import com.kry.pms.service.pay.WechatPayRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WechatPayRecordServiceImpl implements WechatPayRecordService {
	@Autowired
	WechatPayRecordDao wechatPayRecordDao;
	 
	 @Override
	public WechatPayRecord add(WechatPayRecord wechatPayRecord) {
		return wechatPayRecordDao.saveAndFlush(wechatPayRecord);
	}

	@Override
	public void delete(String id) {
		WechatPayRecord wechatPayRecord = wechatPayRecordDao.findById(id).get();
		if (wechatPayRecord != null) {
			wechatPayRecord.setDeleted(Constants.DELETED_TRUE);
		}
		wechatPayRecordDao.saveAndFlush(wechatPayRecord);
	}

	@Override
	public void deleteTrue(String id) {
		wechatPayRecordDao.deleteById(id);
	}

	@Override
	public HttpResponse resultUpdate(String out_trade_no, String trade_state, String trade_state_desc, String hotleCode) {
	 	HttpResponse hr = new HttpResponse();
		WechatPayRecord wpr = wechatPayRecordDao.findByOutTradeNoAndHotelCode(out_trade_no, hotleCode);
		if(wpr == null){
			return hr.error("订单不存在");
		}
		wpr.setTradeState(trade_state);
		wpr.setTradeStateDesc(trade_state_desc);
		hr.addData(wechatPayRecordDao.saveAndFlush(wpr));
		return hr;
	}

	@Override
	public WechatPayRecord modify(WechatPayRecord wechatPayRecord) {
		return wechatPayRecordDao.saveAndFlush(wechatPayRecord);
	}

	@Override
	public WechatPayRecord findById(String id) {
		return wechatPayRecordDao.getOne(id);
	}

	@Override
	public List<WechatPayRecord> getAllByHotelCode(String code) {
//		return null;//默认不实现
		return wechatPayRecordDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<WechatPayRecord> listPage(PageRequest<WechatPayRecord> prq) {
		Example<WechatPayRecord> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(wechatPayRecordDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
