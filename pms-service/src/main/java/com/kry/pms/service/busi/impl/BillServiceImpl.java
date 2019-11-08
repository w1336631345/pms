package com.kry.pms.service.busi.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @Override
    public Bill modify(Bill bill) {
        return billDao.saveAndFlush(bill);
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
    public List<Bill> addFlatBills(List<Bill> list, Employee employee, String orderNum) {
        for (Bill bill : list) {
            bill.setStatus(Constants.Status.BILL_SETTLED);
            bill.setOperationEmployee(employee);
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

    @Override
    public void putAcount(List<RoomRecord> ids) {
        for (int i = 0; i < ids.size(); i++) {
            String id = ids.get(i).getId();
            RoomRecord rr = roomRecordService.findById(id);
            Product p = new Product();
            p.setId("10000");
            Bill bill = new Bill();
            bill.setProduct(p);
            bill.setTotal(rr.getCost());
            bill.setQuantity(1);
            bill.setAccount(rr.getCheckInRecord().getAccount());
            bill.setHotelCode(rr.getHotelCode());
            bill.setOperationRemark("夜审自动入账");
            add(bill);
            rr.setIsAccountEntry("PAY");//入账成功后roomRecord里面入账状态改为pay
            roomRecordService.modify(rr);
        }
    }

}
