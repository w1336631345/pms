package com.kry.pms.service.sys.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.kry.pms.base.Constants;
import com.kry.pms.base.DtoResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.sys.AccountDao;
import com.kry.pms.model.http.request.busi.BillCheckBo;
import com.kry.pms.model.http.response.busi.AccountSummaryVo;
import com.kry.pms.model.persistence.busi.Bill;
import com.kry.pms.model.persistence.busi.CreditGrantingRecord;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.model.persistence.busi.SettleAccountRecord;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.service.busi.BillService;
import com.kry.pms.service.busi.CreditGrantingRecordService;
import com.kry.pms.service.busi.SettleAccountRecordService;
import com.kry.pms.service.sys.AccountService;
import com.kry.pms.service.sys.BusinessSeqService;
import com.kry.pms.util.BigDecimalUtil;

@Service
public class AccountServiceImpl implements AccountService {
	@Autowired
	AccountDao accountDao;
	@Autowired
	BillService billService;
	@Autowired
	CreditGrantingRecordService creditGrantingRecordService;
	@Autowired
	SettleAccountRecordService settleAccountRecordService;
	@Autowired
	BusinessSeqService businessSeqService;

	@Override
	public Account add(Account account) {
		return accountDao.saveAndFlush(account);
	}

	@Override
	public void delete(String id) {
		Account account = accountDao.findById(id).get();
		if (account != null) {
			account.setDeleted(Constants.DELETED_TRUE);
		}
		accountDao.saveAndFlush(account);
	}

	@Override
	public Account modify(Account account) {
		return accountDao.saveAndFlush(account);
	}

	@Override
	public Account findById(String id) {
		return accountDao.getOne(id);
	}

	@Override
	public List<Account> getAllByHotelCode(String code) {
		return null;// 默认不实现
		// return accountDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<Account> listPage(PageRequest<Account> prq) {
		Example<Account> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(accountDao.findAll(ex, req));
	}

	@Override
	public Account billEntry(RoomRecord rr) {

		return null;
	}

	@Override
	public Account billEntry(Bill bill) {
		Account account = findById(bill.getAccount().getId());
		if (account != null) {
			if (bill.getCost() != null) {
				account.setCost(BigDecimalUtil.add(account.getCost(), bill.getCost()));
			}
			if (bill.getPay() != null) {
				account.setPay(BigDecimalUtil.add(account.getPay(), bill.getPay()));
			}
			account.setTotal(BigDecimalUtil.sub(account.getCost(), account.getPay()));
		}
		account.setCurrentBillSeq(account.getCurrentBillSeq() + 1);
		if(Constants.Status.BILL_NEED_SETTLED.equals(bill.getStatus())) {
			
		}
		return modify(account);
	}

	@Override
	public Collection<AccountSummaryVo> getAccountSummaryByOrderNum(String orderNum, String checkInType) {
		List<Account> accs = accountDao.findByOrderNum(orderNum, checkInType);
		Map<String, AccountSummaryVo> asvm = new HashMap<>();
		for (Account acc : accs) {
			AccountSummaryVo asv = null;
			if (!asvm.containsKey(acc.getRoomNum())) {
				asv = new AccountSummaryVo();
				asv.setType("temp");
				asv.setRoomNum(acc.getRoomNum());
				asv.setId(acc.getRoomNum());
				asv.setName(acc.getRoomNum());
				asv.setChildren(new ArrayList<AccountSummaryVo>());
				asvm.put(acc.getRoomNum(), asv);
			} else {
				asv = asvm.get(acc.getRoomNum());
			}
			asv.getChildren().add(new AccountSummaryVo(acc));
		}
		return asvm.values();
	}

	@Override
	public Collection<Account> getAccountByOrderNumAndStatusAndCheckInType(String orderNum, String checkInType,
			String status) {
		return accountDao.findAccountByOrderNumAndStatusAndCheckInType(orderNum, checkInType, status);
	}

	@Transactional
	@Override
	public DtoResponse<Account> checkCustomerBill(BillCheckBo billCheckBo) {
		DtoResponse<Account> rep = new DtoResponse<Account>();
		Account account = findById(billCheckBo.getAccountId());
		List<Bill> bills = null;
		if (account != null) {
			SettleAccountRecord settleAccountRecord = settleAccountRecordService.create(billCheckBo, account);
			List<Bill> flatBills = billService.addFlatBills(billCheckBo.getBills(),billCheckBo.getOperationEmployee(),settleAccountRecord.getRecordNum());
			double total = 0.0;
			for(Bill b:flatBills) {
				if(b.getCost()!=null) {
					total= BigDecimalUtil.sub(total, b.getCost());
				}
				if(b.getPay()!=null) {
					total = BigDecimalUtil.add(total, b.getPay());
				}
			}
			if (Constants.Type.BILL_CHECK_TYPE_ALL.equals(billCheckBo.getCheckType())) {
				bills = billService.checkAccountAllBill(account,total,rep,settleAccountRecord.getRecordNum());
			} else {
				bills = billService.checkBillIds(billCheckBo.getBillIds(),total, rep,settleAccountRecord.getRecordNum());
			}
			if (rep.getStatus() == 0) {
				if (rep.getStatus() == 0) {
					settleAccountRecord.setBills(bills);
					settleAccountRecord.setFlatBills(flatBills);
					settleAccountRecordService.modify(settleAccountRecord);
				}
			}
		} else {
			rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
			rep.setMessage("找不到需要结账的订单");
		}
		if (rep.getStatus() != 0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		rep.setData(account);
		return rep;
	}

	@Override
	public Account createAccount(Customer customer,String roomNum) {
		Account account = new Account();
		account.setCode(businessSeqService.fetchNextSeqNum(customer.getHotelCode(),Constants.Key.BUSINESS_BUSINESS_CUSTOMER_ACCOUNT_SEQ_KEY));
		account.setCustomer(customer);
		account.setName(customer.getName());
		account.setType(Constants.Type.ACCOUNT_CUSTOMER);
		account.setRoomNum(roomNum);
		return add(account);
	}

}
