package com.kry.pms.service.busi.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Transient;
import javax.transaction.Transactional;

import com.kry.pms.dao.busi.CheckInRecordDao;
import com.kry.pms.dao.busi.RoomRecordDao;
import com.kry.pms.dao.goods.ProductDao;
import com.kry.pms.dao.goods.SetMealDao;
import com.kry.pms.model.dto.BillStatistics;
import com.kry.pms.model.persistence.goods.SetMeal;
import com.kry.pms.model.persistence.sys.BookkeepingSet;
import com.kry.pms.service.sys.BookkeepingSetService;
import com.kry.pms.service.sys.SqlTemplateService;
import org.apache.commons.collections4.MapUtils;
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
    private static final String BILL_OP_SPLIT_OFFSET = "split_offset";
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
    @Autowired
    CheckInRecordDao checkInRecordDao;
    @Autowired
    SetMealDao setMealDao;
    @Autowired
    ProductDao productDao;
    @Autowired
    RoomRecordDao roomRecordDao;
    @Autowired
    BookkeepingSetService bookkeepingSetService;

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
                if (bill.getStatus() == null || Constants.Status.NORMAL.equals(bill.getStatus())) {
                    bill.setStatus(Constants.Status.BILL_NEED_SETTLED);
                }
                if (bill.getFeeFlag() == null) {
                    bill.setFeeFlag(Constants.Flag.FEE_RES);
                }
            } else {
                return null;
            }
        } else if (Constants.Type.BILL_TYPE_PACKAGE.equals(bill.getType())) {
            bill.setCost(bill.getTotal());
        } else {
            return null;
        }
        Account account = accountService.billEntry(bill);
        if (account.getRoomNum() != null) {
            bill.setRoomNum(account.getRoomNum());
            bill.setRoomId(account.getRoomId());
        }
        bill.setBillSeq(account.getCurrentBillSeq());
        if (bill.getItems() != null && !bill.getItems().isEmpty()) {
            bill.getItems().forEach(billItem -> {
                billItem.setHotelCode(account.getHotelCode());
            });
        }
        bill.setAccount(account);
        return billDao.saveAndFlush(bill);
    }

    @Override
    public List<Bill> addAll(List<Bill> bills) {
        bills = billDao.saveAll(bills);
        return bills;
    }

    @Override
    public void delete(String id) {
        Bill bill = billDao.findById(id).get();
        if (bill != null) {
            bill.setDeleted(Constants.DELETED_TRUE);
        }
        billDao.saveAndFlush(bill);
    }

    private Bill addArBill(Bill bill) {
        if (bill.getStatus() == null || Constants.Status.NORMAL.equals(bill.getStatus())) {
            bill.setStatus(Constants.Status.BILL_NEED_SETTLED);
        }
        bill.setType(Constants.Type.BILL_TYPE_PACKAGE);
        bill.setBusinessDate(businessSeqService.getBuinessDate(bill.getHotelCode()));
        Account account = accountService.billEntry(bill);
        bill.setBillSeq(account.getCurrentBillSeq());
        bill.setAccount(account);
        return billDao.saveAndFlush(bill);
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
        return billDao.findByAccountAndStatus(account, status);
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
                rep.setMessage("有部分帐务已经处在结帐状态");
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
            addFlatBill(bill, employee, shiftCode, orderNum);
        }
        return list;
    }

    @Override
    public Bill addFlatBill(Bill bill, Employee employee, String shiftCode, String orderNum) {
        bill.setStatus(Constants.Status.BILL_SETTLED);
        bill.setOperationEmployee(employee);
        bill.setShiftCode(shiftCode);
        bill.setHotelCode(employee.getHotelCode());
        bill.setCurrentSettleAccountRecordNum(orderNum);
        bill = add(bill);
        return bill;
    }

    @Override
    public List<Bill> addToArFlatBill(Bill bill, Employee employee, String shiftCode, String orderNum) {
        List<Bill> data = new ArrayList<>();
        bill.setStatus(Constants.Status.BILL_SETTLED);
        bill.setOperationEmployee(employee);
        bill.setShiftCode(shiftCode);
        bill.setHotelCode(employee.getHotelCode());
        bill.setCurrentSettleAccountRecordNum(orderNum);
        bill = add(bill);
        data.add(bill);
        data.add(createArSettleBill(bill, employee, shiftCode, orderNum));
        return data;
    }

    @Override
    public List<Bill> addToMemberFlatBill(Bill bill, Employee employee, String shiftCode, String orderNum) {
        List<Bill> data = new ArrayList<>();
        bill.setStatus(Constants.Status.BILL_SETTLED);
        bill.setOperationEmployee(employee);
        bill.setShiftCode(shiftCode);
        bill.setHotelCode(employee.getHotelCode());
        bill.setCurrentSettleAccountRecordNum(orderNum);
        bill = add(bill);
        data.add(bill);
        data.add(createMemberSettleBill(bill, employee, shiftCode, orderNum));
        return data;
    }


    private Bill createArSettleBill(Bill res, Employee employee, String shiftCode, String recordNum) {
        return addArBill(createPackageBill(res, employee, shiftCode, recordNum));
    }

    private Bill createMemberSettleBill(Bill res, Employee employee, String shiftCode, String recordNum) {
        Bill bill = createPackageBill(res, employee, shiftCode, recordNum);
//        bill.setStatus(Constants.Status.BILL_SETTLED);
        return addArBill(bill);
    }

    private Bill createPackageBill(Bill res, Employee employee, String shiftCode, String recordNum) {
        Bill bill = new Bill();
        bill.setCost(res.getPay());
        bill.setPay(0.0);
        bill.setRoomNum(res.getRoomNum());
        bill.setRoomId(res.getRoomId());
        bill.setOperationEmployee(employee);
        bill.setShiftCode(shiftCode);
        bill.setAccount(res.getTargetAccount());
        bill.setHotelCode(res.getHotelCode());
        bill.setTargetAccount(res.getAccount());
        bill.setTotal(res.getPay());
        bill.setQuantity(1);
        bill.setFeeFlag(Constants.Flag.FEE_FTA);
        bill.setShowName("前台转入");
        bill.setSid(res.getId());
        bill.setRemark("前台转入 " + res.getAccount().getCode());
        bill.setReceiptNum(recordNum);
        return bill;
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

    // 夜审手动入账（停用）
    @Override
    public void putAcount(List<RoomRecord> ids, LocalDate businessDate, Employee emp, String shiftCode) {
        for (int i = 0; i < ids.size(); i++) {
            RoomRecord rr = ids.get(i);
            String checkInRecordId = rr.getCheckInRecord().getId();
            CheckInRecord cir = checkInRecordDao.getOne(checkInRecordId);
            SetMeal sm = cir.getSetMeal();
            Product p = productDao.findByHotelCodeAndCode(rr.getHotelCode(), "1000");//这里必须改修改，不能写死，要找到夜间稽核类型（在product中加）
            if (sm != null) {
                if (sm.getProduct() != null) {//如果有包价，就一笔整的包价价格账，一笔负的价格账，正负得0
                    Product product = sm.getProduct();//入账项目
                    addAudit(product, sm.getTotal(), sm.getAccount(), cir.getHotelCode(), emp, shiftCode, null, "M", businessDate, null, null);
                    addAudit(product, -sm.getTotal(), sm.getAccount(), cir.getHotelCode(), emp, shiftCode, null, "M", businessDate, null, null);
                }
            }
            addAudit(p, rr.getCost(), cir.getAccount(), cir.getHotelCode(), emp, shiftCode, rr.getId(), "M", businessDate, null, null);
            rr.setIsAccountEntry("PAY");// 入账成功后roomRecord里面入账状态改为pay
            roomRecordService.modify(rr);
        }
    }

    // 夜审手动入账(优化)
    @Override
    public void putAcountMap(List<Map<String, Object>> list, LocalDate businessDate, Employee emp, String shiftCode, String hotelCode) {
        Product p = productDao.findByHotelCodeAndCode(hotelCode, Constants.Code.NIGHT_TRIAL);//这里必须改修改，不能写死，要找到夜间稽核类型（在product中加）
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            String id = MapUtils.getString(map, "id");
            String cirId = MapUtils.getString(map, "cirId");
            String roomNum = MapUtils.getString(map, "roomNum");
            String roomId = MapUtils.getString(map, "roomId");
            String setMealId = MapUtils.getString(map, "setMealId");
            String mainAccountId = MapUtils.getString(map, "mainAccountId");
            String productId = MapUtils.getString(map, "productId");
            String setMealAccountId = MapUtils.getString(map, "setMealAccountId");//这个是包价入账的账号
            String cirAccountId = MapUtils.getString(map, "cirAccountId");//这个是包价入账的账号
            Account setMealAccount = new Account();
            setMealAccount.setId(setMealAccountId);
            Account cirAccount = new Account();
            cirAccount.setId(cirAccountId);
            Double cost = MapUtils.getDouble(map, "cost");
            Double setMealCost = MapUtils.getDouble(map, "setMealCost");
            if (setMealId != null && !"".equals(setMealId)) {
                if (productId != null && !"".equals(productId)) {//如果有包价，就一笔整的包价价格账，一笔负的价格账，正负得0
                    Product product = new Product();//入账项目
                    product.setId(productId);
                    addAudit(product, setMealCost, setMealAccount, hotelCode, emp, shiftCode, null, "M", businessDate, roomNum, roomId);
                    addAudit(p, -setMealCost, setMealAccount, hotelCode, emp, shiftCode, null, "M", businessDate, roomNum, roomId);
                }
            }
            if(mainAccountId != null){
                BookkeepingSet bs = bookkeepingSetService.isExist(hotelCode, mainAccountId, p.getId());
                //如果设置了团付设置，入账到主账号
                if (bs != null) {
                    cirAccount.setId(mainAccountId);
                }
            }
            addAudit(p, cost, cirAccount, hotelCode, emp, shiftCode, id, "M", businessDate, roomNum, roomId);
            // 入账成功后roomRecord里面入账状态改为pay
            roomRecordDao.updateIsAccountEntry("PAY", id);
        }
    }

    public void addAudit(Product product, Double cost, Account account, String hotelCode, Employee emp, String shiftCode, String roomRecordId, String type, LocalDate businessDate, String roomNum, String roomId) {
//        //入账只入当前营业日期的账
//        LocalDate businessDate = businessSeqService.getBuinessDate(hotelCode);
        Bill bill = new Bill();
        bill.setProduct(product);
        bill.setTotal(cost);
        bill.setCost(cost);
        bill.setQuantity(1);
        bill.setAccount(account);
        bill.setHotelCode(hotelCode);
        bill.setRemark(roomNum);
        bill.setRoomNum(roomNum);
        bill.setRoomId(roomId);
        bill.setShiftCode("3");
        if ("M".equals(type)) {
            bill.setOperationRemark("夜审手动入账");
            bill.setOperationEmployee(emp);
        }
        if ("A".equals(type)) {
            bill.setOperationRemark("自动夜审入账");
        }
        bill.setRoomRecordId(roomRecordId);
        bill.setBusinessDate(businessDate);
        add(bill);
    }

    // 夜审自动入账
    @Override
    public void putAcountAUTO(List<RoomRecord> ids, LocalDate businessDate) {
        for (int i = 0; i < ids.size(); i++) {
            RoomRecord rr = ids.get(i);
            String checkInRecordId = rr.getCheckInRecord().getId();
            CheckInRecord cir = checkInRecordDao.getOne(checkInRecordId);
            SetMeal sm = cir.getSetMeal();
            Product p = productDao.findByHotelCodeAndCode(rr.getHotelCode(), "1000");//这里必须改修改，不能写死，要找到夜间稽核类型（在product中加）
            if (sm != null) {
                if (sm.getProduct() != null) {//如果有包价，就一笔整的包价价格账，一笔负的价格账，正负得0
                    Product product = sm.getProduct();//入账项目
                    addAudit(product, sm.getTotal(), sm.getAccount(), cir.getHotelCode(), null, null, null, "A", businessDate, rr.getGuestRoom().getRoomNum(), rr.getGuestRoom().getId());
                    addAudit(product, -sm.getTotal(), sm.getAccount(), cir.getHotelCode(), null, null, null, "A", businessDate, rr.getGuestRoom().getRoomNum(), rr.getGuestRoom().getId());
                }
            }
            addAudit(p, rr.getCost(), cir.getAccount(), cir.getHotelCode(), null, null, rr.getId(), "A", businessDate, rr.getGuestRoom().getRoomNum(), rr.getGuestRoom().getId());
            rr.setIsAccountEntry("PAY");// 入账成功后roomRecord里面入账状态改为pay
            roomRecordService.modify(rr);
        }
    }

    @Transactional
    public DtoResponse<Bill> adjust(String id, Double val, boolean shiftCheck, String shiftCode, Product product) {
        DtoResponse<Bill> rep = new DtoResponse<Bill>();
        Bill bill = findById(id);
        if (bill != null && bill.getStatus().equals(Constants.Status.BILL_NEED_SETTLED)) {
            if (!shiftCheck || adjustShiftCheck(bill, val, shiftCode)) {// 不需要确认班次或者班次确认成功
                Bill offsetBill = null;
                offsetBill = copyBill(bill);
                offsetBill.setId(null);
                offsetBill.setProduct(product);
                offsetBill.setAccount(bill.getAccount());
                offsetBill.setTotal(val != null ? val : -bill.getTotal());
                offsetBill.setSid(bill.getId());
                offsetBill.setFeeFlag(Constants.Flag.FEE_RES);
                modify(bill);
                offsetBill = add(offsetBill);
                rep.addData(offsetBill);
            } else {
                rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
                rep.setMessage("操作不被允许");
            }
        } else {
            rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
            rep.setMessage("找不到对应的未结的账");
        }
        return rep;
    }


    private boolean adjustShiftCheck(Bill bill, Double val, String shiftCode) {
        LocalDate businessDate = businessSeqService.getBuinessDate(bill.getHotelCode());
        if (bill.getShiftCode().equals(shiftCode)) {
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public DtoResponse<Bill> offset(String id, Employee employee, String shiftCode) {
        return offset(id, employee, shiftCode, true, BILL_OP_OFFSET);
    }

    /**
     *
     * @param id
     * @param employee
     * @param shiftCode
     * @param offsetCheck
     * @param billFlag
     * @return
     */
    public DtoResponse<Bill> offset(String id, Employee employee, String shiftCode, boolean offsetCheck, String billFlag) {
        DtoResponse<Bill> rep = new DtoResponse<Bill>();
        Bill bill = findById(id);
        if (bill != null && bill.getStatus().equals(Constants.Status.BILL_NEED_SETTLED)) {
            if (!offsetCheck || offsetShiftCheck(bill, employee, shiftCode)) {// 不需要确认班次或者班次确认成功
                Bill offsetBill = null;
                offsetBill = copyBill(bill);
                offsetBill.setId(null);
                offsetBill.setProduct(bill.getProduct());
                offsetBill.setAccount(bill.getAccount());
                if (bill.getCost() != null) {
                    offsetBill.setCost(-bill.getCost());
                }
                if (bill.getPay() != null) {
                    offsetBill.setPay(-bill.getPay());
                }
                offsetBill.setTotal(-bill.getTotal());
                offsetBill.setSid(bill.getId());
                offsetBill.setFeeFlag(billFlag);
                bill.setFeeFlag(Constants.Flag.FEE_OLD_RES);
                offsetBill.setStatus(Constants.Status.BILL_INVALID);
                bill.setStatus(Constants.Status.BILL_INVALID);
                modify(bill);
                offsetBill = add(offsetBill);
                rep.addData(offsetBill);
            } else {
                rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
                rep.setMessage("操作不被允许");
            }
        } else {
            rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
            rep.setMessage("找不到对应的未结的账");
        }
        return rep;

    }

    private boolean offsetShiftCheck(Bill bill, Employee employee, String shiftCode) {
        LocalDate businessDate = businessSeqService.getBuinessDate(bill.getHotelCode());
        if (businessDate.isEqual(bill.getBusinessDate()) && bill.getOperationEmployee().getId().equals(employee.getId()) && bill.getShiftCode().equals(shiftCode)) {
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public DtoResponse<Bill> split(String id, Double val1, Double val2) {
        DtoResponse<Bill> rep = new DtoResponse<Bill>();
        rep = offset(id, null, null, false, BILL_OP_SPLIT_OFFSET);
        if (rep.getStatus() == 0) {
            Bill bill = findById(id);
            Bill newBill1 = null;
            Bill newBill2 = null;
            newBill1 = copyBill(bill);
            newBill2 = copyBill(bill);
            newBill1.setStatus(Constants.Status.BILL_NEED_SETTLED);
            newBill2.setStatus(Constants.Status.BILL_NEED_SETTLED);
            newBill1.setFeeFlag(Constants.Flag.FEE_RES);
            newBill2.setFeeFlag(Constants.Flag.FEE_RES);
            newBill1.setSid(bill.getId());
            newBill2.setSid(bill.getId());
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
                return adjust(bob.getId(), bob.getVal1(), true, bob.getShiftCode(), bob.getProduct());
            case BILL_OP_OFFSET:
                return offset(bob.getId(), bob.getOperationEmployee(), bob.getShiftCode());
            case BILL_OP_SPLIT:
                return split(bob.getId(), bob.getVal1(), bob.getVal2());
            default:
                break;
        }
        return null;
    }

    /**
     * 冲掉原始帐务
     *
     * @param bill
     * @param targetAccount
     * @param shiftCode
     * @param employee
     * @param recordNum
     * @return
     */
    private Bill strikeBill(Bill bill, Account targetAccount, String shiftCode, Employee employee, String recordNum) {
        Bill offsetBill = null;
        offsetBill = copyBill(bill);
        offsetBill.setId(null);
        offsetBill.setProduct(bill.getProduct());
        offsetBill.setAccount(bill.getAccount());
        offsetBill.setTotal(-bill.getTotal());
        offsetBill.setCurrentSettleAccountRecordNum(recordNum);
        offsetBill.setOperationEmployee(employee);
        offsetBill.setShiftCode(shiftCode);
        offsetBill.setTranferRemark("To " + targetAccount.getCode());
        offsetBill.setStatus(Constants.Status.BILL_SETTLED);
        offsetBill.setFeeFlag(Constants.Flag.FEE_OFFSET_TO_TRANSFER);
        bill.setTranferRemark(bill.getTranferRemark() == null ? offsetBill.getTranferRemark() : bill.getTranferRemark() + ";" + offsetBill.getTranferRemark());
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
        targetBill.setTransferType(targetAccount.getType());
        targetBill.setSourceBill(bill);
        targetBill.setFeeFlag(Constants.Flag.FEE_TRANSFER);
        targetBill.setTranferRemark("From:" + bill.getAccount().getCode());
        targetBill.setSid(bill.getId());
        targetBill.setStatus(Constants.Status.BILL_NEED_SETTLED);
        targetBill.setCurrentSettleAccountRecordNum(recordNum);
        data.add(strikeBill);
        targetBill = add(targetBill);
        bill.setTid(targetBill.getId());
        data.add(targetBill);
        return rep.addData(data);
    }

//    @Override
//    public List queryByBo(BillQueryBo query) {
//        org.springframework.data.domain.PageRequest req = org.springframework.data.domain.PageRequest.of(1, 10);
//        return sqlTemplateService.processTemplateQuery(query.getHotelCode(), BillService.class.getSimpleName(), "queryByBo", query);
//    }

    @Override
    public int countUnSettleBill(String accountId) {
        return billDao.countUnSellteBill(accountId);
    }

    @Override
    public int countUnSellteNotZeroBill(String accountId) {
        return billDao.countUnSellteNotZeroBill(accountId);
    }

    @Override
    public BillStatistics sumNeedSettle(Account account) {
        return billDao.sumNeedSettle(account, Constants.Status.BILL_NEED_SETTLED);
    }

    @Override
    public Bill createArSettleBill(Account targetAccount, double total, double cost, double pay, Employee operationEmployee, String shiftCode, String recordNum) {
        Bill bill = new Bill();
        bill.setCost(cost);
        bill.setPay(pay);
        bill.setOperationEmployee(operationEmployee);
        bill.setShiftCode(shiftCode);
        bill.setAccount(targetAccount);
        bill.setHotelCode(targetAccount.getHotelCode());
        bill.setTotal(total);
        bill.setFeeFlag(Constants.Flag.FEE_FTA);
        bill.setShowName("前台转入");
        bill.setReceiptNum(recordNum);
        return addArBill(bill);
    }

    @Override
    public Bill createToArBill(Account account, double processTotal, double pay, Employee operationEmployee, String shiftCode, String recordNum, String remark) {
        Bill bill = new Bill();
        bill.setProduct(productService.findToArProduct(operationEmployee.getHotelCode()));
        bill.setPay(processTotal);
        bill.setTotal(processTotal);
        bill.setOperationEmployee(operationEmployee);
        bill.setShiftCode(shiftCode);
        bill.setAccount(account);
        bill.setRemark(remark);
        bill.setCurrentSettleAccountRecordNum(recordNum);
        bill.setStatus(Constants.Status.BILL_SETTLED);
        bill.setHotelCode(operationEmployee.getHotelCode());
        return add(bill);
    }

    @Override
    public List<Map<String, Object>> getStatusTotal(String hotelCode, String accountId) {


        List<String> statusList = new ArrayList<>();
        statusList.add(Constants.Status.BILL_SETTLED);
        statusList.add(Constants.Status.BILL_NEED_SETTLED);
        List<Map<String, Object>> list = billDao.getStatusTotal(hotelCode, accountId, statusList);
        return list;
    }

    @Override
    public int autoSettleZeroBill(String id) {
        return billDao.autoSettleZeroBill(id);
    }
}
