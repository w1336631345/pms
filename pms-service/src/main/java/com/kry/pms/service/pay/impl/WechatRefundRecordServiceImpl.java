package com.kry.pms.service.pay.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.pay.WechatPayRecordDao;
import com.kry.pms.dao.pay.WechatRefundRecordDao;
import com.kry.pms.model.persistence.pay.WechatPayRecord;
import com.kry.pms.model.persistence.pay.WechatRefundRecord;
import com.kry.pms.service.pay.WechatPayRecordService;
import com.kry.pms.service.pay.WechatRefundRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WechatRefundRecordServiceImpl implements WechatRefundRecordService {
	@Autowired
	WechatRefundRecordDao wechatRefundRecordDao;
	 
	 @Override
	public WechatRefundRecord add(WechatRefundRecord wechatRefundRecord) {
		return wechatRefundRecordDao.saveAndFlush(wechatRefundRecord);
	}

	@Override
	public void delete(String id) {
		WechatRefundRecord wechatRefundRecord = wechatRefundRecordDao.findById(id).get();
		if (wechatRefundRecord != null) {
			wechatRefundRecord.setDeleted(Constants.DELETED_TRUE);
		}
		wechatRefundRecordDao.saveAndFlush(wechatRefundRecord);
	}

	@Override
	public void deleteTrue(String id) {
		wechatRefundRecordDao.deleteById(id);
	}

	@Override
	public WechatRefundRecord modify(WechatRefundRecord wechatRefundRecord) {
		return wechatRefundRecordDao.saveAndFlush(wechatRefundRecord);
	}

	@Override
	public WechatRefundRecord findById(String id) {
		return wechatRefundRecordDao.getOne(id);
	}

	@Override
	public List<WechatRefundRecord> getAllByHotelCode(String code) {
//		return null;//默认不实现
		return wechatRefundRecordDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<WechatRefundRecord> listPage(PageRequest<WechatRefundRecord> prq) {
		Example<WechatRefundRecord> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(wechatRefundRecordDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
