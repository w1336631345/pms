package com.kry.pms.service.sys.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.attribute.standard.SheetCollate;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
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
import com.kry.pms.model.http.response.busi.SettleInfoVo;
import com.kry.pms.model.persistence.busi.Bill;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.CreditGrantingRecord;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.model.persistence.busi.SettleAccountRecord;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.service.busi.BillService;
import com.kry.pms.service.busi.CheckInRecordService;
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
	CheckInRecordService checkInRecordService;
	@Autowired
	BusinessSeqService businessSeqService;

	@Override
	public Account add(Account account) {
		if (account.getTotal() == null) {
			initAccount(account);
		}
		return accountDao.saveAndFlush(account);
	}

	private void initAccount(Account account) {
		account.setPay(0.0);
		account.setCost(0.0);
		account.setCurrentBillSeq(0);
		account.setTotal(0.0);
		if (account.getCode() == null) {
			account.setCode(fetchAccountCode(account.getHotelCode(), account.getType()));
		}
	}

	private String fetchAccountCode(String hotelCode, String type) {
		if (type != null && hotelCode != null) {
			return businessSeqService.fetchNextSeqNum(hotelCode, Constants.Key.BUSINESS_BUSINESS_ACCOUNT_SEQ + type);
		}
		return null;
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
		if (Constants.Status.BILL_NEED_SETTLED.equals(bill.getStatus())) {

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

	private DtoResponse<Account> transferBill(BillCheckBo billCheckBo) {
		DtoResponse<Account> rep = new DtoResponse<Account>();
		String targetAccountId = billCheckBo.getTargetAccountId();
		Account account = findById(billCheckBo.getAccountId());

		if (account != null && targetAccountId != null) {
			Account targetAccount = findById(targetAccountId);
			if (targetAccount != null) {
				if (limitCheck(targetAccount, billCheckBo.getTotal())) {
					List<Bill> flatBills = new ArrayList<>();
					SettleAccountRecord settleAccountRecord = settleAccountRecordService.create(billCheckBo, account,
							targetAccount);
					for (String bid : billCheckBo.getBillIds()) {
						DtoResponse<List<Bill>> itemRep = billService.transfer(bid, targetAccount,
								billCheckBo.getShiftCode(), billCheckBo.getOperationEmployee(),settleAccountRecord.getRecordNum());
						if (itemRep.getStatus() != 0) {
							BeanUtils.copyProperties(itemRep, rep);
							TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
							break;
						} else {
							flatBills.addAll(itemRep.getData());
						}
					}
					List<Bill> bills = billService.checkBillIds(billCheckBo.getBillIds(), rep,
							settleAccountRecord.getRecordNum());
					if (rep.getStatus() == 0) {
						settleAccountRecord.setBills(bills);
						settleAccountRecord.setFlatBills(flatBills);
						settleAccountRecordService.modify(settleAccountRecord);
					}
				} else {
					rep.error(Constants.BusinessCode.CODE_PARAMETER_INVALID, "转账目标账户额度不足，请重新选择");
				}
			} else {
				rep.error(Constants.BusinessCode.CODE_PARAMETER_INVALID, "转账目标账户有误，请重新选择");
			}
		} else {
			rep.error(Constants.BusinessCode.CODE_PARAMETER_INVALID, "转账目标账户有误，请重新选择");
		}
		return rep;
	}

	private boolean limitCheck(Account account, double limit) {
		if (limit <= 0 && account.getAvailableCreditLimit() != null && account.getAvailableCreditLimit() > limit) {
			return true;
		} else {
			return false;
		}
	}

	@Transactional
	@Override
	public DtoResponse<Account> checkCustomerBill(BillCheckBo billCheckBo) {
		if (Constants.Type.BILL_CHECK_WAY_TRANSFER.equals(billCheckBo.getCheckWay())) {
			return transferBill(billCheckBo);
		}
		DtoResponse<Account> rep = new DtoResponse<Account>();
		Account account = findById(billCheckBo.getAccountId());
		List<Bill> bills = null;
		if (account != null) {
			SettleAccountRecord settleAccountRecord = settleAccountRecordService.create(billCheckBo, account);
			List<Bill> flatBills = billService.addFlatBills(billCheckBo.getBills(), billCheckBo.getOperationEmployee(),
					billCheckBo.getShiftCode(), settleAccountRecord.getRecordNum());
			double total = 0.0;
			for (Bill b : flatBills) {
				if (b.getCost() != null) {
					total = BigDecimalUtil.sub(total, b.getCost());
				}
				if (b.getPay() != null) {
					total = BigDecimalUtil.add(total, b.getPay());
				}
			}
			if (Constants.Type.BILL_CHECK_TYPE_ALL.equals(billCheckBo.getCheckType())) {
				bills = billService.checkAccountAllBill(account, total, rep, settleAccountRecord.getRecordNum());
			} else {
				bills = billService.checkBillIds(billCheckBo.getBillIds(), total, rep,
						settleAccountRecord.getRecordNum());
			}
			if (rep.getStatus() == 0) {
				settleAccountRecord.setBills(bills);
				settleAccountRecord.setFlatBills(flatBills);
				settleAccountRecordService.modify(settleAccountRecord);

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
	public Account createAccount(Customer customer, String roomNum) {
		Account account = new Account(0, 0);
		account.setCode(businessSeqService.fetchNextSeqNum(customer.getHotelCode(),
				Constants.Key.BUSINESS_BUSINESS_CUSTOMER_ACCOUNT_SEQ_KEY));
		account.setCustomer(customer);
		account.setHotelCode(customer.getHotelCode());
		account.setName(customer.getName());
		account.setType(Constants.Type.ACCOUNT_CUSTOMER);
		account.setRoomNum(roomNum);
		return add(account);
	}

	@Override
	public DtoResponse<Double> queryRoomPrice(String id) {
		DtoResponse<Double> rep = new DtoResponse<Double>();
		CheckInRecord cir = checkInRecordService.queryByAccountId(id);
		if (cir != null) {
			return rep.addData(cir.getPurchasePrice() == null ? 0.0 : cir.getPurchasePrice());
		} else {
			rep.setStatus(Constants.BusinessCode.CODE_ILLEGAL_OPERATION);
			rep.setMessage("找不到对应的订单");
		}
		return rep;
	}

	@Override
	public SettleInfoVo getSettleInfo(String type, String id) {
		SettleInfoVo siv = null;
		switch (type) {
		case Constants.Type.SETTLE_TYPE_ACCOUNT:
			siv = getSingleAccountSettleInfo(id);
			break;
		case Constants.Type.SETTLE_TYPE_PART:

			break;
		case Constants.Type.SETTLE_TYPE_GROUP:
			siv = getSingleAccountSettleInfo(id);
			break;
		case Constants.Type.SETTLE_TYPE_IGROUP:

			break;
		case Constants.Type.SETTLE_TYPE_ROOM:
			siv = getRoomSettleInfo(id);
			break;
		case Constants.Type.SETTLE_TYPE_LINK:
			siv = getLinkSettleInfo(id);
			break;

		default:
			break;
		}
		return siv;
	}

	private SettleInfoVo getRoomSettleInfo(String id) {
		List<CheckInRecord> cirs = checkInRecordService.findByGuestRoomAndStatusAndDeleted(id,
				Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN, Constants.DELETED_FALSE);
		SettleInfoVo info = new SettleInfoVo();
		for (CheckInRecord cir : cirs) {
			if (cir.getAccount() != null) {
				countSettleInfo(info, cir.getAccount());
			}
		}
		return info;
	}

	private SettleInfoVo getSingleAccountSettleInfo(String id) {
		SettleInfoVo info = new SettleInfoVo();
		Account account = findById(id);
		BeanUtils.copyProperties(account, info);
		return info;
	}

	private void countSettleInfo(SettleInfoVo info, Account account) {
		if (account.getCost() != null) {
			info.setCost(BigDecimalUtil.add(info.getCost(), account.getCost()));
		}
		if (account.getPay() != null) {
			info.setPay(BigDecimalUtil.add(info.getPay(), account.getPay()));
		}
		if (account.getCreditLimit() != null) {
			info.setCreditLimit(BigDecimalUtil.add(info.getCreditLimit(), account.getCreditLimit()));
		}
		if (account.getAvailableCreditLimit() != null) {
			info.setAvailableCreditLimit(
					BigDecimalUtil.add(info.getAvailableCreditLimit(), account.getAvailableCreditLimit()));
		}
		if (account.getTotal() != null) {
			info.setTotal(BigDecimalUtil.add(info.getTotal(), account.getTotal()));
		}
		if (account.getCurrentBillSeq() != null) {
			info.setTotalSeq(info.getTotalSeq() + account.getCurrentBillSeq());
		}
	}

	private SettleInfoVo getLinkSettleInfo(String id) {
		List<CheckInRecord> cirs = checkInRecordService.findByLinkId(id);
		SettleInfoVo info = new SettleInfoVo();
		for (CheckInRecord cir : cirs) {
			if (cir.getAccount() != null) {
				countSettleInfo(info, cir.getAccount());
			}
		}
		return info;
	}

	private String createAccountCode(String hotleCode, String type) {
		return null;
	}

	@Override
	public List<Account> findByHotelCodeAndType(String hotelCode) {
		List<Account> list = accountDao.findByHotelCodeAndType(hotelCode, Constants.Type.ACCOUNT_INNER);
		return list;
	}
}
