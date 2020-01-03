package com.kry.pms.service.busi.impl;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.DtoResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.SettleAccountRecordDao;
import com.kry.pms.model.http.request.busi.BillCheckBo;
import com.kry.pms.model.persistence.busi.Bill;
import com.kry.pms.model.persistence.busi.SettleAccountRecord;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.service.busi.BillService;
import com.kry.pms.service.busi.SettleAccountRecordService;
import com.kry.pms.service.sys.BusinessSeqService;

@Service
public class SettleAccountRecordServiceImpl implements SettleAccountRecordService {
	@Autowired
	SettleAccountRecordDao settleAccountRecordDao;
	@Autowired
	BusinessSeqService businessSeqService;
	@Autowired
	BillService billService;

	@Override
	public SettleAccountRecord add(SettleAccountRecord settleAccountRecord) {
		if(settleAccountRecord.getStatus()==null) {
			settleAccountRecord.setStatus(Constants.Status.NORMAL);
		}
		if(settleAccountRecord.getHotelCode()==null) {
			settleAccountRecord.setHotelCode(settleAccountRecord.getAccount().getHotelCode());
		}
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
		return null;// 默认不实现
		// return settleAccountRecordDao.findByHotelCode(code);
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
		scr.setRecordNum(businessSeqService.fetchNextSeqNum(billCheckBo.getHotelCode(),
				Constants.Key.BUSINESS_BUSINESS_SETTLE_SEQ_KEY));
		scr.setShiftCode(billCheckBo.getShiftCode());
		scr.setAccount(account);
		scr.setHotelCode(account.getHotelCode());
		scr.setOperationEmployee(billCheckBo.getOperationEmployee());
		scr.setSettleTime(LocalDateTime.now());
		return add(scr);
	}

	@Transient
	@Override
	public DtoResponse<String> cancle(String id) {
		DtoResponse<String> rep = new DtoResponse<String>();
		SettleAccountRecord sar = findById(id);
		if (sar != null) {
			for (Bill bill : sar.getBills()) {
				bill.setStatus(Constants.Status.BILL_NEED_SETTLED);
				bill.setCurrentSettleAccountRecordNum(null);
				billService.modify(bill);
			}
			for (Bill fb : sar.getFlatBills()) {
				fb.setStatus(Constants.Status.BILL_INVALID);
				fb.setCurrentSettleAccountRecordNum(null);
				Bill rebill;
				try {
					rebill = (Bill) fb.clone();
					rebill.setId(null);
					rebill.setItems(null);
					rebill.setProduct(fb.getProduct());
					rebill.setAccount(fb.getAccount());
					rebill.setCurrentSettleAccountRecordNum(null);
					rebill.setStatus(Constants.Status.BILL_INVALID);
					if(fb.getCost()!=null) {						
						rebill.setCost(-fb.getCost());
					}
					if(fb.getPay()!=null) {						
						rebill.setPay(-fb.getPay());
					}
					if(fb.getTotal()!=null) {						
						rebill.setTotal(-fb.getTotal());
					}
					rebill.setOperationEmployee(fb.getOperationEmployee());
					billService.add(rebill);
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				billService.modify(fb);
			}
		}
		sar.setCancleTime(LocalDateTime.now());
		sar.setCancleEmployee(sar.getOperationEmployee());
		sar.setStatus(Constants.Status.SETTLE_ACCOUNT_CANCLE);
		modify(sar);
		return rep;
	}

}
