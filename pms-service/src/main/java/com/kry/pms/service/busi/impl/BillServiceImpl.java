package com.kry.pms.service.busi.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Transient;
import javax.transaction.Transactional;

import com.kry.pms.service.sys.SqlTemplateService;
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
import com.kry.pms.model.http.request.busi.BillOperationBo;
import com.kry.pms.model.http.request.busi.BillQueryBo;
import com.kry.pms.model.http.request.busi.BillSettleBo;
import com.kry.pms.model.persistence.busi.Bill;
import com.kry.pms.model.persistence.busi.BillItem;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.model.persistence.goods.Product;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.service.busi.BillItemService;
import com.kry.pms.service.busi.BillService;
import com.kry.pms.service.busi.RoomRecordService;
import com.kry.pms.service.goods.ProductService;
import com.kry.pms.service.sys.AccountService;
import com.kry.pms.service.sys.BusinessSeqService;
import com.kry.pms.util.BigDecimalUtil;

@Service
public class BillServiceImpl implements BillService {

    private static final String BILL_OP_ADJUST = "adjust";
    private static final String BILL_OP_OFFSET = "offset";
    private static final String BILL_OP_SPLIT = "split";
    @Autowired
    EntityManager entityManager;
    @Autowired
    SqlTemplateService sqlTemplateService;
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

    @Override
    public Bill modify(Bill bill) {
        return billDao.saveAndFlush(bill);
        // 暂不支持直接修改入账记录,请勿实现
//		return null;
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
    public List<Bill> findByAccountAndStatus(Account account, String status) {
        return billDao.findByAccountAndStatus(account,status);
    }

    @Override
    public List<Bill> checkBillIds(List<String> billIds, double total, DtoResponse<Account> rep, String recordNum) {
        List<Bill> bills = billDao.findAllById(billIds);
        return checkBills(bills, total, rep, recordNum);
    }

    public List<Bill> checkBillIds(List<String> billIds, DtoResponse<Account> rep, String recordNum) {
        List<Bill> bills = billDao.findAllById(billIds);
        for (Bill b : bills) {
            if (Constants.Status.BILL_NEED_SETTLED.equals(b.getStatus())) {
                b.setStatus(Constants.Status.BILL_SETTLED);
                b.setCurrentSettleAccountRecordNum(recordNum);
                modify(b);
            } else {
                rep.error(Constants.BusinessCode.CODE_ILLEGAL_OPERATION, "存在已结账账务");
                break;
            }
        }
        return bills;

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
//        if (total != 0) {
//            rep.setStatus(Constants.BusinessCode.CODE_ILLEGAL_OPERATION);
//            rep.setMessage("账务不平，无法结账");
//        }
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
    public DtoResponse<Bill> adjust(String id, Double val, boolean shiftCheck, String shiftCode) {
        DtoResponse<Bill> rep = new DtoResponse<Bill>();
        Bill bill = findById(id);
        if (bill != null && bill.getStatus().equals(Constants.Status.BILL_NEED_SETTLED)) {
            if (!shiftCheck || adjustShiftCheck(bill, val, shiftCode)) {// 不需要确认班次或者班次确认成功
                Bill offsetBill = null;
                offsetBill = copyBill(bill);
                offsetBill.setId(null);
                offsetBill.setProduct(bill.getProduct());
                offsetBill.setAccount(bill.getAccount());
                offsetBill.setTotal(val != null ? val : -bill.getTotal());
                if (val == null) {
                    offsetBill.setStatus(Constants.Status.BILL_INVALID);
                    bill.setStatus(Constants.Status.BILL_INVALID);
                    modify(bill);
                }
                offsetBill = add(offsetBill);
                rep.addData(offsetBill);
            }

        } else {
            rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
            rep.setMessage("找不到对应的未结的账");
        }
        return rep;
    }

    private boolean adjustShiftCheck(Bill bill, Double val, String shiftCode) {
        return true;
    }

    @Transactional
    @Override
    public DtoResponse<Bill> offset(String id) {
        return adjust(id, null, true, null);
    }

    @Transactional
    @Override
    public DtoResponse<Bill> split(String id, Double val1, Double val2) {
        DtoResponse<Bill> rep = new DtoResponse<Bill>();
        rep = adjust(id, null, false, null);
        if (rep.getStatus() == 0) {
            Bill bill = findById(id);
            Bill newBill1 = null;
            Bill newBill2 = null;
            newBill1 = copyBill(bill);
            newBill2 = copyBill(bill);
            newBill1.setStatus(Constants.Status.BILL_NEED_SETTLED);
            newBill2.setStatus(Constants.Status.BILL_NEED_SETTLED);
            newBill1.setTotal(val1);
            newBill2.setTotal(val2);
            add(newBill1);
            add(newBill2);
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
            bill.setItems(null);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return bill;

    }

    @Override
    public DtoResponse<Bill> operation(BillOperationBo bob) {
        switch (bob.getOp()) {
            case BILL_OP_ADJUST:
                return adjust(bob.getId(), bob.getVal1(), true, bob.getShiftCode());
            case BILL_OP_OFFSET:
                return offset(bob.getId());
            case BILL_OP_SPLIT:
                return split(bob.getId(), bob.getVal1(), bob.getVal2());
            default:
                break;
        }
        return null;
    }

    private Bill strikeBill(Bill bill, Account targetAccount, String shiftCode, Employee employee, String recordNum) {
        Bill offsetBill = null;
        offsetBill = copyBill(bill);
        offsetBill.setId(null);
        offsetBill.setProduct(bill.getProduct());
        offsetBill.setAccount(bill.getAccount());
        offsetBill.setTotal(-bill.getTotal());
        offsetBill.setCurrentSettleAccountRecordNum(recordNum);
        offsetBill.setTranferRemark("To " + targetAccount.getCode());
        offsetBill.setStatus(Constants.Status.BILL_SETTLED);
        bill.setTranferRemark(offsetBill.getTranferRemark());
        bill.setStatus(Constants.Status.BILL_SETTLED);
        bill.setCurrentSettleAccountRecordNum(recordNum);
        modify(bill);
        offsetBill = add(offsetBill);
        return offsetBill;
    }

    @Override
    public DtoResponse<List<Bill>> transfer(String bid, Account targetAccount, String shiftCode, Employee employee,
                                            String recordNum) {
        Bill bill = findById(bid);
        return transfer(bill, targetAccount, shiftCode, employee, recordNum);

    }

    @Override
    public List<Bill> findByIds(List<String> ids) {
        return billDao.findAllById(ids);
    }

    @Override
    public DtoResponse<List<Bill>> transfer(Bill bill, Account targetAccount, String shiftCode,
                                            Employee operationEmployee, String recordNum) {
        DtoResponse<List<Bill>> rep = new DtoResponse<List<Bill>>();
        Bill strikeBill = strikeBill(bill, targetAccount, shiftCode, operationEmployee, recordNum);
        List<Bill> data = new ArrayList<Bill>();
        Bill targetBill = copyBill(bill);
        targetBill.setShiftCode(shiftCode);
        targetBill.setBusinessDate(businessSeqService.getBuinessDate(bill.getHotelCode()));
        targetBill.setOperationEmployee(operationEmployee);
        targetBill.setAccount(targetAccount);
        targetBill.setTransferFlag(Constants.FLAG_YES);
        targetBill.setTranferRemark("From:" + bill.getAccount().getCode());
        targetBill.setStatus(Constants.Status.BILL_NEED_SETTLED);
        targetBill.setCurrentSettleAccountRecordNum(recordNum);
        data.add(strikeBill);
        data.add(add(targetBill));
        return rep.addData(data);
    }

    @Override
    public List queryByBo(BillQueryBo query) {
        org.springframework.data.domain.PageRequest req = org.springframework.data.domain.PageRequest.of(1, 10);
       return  sqlTemplateService.processTemplateQuery(query.getHotelCode(),BillService.class.getSimpleName(),"queryByBo",query);
    }
}
