package com.kry.pms.service.busi.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.BosBillDao;
import com.kry.pms.dao.goods.BosBusinessSiteDao;
import com.kry.pms.model.http.request.busi.BosBillCheckBo;
import com.kry.pms.model.persistence.busi.Bill;
import com.kry.pms.model.persistence.busi.BosBill;
import com.kry.pms.model.persistence.busi.BosBillItem;
import com.kry.pms.model.persistence.goods.BosBusinessSite;
import com.kry.pms.model.persistence.goods.Product;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.service.busi.BosBillItemService;
import com.kry.pms.service.busi.BosBillService;
import com.kry.pms.service.goods.ProductService;
import com.kry.pms.service.sys.AccountService;
import com.kry.pms.service.sys.BusinessSeqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class BosBillServiceImpl implements BosBillService {
    @Autowired
    BosBillDao bosBillDao;
    @Autowired
    BosBillItemService bosBillItemService;
    @Autowired
    ProductService productService;
    @Autowired
    BusinessSeqService businessSeqService;
    @Autowired
    AccountService accountService;
    @Autowired
    BosBusinessSiteDao bosBusinessSiteDao;

    @Override
    public String addBosBill(Bill bill){
        if(bill.getProduct() != null){
            List<BosBusinessSite> list = bosBusinessSiteDao.findByHotelCodeAndProductId(bill.getHotelCode(), bill.getProduct().getId());
            if(list != null && !list.isEmpty()){
                BosBill bosBill = BosBill.billToBosBill(bill);
                bosBill = add(bosBill);
                return bosBill.getId();
            }else {
                return null;
            }
        }
        return null;
    }
    @Override
    public BosBill add(BosBill bosBill) {
        String serialNumber = businessSeqService.fetchNextSeqNum(bosBill.getHotelCode(), Constants.Key.SERIAL_NUMBER);
        if(bosBill.getSerialNumber() == null){
            bosBill.setSerialNumber(serialNumber);
        }
        if (bosBill.getProduct() != null && bosBill.getProduct().getId() != null) {
            Product p = productService.findById(bosBill.getProduct().getId());
            if (p != null) {
                if (1 == p.getDirection()) {
                    bosBill.setCost(bosBill.getTotal());
                } else {
                    bosBill.setPay(bosBill.getTotal());
                }
                bosBill.setType(p.getType());
                bosBill.setCreateDate(LocalDateTime.now());
                bosBill.setBusinessDate(businessSeqService.getBuinessDate(p.getHotelCode()));
                if (bosBill.getStatus() == null || Constants.Status.NORMAL.equals(bosBill.getStatus())) {
                    bosBill.setStatus("M");
                }
                if (bosBill.getFeeFlag() == null) {
                    bosBill.setFeeFlag(Constants.Flag.FEE_RES);
                }
            } else {
                return null;
            }
        } else if (Constants.Type.BILL_TYPE_PACKAGE.equals(bosBill.getType())) {
            bosBill.setCost(bosBill.getTotal());
        } else {
            return null;
        }
//        Account account = accountService.bosBillEntry(bosBill);
//        if (account.getRoomNum() != null) {
//            bosBill.setRoomNum(account.getRoomNum());
//            bosBill.setRoomId(account.getRoomId());
//        }
//        bosBill.setBillSeq(account.getCurrentBillSeq());
        if (bosBill.getItems() != null && !bosBill.getItems().isEmpty()) {
            bosBill.getItems().forEach(billItem -> {
                billItem.setHotelCode(bosBill.getHotelCode());
            });
        }
//        bosBill.setAccount(account);
        BosBill b = bosBillDao.saveAndFlush(bosBill);
        return b;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public BosBill modify(BosBill bosBill) {
        //删除原有的
//        List<BosBillItem> old = bosBillItemService.findByBosBillId(bosBill.getId());
//        for(int j=0; j<old.size(); j++){
//            bosBillItemService.deleteTrue(old.get(j).getId());
//        }
        List<BosBillItem> list = bosBill.getItems();
        for(int i=0; i<list.size(); i++){
            BosBillItem bosBillItem = list.get(i);
            bosBillItem.setBosBill(bosBill);
            if(bosBillItem.getStatus() == null){
                bosBillItem.setStatus("M");
            }
            bosBillItem.setHotelCode(bosBill.getHotelCode());
//            bosBillItemService.add(bosBillItem);
        }
        bosBillDao.saveAndFlush(bosBill);
        return bosBill;
    }
    //转房账
    @Override
    public BosBill transferAccount(String bosBillId, String roomNum, String roomId, String accountId) {
        BosBill bosBill = bosBillDao.getOne(bosBillId);
        bosBill.setRoomNum(roomNum);
        bosBill.setRoomId(roomId);
        Account account = new Account();
        account.setId(accountId);
        bosBill.setAccount(account);
        bosBill.setStatus("R");//转房账
        return bosBillDao.saveAndFlush(bosBill);
    }

    @Override
    public List<BosBill> findQuery(String hotelCode,String siteId, String roomNum, String accountCode, String[] statusList) {
        String listIsNumm = null;
        List<String> status = null;
        if(statusList != null){
            status = Arrays.asList(statusList);
            listIsNumm = "yes";
        }
        List<BosBill> list = bosBillDao.findQuery(hotelCode, siteId, roomNum, accountCode, status, listIsNumm);
        return list;
    }

    @Override
    public HttpResponse<BosBill> cancellation(BosBill bosBill) {
        HttpResponse hr = new HttpResponse();
//        bosBill.setId(null);
        if(bosBill.getCost() != null){
            bosBill.setCost(0.0);
        }
        if(bosBill.getPay() != null){
            bosBill.setPay(0.0);
        }
        if(bosBill.getTotal() != null){
            bosBill.setTotal(0.0);
        }
//        bosBill.setStatus("C");
        List<BosBillItem> list = bosBill.getItems();
        for(int i=0; i<list.size(); i++){
            BosBillItem b = list.get(i);
//            b.setId(null);
//            b.setQuantity(-b.getQuantity());
            b.setStatus("C");
            b.setDeleted(1);
            bosBillItemService.modify(b);
        }
        bosBillDao.saveAndFlush(bosBill);
        return hr.ok();
    }

    @Override
    @Transactional
    public HttpResponse check(BosBillCheckBo bosBillCheckBo) {
        HttpResponse hr = new HttpResponse();
        BosBill bosBill = bosBillDao.getOne(bosBillCheckBo.getBosBillId());
        bosBill.setStatus("O");
        String num = businessSeqService.fetchNextSeqNum(bosBillCheckBo.getHotelCode(),
                Constants.Key.BOS_CHECK_NUMBER);
        bosBill.setCurrentSettleAccountRecordNum(num);
        List<BosBill> list = bosBillCheckBo.getBosBills();
        Double total = bosBill.getTotal();
        Double checkTotal = 0.0;
        for(int i=0; i<list.size(); i++){
            checkTotal = checkTotal + list.get(i).getTotal();
        }
        if(total - checkTotal != 0){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return hr.error("账务不平，结账失败");
        }else {
            for(int i=0; i<list.size(); i++){
                list.get(i).setSerialNumber(bosBill.getSerialNumber());
                addFlatBosBill(list.get(i), bosBillCheckBo.getEmployee(), bosBillCheckBo.getShiftCode(), num);
            }
            List<BosBillItem> bbts = bosBill.getItems();
            for(int j=0; j<bbts.size(); j++){
                BosBillItem bosBillItem = bbts.get(j);
                bosBillItem.setStatus("O");
            }
            bosBillDao.saveAndFlush(bosBill);
            return hr.ok();
        }
    }

    @Override
    public BosBill addFlatBosBill(BosBill bosBill, Employee employee, String shiftCode, String orderNum) {
        bosBill.setStatus("O");
        bosBill.setOperationEmployee(employee);
        bosBill.setShiftCode(shiftCode);
        bosBill.setHotelCode(employee.getHotelCode());
        bosBill.setCurrentSettleAccountRecordNum(orderNum);
        bosBill = add(bosBill);
        return bosBill;
    }

    @Override
    public boolean updateStatus(String bosBillId){
        BosBill bosBill = bosBillDao.getOne(bosBillId);
        bosBill.setStatus("M");
        try {
            bosBillDao.saveAndFlush(bosBill);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public BosBill findById(String id) {
        return null;
    }

    @Override
    public List<BosBill> getAllByHotelCode(String code) {
        return null;
    }

    @Override
    public PageResponse<BosBill> listPage(PageRequest<BosBill> prq) {
        return null;
    }
}
