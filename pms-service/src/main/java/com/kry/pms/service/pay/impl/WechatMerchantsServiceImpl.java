package com.kry.pms.service.pay.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.pay.WechatMerchantsDao;
import com.kry.pms.dao.pay.WechatRefundRecordDao;
import com.kry.pms.model.persistence.pay.WechatMerchants;
import com.kry.pms.model.persistence.pay.WechatRefundRecord;
import com.kry.pms.service.pay.WechatMerchantsService;
import com.kry.pms.service.pay.WechatRefundRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WechatMerchantsServiceImpl implements WechatMerchantsService {
	@Autowired
	WechatMerchantsDao wechatMerchantsDao;
	 
	 @Override
	public WechatMerchants add(WechatMerchants wechatMerchants) {
		return wechatMerchantsDao.saveAndFlush(wechatMerchants);
	}

	@Override
	public void delete(String id) {
		WechatMerchants wechatMerchants = wechatMerchantsDao.findById(id).get();
		if (wechatMerchants != null) {
			wechatMerchants.setDeleted(Constants.DELETED_TRUE);
		}
		wechatMerchantsDao.saveAndFlush(wechatMerchants);
	}

	@Override
	public void deleteTrue(String id) {
		wechatMerchantsDao.deleteById(id);
	}

	@Override
	public WechatMerchants modify(WechatMerchants wechatMerchants) {
		return wechatMerchantsDao.saveAndFlush(wechatMerchants);
	}

	@Override
	public WechatMerchants findById(String id) {
		return wechatMerchantsDao.getOne(id);
	}

	@Override
	public List<WechatMerchants> getAllByHotelCode(String code) {
//		return null;//默认不实现
		return wechatMerchantsDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<WechatMerchants> listPage(PageRequest<WechatMerchants> prq) {
		Example<WechatMerchants> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(wechatMerchantsDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
