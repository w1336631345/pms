package com.kry.pms.service.busi.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.SettleAccountRecordDao;
import com.kry.pms.model.http.request.busi.BillCheckBo;
import com.kry.pms.model.persistence.busi.SettleAccountRecord;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.service.busi.SettleAccountRecordService;
import com.kry.pms.service.sys.BusinessSeqService;

@Service
public class  SettleAccountRecordServiceImpl implements  SettleAccountRecordService{
	@Autowired
	 SettleAccountRecordDao settleAccountRecordDao;
	@Autowired
	BusinessSeqService businessSeqService;
	 
	 @Override
	public SettleAccountRecord add(SettleAccountRecord settleAccountRecord) {
		return settleAccountRecordDao.saveAndFlush(settleAccountRecord);
	}

	@Override
	public void delete(String id) {
		SettleAccountRecord settleAccountRecord = settleAccountRecordDao.findById(id).get();
		if (settleAccountRecord != null) {
			settleAccountRecord.setDeleted(Constants.DELETED_TRUE);
		}
		modify(settleAccountRecord);
	}

	@Override
	public SettleAccountRecord modify(SettleAccountRecord settleAccountRecord) {
		return settleAccountRecordDao.saveAndFlush(settleAccountRecord);
	}

	@Override
	public SettleAccountRecord findById(String id) {
		return settleAccountRecordDao.getOne(id);
	}

	@Override
	public List<SettleAccountRecord> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return settleAccountRecordDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<SettleAccountRecord> listPage(PageRequest<SettleAccountRecord> prq) {
		Example<SettleAccountRecord> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(settleAccountRecordDao.findAll(ex, req));
	}

	@Override
	public SettleAccountRecord create(BillCheckBo billCheckBo, Account account) {
		SettleAccountRecord scr = new SettleAccountRecord();
		scr.setRecordNum(businessSeqService.fetchNextSeqNum(billCheckBo.getHotelCode(),Constants.Key.BUSINESS_BUSINESS_SETTLE_SEQ_KEY));
		scr.setAccount(account);
		scr.setOperationEmployee(billCheckBo.getOperationEmployee());
		scr.setSettleTime(LocalDateTime.now());
		return add(scr);
	}
	 
	 
	 
	 
}
