package com.kry.pms.service.busi.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.DtoResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.CreditGrantingRecordDao;
import com.kry.pms.model.persistence.busi.CreditGrantingRecord;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.service.busi.CreditGrantingRecordService;
import com.kry.pms.service.sys.AccountService;
import com.kry.pms.util.BigDecimalUtil;

@Service
public class CreditGrantingRecordServiceImpl implements CreditGrantingRecordService {
	@Autowired
	CreditGrantingRecordDao creditGrantingRecordDao;
	@Autowired
	AccountService accountService;

	private static final String TYPE_MEMBER = "M";
	private static final String TYPE_AR = "AR";
	private static final String TYPE_CARD = "CC";

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
		return null;// 默认不实现
		// return creditGrantingRecordDao.findByHotelCode(code);
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

	@Override
	public List<CreditGrantingRecord> queryByGrantingAccountId(String id) {
		return creditGrantingRecordDao.findByGrantingAccountId(id);
	}

	@Override
	public List<CreditGrantingRecord> queryByAccountId(String id) {
		return creditGrantingRecordDao.findByAccountId(id);
	}

	private DtoResponse<CreditGrantingRecord> addArOrMemberCreditGrantingRecord(
			CreditGrantingRecord creditGrantingRecord) {
		DtoResponse<CreditGrantingRecord> rep = new DtoResponse<CreditGrantingRecord>();
		creditGrantingRecord.getGrantingAccount();
		if (creditGrantingRecord.getGrantingAccount() != null
				&& creditGrantingRecord.getGrantingAccount().getId() != null) {
			Account gAccount = accountService.findById(creditGrantingRecord.getGrantingAccount().getId());
			Account account = accountService.findById(creditGrantingRecord.getAccount().getId());
			if (gAccount.getAvailableCreditLimit() != null
					&& gAccount.getAvailableCreditLimit() > creditGrantingRecord.getGrantingLimit()) {
				gAccount.setAvailableCreditLimit(BigDecimalUtil.sub(gAccount.getAvailableCreditLimit(),
						creditGrantingRecord.getGrantingLimit()));
				if(account.getAvailableCreditLimit()==null) {
					account.setAvailableCreditLimit(creditGrantingRecord.getGrantingLimit());
				}else {
					account.setAvailableCreditLimit(BigDecimalUtil.add(account.getAvailableCreditLimit(),
									creditGrantingRecord.getGrantingLimit()));
				}
				accountService.modify(account);
				accountService.modify(gAccount);
				rep.addData(add(creditGrantingRecord));
			} else {
				rep.setStatus(Constants.BusinessCode.CODE_ILLEGAL_OPERATION);
				rep.setMessage("账户额度不足");
			}
		} else {
			rep.setStatus(Constants.BusinessCode.CODE_ILLEGAL_OPERATION);
			rep.setMessage("账户信息错误");
		}
		return rep;
	}

	private DtoResponse<CreditGrantingRecord> addCreditCardGrantingRecord(CreditGrantingRecord creditGrantingRecord) {
		DtoResponse<CreditGrantingRecord> rep = new DtoResponse<CreditGrantingRecord>();
		Account account = accountService.findById(creditGrantingRecord.getAccount().getId());
		account.setAvailableCreditLimit(
				BigDecimalUtil.add(account.getAvailableCreditLimit() == null ? 0 : account.getAvailableCreditLimit(),
						creditGrantingRecord.getGrantingLimit()));
		accountService.modify(account);
		rep.addData(add(creditGrantingRecord));
		return rep;
	}

	@Override
	public DtoResponse<CreditGrantingRecord> createRecord(CreditGrantingRecord creditGrantingRecord) {
		switch (creditGrantingRecord.getType()) {
		case TYPE_AR:
			return addArOrMemberCreditGrantingRecord(creditGrantingRecord);
		case TYPE_MEMBER:
			return addArOrMemberCreditGrantingRecord(creditGrantingRecord);
		case TYPE_CARD:
			return addCreditCardGrantingRecord(creditGrantingRecord);
		default:
			DtoResponse<CreditGrantingRecord> rep = new DtoResponse<CreditGrantingRecord>();
			rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
			rep.setMessage("不支持该类型授信");
			return rep;
		}
	}

}
