package com.kry.pms.service.sys.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.kry.pms.dao.busi.RoomRecordDao;
import com.kry.pms.model.persistence.goods.Product;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.service.busi.*;
import com.kry.pms.service.goods.ProductService;
import org.apache.commons.collections4.MapUtils;
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
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.model.persistence.busi.SettleAccountRecord;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.sys.Account;
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
    @Autowired
    RoomRecordDao roomRecordDao;
    @Autowired
    ProductService productService;

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
        account.setStatus(Constants.Status.ACCOUNT_NEW);
        if (account.getCode() == null) {
            account.setStatus(fetchAccountCode(account.getHotelCode(), account.getType()));
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
        account.setStatus(Constants.Status.ACCOUNT_IN);
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

    @Override
    public DtoResponse<List<Bill>> transferBill(List<Bill> bills, Double total, Account account, Account targetAccount,
                                                String shiftCode, Employee operationEmployee, String recordNum) {
        DtoResponse<List<Bill>> rep = new DtoResponse<List<Bill>>();
        if (limitCheck(targetAccount, total)) {
            List<Bill> flatBills = new ArrayList<>();
            for (Bill bill : bills) {
                DtoResponse<List<Bill>> itemRep = billService.transfer(bill, targetAccount, shiftCode,
                        operationEmployee, recordNum);
                if (itemRep.getStatus() != 0) {
                    BeanUtils.copyProperties(itemRep, rep);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    break;
                } else {
                    flatBills.addAll(itemRep.getData());
                }
            }
            if (rep.getStatus() == 0) {
                rep.addData(flatBills);
            }
        } else {
            rep.error(Constants.BusinessCode.CODE_PARAMETER_INVALID, "转账目标账户额度不足，请重新选择");
        }

        return rep;
    }

    @Override
    public DtoResponse<List<Bill>> cancleTransfer(List<Bill> flatBills, Double total, Account account, Account targetAccount, String shiftCode, Employee operationEmployee, String cancleNum) {
        for (Bill bill : flatBills) {
            if (bill.getAccount().getId().equals(targetAccount.getId())) {
                List<Bill> bills = billService.transfer(bill, account, shiftCode, operationEmployee, cancleNum).getData();
                for (Bill b : bills) {
                    b.setStatus(Constants.Status.BILL_INVALID);
                    billService.modify(b);
                }
            }
            bill.setStatus(Constants.Status.BILL_INVALID);
            billService.modify(bill);
        }
        return null;
    }

    private DtoResponse<Account> transferBill(BillCheckBo billCheckBo) {
        DtoResponse<Account> rep = new DtoResponse<Account>();
        String targetAccountId = billCheckBo.getTargetAccountId();
        Account account = findById(billCheckBo.getAccountId());
        if (account != null && targetAccountId != null) {
            Account targetAccount = findById(targetAccountId);
            if (targetAccount != null) {
                if (limitCheck(targetAccount, billCheckBo.getTotal())) {
                    SettleAccountRecord settleAccountRecord = settleAccountRecordService.create(billCheckBo, account,
                            targetAccount);
                    List<Bill> bills = billService.findByIds(billCheckBo.getBillIds());
                    DtoResponse<List<Bill>> crep = transferBill(bills, billCheckBo.getTotal(), account, targetAccount,
                            billCheckBo.getShiftCode(), billCheckBo.getOperationEmployee(),
                            settleAccountRecord.getRecordNum());
                    if (crep.getStatus() == 0) {
                        settleAccountRecord.setBills(bills);
                        settleAccountRecord.setFlatBills(crep.getData());
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

    private boolean limitCheck(Account account, Double limit) {
        if (account.getType().equals(Constants.Type.ACCOUNT_AR)
                || account.getType().equals(Constants.Type.ACCOUNT_MEMBER)) {
            if (limit <= 0 || (account.getAvailableCreditLimit() != null && account.getAvailableCreditLimit() > limit)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private DtoResponse<Account> checkBill(BillCheckBo billCheckBo) {
        String recordNum = businessSeqService.fetchNextSeqNum(billCheckBo.getHotelCode(),
                Constants.Key.BUSINESS_BUSINESS_SETTLE_SEQ_KEY);
        billCheckBo.setMainSettleRecordNum(recordNum);
        billCheckBo.setExtBills(addExtBills(billCheckBo));
        switch (billCheckBo.getCheckType()) {
            case Constants.Type.SETTLE_TYPE_ACCOUNT:
                return checkAccountBill(billCheckBo);
            case Constants.Type.SETTLE_TYPE_PART:
                return checkAccountBill(billCheckBo);
            case Constants.Type.SETTLE_TYPE_GROUP:
                return checkGroupBill(billCheckBo);
            case Constants.Type.SETTLE_TYPE_LINK:
                return checkLinkBill(billCheckBo);
            case Constants.Type.SETTLE_TYPE_IGROUP:
                return checkIGroupBill(billCheckBo);
            case Constants.Type.SETTLE_TYPE_ROOM:
                return checkRoomBill(billCheckBo);
        }
        DtoResponse<Account> rep = new DtoResponse<Account>();
        return rep;
    }
    private DtoResponse<Account> checkAccountBill(BillCheckBo billCheckBo) {
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
            if (Constants.Type.SETTLE_TYPE_ACCOUNT.equals(billCheckBo.getCheckType())) {
                bills = billService.checkAccountAllBill(account, total, rep, settleAccountRecord.getRecordNum());
            } else {
                bills = billService.checkBillIds(billCheckBo.getBillIds(), total, rep,
                        settleAccountRecord.getRecordNum());
            }
            if (rep.getStatus() == 0) {
                settleAccountRecord.setBills(bills);
                settleAccountRecord.setFlatBills(flatBills);
                settleAccountRecord.setExtBills(billCheckBo.getExtBills());
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

    private List<Bill> addExtBills(BillCheckBo billCheckBo) {
        List<Bill> bills = null;
        if (billCheckBo.getExtBills() != null && !billCheckBo.getExtBills().isEmpty()) {
            bills = new ArrayList<>();
            for (Bill eb : billCheckBo.getExtBills()) {
                eb.setHotelCode(billCheckBo.getHotelCode());
                eb.setFeeFlag(Constants.Type.BILL_FLAG_EXT_ROOM_FEE);
                bills.add(billService.add(eb));
            }
        }
        return bills;
    }

    private DtoResponse<Account> checkRoomBill(BillCheckBo billCheckBo) {
        DtoResponse<Account> rep = new DtoResponse<Account>();
        List<CheckInRecord> cirs = checkInRecordService.findByGuestRoomAndStatusAndDeleted(billCheckBo.getAccountId(), null, Constants.DELETED_FALSE);
        if (!cirs.isEmpty() && cirs.size() == 0) {
            billCheckBo.setCheckType(Constants.Type.SETTLE_TYPE_ACCOUNT);
            billCheckBo.setAccountId(cirs.get(0).getAccount().getId());
            return checkAccountBill(billCheckBo);
        } else {
            Account mainAccount = findMainAccount(cirs, billCheckBo.getMainAccountId());
            if (mainAccount != null) {
                boolean result = transferBill(cirs, mainAccount, billCheckBo);
                if (result) {
                    billCheckBo.setCheckType(Constants.Type.SETTLE_TYPE_ACCOUNT);
                    billCheckBo.setAccountId(mainAccount.getId());
                    return checkAccountBill(billCheckBo);
                } else {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
                    rep.setMessage("结帐过程中转账失败");
                }
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
                rep.setMessage("无法找到结帐主账户");
            }
        }
        return rep;
    }

    private Account findMainAccount(List<CheckInRecord> list, String mainAccountId) {
        Account mainAccount = null;
        if (mainAccountId != null) {
            for (CheckInRecord cir : list) {
                if (cir.getAccount() != null && cir.getAccount().getId().equals(mainAccountId)) {
                    mainAccount = cir.getAccount();
                }
            }
        } else {
            int cseq = 0;
            for (CheckInRecord cir : list) {
                if (cir.getAccount() != null && cir.getAccount().getCurrentBillSeq() > cseq) {
                    mainAccount = cir.getAccount();
                }
            }
        }
        return mainAccount;
    }

    private DtoResponse<Account> checkIGroupBill(BillCheckBo billCheckBo) {
        DtoResponse<Account> rep = new DtoResponse<Account>();
        String id = billCheckBo.getAccountId();
        CheckInRecord cir = checkInRecordService.queryByAccountId(id);
        List<CheckInRecord> cirs = checkInRecordService.findByOrderNumC(cir.getOrderNum());
        Account targetAccount = cir.getAccount();
        boolean result = transferBill(cirs, targetAccount, billCheckBo);
        if (result) {
            billCheckBo.setCheckType(Constants.Type.SETTLE_TYPE_ACCOUNT);
            billCheckBo.setAccountId(targetAccount.getId());
            return checkAccountBill(billCheckBo);
        } else {
            rep.setStatus(Constants.BusinessCode.CODE_ILLEGAL_OPERATION);
            rep.setMessage("部分帐务无法转到主账户");
            return rep;
        }
    }

    private DtoResponse<Account> checkGroupBill(BillCheckBo billCheckBo) {
        DtoResponse<Account> rep = new DtoResponse<Account>();
        String id = billCheckBo.getAccountId();
        CheckInRecord cir = checkInRecordService.queryByAccountId(id);
        List<CheckInRecord> cirs = checkInRecordService.findByOrderNum(cir.getOrderNum());
        Account targetAccount = cir.getAccount();
        boolean result = true;
        for (CheckInRecord item : cirs) {
            if (Constants.Type.CHECK_IN_RECORD_CUSTOMER.equals(item.getType())) {
                Account account = item.getAccount();
                if (Constants.Status.ACCOUNT_NEW.equals(account.getStatus())||Constants.Status.ACCOUNT_SETTLE.equals(account.getStatus())) {
                }else{
                    result = false;
                }
            }
        }
        if (!result) {
            rep.setStatus(Constants.BusinessCode.CODE_ILLEGAL_OPERATION);
            rep.setMessage("存在未结帐的团员，请先结清团员帐务");
            return rep;
        } else {
            billCheckBo.setCheckType(Constants.Type.SETTLE_TYPE_ACCOUNT);
            return checkAccountBill(billCheckBo);
        }
    }

    private boolean transferBill(List<CheckInRecord> cirs, Account targetAccount, BillCheckBo billCheckBo) {
        for (CheckInRecord item : cirs) {
            if (Constants.Type.CHECK_IN_RECORD_CUSTOMER.equals(item.getType())) {
                Account account = item.getAccount();
                if (!account.getId().equals(targetAccount.getId()) && account.getTotal() != 0.0) {
                    boolean result = transferBill(account, targetAccount, billCheckBo);
                    if (!result) {
                        // TODO 失败
                        return result;
                    }
                }
            }
        }
        return true;
    }


    private boolean transferBill(Account account, Account targetAccount, BillCheckBo billCheckBo) {
        List<Bill> bills = billService.findByAccountAndStatus(account, Constants.Status.BILL_NEED_SETTLED);
        if (bills != null) {
            SettleAccountRecord settleAccountRecord = settleAccountRecordService.create(billCheckBo, account, targetAccount);
            DtoResponse<List<Bill>> rep = transferBill(bills, account.getTotal(), account, targetAccount, billCheckBo.getShiftCode(), billCheckBo.getOperationEmployee(), settleAccountRecord.getRecordNum());
            if (rep.getStatus() == 0) {
                return true;
            }
        }
        return false;
    }

    private DtoResponse<Account> checkLinkBill(BillCheckBo billCheckBo) {
        DtoResponse<Account> rep = new DtoResponse<Account>();
        String id = billCheckBo.getAccountId();
        List<CheckInRecord> cirs = checkInRecordService.findByLinkId(id);
        Account account = findMainAccount(cirs, billCheckBo.getMainAccountId());
        boolean result = transferBill(cirs, account, billCheckBo);
        if (result) {
            billCheckBo.setCheckType(Constants.Type.SETTLE_TYPE_ACCOUNT);
            billCheckBo.setAccountId(account.getId());
            return checkAccountBill(billCheckBo);
        } else {
            rep.setStatus(Constants.BusinessCode.CODE_ILLEGAL_OPERATION);
            rep.setMessage("部分帐务无法转到主账户");
            return rep;
        }
    }


    @Transactional
    @Override
    public DtoResponse<Account> checkCustomerBill(BillCheckBo billCheckBo) {
        if (Constants.Type.BILL_CHECK_WAY_TRANSFER.equals(billCheckBo.getCheckWay())) {
            return transferBill(billCheckBo);
        } else {
            return checkBill(billCheckBo);
        }
    }

    @Override
    public Account createAccount(Customer customer, String roomNum) {
        Account account = new Account(0, 0);
        account.setStatus(businessSeqService.fetchNextSeqNum(customer.getHotelCode(),
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
        Double result = 0.0;
        DtoResponse<Double> rep = new DtoResponse<Double>();
        CheckInRecord cir = checkInRecordService.queryByAccountId(id);
        LocalDate now = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = now.format(fmt);
        if (cir != null) {
            Map<String, Object> map = roomRecordDao.recordDateAndRoomPrice(date, cir.getId());
            Double price = MapUtils.getDouble(map, "room_price");
            if (price != null) {
                result = price;
            } else {
                result = cir.getPersonalPrice();
            }
            return rep.addData(result);
        } else {
            rep.setStatus(Constants.BusinessCode.CODE_ILLEGAL_OPERATION);
            rep.setMessage("找不到对应的订单");
        }
        return rep;
    }

    @Override
    public SettleInfoVo getSettleInfo(String type, String id, String extFee, String hotelCode) {
        SettleInfoVo siv = null;
        switch (type) {
            case Constants.Type.SETTLE_TYPE_ACCOUNT:
                siv = getSingleAccountSettleInfo(id, extFee);
                break;
            case Constants.Type.SETTLE_TYPE_PART:

                break;
            case Constants.Type.SETTLE_TYPE_GROUP:
                siv = getGroupAccountSettleInfo(id, extFee, hotelCode);
                break;
            case Constants.Type.SETTLE_TYPE_IGROUP:
                siv = getGroupAccountSettleInfo(id, extFee, hotelCode);
                break;
            case Constants.Type.SETTLE_TYPE_ROOM:
                siv = getRoomSettleInfo(id, extFee, hotelCode);
                break;
            case Constants.Type.SETTLE_TYPE_LINK:
                siv = getLinkSettleInfo(id, extFee, hotelCode);
                break;

            default:
                break;
        }
        if (siv != null) {
            countExtBillToInfo(siv);
        }
        return siv;
    }

    private void countExtBillToInfo(SettleInfoVo siv) {
        if (!siv.getBills().isEmpty()) {
            for (Bill bill : siv.getBills()) {
                siv.setCost(BigDecimalUtil.add(siv.getCost(), bill.getCost()));
                siv.setTotal(BigDecimalUtil.add(siv.getTotal(), bill.getCost()));
            }
        }
    }

    private SettleInfoVo getRoomSettleInfo(String id, String extFee, String hotelCode) {
        List<CheckInRecord> cirs = checkInRecordService.findByGuestRoomAndStatusAndDeleted(id,Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN, Constants.DELETED_FALSE);
        return createSettleInfo(cirs, extFee, hotelCode);
    }

    private Bill createExtFee(CheckInRecord cir, Product product, Double cost) {
        Bill bill = new Bill();
        bill.setCost(cost);
        bill.setTotal(cost);
        bill.setProduct(product);
        bill.setQuantity(1);
        GuestRoom guestRoom = new GuestRoom();
        guestRoom.setId(cir.getGuestRoom().getId());
        guestRoom.setRoomNum(cir.getGuestRoom().getRoomNum());
        Account account = new Account();
        account.setId(cir.getAccount().getId());
        account.setName(cir.getAccount().getName());
        account.setRoomNum(cir.getAccount().getRoomNum());
        bill.setAccount(account);
        bill.setGuestRoom(guestRoom);
        bill.setStatus(Constants.Status.BILL_NEED_SETTLED);
        return bill;
    }

    private SettleInfoVo getGroupAccountSettleInfo(String id, String extFee, String hotelCode) {
        CheckInRecord cir = checkInRecordService.queryByAccountId(id);
        SettleInfoVo settleInfoVo = new SettleInfoVo();
        if (cir != null) {
            List<CheckInRecord> cirs = checkInRecordService.findByOrderNumC(cir.getOrderNum());
            settleInfoVo = createSettleInfo(cirs, extFee, hotelCode);
        } else {
            return null;
        }
        return settleInfoVo;
    }

    private boolean accountCheck(List<CheckInRecord> cirs) {
        for (CheckInRecord cir : cirs) {

        }
        return false;
    }

    private int queryAccountCount(String orderNum, String status) {
        return accountDao.queryAccounCountByOrderNumAndStatus(orderNum, status);
    }

    private SettleInfoVo getSingleAccountSettleInfo(String id, String extFee) {
        SettleInfoVo info = new SettleInfoVo();
        Account account = findById(id);
        CheckInRecord cir = checkInRecordService.queryByAccountId(id);
        if (cir != null) {
            return createSettleInfo(cir, extFee, cir.getHotelCode());
        }
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
            info.setTotalSeq(info.getTotalSeq() == null ? 0 : info.getTotalSeq() + account.getCurrentBillSeq());
        }
    }

    private SettleInfoVo getLinkSettleInfo(String id, String extFee, String hotelCode) {
        List<CheckInRecord> cirs = checkInRecordService.findByLinkId(id);
        return createSettleInfo(cirs, extFee, hotelCode);

    }

    private SettleInfoVo createSettleInfo(CheckInRecord cir, String extFee, String hotelCode) {
        SettleInfoVo info = new SettleInfoVo();
        Product product = null;
        if (extFee != null) {
            switch (extFee) {
                case Constants.Type.EXT_FEE_NONE:
                    countSettleInfo(info, cir.getAccount());
                    break;
                case Constants.Type.EXT_FEE_HALF:
                    product = productService.findHalfRoomFee(hotelCode);
                    if (cir.getAccount() != null&&Constants.Type.CHECK_IN_RECORD_CUSTOMER.equals(cir.getType())) {
                        countSettleInfo(info, cir.getAccount());
                        if (cir.getPersonalPrice() != null && cir.getPersonalPrice() != 0.0) {
                            info.getBills().add(createExtFee(cir, product, cir.getPersonalPrice() / 2));
                        }
                    }
                    break;
                case Constants.Type.EXT_FEE_FULL:
                    product = productService.findFullRoomFee(hotelCode);
                    if (cir.getAccount() != null&&Constants.Type.CHECK_IN_RECORD_CUSTOMER.equals(cir.getType())) {
                        countSettleInfo(info, cir.getAccount());
                        if (cir.getPersonalPrice() != null && cir.getPersonalPrice() != 0.0) {
                            info.getBills().add(createExtFee(cir, product, cir.getPersonalPrice()));
                        }
                    }
                    break;
            }
        }
        return info;
    }

    private SettleInfoVo createSettleInfo(List<CheckInRecord> cirs, String extFee, String hotelCode) {
        SettleInfoVo info = new SettleInfoVo();
        Product product = null;
        if (extFee != null) {
            switch (extFee) {
                case Constants.Type.EXT_FEE_NONE:
                    for (CheckInRecord cir : cirs) {
                        if (cir.getAccount() != null) {
                            countSettleInfo(info, cir.getAccount());
                        }
                    }
                    break;
                case Constants.Type.EXT_FEE_HALF:
                    product = productService.findHalfRoomFee(hotelCode);
                    for (CheckInRecord cir : cirs) {
                        if (cir.getAccount() != null&&Constants.Type.CHECK_IN_RECORD_CUSTOMER.equals(cir.getType())) {
                            countSettleInfo(info, cir.getAccount());
                            if (cir.getPersonalPrice() != null && cir.getPersonalPrice() > 0.0) {
                                info.getBills().add(createExtFee(cir, product, cir.getPersonalPrice() / 2));
                            }
                        }
                    }
                    break;
                case Constants.Type.EXT_FEE_FULL:
                    product = productService.findFullRoomFee(hotelCode);
                    for (CheckInRecord cir : cirs) {
                        if (cir.getAccount() != null&&Constants.Type.CHECK_IN_RECORD_CUSTOMER.equals(cir.getType())) {
                            countSettleInfo(info, cir.getAccount());
                            if (cir.getPersonalPrice() != null && cir.getPersonalPrice() > 0.0) {
                                info.getBills().add(createExtFee(cir, product, cir.getPersonalPrice()));
                            }
                        }
                    }
                    break;
            }
        }
        return info;
    }

    @Override
    public List<Account> findByHotelCodeAndType(String hotelCode) {
        List<Account> list = accountDao.findByHotelCodeAndType(hotelCode, Constants.Type.ACCOUNT_INNER);
        return list;
    }
}
