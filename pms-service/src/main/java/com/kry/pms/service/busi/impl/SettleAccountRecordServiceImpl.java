package com.kry.pms.service.busi.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Transient;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
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
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.service.busi.BillService;
import com.kry.pms.service.busi.SettleAccountRecordService;
import com.kry.pms.service.sys.AccountService;
import com.kry.pms.service.sys.BusinessSeqService;

@Service
public class SettleAccountRecordServiceImpl implements SettleAccountRecordService {
    @Autowired
    SettleAccountRecordDao settleAccountRecordDao;
    @Autowired
    BusinessSeqService businessSeqService;
    @Autowired
    AccountService accountService;
    @Autowired
    BillService billService;

    @Override
    public SettleAccountRecord add(SettleAccountRecord settleAccountRecord) {
        if (settleAccountRecord.getStatus() == null) {
            settleAccountRecord.setStatus(Constants.Status.NORMAL);
        }
        if (settleAccountRecord.getHotelCode() == null) {
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
        if (billCheckBo.getMainSettleRecordNum() != null) {
            scr.setRecordNum(billCheckBo.getMainSettleRecordNum());
        } else {
            scr.setRecordNum(businessSeqService.fetchNextSeqNum(billCheckBo.getHotelCode(),
                    Constants.Key.BUSINESS_BUSINESS_SETTLE_SEQ_KEY));
        }
        scr.setShiftCode(billCheckBo.getShiftCode());
        scr.setBusinessDate(businessSeqService.getBuinessDate(billCheckBo.getHotelCode()));
        scr.setAccount(account);
        scr.setHotelCode(billCheckBo.getHotelCode());
        scr.setOperationEmployee(billCheckBo.getOperationEmployee());
        scr.setSettleTime(LocalDateTime.now());
        return add(scr);
    }

    private DtoResponse<String> cancleSettle(String id, String shiftCode, Employee operationEmployee) {
        DtoResponse<String> rep = new DtoResponse<String>();
        SettleAccountRecord sar = findById(id);
        if (sar != null) {
            for (Bill bill : sar.getBills()) {
                bill.setStatus(Constants.Status.BILL_NEED_SETTLED);
                bill.setCurrentSettleAccountRecordNum(null);
                billService.modify(bill);
            }
//            if (sar.getExtBills() != null && !sar.getExtBills().isEmpty()) {
//                for (Bill eb : sar.getExtBills()) {
//                    billService.adjust(eb.getId(), null, false, shiftCode);
//                }
//            }
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
                    rebill.setTargetAccount(fb.getTargetAccount());
                    rebill.setCurrentSettleAccountRecordNum(null);
                    rebill.setStatus(Constants.Status.BILL_INVALID);
                    if (fb.getCost() != null) {
                        rebill.setCost(-fb.getCost());
                    }
                    if (fb.getPay() != null) {
                        rebill.setPay(-fb.getPay());
                    }
                    if (fb.getTotal() != null) {
                        rebill.setTotal(-fb.getTotal());
                    }
                    rebill.setOperationEmployee(operationEmployee);
                    rebill.setShiftCode(shiftCode);
                    billService.add(rebill);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                billService.modify(fb);
            }
        }
        modify(sar);
        return rep;
    }

    @Transactional
    @Override
    public DtoResponse<String> cancle(String id, String shiftCode, Employee operationEmployee) {
        SettleAccountRecord sar = findById(id);
        LocalDate businessDate = businessSeqService.getBuinessDate(operationEmployee.getHotelCode());
        if (sar != null) {
            if (!businessDate.isEqual(sar.getBusinessDate())||!shiftCode.equals(sar.getShiftCode())||!operationEmployee.getId().equals(sar.getOperationEmployee().getId())) {
                DtoResponse<String> rep = new DtoResponse<>();
                rep.error(Constants.BusinessCode.CODE_ILLEGAL_OPERATION,"无法处理（只能撤销本人本班次的结帐）");
                return rep;
            } else {
                sar.setCancleNum(businessSeqService.fetchNextSeqNum(sar.getHotelCode(),
                        Constants.Key.BUSINESS_CANCLE_SETTLE_SEQ_KEY));
                sar.setCancleEmployee(operationEmployee);
                sar.setCancleTime(LocalDateTime.now());
                sar.setCancleShiftCode(shiftCode);
                sar.setStatus(Constants.Status.SETTLE_ACCOUNT_CANCLE);
                if (Constants.Type.BILL_CHECK_WAY_TRANSFER.equals(sar.getSettleWay())) {
                    return cancleTransfer(sar, shiftCode, operationEmployee);
                } else {
                    return cancleSettle(id, shiftCode, operationEmployee);
                }
            }
        }
        DtoResponse<String> rep = new DtoResponse<>();
        rep.error(Constants.BusinessCode.CODE_ILLEGAL_OPERATION,"找不到该结帐信息");
        return rep;
    }

    @Override
    public SettleAccountRecord createARSettle(BillCheckBo billCheckBo, Account account) {
        String recordNum = businessSeqService.fetchNextSeqNum(billCheckBo.getOperationEmployee().getHotelCode(),
                Constants.Key.BUSINESS_BUSINESS_AR_SETTLE_NUM_KEY);
        return create(recordNum,account, account, billCheckBo.getOperationEmployee(), billCheckBo.getShiftCode(),
                billCheckBo.getCheckWay(), billCheckBo.getMainSettleRecordNum());    }

    private DtoResponse<String> cancleTransfer(SettleAccountRecord sar, String shiftCode, Employee operationEmployee) {
        DtoResponse<String> rep = new DtoResponse<String>();
        DtoResponse<List<Bill>> crep = accountService.cancleTransfer(sar.getFlatBills(), sar.getTotal(), sar.getAccount(),
                sar.getTargetAccount(), shiftCode, operationEmployee, sar.getCancleNum());
        for (Bill bill : sar.getBills()) {
            bill.setStatus(Constants.Status.BILL_NEED_SETTLED);
            bill.setTranferRemark(null);
            bill.setCurrentSettleAccountRecordNum(null);
            billService.modify(bill);
        }
//		BeanUtils.copyProperties(crep, rep);
        return rep;
    }

    @Override
    public SettleAccountRecord createToAr(BillCheckBo billCheckBo, Account account, Account targetAccount) {
        SettleAccountRecord scr = new SettleAccountRecord();
        scr.setRecordNum(businessSeqService.fetchNextSeqNum(targetAccount.getHotelCode(),
                Constants.Key.BUSINESS_BUSINESS_TRANSFER_SEQ_KEY));
        scr.setShiftCode(billCheckBo.getShiftCode());
        scr.setAccount(account);
        scr.setBusinessDate(businessSeqService.getBuinessDate(targetAccount.getHotelCode()));
        scr.setTargetAccount(targetAccount);
        scr.setSettleWay(billCheckBo.getCheckWay());
        scr.setHotelCode(account.getHotelCode());
        scr.setOperationEmployee(billCheckBo.getOperationEmployee());
        scr.setSettleTime(LocalDateTime.now());
        return add(scr);
    }

    @Override
    public SettleAccountRecord create(BillCheckBo billCheckBo, Account account, Account targetAccount) {
        String recordNum = businessSeqService.fetchNextSeqNum(billCheckBo.getOperationEmployee().getHotelCode(),
                Constants.Key.BUSINESS_BUSINESS_TRANSFER_SEQ_KEY);
        return create(recordNum,account, targetAccount, billCheckBo.getOperationEmployee(), billCheckBo.getShiftCode(),
                billCheckBo.getCheckWay(), billCheckBo.getMainSettleRecordNum());
    }

    private SettleAccountRecord create(String recordNum,Account account, Account targetAccount, Employee employee, String shiftCode,
                                       String checkWay, String mainRecordNum) {
        SettleAccountRecord scr = new SettleAccountRecord();
        scr.setRecordNum(recordNum);
        scr.setShiftCode(shiftCode);
        if (mainRecordNum != null) {
            scr.setIsSubRecord(true);
            scr.setParentNum(mainRecordNum);
        }
        scr.setAccount(account);
        scr.setSettleWay(checkWay);
        scr.setTargetAccount(targetAccount);
        scr.setHotelCode(employee.getHotelCode());
        scr.setOperationEmployee(employee);
        scr.setBusinessDate(businessSeqService.getBuinessDate(employee.getHotelCode()));
        scr.setSettleTime(LocalDateTime.now());
        return add(scr);
    }

}
