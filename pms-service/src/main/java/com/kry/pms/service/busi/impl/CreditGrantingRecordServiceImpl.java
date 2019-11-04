package com.kry.pms.service.busi.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.CreditGrantingRecordDao;
import com.kry.pms.model.persistence.busi.CreditGrantingRecord;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.service.busi.CreditGrantingRecordService;

@Service
public class  CreditGrantingRecordServiceImpl implements  CreditGrantingRecordService{
	@Autowired
	 CreditGrantingRecordDao creditGrantingRecordDao;
	 
	 @Override
	public CreditGrantingRecord add(CreditGrantingRecord creditGrantingRecord) {
		return creditGrantingRecordDao.saveAndFlush(creditGrantingRecord);
	}

	@Override
	public void delete(String id) {
		CreditGrantingRecord creditGrantingRecord = creditGrantingRecordDao.findById(id).get();
		if (creditGrantingRecord != null) {
			creditGrantingRecord.setDeleted(Constants.DELETED_TRUE);
		}
		modify(creditGrantingRecord);
	}

	@Override
	public CreditGrantingRecord modify(CreditGrantingRecord creditGrantingRecord) {
		return creditGrantingRecordDao.saveAndFlush(creditGrantingRecord);
	}

	@Override
	public CreditGrantingRecord findById(String id) {
		return creditGrantingRecordDao.getOne(id);
	}

	@Override
	public List<CreditGrantingRecord> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return creditGrantingRecordDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<CreditGrantingRecord> listPage(PageRequest<CreditGrantingRecord> prq) {
		Example<CreditGrantingRecord> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(creditGrantingRecordDao.findAll(ex, req));
	}
	 
}
