package com.kry.pms.service.busi.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Transient;
import javax.transaction.Transactional;

import com.kry.pms.service.busi.RoomRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.DtoResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.BillDao;
import com.kry.pms.model.http.request.busi.BillBo;
import com.kry.pms.model.http.request.busi.BillSettleBo;
import com.kry.pms.model.persistence.busi.Bill;
import com.kry.pms.model.persistence.busi.BillItem;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.model.persistence.goods.Product;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.model.persistence.sys.BusinessSeq;
import com.kry.pms.service.busi.BillItemService;
import com.kry.pms.service.busi.BillService;
import com.kry.pms.service.goods.ProductService;
import com.kry.pms.service.sys.AccountService;
import com.kry.pms.service.sys.BusinessSeqService;
import com.kry.pms.util.BigDecimalUtil;

import antlr.CppCodeGenerator;

@Service
public class BillServiceImpl implements BillService {
	@Autowired
	BillDao billDao;
	@Autowired
	BillItemService billItemService;
	@Autowired
	ProductService productService;
	@Autowired
	BusinessSeqService businessSeqService;
	@Autowired
	RoomRecordService roomRecordService;
	@Autowired
	AccountService accountService;

	@Transient
	@Override
	public Bill add(Bill bill) {
		if (bill.getProduct() != null && bill.getProduct().getId() != null) {
			Product p = productService.findById(bill.getProduct().getId());
			if (p != null) {
				if (1 == p.getDirection()) {
					bill.setCost(bill.getTotal());
				} else {
					bill.setPay(bill.getTotal());
				}
				bill.setType(p.getType());
				bill.setCreateDate(LocalDateTime.now());
				bill.setBusinessDate(businessSeqService.getBuinessDate(p.getHotelCode()));
				if (bill.getStatus() == null) {
					bill.setStatus(Constants.Status.BILL_NEED_SETTLED);
				}
			}
		} else {
			return null;
		}
		Account account = accountService.billEntry(bill);
		bill.setBillSeq(account.getCurrentBillSeq());
		bill.setAccount(account);
		return billDao.saveAndFlush(bill);
	}

	@Override
	public void delete(String id) {
		Bill bill = billDao.findById(id).get();
		if (bill != null) {
			bill.setDeleted(Constants.DELETED_TRUE);
		}
		billDao.saveAndFlush(bill);
	}

	@Deprecated
	@Override
	public Bill modify(Bill bill) {
//		return billDao.saveAndFlush(bill);
		// 暂不支持直接修改入账记录,请勿实现
		return null;
	}

	@Override
	public Bill findById(String id) {
		return billDao.findById(id).orElse(null);
	}

	@Override
	public List<Bill> getAllByHotelCode(String code) {
		return null;// 默认不实现
		// return billDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<Bill> listPage(PageRequest<Bill> prq) {
		Example<Bill> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(billDao.findAll(ex, req));
	}

	@Override
	public boolean billEntry(RoomRecord rr, LocalDate recordDate) {
		Account account = rr.getCheckInRecord().getAccount();
//		BillItem billItem = billItemService.createAndSaveBillItem(bill, rr, recordDate);
//		bill.setCurrentItemSeq(billItem.getItemSeq());
//		bill.setTotal(billItem.getTotal() + bill.getTotal());
		return true;
	}

	@Override
	public boolean checkAndPayBill(List<CheckInRecord> crs, double total) {
		double billTatal = 0.0;
		for (CheckInRecord cir : crs) {
//			billTatal += cir.getBill().getTotal();
		}
		if (total == billTatal) {

		}
		return false;

	}

	@Override
	public boolean checkAndPayBill(BillSettleBo bsb) {
		double actualTotal = 0.0;
		for (BillBo bb : bsb.getBills()) {
			Bill bill = findById(bb.getBillId());
			if (bill != null) {
				List<String> itemIds = bb.getItems();
				List<BillItem> bis = billItemService.findByIds(itemIds);
				if (bis != null && !bis.isEmpty()) {
					for (BillItem bi : bis) {
						actualTotal += bi.getTotal();
						bi.setStatus(Constants.Status.BILL_SETTLED);
						billItemService.modify(bi);
					}
				}
			} else {
				// TODO
			}
		}
		if (bsb.getTatal() == actualTotal) {
			return true;
		}
		return false;
	}

	@Override
	public List<Bill> findByAccountId(String id) {
		return billDao.findByAccountId(id);
	}

	@Override
	public List<Bill> checkBillIds(List<String> billIds, double total, DtoResponse<Account> rep, String recordNum) {
		List<Bill> bills = billDao.findAllById(billIds);
		return checkBills(bills, total, rep, recordNum);
	}

	private List<Bill> checkBills(List<Bill> bills, double total, DtoResponse<Account> rep, String recordNum) {
		for (Bill b : bills) {
			if (Constants.Status.BILL_NEED_SETTLED.equals(b.getStatus())) {
				if (b.getCost() != null) {
					total = BigDecimalUtil.sub(total, b.getCost());
				}
				if (b.getPay() != null) {
					total = BigDecimalUtil.add(total, b.getPay());
				}
				b.setStatus(Constants.Status.BILL_SETTLED);
				b.setCurrentSettleAccountRecordNum(recordNum);
				modify(b);
			} else {
				rep.setStatus(Constants.BusinessCode.CODE_ILLEGAL_OPERATION);
				break;
			}
		}
		if (total != 0) {
			rep.setStatus(Constants.BusinessCode.CODE_ILLEGAL_OPERATION);
			rep.setMessage("账务不平，无法结账");
		}
		return bills;
	}

	@Override
	public List<Bill> addFlatBills(List<Bill> list, Employee employee, String shiftCode, String orderNum) {
		for (Bill bill : list) {
			bill.setStatus(Constants.Status.BILL_SETTLED);
			bill.setOperationEmployee(employee);
			bill.setShiftCode(shiftCode);
			bill.setCurrentSettleAccountRecordNum(orderNum);
			bill = add(bill);
		}
		return list;
	}

	@Override
	public List<Bill> checkAccountAllBill(Account account, double total, DtoResponse<Account> rep, String recordNum) {
		List<Bill> bills = billDao.findByAccountAndStatus(account, Constants.Status.BILL_NEED_SETTLED);
		bills = checkBills(bills, total, rep, recordNum);
		if (rep.getStatus() == 0) {
			account.setStatus(Constants.Status.ACCOUNT_SETTLE);
			accountService.modify(account);
		}
		return bills;
	}

	// 夜审手动入账
	@Override
	public void putAcount(List<RoomRecord> ids, LocalDate businessDate, Employee emp, String shiftCode) {
		for (int i = 0; i < ids.size(); i++) {
			String id = ids.get(i).getId();
			RoomRecord rr = roomRecordService.findById(id);
			Product p = new Product();
			p.setId("10000");
			Bill bill = new Bill();
			bill.setProduct(p);
			bill.setTotal(rr.getCost());
			bill.setCost(rr.getCost());
			bill.setQuantity(1);
			bill.setAccount(rr.getCheckInRecord().getAccount());
			bill.setHotelCode(rr.getHotelCode());
			bill.setOperationRemark("夜审手动入账");
//            bill.setOperationEmployee(emp);
			bill.setShiftCode(shiftCode);
			bill.setRoomRecordId(rr.getId());
			bill.setBusinessDate(businessDate);
			add(bill);
			rr.setIsAccountEntry("PAY");// 入账成功后roomRecord里面入账状态改为pay
			roomRecordService.modify(rr);
		}
	}

	// 夜审自动入账
	@Override
	public void putAcountAUTO(List<RoomRecord> ids, LocalDate businessDate) {
		for (int i = 0; i < ids.size(); i++) {
			String id = ids.get(i).getId();
			RoomRecord rr = roomRecordService.findById(id);
			Product p = new Product();
			p.setId("10000");
			Bill bill = new Bill();
			bill.setProduct(p);
			bill.setTotal(rr.getCost());
			bill.setCost(rr.getCost());
			bill.setQuantity(1);
			bill.setAccount(rr.getCheckInRecord().getAccount());
			bill.setHotelCode(rr.getHotelCode());
			bill.setOperationRemark("夜审自动入账");
			bill.setRoomRecordId(rr.getId());
			bill.setBusinessDate(businessDate);
			add(bill);
			rr.setIsAccountEntry("PAY");// 入账成功后roomRecord里面入账状态改为pay
			roomRecordService.modify(rr);
		}
	}

	@Transactional
	@Override
	public DtoResponse<Bill> adjust(String id, Double val) {
		DtoResponse<Bill> rep = new DtoResponse<Bill>();
		Bill bill = findById(id);
		if (bill != null) {
			if (val == null) {
				val = -bill.getTotal();
			}
			Bill offsetBill = null;
			offsetBill = copyBill(bill);
			offsetBill.setId(null);
			offsetBill.setProduct(bill.getProduct());
			offsetBill.setAccount(bill.getAccount());
			if (bill.getCost() != null && bill.getCost() != 0) {
				offsetBill.setCost(val);
			}
			if (bill.getPay() != null && bill.getPay() != 0) {
				offsetBill.setPay(val);
			}
			offsetBill = add(offsetBill);
			rep.setData(offsetBill);

		} else {
			rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
			rep.setMessage("找不到对应的订单");
		}
		return rep;
	}

	@Transactional
	@Override
	public DtoResponse<Bill> offset(String id) {
		return adjust(id, null);
	}

	@Transactional
	@Override
	public DtoResponse<Bill> split(String id, Double val1, Double val2) {
		DtoResponse<Bill> rep = new DtoResponse<Bill>();
		rep = adjust(id, null);
		if (rep.getStatus() == 0) {
			Bill bill = findById(id);
			Bill newBill1= null;
			Bill newBill2 = null;
			Double total = BigDecimalUtil.add(val1, val2);
			if (bill.getTotal() == total) {
				newBill1 = copyBill(bill);
				newBill2 = copyBill(bill);
				if (bill.getCost() != null && bill.getCost() != 0) {
					newBill1.setCost(val1);
					newBill2.setCost(val2);
				}
				if (bill.getPay() != null && bill.getPay() != 0) {
					newBill1.setPay(val1);
					newBill2.setPay(val2);
				}
				add(newBill1);
				add(newBill2);
			}
		}
		return rep;
	}

	private Bill copyBill(Bill src) {
		Bill bill = null;
		try {
			bill = (Bill) src.clone();
			bill.setId(null);
			bill.setProduct(src.getProduct());
			bill.setAccount(src.getAccount());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return bill;

	}
}
