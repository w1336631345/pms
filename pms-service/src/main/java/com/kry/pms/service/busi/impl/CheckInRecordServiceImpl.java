package com.kry.pms.service.busi.impl;

import com.kry.pms.base.*;
import com.kry.pms.dao.busi.CheckInRecordDao;
import com.kry.pms.dao.busi.RoomLinkDao;
import com.kry.pms.dao.busi.RoomRecordDao;
import com.kry.pms.dao.marketing.RoomPriceSchemeDao;
import com.kry.pms.model.annotation.UpdateAnnotation;
import com.kry.pms.model.func.UseInfoAble;
import com.kry.pms.model.http.request.busi.*;
import com.kry.pms.model.http.response.busi.AccountSummaryVo;
import com.kry.pms.model.http.response.busi.CheckInRecordListVo;
import com.kry.pms.model.other.wrapper.CheckInRecordWrapper;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.DailyVerify;
import com.kry.pms.model.persistence.busi.RoomLink;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.model.persistence.goods.SetMeal;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.marketing.RoomPriceScheme;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.RoomType;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.mq.OrderMqService;
import com.kry.pms.service.busi.*;
import com.kry.pms.service.guest.CustomerService;
import com.kry.pms.service.log.UpdateLogService;
import com.kry.pms.service.msg.MsgSendService;
import com.kry.pms.service.msg.impl.MsgSendServiceImpl;
import com.kry.pms.service.org.EmployeeService;
import com.kry.pms.service.room.*;
import com.kry.pms.service.sys.*;
import com.kry.pms.service.util.BeanChangeUtil;
import com.kry.pms.service.util.UpdateUtil;
import freemarker.template.TemplateException;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class CheckInRecordServiceImpl implements CheckInRecordService {
    @Autowired
    OrderMqService orderMqService;
    @Autowired
    CheckInRecordDao checkInRecordDao;
    @Autowired
    RoomRecordService roomRecordService;
    @Autowired
    GuestRoomService guestRoomService;
    @Autowired
    CustomerService customerService;
    @Autowired
    RoomStatisticsService roomStatisticsService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    RoomUsageService roomUsageService;
    @Autowired
    BusinessSeqService businessSeqService;
    @Autowired
    RoomTypeService roomTypeService;
    @Autowired
    AccountService accountService;
    @Autowired
    RoomLinkDao roomLinkDao;
    @Autowired
    SqlTemplateService sqlTemplateService;
    @Autowired
    RoomPriceSchemeDao roomPriceSchemeDao;
    @Autowired
    GuestRoomStatusService guestRoomStatusService;
    @Autowired
    BeanChangeUtil beanChangeUtil;
    @Autowired
    UpdateLogService updateLogService;
    @Autowired
    DateTimeService dateTimeService;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    BillService billService;
    @Autowired
    DailyVerifyService dailyVerifyService;
    @Autowired
    RoomRecordDao roomRecordDao;
    @Autowired
    ReceptionService receptionService;
    @Autowired
    MsgSendService msgSendService;
    @Autowired
    CreditGrantingRecordService creditGrantingRecordService;

    @Override
    public CheckInRecord add(CheckInRecord checkInRecord) {
        return checkInRecordDao.saveAndFlush(checkInRecord);
    }

    @Override
    public void delete(String id) {
        CheckInRecord checkInRecord = checkInRecordDao.findById(id).get();
        if (checkInRecord != null) {
            checkInRecord.setDeleted(Constants.DELETED_TRUE);
        }
        modify(checkInRecord);
    }

    @Override
    public CheckInRecord modify(CheckInRecord checkInRecord) {
        CheckInRecord dbCir = findById(checkInRecord.getId());
        if (dbCir != null) {
            checkInRecord.setMainRecord(dbCir.getMainRecord());
            if (checkInRecord.getCustomer() != null) {
                Customer customer = customerService.modify(checkInRecord.getCustomer());
                checkInRecord.setCustomer(customer);
            }
            return checkInRecordDao.saveAndFlush(checkInRecord);
        }
        return null;
    }

    @Override
    @Transactional
    @UpdateAnnotation(name = "?????????", value = "orderNum", type = "GO")
    public HttpResponse modifyInfo(CheckInRecord checkInRecord) {
        HttpResponse hr = new HttpResponse();
        CheckInRecord dbCir = checkInRecordDao.getOne(checkInRecord.getId());
//        CheckInRecord oldCir = new CheckInRecord();
//        BeanUtils.copyProperties(dbCir, oldCir);
        boolean updateRoomPriceS = false;
        boolean updateName = false;
        boolean updateTime = false;
        boolean isSecrecy = false;
        boolean personalPrice = false;
        if ((Constants.Type.CHECK_IN_RECORD_RESERVE).equals(dbCir.getType())) {
            return hr.error("???????????????????????????????????????");
        }
        //???????????????????????????
        if (!dbCir.getRoomPriceScheme().getId().equals(checkInRecord.getRoomPriceScheme().getId())) {
            updateRoomPriceS = true;
        }
        //????????????????????????
        if (!dbCir.getName().equals(checkInRecord.getName())) {
            updateName = true;
            checkInRecord.setGroupName(checkInRecord.getName());
        }
        LocalTime criticalTime = checkInRecord.getArriveTime().toLocalTime();
        LocalDate startDate = checkInRecord.getArriveTime().toLocalDate();
        int days = checkInRecord.getDays();
        //????????????????????????
        if (!dbCir.getArriveTime().isEqual(checkInRecord.getArriveTime()) || !dbCir.getLeaveTime().isEqual(checkInRecord.getLeaveTime())) {
            updateTime = true;
            criticalTime = systemConfigService.getCriticalTime(checkInRecord.getHotelCode());
            if (checkInRecord.getArriveTime().toLocalTime().isBefore(criticalTime)) {
                startDate = startDate.plusDays(-1);
            }
            checkInRecord.setStartDate(startDate);
            //?????????????????????roomRecord
            days = (int) checkInRecord.getArriveTime().toLocalDate()
                    .until(checkInRecord.getLeaveTime().toLocalDate(), ChronoUnit.DAYS);
            if (checkInRecord.getArriveTime().toLocalTime().isBefore(criticalTime)) {
                days = days + 1;
            }
            checkInRecord.setDays(days);
        }
        if (checkInRecord.getIsSecrecy() != null) {
            if (!checkInRecord.getIsSecrecy().equals(dbCir.getIsSecrecy())) {
                isSecrecy = true;
            }
        }
        // ??????????????????????????????????????????????????????
        if (("G").equals(dbCir.getType())) {
            // ??????????????????????????????
            List<CheckInRecord> children = checkInRecordDao.findByMainRecordAndDeleted(dbCir, Constants.DELETED_FALSE);
            checkInRecord.setCustomer(null);
            if (updateName) {//??????????????????
                if (checkInRecord.getAccount() != null) {
                    Account account = accountService.findById(checkInRecord.getAccount().getId());//????????????????????????????????????
                    account.setName(checkInRecord.getName());
                    accountService.modify(account);
                }
            }

            for (int i = 0; i < children.size(); i++) {
                CheckInRecord cir = children.get(i);
                if (updateRoomPriceS) {//????????????????????????
                    Map<String, Object> map = roomPriceSchemeDao.roomTypeAndPriceScheme(cir.getRoomType().getId(), checkInRecord.getRoomPriceScheme().getId());
                    Double price = MapUtils.getDouble(map, "price");
                    if (price == null) {
                        price = cir.getRoomType().getPrice();
                    }
                    if(!cir.getPersonalPrice().equals(price * cir.getPersonalPercentage())){
                        //????????????????????????????????????????????????????????????????????????roomRecord??????????????????
//                        List<RoomRecord> list = roomRecordService.findByHotelCodeAndCheckInRecord(cir.getHotelCode(), cir.getId());
                        List<RoomRecord> list = roomRecordService.findByCheckInRecordAndIsAccountEntry(cir.getId(), "NO");
                        for(int r=0; r<list.size(); r++){
                                list.get(r).setCost(price * cir.getPersonalPercentage());
                                roomRecordDao.saveAndFlush(list.get(r));
                        }
                    }
                    //???????????????????????????????????????
                    cir.setRoomPriceScheme(checkInRecord.getRoomPriceScheme());
                    if (!Constants.Type.CHECK_IN_RECORD_RESERVE.equals(cir.getType())) {
                        cir.setPurchasePrice(price);
                        cir.setPersonalPrice(price * cir.getPersonalPercentage());
                    }
                    String setMealId = MapUtils.getString(map, "setMealId");
                    if (setMealId != null) {
                        SetMeal sm = new SetMeal();
                        sm.setId(setMealId);
                        //????????????????????????????????????
                        cir.setSetMeal(sm);
                    } else {
                        cir.setSetMeal(null);
                    }
                }
                if (updateName) {//??????????????????
                    cir.setGroupName(checkInRecord.getName());
                }
                if (updateTime) {
//                    if (checkInRecord.getArriveTime().isAfter(cir.getArriveTime()) || checkInRecord.getLeaveTime().isBefore(cir.getLeaveTime())) {
//                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                        return hr.error("????????????????????????????????????????????????");
//                    }
                    //?????????????????????????????????????????????????????????????????????
                    //?????????????????????...
                    if("Y".equals(checkInRecord.getIsSync())){
                        boolean b = roomStatisticsService.extendTime(new CheckInRecordWrapper(cir),
                                checkInRecord.getArriveTime(), checkInRecord.getLeaveTime());
                        if (!b) {
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                            return hr.error("????????????");
                        }
                        cir.setArriveTime(checkInRecord.getArriveTime());
                        cir.setLeaveTime(checkInRecord.getLeaveTime());
                        cir.setDays(checkInRecord.getDays());
                        cir.setStartDate(checkInRecord.getStartDate());
                        List<RoomRecord> list = roomRecordService.findByHotelCodeAndCheckInRecord(cir.getHotelCode(), cir.getId());
                        List<LocalDate> recordDates = new ArrayList<>();
                        for (int j = 0; j < days; j++) {
                            recordDates.add(cir.getStartDate().plusDays(j));
                        }
                        for (int r = 0; r < list.size(); r++) {
                            //??????????????????????????????????????????????????????????????????????????????
                            if(recordDates.contains(list.get(r).getRecordDate())){
                                //?????????????????????
                                recordDates.remove(list.get(r).getRecordDate());
                            }else {
                                roomRecordService.deleteTrue(list.get(r).getId());
                            }
                        }
                        for(int m = 0; m<recordDates.size(); m++){
                            RoomRecord rr = null;
                            rr = new RoomRecord();
                            rr.setCheckInRecord(cir);
                            rr.setHotelCode(cir.getHotelCode());
                            rr.setCustomer(cir.getCustomer());
                            rr.setGuestRoom(cir.getGuestRoom());
                            rr.setCost(cir.getPersonalPrice());
                            rr.setCostRatio(cir.getPersonalPercentage());
                            rr.setRecordDate(recordDates.get(m));
                            roomRecordService.add(rr);
                        }
//                        roomRecordService.createRoomRecord(cir);
                    }else {
                        if (checkInRecord.getArriveTime().isAfter(cir.getArriveTime()) || checkInRecord.getLeaveTime().isBefore(cir.getLeaveTime())) {
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                            return hr.error("????????????????????????????????????????????????");
                        }
                    }
                    //?????????????????????...
                }
                if (isSecrecy) {
                    cir.setIsSecrecy(checkInRecord.getIsSecrecy());
                }
                cir.setCorp(checkInRecord.getCorp());
                checkInRecordDao.saveAndFlush(cir);
                boolean b = roomStatisticsService.updateGuestRoomStatus(new CheckInRecordWrapper(cir));
                if (!b) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return hr.error("???????????????????????????");
                }
            }
        } else {//??????????????????????????????
            if (!dbCir.getPersonalPrice().equals(checkInRecord.getPersonalPrice())) {//????????????
                personalPrice = true;
                //????????????????????????????????????????????????????????????????????????roomRecord??????????????????
//                List<RoomRecord> list = roomRecordService.findByHotelCodeAndCheckInRecord(checkInRecord.getHotelCode(), checkInRecord.getId());
                List<RoomRecord> list = roomRecordService.findByCheckInRecordAndIsAccountEntry(checkInRecord.getId(), "NO");
                for (int r = 0; r < list.size(); r++) {
                    RoomRecord rr = list.get(r);
                    rr.setCost(checkInRecord.getPersonalPrice());
                    roomRecordService.modify(rr);
                }
            }
            checkInRecord.setMainRecord(dbCir.getMainRecord());
            if (checkInRecord.getCustomer() != null) {
                if (checkInRecord.getCustomer().getId() != null) {
                    Customer c = customerService.findById(checkInRecord.getCustomer().getId());
                    if (c != null) {
                        if (!dbCir.getName().equals(c.getName())) {
                            checkInRecord.setName(c.getName());
                            if (checkInRecord.getAccount() != null) {
                                Account account = accountService.findById(checkInRecord.getAccount().getId());//????????????????????????????????????
                                account.setCustomer(c);
                                account.setName(c.getName());
                                accountService.modify(account);
                            }
                        }
                    }
                }
            }
            if (updateTime) {
                if (dbCir.getMainRecord() != null) {
                    CheckInRecord main = dbCir.getMainRecord();
                    if (checkInRecord.getArriveTime().isBefore(main.getArriveTime()) || checkInRecord.getLeaveTime().isAfter(main.getLeaveTime())) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return hr.error("????????????????????????????????????");
                    }
                }
                boolean b = roomStatisticsService.extendTime(new CheckInRecordWrapper(dbCir),
                        checkInRecord.getArriveTime(), checkInRecord.getLeaveTime());
                if (!b) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return hr.error("????????????");
                }
                //???????????????????????????
//                List<RoomRecord> list = roomRecordService.findByHotelCodeAndCheckInRecord(checkInRecord.getHotelCode(), checkInRecord.getId());
//                for (int r = 0; r < list.size(); r++) {
//                    roomRecordService.deleteTrue(list.get(r).getId());
//                }
//                checkInRecord.setDays(days);
//                roomRecordService.createRoomRecord(checkInRecord);
                //??????????????????????????????????????????
                List<CheckInRecord> together = checkInTogether(checkInRecord.getHotelCode(), checkInRecord.getOrderNum(), checkInRecord.getGuestRoom().getId());
                for (int t = 0; t < together.size(); t++) {
                    List<RoomRecord> list = roomRecordService.findByHotelCodeAndCheckInRecord(checkInRecord.getHotelCode(), together.get(t).getId());
                    List<LocalDate> recordDates = new ArrayList<>();
                    for (int j = 0; j < days; j++) {
                        recordDates.add(checkInRecord.getStartDate().plusDays(j));
                    }
                    for (int r = 0; r < list.size(); r++) {
                        //??????????????????????????????????????????????????????????????????????????????
                        if(recordDates.contains(list.get(r).getRecordDate())){
                            //?????????????????????
                            recordDates.remove(list.get(r).getRecordDate());
                        }else {
                            roomRecordService.deleteTrue(list.get(r).getId());
                        }
                    }
                    together.get(t).setDays(days);
                    together.get(t).setArriveTime(checkInRecord.getArriveTime());
                    together.get(t).setLeaveTime(checkInRecord.getLeaveTime());
                    together.get(t).setStartDate(checkInRecord.getStartDate());
                    checkInRecordDao.saveAndFlush(together.get(t));
                    for(int m = 0; m<recordDates.size(); m++){
                        RoomRecord rr = null;
                        rr = new RoomRecord();
//                        rr.setCheckInRecord(checkInRecord);
                        rr.setCheckInRecord(together.get(t));
                        rr.setHotelCode(together.get(t).getHotelCode());
                        rr.setCustomer(together.get(t).getCustomer());
                        rr.setGuestRoom(together.get(t).getGuestRoom());
                        rr.setCost(together.get(t).getPersonalPrice());
                        rr.setCostRatio(together.get(t).getPersonalPercentage());
                        rr.setRecordDate(recordDates.get(m));
                        roomRecordService.add(rr);
                    }
//                    roomRecordService.createRoomRecord(together.get(t));
                }
            }
        }
//        String str = beanChangeUtil.contrastObj(oldCir, checkInRecord);
//        if (str.equals("")) {
//            System.out.println("????????????");
//        } else {
//            System.out.println(str);
//        }
        hr.addData(checkInRecordDao.saveAndFlush(checkInRecord));
        boolean b = roomStatisticsService.updateGuestRoomStatus(new CheckInRecordWrapper(checkInRecord));
        if (!b) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return hr.error("???????????????????????????");
        }
        return hr.ok();
    }

    private void updateCustomer(CheckInRecord dbCir, CheckInRecord cir) {
        if (cir.getGuestRoom() != null && cir.getCustomer() != null) {
            if (dbCir.getCustomer() != null && dbCir.getCustomer().getId().equals(cir.getCustomer().getId())) {
                guestRoomStatusService.updateSummary(cir.getGuestRoom(), dbCir.getCustomer().getName(),
                        cir.getCustomer().getName());
            }
        }
    }

    @Override
    public CheckInRecord update(CheckInRecord checkInRecord) {
        return checkInRecordDao.save(checkInRecord);
    }

    @Override
    @UpdateAnnotation(name = "?????????", value = "orderNum", type = "GO")
    public CheckInRecord updateLog(CheckInRecord checkInRecord) {
        return checkInRecordDao.saveAndFlush(checkInRecord);
    }

    @Override
    @Transactional
    public HttpResponse updateAll(CheckUpdateItemTestBo checkUpdateItemTestBo) {
        HttpResponse hr = new HttpResponse();
        String[] ids = checkUpdateItemTestBo.getIds();
        List<String> rooms = new ArrayList<>();
        boolean roomI = true;
        for (int i = 0; i < ids.length; i++) {
            CheckInRecord cir = findById(ids[i]);
            if (checkUpdateItemTestBo.getPersonalPrice() != null) {
                if (!checkUpdateItemTestBo.getPersonalPrice().equals(cir.getPersonalPrice())) {//???????????????
                    List<RoomRecord> list = roomRecordService.findByHotelCodeAndCheckInRecord(cir.getHotelCode(), cir.getId());
                    for (int r = 0; r < list.size(); r++) {
                        RoomRecord rr = list.get(r);
                        rr.setCost(checkUpdateItemTestBo.getPersonalPrice());
                        roomRecordService.modify(rr);
                    }
                }
            }
            GuestRoom gr = cir.getGuestRoom();
            if (rooms.contains(gr.getRoomNum())) {//????????????????????????????????????????????????????????????
                roomI = false;
            } else {
                rooms.add(gr.getRoomNum());
                roomI = true;
            }
            String oldRemark = cir.getRemark();
            String newRemark = checkUpdateItemTestBo.getRemark();
            if (oldRemark != null) {
                if (newRemark != null) {
                    newRemark = oldRemark + "," + newRemark;
                } else {
                    newRemark = oldRemark;
                }
            }
            //*************???????????????????????????*************
            CheckInRecord cirLog = new CheckInRecord();
            BeanUtils.copyProperties(checkUpdateItemTestBo, cirLog);
            cirLog.setId(ids[i]);
            cirLog.setRemark(newRemark);
            cirLog.setOrderNum(cir.getOrderNum());
            cirLog.setHotelCode(cir.getHotelCode());
            cirLog.setAccount(cir.getAccount());
            updateLogService.updateCirAllLog(cirLog);
            //*************???????????????????????????*************
            cir.setRemark(newRemark);
            boolean updateTime = false;
            LocalDateTime at = checkUpdateItemTestBo.getArriveTime();
            LocalDateTime lt = checkUpdateItemTestBo.getLeaveTime();
            if (at == null || "".equals(at)) {
                at = cir.getArriveTime();
            }
            if (lt == null || "".equals(lt)) {
                lt = cir.getLeaveTime();
            }
            LocalDate startDate = at.toLocalDate();
            LocalTime criticalTime = systemConfigService.getCriticalTime(cir.getHotelCode());
            if (!cir.getArriveTime().isEqual(at) || !cir.getLeaveTime().isEqual(lt)) {
                updateTime = true;
                if (roomI) {
                    boolean b = roomStatisticsService.extendTime(new CheckInRecordWrapper(cir), at, lt);
                    if (!b) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return hr.error("????????????");
                    }
                }
                if (at.toLocalTime().isBefore(criticalTime)) {
                    startDate = startDate.plusDays(-1);
                }
                cir.setStartDate(startDate);
            }
            UpdateUtil.copyNullProperties(checkUpdateItemTestBo, cir);
            if (updateTime) {
                if (cir.getMainRecord() != null) {
                    CheckInRecord main = cir.getMainRecord();
                    if (at.isBefore(main.getArriveTime()) || lt.isAfter(main.getLeaveTime())) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return hr.error("????????????????????????????????????");
                    }
                }
//                boolean b = roomStatisticsService.extendTime(new CheckInRecordWrapper(cir), at, lt);
//                if (!b) {
//                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                    return hr.error("????????????");
//                }
                //?????????????????????roomRecord
                int days = (int) at.toLocalDate()
                        .until(lt.toLocalDate(), ChronoUnit.DAYS);
                if (cir.getArriveTime().toLocalTime().isBefore(criticalTime)) {
                    days = days + 1;
                }
                cir.setDays(days);
                List<RoomRecord> list = roomRecordService.findByHotelCodeAndCheckInRecord(cir.getHotelCode(), cir.getId());
                List<LocalDate> recordDates = new ArrayList<>();
                for (int j = 0; j < days; j++) {
                    recordDates.add(cir.getStartDate().plusDays(j));
                }
                for (int r = 0; r < list.size(); r++) {
                    //??????????????????????????????????????????????????????????????????????????????
                    if(recordDates.contains(list.get(r).getRecordDate())){
                        //?????????????????????
                        recordDates.remove(list.get(r).getRecordDate());
                    }else {
                        roomRecordService.deleteTrue(list.get(r).getId());
                    }
                }
                for(int m = 0; m<recordDates.size(); m++){
                    RoomRecord rr = null;
                    rr = new RoomRecord();
                    rr.setCheckInRecord(cir);
                    rr.setHotelCode(cir.getHotelCode());
                    rr.setCustomer(cir.getCustomer());
                    rr.setGuestRoom(cir.getGuestRoom());
                    rr.setCost(cir.getPersonalPrice());
                    rr.setCostRatio(cir.getPersonalPercentage());
                    rr.setRecordDate(recordDates.get(m));
                    roomRecordService.add(rr);
                }
//                roomRecordService.createRoomRecord(cir);
                //***********?????????************
                //????????????????????????????????????
                if (roomI) {
                    List<CheckInRecord> together = checkInTogether(cir.getHotelCode(), cir.getOrderNum(), cir.getGuestRoom().getId());
                    for (int t = 0; t < together.size(); t++) {
                        List<RoomRecord> listT = roomRecordService.findByHotelCodeAndCheckInRecord(cir.getHotelCode(), together.get(t).getId());
                        List<LocalDate> recordDates2 = new ArrayList<>();
                        for (int j = 0; j < days; j++) {
                            recordDates2.add(together.get(t).getStartDate().plusDays(j));
                        }
                        for (int r = 0; r < listT.size(); r++) {
                            //??????????????????????????????????????????????????????????????????????????????
                            if(recordDates2.contains(listT.get(r).getRecordDate())){
                                //?????????????????????
                                recordDates2.remove(listT.get(r).getRecordDate());
                            }else {
                                roomRecordService.deleteTrue(listT.get(r).getId());
                            }
                        }

                        together.get(t).setDays(days);
                        together.get(t).setStartDate(cir.getStartDate());
                        together.get(t).setArriveTime(at);
                        together.get(t).setLeaveTime(lt);
                        checkInRecordDao.saveAndFlush(together.get(t));
                        for(int m = 0; m<recordDates2.size(); m++){
                            RoomRecord rr = null;
                            rr = new RoomRecord();
                            rr.setCheckInRecord(together.get(t));
                            rr.setHotelCode(together.get(t).getHotelCode());
                            rr.setCustomer(together.get(t).getCustomer());
                            rr.setGuestRoom(together.get(t).getGuestRoom());
                            rr.setCost(together.get(t).getPersonalPrice());
                            rr.setCostRatio(together.get(t).getPersonalPercentage());
                            rr.setRecordDate(recordDates2.get(m));
                            roomRecordService.add(rr);
                        }
//                        roomRecordService.createRoomRecord(together.get(t));
                    }
                }
                //????????????????????????????????????
                //***********?????????************
            }
            checkInRecordDao.save(cir);
            roomStatisticsService.updateGuestRoomStatus(new CheckInRecordWrapper(cir));
        }
        return hr;
    }

    //????????????????????????????????????????????????????????????
    @Override
    @UpdateAnnotation(name = "?????????", value = "orderNum", type = "GO")
    public HttpResponse updateAllLog(CheckInRecord checkInRecord) {
        HttpResponse hr = new HttpResponse();
        hr.addData(checkInRecord);
        return hr;
    }

    @Transactional
    @Override
    public HttpResponse cancelIn(String[] ids, String hotelCode) {
        HttpResponse hr = new HttpResponse();
        LocalDate businessDate = businessSeqService.getBuinessDate(hotelCode);
        for (int i = 0; i < ids.length; i++) {
            CheckInRecord cir = findById(ids[i]);
            LocalDate arriveTime = cir.getArriveTime().toLocalDate();
            if (!businessDate.isEqual(arriveTime)) {
                return hr.error("????????????????????????????????????????????????");
            }
            if (!Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN.equals(cir.getStatus())) {
                return hr.error("???????????????????????????????????????");
            }
            //?????????????????????????????????????????????????????????
            cir.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);
            checkInRecordDao.save(cir);
            if (Constants.Type.CHECK_IN_RECORD_CUSTOMER.equals(cir.getType())) {
                //????????????????????????????????????????????????????????????????????????????????????
                List<CheckInRecord> tList = checkInRecordDao.findByOrderNumAndGuestRoomAndDeletedAndStatus(
                        cir.getOrderNum(), cir.getGuestRoom(), Constants.DELETED_FALSE, Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN);
                if(tList == null || tList.isEmpty()){//??????????????????????????????????????????
                    UseInfoAble info = new CheckInRecordWrapper(cir);
                    boolean result = roomStatisticsService.cancleCheckIn(info);
                    if (!result) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return hr.error("???????????????????????????");
                    }
                    //????????????????????????????????????????????????cir?????????????????????????????????????????????????????????????????????????????????????????????????????????
                    //???????????????????????????????????????????????????????????????????????????cirId???uniqueId??????????????????????????????????????????????????????
                    //?????????????????????????????????????????????
                    //???????????????1????????????????????????cirId??????????????????uniqueId???????????????????????????uniqueId???????????????????????????cirId
                    //???????????????2???
                }else {
                    roomUsageService.updateUniqueId(cir.getId(), tList.get(0).getId());
                }
            }
        }
        return hr;
    }

    @Override
    public CheckInRecord findById(String id) {
        return checkInRecordDao.getOne(id);
    }

    @Override
    public CheckInRecord logFindById(String id) {
        CheckInRecord checkInRecord = checkInRecordDao.logFindById(id);
        return checkInRecord;
    }

    @Override
    public List<CheckInRecord> getAllByHotelCode(String code) {
        return null;// ???????????????
        // return checkInRecordDao.findByHotelCode(code);
    }

    @Override
    public PageResponse<CheckInRecord> listPage(PageRequest<CheckInRecord> prq) {
        Example<CheckInRecord> ex = Example.of(prq.getExb());
        org.springframework.data.domain.PageRequest req;
        if (prq.getOrderBy() != null) {
            Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
            req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
        } else {
            req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
        }
        return convent(checkInRecordDao.findAll(ex, req));
    }

    @Override
    public void checkIn(CheckInBo checkInBo, DtoResponse<List<CheckInRecord>> rep) {
        for (CheckInItemBo ciib : checkInBo.getItems()) {
            GuestRoom gr = guestRoomService.findById(ciib.getRoomId());
            LocalDateTime time = LocalDateTime.now();
            ArrayList<CheckInRecord> list = createCheckInRecord(ciib, gr, time);
            // TODO check is group
//			roomStatisticsService.checkIn(gr, time, ciib.getDays(), false);

        }
    }

    private ArrayList<CheckInRecord> createCheckInRecord(CheckInItemBo ciib, GuestRoom gr, LocalDateTime time) {
        ArrayList<CheckInRecord> list = new ArrayList<>();
        CheckInRecord cir = null;
        Customer customer = null;
        LocalDate startDate = time.toLocalDate();
        StringBuilder sb = new StringBuilder();
        LocalTime criticalTime = systemConfigService.getCriticalTime(gr.getHotelCode());
        if (time.toLocalTime().isBefore(criticalTime)) {
            startDate = startDate.plusDays(-1);
        }
        for (int i = 0; i < ciib.getGuests().size(); i++) {
            cir = new CheckInRecord();
            cir.setArriveTime(time);
            ciib.setHotelCode(gr.getHotelCode());
            BeanUtils.copyProperties(ciib, cir);
            customer = customerService.createOrGetCustomer(gr.getHotelCode(), ciib.getGuests().get(i));
            cir.setCustomer(customer);
            cir.setGuestRoom(gr);
            cir.setStartDate(startDate);
            list.add(add(cir));
            roomRecordService.createRoomRecord(cir);
            sb.append(customer.getGuestInfo().getName());
            sb.append(" ");
        }
        return list;
    }

    @Transactional
    @Override
    public DtoResponse<List<CheckInRecord>> checkOut(String type, String id, String orderNum) {
        switch (type) {
            case Constants.Type.SETTLE_TYPE_ACCOUNT:
                System.out.println("????????????");
                return checkOutAccout(id);
            case Constants.Type.SETTLE_TYPE_PART:
                DtoResponse<List<CheckInRecord>> response = new DtoResponse<>();
                response.setCode(Constants.BusinessCode.CODE_PARAMETER_INVALID);
                response.setMessage("????????????????????????");
                return response;
            case Constants.Type.SETTLE_TYPE_GROUP:
                return checkOutGroup(id);
            case Constants.Type.SETTLE_TYPE_IGROUP:
                return checkOutGroup(id);
            case Constants.Type.SETTLE_TYPE_ROOM:
                return checkOutRoom(id, orderNum);
            case Constants.Type.SETTLE_TYPE_LINK:
                return checkOutLink(id);
            default:
                DtoResponse<List<CheckInRecord>> rep = new DtoResponse<>();
                rep.setCode(Constants.BusinessCode.CODE_PARAMETER_INVALID);
                rep.setMessage("????????????????????????");
                return rep;

        }
    }

    private DtoResponse<List<CheckInRecord>> checkOutLink(String id) {
        List<CheckInRecord> cirs = findByLinkId(id);
        return checkOut(cirs);
    }

    private DtoResponse<List<CheckInRecord>> checkOutRoom(String id, String orderNum) {
        GuestRoom guestRoom = guestRoomService.findById(id);
        if (guestRoom != null) {
            return checkOut(findByOrderNumAndGuestRoomAndDeleted(orderNum, guestRoom, Constants.DELETED_FALSE));
        } else {
            return null;
        }
    }

    private DtoResponse<List<CheckInRecord>> checkOutGroup(String id) {
        CheckInRecord cir = queryByAccountId(id);
        checkOut(cir);
        DtoResponse<List<CheckInRecord>> rep = checkOut(findByOrderNumC(cir.getHotelCode(), cir.getOrderNum()));
        if (rep.getStatus() != 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return rep;
    }

    private DtoResponse<List<CheckInRecord>> checkOutAccout(String id) {
        CheckInRecord cir = queryByAccountId(id);
        DtoResponse<List<CheckInRecord>> response = new DtoResponse<>();
        List<CheckInRecord> data = new ArrayList<>();
        DtoResponse<CheckInRecord> rep = checkOut(cir);
        if (rep.getStatus() != 0) {
            System.out.println("1111111status!=0");
            BeanUtils.copyProperties(rep, response);
        } else {
            System.out.println("2222222status??????0");
            List<CheckInRecord> togethers = checkInTogetherByStatus(cir.getHotelCode(), cir.getOrderNum(), cir.getGuestRoom().getId(), Arrays.asList(Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN,
                    Constants.Status.CHECKIN_RECORD_STATUS_ASSIGN));
            if (togethers != null) {
                System.out.println("222221111status1??????");
                rep.setStatus(1);
            }
            data.add(rep.getData());
        }
        return response.addData(data);
    }

    private DtoResponse<List<CheckInRecord>> checkOut(List<CheckInRecord> cirs) {
        DtoResponse<List<CheckInRecord>> response = new DtoResponse<>();
        List<CheckInRecord> data = new ArrayList<>();
        for (CheckInRecord cir : cirs) {
            if (Constants.Status.CHECKIN_RECORD_STATUS_CHECK_OUT.equals(cir.getStatus())) {
                continue;
            }
            DtoResponse<CheckInRecord> rep = checkOut(cir);
            if (rep.getStatus() != 0) {
                BeanUtils.copyProperties(rep, response);
                break;
            } else {
                data.add(rep.getData());
            }
        }
        if (response.getStatus() != 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        response.setData(data);
        return response;
    }

    private DtoResponse<CheckInRecord> checkOut(CheckInRecord cir) {
        DtoResponse<CheckInRecord> rep = new DtoResponse<>();
        if (cir == null) {
            rep.setStatus(Constants.BusinessCode.CODE_ILLEGAL_OPERATION);
            rep.setMessage("11???????????????");
        } else if (cir.getAccount() != null) {
            if (!accountService.accountCheckAndSettledZeroBill(cir.getAccount())) {
                rep.setStatus(Constants.BusinessCode.CODE_ILLEGAL_OPERATION);
                rep.setMessage(cir.getAccount().getCode() + ":??????????????????");
            } else {
                if (Constants.Status.CHECKIN_RECORD_STATUS_OUT_UNSETTLED.equals(cir.getStatus())) {
                    System.out.println("1122=====??????S status");
                    cir.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_CHECK_OUT);
//                    creditGrantingRecordService.disableAllCreditGranting(cir.getAccount());
                } else if (Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION.equals(cir.getStatus())) {
                    System.out.println("1133=====??????R status");
                    //S?????? ???????????????????????????
                } else {
                    System.out.println("1144===?????????????????? status");
                    cir.setActualTimeOfLeave(LocalDateTime.now());
                    boolean b = roomStatisticsService.checkOut(new CheckInRecordWrapper(cir));
                    if (!b) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        rep.setStatus(Constants.BusinessCode.CODE_ILLEGAL_OPERATION);
                        rep.setMessage(cir.getAccount().getCode() + ":???????????????");
                        return rep;
                    }
                    cir.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_CHECK_OUT);
                    checkRoomRecord(cir);
                }

                modify(cir);
            }
        } else {
            rep.setStatus(Constants.BusinessCode.CODE_ILLEGAL_OPERATION);
            rep.setMessage("??????????????????");
        }
        rep.setData(cir);
        return rep;
    }

    private void checkRoomRecord(CheckInRecord cir){
        LocalDate actualLeaveDate = dateTimeService.getStartDate(cir.getHotelCode(), cir.getActualTimeOfLeave());
        if (cir.getLeaveTime().toLocalDate().isAfter(actualLeaveDate)) {
            roomRecordService.deletedRecordAfter(cir.getId(), actualLeaveDate);
        }
    }

    @Override
    public CheckInRecord checkInByTempName(String tempName, String roomId, DtoResponse<String> response) {
        GuestRoom gr = guestRoomService.findById(roomId);
        if (gr != null) {
            Customer customer = customerService.createTempCustomer(gr.getHotelCode(), tempName);

        } else {

        }

        return null;
    }

    @Override
    public List<CheckInRecord> findByBookId(String bookId) {
        return checkInRecordDao.fingByBookId(bookId);
    }

    @Override
    public List<CheckInRecord> findDetailByBookingId(String bookId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse checkInByTempName(int humanCount, CheckInRecord cir, GuestRoom gr,
                                          DtoResponse<String> response) {//humanCount???????????????
        HttpResponse hr = new HttpResponse();
        LocalTime criticalTime = systemConfigService.getCriticalTime(gr.getHotelCode());
        LocalDate startDate = cir.getArriveTime().toLocalDate();
        if (cir.getArriveTime().toLocalTime().isBefore(criticalTime)) {
            startDate = startDate.plusDays(-1);
        }
        List<CheckInRecord> data = new ArrayList<CheckInRecord>();
        String tempName = gr.getRoomNum();
        List<String> layout = null;
        if (cir.getRoomLayout() != null && !cir.getRoomLayout().isEmpty()) {
            layout = new ArrayList<String>();
            layout.addAll(cir.getRoomLayout());
        }
        List<String> requirement = null;
        for (int i = 0; i < cir.getRoomRequirement().size(); i++) {
            requirement = new ArrayList<>();
            requirement.add(cir.getRoomRequirement().get(i));
        }
        for (int i = 1; i <= humanCount; i++) {
            CheckInRecord ncir = null;
            try {
                ncir = (CheckInRecord) cir.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            Customer customer = null;
            Account account = null;
            if (cir.getCustomer() != null && i == 1) {//????????????????????????????????????????????????????????????????????????????????????????????????
                //??????????????????????????????????????????????????????????????????i==1?????????????????????
                customer = customerService.findById(cir.getCustomer().getId());
                if (("No Room").equals(customer.getName())) {
                    customer.setName(tempName + "#" + i);
                    customerService.modify(customer);
                }
                account = accountService.findById(cir.getAccount().getId());
                account.setName(customer.getName());
                accountService.modify(account);
            } else {
                customer = customerService.createTempCustomer(gr.getHotelCode(), tempName + "#" + i);
                account = new Account(0, 0);
                account.setRoomNum(gr.getRoomNum());
                account.setRoomId(gr.getId());
                account.setCustomer(customer);
                account.setHotelCode(gr.getHotelCode());
                account.setName(customer.getName());//?????????????????????????????????
                account.setCode(businessSeqService.fetchNextSeqNum(gr.getHotelCode(),
                        Constants.Key.BUSINESS_BUSINESS_CUSTOMER_ACCOUNT_SEQ_KEY));
                account.setType(Constants.Type.ACCOUNT_CUSTOMER);
            }
            ncir.setHumanCount(1);
            ncir.setRoomLayout(layout);
            ncir.setRoomRequirement(requirement);
            if (i == 1) {//???????????????????????????????????????????????????????????????????????????0
                ncir.setOriginalPrice(gr.getRoomType().getPrice());//??????
                ncir.setPersonalPrice(cir.getPersonalPrice());
                ncir.setPersonalPercentage(1.0);
            } else {
                ncir.setOriginalPrice(0.0);
                ncir.setPersonalPrice(0.0);
                ncir.setPersonalPercentage(0.0);
            }
//            ncir.setPersonalPrice(cir.getPurchasePrice());
//            ncir.setPersonalPercentage(1.0);
            ncir.setId(null);
//            ncir.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_ASSIGN);//??????????????????A
            ncir.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);//???????????????????????????
            ncir.setCustomer(customer);
            ncir.setName(customer.getName());
            ncir.setType(Constants.Type.CHECK_IN_RECORD_CUSTOMER);
            ncir.setGuestRoom(gr);
            ncir.setSubRecords(null);
            ncir.setStartDate(startDate);
            ncir.setHotelCode(gr.getHotelCode());
            ncir.setCheckInCount(1);
            ncir.setRoomCount(1);
            ncir.setAccount(account);
            ncir.setGroupType(cir.getGroupType());// ???????????????????????????/?????????
            ncir.setFitType(cir.getFitType());
			ncir.setReserveId(cir.getId());// ??????????????????id
            ncir.setMainRecord(cir.getMainRecord());
            //????????????????????????????????????
            if (cir.getMainRecord() != null) {
                ncir.setGroupName(cir.getMainRecord().getGroupName());
            }
            ncir = add(ncir);
            data.add(ncir);
            roomRecordService.createRoomRecord(ncir);
            boolean result = roomStatisticsService.assignRoom(new CheckInRecordWrapper(ncir));
            if (!result) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return hr.error("????????????");
            }
//			guestRoomStatausService.checkIn(ncir);
        }
        hr.setData(data);
        return hr;
    }

    @Transactional
    @Override
    public HttpResponse book(CheckInRecord checkInRecord) {
        HttpResponse hr = new HttpResponse();
        String orderNum = businessSeqService.fetchNextSeqNum(checkInRecord.getHotelCode(),
                Constants.Key.BUSINESS_ORDER_NUM_SEQ_KEY);
        if (checkInRecord.getName() == null || "".equals(checkInRecord.getName())) {
            checkInRecord.setName("???????????????" + orderNum);
        }
        LocalTime criticalTime = systemConfigService.getCriticalTime(checkInRecord.getHotelCode());
        LocalDate startDate = checkInRecord.getArriveTime().toLocalDate();
        if (checkInRecord.getArriveTime().toLocalTime().isBefore(criticalTime)) {
            startDate = startDate.plusDays(-1);
        }
        checkInRecord.setStartDate(startDate);
        checkInRecord.setGroupName(checkInRecord.getName());
        checkInRecord.setOrderNum(orderNum);
        checkInRecord.setCheckInCount(0);
        if (checkInRecord.getSubRecords() != null && !checkInRecord.getSubRecords().isEmpty()) {
            checkInRecord.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);
            checkInRecord.setType(Constants.Type.CHECK_IN_RECORD_GROUP);
//            initGroup(checkInRecord);
            initGroupAccount(checkInRecord);
            checkInRecord = add(checkInRecord);
            for (CheckInRecord item : checkInRecord.getSubRecords()) {
                if (item.getRoomCount() != null && item.getRoomCount() > 0) {
                    item.setOrderNum(orderNum);
                    item.setHoldTime(checkInRecord.getHoldTime());
                    item.setArriveTime(checkInRecord.getArriveTime());
                    item.setLeaveTime(checkInRecord.getLeaveTime());
                    item.setStartDate(checkInRecord.getStartDate());
                    item.setDays(checkInRecord.getDays());
                    item.setContactName(checkInRecord.getContactName());
                    //?????????????????????????????????????????????????????????????????????????????????????????????????????????
                    if (item.getPurchasePrice() == null || "".equals(item.getPurchasePrice())) {
                        item.setPurchasePrice(item.getOriginalPrice());
                    }
                    item.setPersonalPrice(item.getPurchasePrice());
                    item.setSalesMen(checkInRecord.getSalesMen());
                    item.setMarketingSources(checkInRecord.getMarketingSources());
                    item.setDistributionChannel(checkInRecord.getDistributionChannel());
                    item.setContactMobile(checkInRecord.getContactMobile());
                    item.setRoomType(roomTypeService.findById(item.getRoomTypeId()));
                    item.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);
                    item.setType(Constants.Type.CHECK_IN_RECORD_RESERVE);
                    item.setGroupType(checkInRecord.getGroupType());
                    item.setGroupName(checkInRecord.getGroupName());
                    item.setHotelCode(checkInRecord.getHotelCode());
                    item.setFitType(checkInRecord.getFitType());
                    item.setMainRecord(checkInRecord);
                    item.setCheckInCount(0);
                    if (item.getRoomType() == null) {
                        item.setRoomType(roomTypeService.findById(item.getRoomTypeId()));
                    }
//                    item.setProtocolCorpation(checkInRecord.getProtocolCorpation());
                    item.setCorp(checkInRecord.getCorp());
                    item = add(item);
                    boolean result = roomStatisticsService.reserve(new CheckInRecordWrapper(item));
                    if (!result) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return hr.error("????????????");
                    }
                }
            }

        } else {
            Customer customer = checkInRecord.getCustomer();
            customer.setHotelCode(checkInRecord.getHotelCode());
            if (customer == null || customer.getId() == null) {
                customer = new Customer();
                customer.setHotelCode(checkInRecord.getHotelCode());
                customer = customerService.add(customer);
            }
            checkInRecord.setCustomer(customer);
            checkInRecord.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);
            checkInRecord.setType(Constants.Type.CHECK_IN_RECORD_CUSTOMER);
            checkInRecord = add(checkInRecord);
            Account account = accountService.createAccount(checkInRecord.getCustomer());
            checkInRecord.setAccount(account);
            modify(checkInRecord);
        }
        hr.setData(checkInRecord);
        HttpResponse rep = msgSendService.bookSendMsg(checkInRecord);
        return hr;
    }

    @Override
    @Transactional
    public HttpResponse bookByRoomList(CheckInRecordListBo cirlb, User user) {
        HttpResponse hr = new HttpResponse();
        Map<String, Object> map = new HashMap<>();
        List<CheckInRecord> cirs = new ArrayList<>();
        List<CheckInRecord> list = cirlb.getCirs();
        String roomLinkId = null;
        String orderNum = null;
        if (cirlb.getIsRoomLink()) {
            RoomLink rl = new RoomLink();
            rl.setDeleted(Constants.DELETED_FALSE);
            rl.setHotelCode(user.getHotelCode());
            roomLinkDao.save(rl);
            roomLinkId = rl.getId();
            orderNum = businessSeqService.fetchNextSeqNum(user.getHotelCode(), Constants.Key.BUSINESS_ORDER_NUM_SEQ_KEY);
        }
        boolean nativeUrl = false;
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getFitType() == null){
                list.get(i).setFitType("P");
            }
            if(cirlb.getIsRoomLink()){
                list.get(i).setOrderNum(orderNum);//??????????????????????????????????????????????????????
                if(list.size() > 1){//????????????????????????????????????1???????????????????????????????????????id?????????1??????????????????????????????id
                    list.get(i).setRoomLinkId(roomLinkId);
                }
            }else {//?????????????????????????????????????????????
                orderNum = businessSeqService.fetchNextSeqNum(user.getHotelCode(), Constants.Key.BUSINESS_ORDER_NUM_SEQ_KEY);
                list.get(i).setOrderNum(orderNum);
            }
            list.get(i).setHotelCode(user.getHotelCode());
            list.get(i).setOderFrom("?????????????????????");
            HttpResponse hr2 = bookByRoomTypeTest(list.get(i), user);
            if (hr2.getStatus() == 9999) {
                return hr2;
            }
            Map<String, Object> map2 = (Map<String, Object>) hr2.getData();
            boolean url = MapUtils.getBoolean(map2, "nativeUrl");
            if (url) {
                nativeUrl = url;
                CheckInRecord cir = (CheckInRecord) MapUtils.getObject(map2, "cir");
                cirs.add(cir);
            }
        }
        map.put("nativeUrl", nativeUrl);
        map.put("cir", cirs);
        hr.setData(map);
        return hr;
    }

    @Override
    @Transactional
    public HttpResponse bookByRoomTypeTest(CheckInRecord checkInRecord, User user) {
        HttpResponse hr = new HttpResponse();
//        LocalDate businessDate = businessSeqService.getBuinessDate(checkInRecord.getHotelCode());
        if (checkInRecord.getOrderNum() == null) {
            String orderNum = businessSeqService.fetchNextSeqNum(checkInRecord.getHotelCode(),
                    Constants.Key.BUSINESS_ORDER_NUM_SEQ_KEY);
            checkInRecord.setOrderNum(orderNum);
        }
        checkInRecord.setHumanCount(1);
        checkInRecord.setCheckInCount(1);
        checkInRecord.setRoomCount(1);
        checkInRecord.setType(Constants.Type.CHECK_IN_RECORD_CUSTOMER);
        checkInRecord.setGroupType(Constants.Type.CHECK_IN_RECORD_GROUP_TYPE_NO);
        checkInRecord.setSingleRoomCount(1);
        LocalTime criticalTime = systemConfigService.getCriticalTime(user.getHotelCode());
        LocalDate startDate = checkInRecord.getArriveTime().toLocalDate();
        boolean nativeUrl = false;
        if (checkInRecord.getArriveTime().toLocalTime().isBefore(criticalTime)) {
            startDate = startDate.plusDays(-1);
            DailyVerify dailyVerify = dailyVerifyService.findByHotelCodeAndBusinessDate(user.getHotelCode(), startDate);
            if (dailyVerify != null) {
                if ((Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN).equals(checkInRecord.getStatus())) {
                    nativeUrl = true;
                }
            }
        }
        checkInRecord.setStartDate(startDate);
        GuestRoom gr = guestRoomService.findById(checkInRecord.getGuestRoom().getId());
        checkInRecord.setGuestRoom(gr);
        checkInRecord.setHotelCode(gr.getHotelCode());
        //?????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (checkInRecord.getPurchasePrice() == null || "".equals(checkInRecord.getPurchasePrice())) {
            if (checkInRecord.getRoomType() != null) {
                RoomType rt = roomTypeService.findById(checkInRecord.getRoomType().getId());
                checkInRecord.setPurchasePrice(rt.getPrice());
                checkInRecord.setPersonalPrice(rt.getPrice());
                if (checkInRecord.getOriginalPrice() == null || "".equals(checkInRecord.getOriginalPrice())) {
                    checkInRecord.setOriginalPrice(rt.getPrice());
                }
            }
        }
        String tempName = gr.getRoomNum();
        Customer customer = null;
        if (checkInRecord.getCustomer() == null) {
            customer = customerService.createTempCustomer(gr.getHotelCode(), tempName + "#" + 1);
        } else {
            customer = checkInRecord.getCustomer();
        }
        checkInRecord.setCustomer(customer);
        checkInRecord.setName(customer.getName());
        Account account = accountService.createAccount(customer, tempName);
        checkInRecord.setAccount(account);
        CheckInRecord cir = add(checkInRecord);
        boolean result = roomStatisticsService.reserve(new CheckInRecordWrapper(cir));//??????
        if (!result) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return hr.error("????????????");
        }
        boolean result2 = roomStatisticsService.assignRoom(new CheckInRecordWrapper(cir));//??????
        if (!result2) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return hr.error("????????????");
        }
        if ((Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN).equals(checkInRecord.getStatus())) {
            checkInRecord.setActualTimeOfArrive(LocalDateTime.now());
            checkInRecordDao.saveAndFlush(checkInRecord);
            boolean result3 = roomStatisticsService.checkIn(new CheckInRecordWrapper(checkInRecord));
            if (!result3) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return hr.error("????????????");
            }
        }
//        roomRecordService.createRoomRecord(cir);
        List<RoomRecord> roomRecordList = roomRecordService.createRoomRecordTo(cir);
//        if ((Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN).equals(checkInRecord.getStatus())) {
//            receptionService.checkInAuditRoomRecord("I",cir, businessDate, user);
//        }
//        hr.addData(cir);
        Map<String, Object> map = new HashMap<>();
        map.put("nativeUrl", nativeUrl);
        map.put("cir", cir);
        hr.setData(map);
        //????????????
        msgSendService.bookSendMsg(cir);
        return hr;
    }

    //????????????
    @Override
    @Transactional
    public HttpResponse singleRoom(CheckInRecord checkInRecord) {
        HttpResponse hr = new HttpResponse();
        LocalTime criticalTime = systemConfigService.getCriticalTime(checkInRecord.getHotelCode());
        LocalDate startDate = checkInRecord.getArriveTime().toLocalDate();
        if (checkInRecord.getArriveTime().toLocalTime().isBefore(criticalTime)) {
            startDate = startDate.plusDays(-1);
        }
        checkInRecord.setStartDate(startDate);
        String orderNum = businessSeqService.fetchNextSeqNum(checkInRecord.getHotelCode(),
                Constants.Key.BUSINESS_ORDER_NUM_SEQ_KEY);
        checkInRecord.setOrderNum(orderNum);
        checkInRecord.setRoomCount(1);
        checkInRecord.setGroupType(Constants.Type.CHECK_IN_RECORD_GROUP_TYPE_NO);
        if (checkInRecord.getHumanCount() != null) {
            checkInRecord.setSingleRoomCount(checkInRecord.getHumanCount());
        } else {
            checkInRecord.setHumanCount(1);
            checkInRecord.setSingleRoomCount(1);
        }
        //?????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (checkInRecord.getPurchasePrice() == null || "".equals(checkInRecord.getPurchasePrice())) {
            checkInRecord.setPurchasePrice(checkInRecord.getOriginalPrice());
            checkInRecord.setPersonalPrice(checkInRecord.getOriginalPrice());
        }
        if (checkInRecord.getGuestRoom() != null && checkInRecord.getGuestRoom().getId() != null) {//?????????????????????????????????
            checkInRecord.setType(Constants.Type.CHECK_IN_RECORD_CUSTOMER);
//            checkInRecord.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_ASSIGN);//???????????????????????????A???????????????????????????
            checkInRecord.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);//????????????????????????
            GuestRoom gr = guestRoomService.findById(checkInRecord.getGuestRoom().getId());

            checkInRecord.setCheckInCount(1);

            List<CheckInRecord> listc = new ArrayList<>();
            for (int i = 0; i < checkInRecord.getHumanCount(); i++) {
                CheckInRecord addCir = new CheckInRecord();
                Customer customer = null;
                if (checkInRecord.getHumanCount() == 1) {
                    if (checkInRecord.getCustomer() == null) {
                        if (checkInRecord.getName() != null) {
                            customer = customerService.createTempCustomer(checkInRecord.getHotelCode(), checkInRecord.getName());
                        } else {
                            customer = customerService.createTempCustomer(checkInRecord.getHotelCode(), gr.getRoomNum() + "#" + (i + 1));
                        }
                    } else {
                        customer = customerService.findById(checkInRecord.getCustomer().getId());
                    }
                } else {
                    customer = customerService.createTempCustomer(checkInRecord.getHotelCode(), gr.getRoomNum() + "#" + (i + 1));
                }
                Account account = accountService.createAccount(customer, gr);
                account.setRoomId(gr.getId());
                checkInRecord.setAccount(account);
                checkInRecord.setCustomer(customer);
                checkInRecord.setName(customer.getName());
                BeanUtils.copyProperties(checkInRecord, addCir);
                listc.add(addCir);
            }
            CheckInRecord rcir = null;
            for (int j = 0; j < listc.size(); j++) {
                CheckInRecord cir = listc.get(j);
                cir.setHumanCount(1);
                cir.setSingleRoomCount(1);
                if (j == 0) {
                    cir.setOriginalPrice(gr.getRoomType().getPrice());//??????
                    cir.setPersonalPrice(cir.getPurchasePrice());
                    if (cir.getPersonalPercentage() == null) {
                        cir.setPersonalPercentage(1.0);
                    }
                } else {
                    cir.setOriginalPrice(0.0);
                    cir.setPersonalPrice(0.0);
                    cir.setPersonalPercentage(0.0);
                }
                CheckInRecord c = checkInRecordDao.saveAndFlush(cir);

                roomRecordService.createRoomRecord(c);
                if (j == 0) {
                    boolean result2 = roomStatisticsService.reserve(new CheckInRecordWrapper(c));
                    if (!result2) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return hr.error("????????????");
                    }
                    rcir = c;
                }
                boolean result = roomStatisticsService.assignRoom(new CheckInRecordWrapper(c));
                if (!result) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return hr.error("????????????");
                }

            }
//            CheckInRecord cir = add(checkInRecord);
            hr.setData(rcir);
            if(hr.getStatus()==0){
                orderMqService.sendNewOrder(rcir.getHotelCode(),rcir.getOrderNum());
            }
            //????????????
            msgSendService.bookSendMsg(rcir);
            return hr;
        } else {//????????????????????????????????????
            //************???????????????*************
            //??????????????????????????????????????????????????????????????????????????????
            Customer customer = null;
            Account account = null;
            if (checkInRecord.getCustomer() == null) {
                if (checkInRecord.getName() != null) {
                    customer = customerService.createTempCustomer(checkInRecord.getHotelCode(), checkInRecord.getName());
                    account = accountService.createAccount(customer, checkInRecord.getName());
                } else {
                    customer = customerService.createTempCustomer(checkInRecord.getHotelCode(), "No Name");
                    account = accountService.createAccount(customer, "No Room");
                }
            } else {
                customer = customerService.findById(checkInRecord.getCustomer().getId());
                account = accountService.createAccount(customer, "No Room");
            }
            checkInRecord.setAccount(account);
            checkInRecord.setCustomer(customer);
            checkInRecord.setName(customer.getName());
            //??????????????????????????????????????????????????????????????????????????????
            //************???????????????*************
            checkInRecord.setCheckInCount(0);
            checkInRecord.setType(Constants.Type.CHECK_IN_RECORD_RESERVE);
            checkInRecord.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);

            CheckInRecord cir = checkInRecordDao.saveAndFlush(checkInRecord);
            boolean result3 = roomStatisticsService.reserve(new CheckInRecordWrapper(cir));
            if (!result3) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return hr.error("????????????");
            }
//            roomRecordService.createRoomRecord(cir);//??????????????????????????????????????????roomRecord
            hr.setData(cir);
            if(hr.getStatus()==0){
                orderMqService.sendNewOrder(cir.getHotelCode(),cir.getOrderNum());
            }
            //????????????
            msgSendService.bookSendMsg(cir);
            return hr;
        }

    }

    @Transactional
    public CheckInRecord bookByRoomType(CheckInRecord checkInRecord) {
        String orderNum = businessSeqService.fetchNextSeqNum(checkInRecord.getHotelCode(),
                Constants.Key.BUSINESS_ORDER_NUM_SEQ_KEY);
        checkInRecord.setOrderNum(orderNum);
        if (checkInRecord.getCustomer() != null) {
            if (checkInRecord.getCustomer().getId() != null && checkInRecord.getCustomer().getId() != "") {

            } else {
                checkInRecord.setCustomer(null);
            }
        }
        int humanCount = checkInRecord.getHumanCount();
        DtoResponse<String> response = new DtoResponse<String>();
        for (int i = 0; i < humanCount; i++) {
            CheckInRecord cir = new CheckInRecord();
            BeanUtils.copyProperties(cir, checkInRecord);
            cir.setMainRecord(checkInRecord);
            cir.setStatus(Constants.Type.CHECK_IN_RECORD_RESERVE);// R
            cir.setCheckInCount(1);
            cir.setRoomCount(1);
            add(checkInRecord);
            GuestRoom gr = guestRoomService.findById(checkInRecord.getGuestRoom().getId());
            checkInByTempName(cir.getSingleRoomCount(), cir, gr, response);
        }

        if (checkInRecord.getSubRecords() != null && !checkInRecord.getSubRecords().isEmpty()) {
            checkInRecord.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);
            checkInRecord.setType(Constants.Type.CHECK_IN_RECORD_GROUP);
            initGroup(checkInRecord);
            initGroupAccount(checkInRecord);
            checkInRecord = add(checkInRecord);
            for (CheckInRecord item : checkInRecord.getSubRecords()) {
                if (item.getRoomCount() != null && item.getRoomCount() > 0) {
                    item.setOrderNum(orderNum);
                    item.setGroupType(checkInRecord.getGroupType());
                    item.setHoldTime(checkInRecord.getHoldTime());
                    item.setArriveTime(checkInRecord.getArriveTime());
                    item.setLeaveTime(checkInRecord.getLeaveTime());
                    item.setDays(checkInRecord.getDays());
                    item.setContactName(checkInRecord.getContactName());
                    item.setSalesMen(checkInRecord.getSalesMen());
                    item.setDistributionChannel(checkInRecord.getDistributionChannel());
                    item.setContactMobile(checkInRecord.getContactMobile());
                    item.setRoomType(roomTypeService.findById(item.getRoomTypeId()));
                    item.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);
                    item.setType(Constants.Type.CHECK_IN_RECORD_RESERVE);
                    item.setGroupType(checkInRecord.getGroupType());
                    item.setHotelCode(checkInRecord.getHotelCode());
                    item.setMainRecord(checkInRecord);
                    if (item.getRoomType() == null) {
                        item.setRoomType(roomTypeService.findById(item.getRoomTypeId()));
                    }
//                    item.setProtocolCorpation(checkInRecord.getProtocolCorpation());
                    item.setCorp(checkInRecord.getCorp());
//					boolean bookResult = roomStatisticsService.booking(item.getRoomType(), item.getArriveTime(),
//							item.getRoomCount(), item.getDays());
//					if (!bookResult) {
//						// ????????????
//						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//					}
                    add(item);
                }
            }

        } else {
            Customer customer = checkInRecord.getCustomer();
            if (customer != null && customer.getId() == null) {
                customer = customerService.add(customer);
            }
            checkInRecord.setCustomer(customer);
            checkInRecord.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);
            checkInRecord.setType(Constants.Type.CHECK_IN_RECORD_CUSTOMER);
//			roomStatisticsService.booking(checkInRecord.getRoomType(), checkInRecord.getArriveTime(), 1,
//					checkInRecord.getDays());
            checkInRecord = add(checkInRecord);
            Account account = accountService.createAccount(checkInRecord.getCustomer());
            checkInRecord.setAccount(account);
            modify(checkInRecord);

        }
        return checkInRecord;
    }

    private void initGroup(CheckInRecord checkInRecord) {

    }

    private void initGroupAccount(CheckInRecord checkInRecord) {
        Account account = new Account(0, 0);
        if (checkInRecord.getGroup() != null) {
            account.setGroup(checkInRecord.getGroup());
        }
        if ((Constants.Type.CHECK_IN_RECORD_GROUP).equals(checkInRecord.getIsGOrU())) {
            account.setCode(businessSeqService.fetchNextSeqNum(checkInRecord.getHotelCode(), Constants.Key.BUSINESS_BUSINESS_GROUP_ACCOUNT_SEQ_KEY));
            account.setType(Constants.Type.ACCOUNT_GROUP);
        } else {
            account.setCode(businessSeqService.fetchNextSeqNum(checkInRecord.getHotelCode(), Constants.Key.BUSINESS_BUSINESS_GROUP_CUSTOMER_ACCOUNT_SEQ_KEY));
            account.setType(Constants.Type.ACCOUNT_GROUP_CUSTOMER);
        }
        account.setName(checkInRecord.getName());
        account.setHotelCode(checkInRecord.getHotelCode());
        checkInRecord.setAccount(account);
    }

    @Override
    public PageResponse<CheckInRecord> notYet(int pageIndex, int pageSize, String status, User user) {
        Pageable page = org.springframework.data.domain.PageRequest.of(pageIndex - 1, pageSize);
        Specification<CheckInRecord> specification = new Specification<CheckInRecord>() {
            @Override
            public Predicate toPredicate(Root<CheckInRecord> root, CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                ArrayList<Predicate> list = new ArrayList<>();
                // ????????????
                if (user != null) {
                    Path<Object> hotelCode = root.get("hotelCode");
                    Predicate p1 = criteriaBuilder.equal(hotelCode.as(String.class), user.getHotelCode());
                    list.add(p1);
                }
                // ?????????R????????????I????????????O????????????D??????????????????N????????????S?????????????????????X????????????
                if (status != null && status != "") {
                    Path<Object> status1 = root.get("status");
                    Predicate p1 = criteriaBuilder.equal(status1.as(String.class), status);
                    list.add(p1);
                }
                Predicate[] parr = new Predicate[list.size()];
                parr = list.toArray(parr);
                return criteriaBuilder.and(parr);
            }
        };
        Page<CheckInRecord> p = checkInRecordDao.findAll(specification, page);
        return convent(p);
    }

    @Override
    public PageResponse<CheckInRecord> accountEntryList(int pageIndex, int pageSize, User user) {
        Pageable page = org.springframework.data.domain.PageRequest.of(pageIndex - 1, pageSize);
        ParamSpecification<CheckInRecord> psf = new ParamSpecification<CheckInRecord>();
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> emap = new HashMap<>();
        emap.put("hotelCode", user.getHotelCode());
        emap.put("status", "I");
        emap.put("type", "C");
        map.put("equals", emap);
        Specification<CheckInRecord> specification = psf.createSpecification(map);
        Page<CheckInRecord> p = checkInRecordDao.findAll(specification, page);
        return convent(p);
    }

    @Override
    public List<CheckInRecord> accountEntryListAll(String hotelCode) {
        ParamSpecification<CheckInRecord> psf = new ParamSpecification<CheckInRecord>();
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> emap = new HashMap<>();
        emap.put("hotelCode", hotelCode);
        emap.put("status", "I");
        map.put("equals", emap);
        Specification<CheckInRecord> specification = psf.createSpecification(map);
        List<CheckInRecord> list = checkInRecordDao.findAll(specification);
        return list;
    }

    @Override
    public PageResponse<Map<String, Object>> accountEntryListMap(int pageIndex, int pageSize, User user) {
        Pageable page = org.springframework.data.domain.PageRequest.of(pageIndex - 1, pageSize);
        LocalDate businessDate = businessSeqService.getBuinessDate(user.getHotelCode());
        Page<Map<String, Object>> p = checkInRecordDao.accountEntryListMap(page, user.getHotelCode(), businessDate, Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN);
        PageResponse<Map<String, Object>> pr = new PageResponse<>();
        pr.setPageSize(p.getNumberOfElements());
        pr.setPageCount(p.getTotalPages());
        pr.setTotal(p.getTotalElements());
        pr.setCurrentPage(p.getNumber());
        pr.setContent(p.getContent());
        return pr;
    }

    @Override
    public PageResponse<Map<String, Object>> unreturnedGuests(int pageIndex, int pageSize, String mainNum, String status, User user) {
        LocalDate businessDate = businessSeqService.getBuinessDate(user.getHotelCode());
        LocalDateTime time = LocalDateTime.of(businessDate, LocalTime.now().withNano(0));
        Pageable page = org.springframework.data.domain.PageRequest.of(pageIndex - 1, pageSize);
        String hotelCode = null;
        if (user != null) {
            hotelCode = user.getHotelCode();
        }
        Page<Map<String, Object>> p = checkInRecordDao.unreturnedGuests(page, mainNum, status, hotelCode, time, businessDate);
        PageResponse<Map<String, Object>> pr = new PageResponse<>();
        pr.setPageSize(p.getNumberOfElements());
        pr.setPageCount(p.getTotalPages());
        pr.setTotal(p.getTotalElements());
        pr.setCurrentPage(p.getNumber());
        pr.setContent(p.getContent());
        return pr;
    }

    @Override
    public List<Map<String, Object>> getStatistics(User user) {
        LocalDate businessDate = businessSeqService.getBuinessDate(user.getHotelCode());
        LocalDateTime time = LocalDateTime.of(businessDate, LocalTime.now().withNano(0));
        String hotelCode = null;
        if (user != null) {
            hotelCode = user.getHotelCode();
        }
        List<Map<String, Object>> list = checkInRecordDao.getStatistics(hotelCode, time, businessDate);
        return list;
    }


    public Collection<AccountSummaryVo> getAccountSummaryByOrderNum2(String hotelCode, String orderNum, String type) {
        List<AccountSummaryVo> list = checkInRecordDao.querySummeryByOrderNumAndType(hotelCode, orderNum, type);
        return accountListToTree(list);
    }
    @Override
    public Collection<AccountSummaryVo> getAccountSummaryByOrderNum3(String hotelCode, String orderNum, String type, String fitType) {
        List<AccountSummaryVo> list = checkInRecordDao.querySummeryByOrderNumAndType3(hotelCode, orderNum, type, fitType);
        return accountListToTree(list);
    }

    private Collection<AccountSummaryVo> accountListToTree(List<AccountSummaryVo> list) {
        Map<String, AccountSummaryVo> asvm = new HashMap<>();
        AccountSummaryVo asv = null;
        for (AccountSummaryVo item : list) {
            item.setSettleType(Constants.Type.SETTLE_TYPE_ACCOUNT);
            if (!asvm.containsKey(item.getRoomNum())) {
                asv = new AccountSummaryVo();
                asv.setType("room");
                asv.setSettleType(Constants.Type.SETTLE_TYPE_ROOM);
                asv.setRoomNum(item.getRoomNum());
                asv.setId(item.getGuestRoomId());
                asv.setName(item.getRoomNum());
                asv.setOrderNum(item.getOrderNum());
                asv.setChildren(new ArrayList<AccountSummaryVo>());
                asvm.put(item.getRoomNum(), asv);
            } else {
                asv = asvm.get(item.getRoomNum());
            }
            asv.getChildren().add(item);
        }
        return asvm.values();
    }

    private Collection<AccountSummaryVo> checkInRecordToAccountSummaryVo(List<CheckInRecord> data) {
        Map<String, AccountSummaryVo> asvm = new HashMap<>();
        AccountSummaryVo asv = null;
        for (CheckInRecord cir : data) {
            if (cir.getAccount() != null && cir.getGuestRoom() != null) {
                Account acc = cir.getAccount();
                acc.setRoomNum(cir.getGuestRoom().getRoomNum());
                acc.setName(cir.getCustomer().getName());
                if (!asvm.containsKey(cir.getGuestRoom().getRoomNum())) {
                    asv = new AccountSummaryVo();
                    asv.setType("room");
                    asv.setSettleType(Constants.Type.SETTLE_TYPE_ROOM);
                    asv.setRoomNum(acc.getRoomNum());
                    asv.setGuestRoomId(cir.getGuestRoom().getId());
                    asv.setId(cir.getGuestRoom().getId());
                    asv.setName(acc.getRoomNum());
                    asv.setOrderNum(cir.getOrderNum());
                    asv.setChildren(new ArrayList<AccountSummaryVo>());
                    asvm.put(acc.getRoomNum(), asv);
                } else {
                    asv = asvm.get(acc.getRoomNum());
                }
                asv.getChildren().add(new AccountSummaryVo(cir,cir.getGuestRoom().getId()));
            }
        }
        return asvm.values();
    }


    @Override
    public PageResponse<CheckInRecordListVo> querySummaryList(PageRequest<CheckInRecord> prq) {
        PageResponse<CheckInRecord> cirp = listPage(prq);
        List<CheckInRecordListVo> data = new ArrayList<CheckInRecordListVo>();
        for (CheckInRecord cir : cirp.getContent()) {
            data.add(new CheckInRecordListVo(cir));
        }
        PageResponse<CheckInRecordListVo> rep = new PageResponse<>();
        BeanUtils.copyProperties(cirp, rep);
        rep.setContent(data);
        return rep;
    }

    @Override
    public PageResponse<Map<String, Object>> querySummaryListTo(PageRequest<CheckInRecord> prq) {
        Pageable page = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
        String hotelCode = null;
        Page<Map<String, Object>> p = checkInRecordDao.resverList(page, hotelCode,
                prq.getExb().getType(), prq.getExb().getFitType(), prq.getExb().getStatus(), prq.getExb().getGroupType());
        PageResponse<Map<String, Object>> pr = new PageResponse<>();
        pr.setPageSize(p.getNumberOfElements());
        pr.setPageCount(p.getTotalPages());
        pr.setTotal(p.getTotalElements());
        pr.setCurrentPage(p.getNumber());
        pr.setContent(p.getContent());
        return pr;
    }

    @Override
    public PageResponse<Map<String, Object>> querySummaryListToBySql(String hotelCode, PageRequest pageRequest) throws IOException, TemplateException {
        return sqlTemplateService.queryForPage(hotelCode, "resverList", pageRequest);
    }

    @Override
    public PageResponse<Map<String, Object>> resverListHis(String hotelCode, PageRequest pageRequest) throws IOException, TemplateException {
        return sqlTemplateService.queryForPage(hotelCode, "resverListHis", pageRequest);
    }

    @Override
    public List<Map<String, Object>> querySummaryListToBySqlTotal(String hotelCode, Map<String, Object> params) throws IOException, TemplateException {
        List<Map<String, Object>> list = sqlTemplateService.processByCode(hotelCode, "resverListTotal", params);
        Map<String, Object> map = new HashMap<>();
        if (list != null && !list.isEmpty()) {
            map.put("total", MapUtils.getDouble(list.get(0), "total"));
            map.put("creditLimit", MapUtils.getDouble(list.get(0), "creditLimit"));
            map.put("humanCount", MapUtils.getDouble(list.get(0), "humanCount"));
            map.put("humanCountTotal", MapUtils.getDouble(list.get(0), "humanCountTotal"));
            map.put("priceTotal", MapUtils.getDouble(list.get(0), "priceTotal"));
        }
        List<Map<String, Object>> list2 = sqlTemplateService.processByCode(hotelCode, "resverListTotal2", params);
        if (list2 != null && !list2.isEmpty()) {
            map.put("roomCount", MapUtils.getDouble(list2.get(0), "roomCount"));
            map.put("roomCountTotal", MapUtils.getDouble(list2.get(0), "roomCountTotal"));
        }
        List<Map<String, Object>> rlist = new ArrayList<>();
        rlist.add(map);
        return rlist;
    }

    @Override
    public List<CheckInRecord> findByOrderNum(String hotelCode, String orderNum) {
        return checkInRecordDao.findByHotelCodeAndOrderNumAndDeleted(hotelCode, orderNum, Constants.DELETED_FALSE);
    }

    @Override
    public List<CheckInRecord> findByOrderNumC(String hotelCode, String orderNum) {
        return checkInRecordDao.findByHotelCodeAndOrderNumAndTypeAndDeleted(hotelCode, orderNum, Constants.Type.CHECK_IN_RECORD_CUSTOMER, Constants.DELETED_FALSE);
    }

    @Override
    public List<Map<String, Object>> findByOrderNumC2(String hotelCode, String orderNum) throws IOException, TemplateException {
        Map<String, Object> map = new HashMap<>();
        map.put("orderNum", orderNum);
        map.put("type", Constants.Type.CHECK_IN_RECORD_CUSTOMER);
        List<Map<String, Object>> list = sqlTemplateService.processByCode(hotelCode, "findByOrderNumC", map);
        return list;
    }

    @Override
    public List<Map<String, Object>> findByOrderNum2(String hotelCode, String orderNum) throws IOException, TemplateException {
        Map<String, Object> map = new HashMap<>();
        map.put("orderNum", orderNum);
//        List list = sqlTemplateService.processTemplateQuery(hotelCode, CheckInRecordService.class.getSimpleName(), "findByOrderNum2", orderNum);
        List<Map<String, Object>> list = sqlTemplateService.processByCode(hotelCode, "findByOrderNum2", map);
        return list;
    }

    @Override
    public List<Map<String, Object>> sqlOrderNum(String orderNum) {
        List<Map<String, Object>> list = checkInRecordDao.sqlOrderNumAndDeleted(orderNum, Constants.DELETED_FALSE);
        return list;
    }

    @Override
    public CheckInRecord addReserve(CheckInRecord checkInRecord) {
        if (checkInRecord.getMainRecordId() != null) {
            CheckInRecord mainCheckInRecord = findById(checkInRecord.getMainRecordId());
            checkInRecord.setHoldTime(mainCheckInRecord.getHoldTime());
            if (checkInRecord.getDays() == null) {
                checkInRecord.setDays(mainCheckInRecord.getDays());
            }
            checkInRecord.setContactName(mainCheckInRecord.getContactName());
            checkInRecord.setSalesMen(mainCheckInRecord.getSalesMen());
            checkInRecord.setDistributionChannel(mainCheckInRecord.getDistributionChannel());
            checkInRecord.setContactMobile(mainCheckInRecord.getContactMobile());
            checkInRecord.setOrderNum(mainCheckInRecord.getOrderNum());
            checkInRecord.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);
            checkInRecord.setType(Constants.Type.CHECK_IN_RECORD_RESERVE);
            checkInRecord.setHotelCode(mainCheckInRecord.getHotelCode());
            checkInRecord.setGroupType(mainCheckInRecord.getGroupType());
            checkInRecord.setGroupName(mainCheckInRecord.getName());
            checkInRecord.setRoomType(roomTypeService.findById(checkInRecord.getRoomTypeId()));
            checkInRecord.setMainRecord(mainCheckInRecord);
            checkInRecord.setFitType(mainCheckInRecord.getFitType());
            checkInRecord.setCorp(mainCheckInRecord.getCorp());
            checkInRecord = add(checkInRecord);
            mainCheckInRecord.setRoomCount(mainCheckInRecord.getRoomCount() + checkInRecord.getRoomCount());
//			mainCheckInRecord.getSubRecords().add(checkInRecord);
            modify(mainCheckInRecord);
        }
        return checkInRecord;
    }

    @Override
    @Transactional
    public HttpResponse addReserve(List<CheckInRecord> checkInRecords) {
        HttpResponse hr = new HttpResponse();
        String mainId = null;
        int humanCount = 0;
        for (CheckInRecord cir : checkInRecords) {
            cir.setPersonalPrice(cir.getPurchasePrice());
            //?????????????????????????????????????????????????????????????????????????????????????????????????????????
            if (cir.getPurchasePrice() == null || "".equals(cir.getPurchasePrice())) {
                cir.setPurchasePrice(cir.getOriginalPrice());
            }
            if (cir.getPersonalPrice() == null || "".equals(cir.getPersonalPrice())) {
                cir.setPersonalPrice(cir.getOriginalPrice());
            }
            cir = addReserve(cir);
            //????????????????????????
            boolean result = roomStatisticsService.reserve(new CheckInRecordWrapper(cir));
            if (!result) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return hr.error("???????????????????????????");
            }
            mainId = cir.getMainRecordId();
            humanCount = humanCount + cir.getHumanCount();
        }
        if (mainId != null) {
            CheckInRecord main = checkInRecordDao.getOne(mainId);
            main.setHumanCount(main.getHumanCount() + humanCount);
            checkInRecordDao.saveAndFlush(main);
        }
        hr.addData(checkInRecords);
        return hr;
    }

    @Transactional
    @Override
    public CheckInRecord addTogether(TogetherBo togetherBo) {
        CheckInRecord cir = findById(togetherBo.getCurrentId());
        CheckInRecord ncir = null;
        if (cir != null) {
            List<String> roomLayout = null;
            if (cir.getRoomLayout() != null && !cir.getRoomLayout().isEmpty()) {
                roomLayout = new ArrayList<>();
                roomLayout.addAll(cir.getRoomLayout());
            }
            List<String> requirement = null;
            if (cir.getRoomRequirement() != null && !cir.getRoomRequirement().isEmpty()) {
                requirement = new ArrayList<>();
                requirement.addAll(cir.getRoomRequirement());
            }
            try {
                ncir = (CheckInRecord) cir.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            ncir.setRoomLayout(roomLayout);
            ncir.setRoomRequirement(requirement);
            ncir.setId(null);
            ncir.setAccount(null);
            ncir.setSubRecords(null);
            ncir.setStatus(togetherBo.getStatus());
            Customer customer = customerService.createOrGetCustomer(cir.getGuestRoom().getHotelCode(),
                    togetherBo.getName(), togetherBo.getIdCardNum(), togetherBo.getMobile());
            Account account = accountService.createAccount(customer, cir.getGuestRoom().getRoomNum());
            ncir.setAccount(account);
            ncir.setCustomer(customer);
            ncir = add(ncir);
        }
        return ncir;
    }

    @Override
    public List<CheckInRecord> findRoomTogetherRecord(CheckInRecord cir, String status) {
        CheckInRecord exCir = new CheckInRecord();
        exCir.setGuestRoom(cir.getGuestRoom());
        exCir.setOrderNum(cir.getOrderNum());
        exCir.setHotelCode(cir.getHotelCode());
        if (status != null) {
            exCir.setStatus(status);
        }
        exCir.setType(Constants.Type.CHECK_IN_RECORD_CUSTOMER);
        Example<CheckInRecord> ex = Example.of(exCir);
        return checkInRecordDao.findAll(ex);
    }


    @Override
    public List<CheckInRecord> findByOrderNumAndGuestRoomAndDeleted(String orderNum, GuestRoom guestRoom, int delete) {
        List<CheckInRecord> list = checkInRecordDao.findByOrderNumAndGuestRoomAndDeleted(orderNum, guestRoom, delete);
        return list;
    }


    // ????????????
    @Override
    @Transactional
    public HttpResponse callOffAssignRoom(String[] ids) {
        HttpResponse hr = new HttpResponse();
//        List<String> reserveIds = new ArrayList<>();
        String mainRecordId = null;
        for (int i = 0; i < ids.length; i++) {
            CheckInRecord cir = findById(ids[i]);
            if (cir.getMainRecord() != null) {
                mainRecordId = cir.getMainRecord().getId();
            }else if("P".equals(cir.getFitType())){//?????????????????????????????????????????????????????????

            }else {
                continue;
            }
            if (("G").equals(cir.getType())) {
                return hr.error("??????????????????");
            }
//            cir.setDeleted(Constants.DELETED_TRUE);
//            // ??????????????????????????????????????????????????????
//            modify(cir);
            // ???????????????????????????????????????
            // ?????????????????????????????????????????????????????????????????????????????????
            List<CheckInRecord> list = checkInRecordDao.findByOrderNumAndGuestRoomAndDeleted(cir.getOrderNum(),
                    cir.getGuestRoom(), Constants.DELETED_FALSE);
            // ?????????????????????????????????????????????????????????
            if (list == null || list.isEmpty()) {
                // ...????????????????????????
            }else {//?????????????????????????????????
                for(int c=0; c<list.size(); c++){
                    CheckInRecord cr = list.get(c);
                    boolean result = roomStatisticsService.cancleAssign(new CheckInRecordWrapper(cr));
                    if (!result) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return hr.error("???????????????????????????");
                    }
                    if(cr.getReserveId() == null){//??????????????????????????????????????????????????????????????????
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return hr.error("?????????????????????????????????????????????????????????");
                    }else {

                    }
                    CheckInRecord reserve = findById(cr.getReserveId());
                    reserve.setCheckInCount(reserve.getCheckInCount()-1);
                    if(c==0){//???????????????????????????+1?????????
                        reserve.setRoomCount(reserve.getRoomCount() +1);
                    }
                    reserve.setHumanCount(reserve.getHumanCount()+1);
                    reserve.setDeleted(Constants.DELETED_FALSE);
                    checkInRecordDao.saveAndFlush(reserve);
                    roomRecordDao.deleteByCheckInRecord(cr);//??????roomRecord
//                    if(cr.getReserveId() != null){//????????????????????????????????????account???customer
//                        if(reserve.getAccount() != null && !reserve.getAccount().getId().equals(cr.getAccount().getId())){
//                            accountService.delete(cr.getAccount().getId());//???????????????account
//                        }
//                        if(reserve.getCustomer() != null){
//                            if(!reserve.getCustomer().getId().equals(cr.getCustomer().getId())){
//                                customerService.delete(cr.getCustomer().getId());//??????customer
//                            }
//                        }
//                    }
                    cr.setDeleted(Constants.DELETED_TRUE);
                    checkInRecordDao.saveAndFlush(cr);//??????????????????????????????????????????
                }
            }
            // ???????????????????????????id???????????????
//            if (cir.getReserveId() != null) {
//                if (!reserveIds.contains(cir.getReserveId())) {
//                    reserveIds.add(cir.getReserveId());
//                }
//            }
        }
//        updateCount(reserveIds, mainRecordId);
        return hr;
    }

    // ????????????
    @Override
    @Transactional
    public HttpResponse callOffReserve(String[] ids) {
        HttpResponse hr = new HttpResponse();
        CheckInRecord main = null;
        int roomCount = 0;
        int humanCount = ids.length;
        for (int i = 0; i < ids.length; i++) {
            CheckInRecord cir = checkInRecordDao.getOne(ids[i]);
            if (cir.getMainRecord() != null) {
                main = cir.getMainRecord();
            }
            // ???????????????????????????
            if (("G").equals(cir.getType()) || ("R").equals(cir.getType())) {
                return hr.error("??????/??????????????????");
            }
            boolean b = accountService.accountCheck(cir.getAccount().getId());
            if (!b) {
                return hr.error(cir.getName() + "??????????????????");
            }
//            if ((Constants.Status.CHECKIN_RECORD_STATUS_ASSIGN).equals(cir.getStatus())) {//??????
            if ((Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION).equals(cir.getStatus())) {//????????????????????????????????????
                boolean result = roomStatisticsService.cancleAssign(new CheckInRecordWrapper(cir));//????????????
                if (!result) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return hr.error("???????????????????????????");
                }
            }
            //????????????????????????????????????????????????????????????????????????????????????????????????????????????
            //?????????????????????????????????????????????????????????????????????????????????
            if ((Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN).equals(cir.getStatus())) {
                //??????????????????????????????R????????????????????????????????????cir??????I??????R
                cir.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);
                boolean result = roomStatisticsService.cancleCheckIn(new CheckInRecordWrapper(cir));//???????????????
                if (!result) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return hr.error("???????????????????????????");
                }
                boolean result2 = roomStatisticsService.cancleAssign(new CheckInRecordWrapper(cir));//???????????????
                if (!result2) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return hr.error("???????????????????????????");
                }
            }
            int together = checkInRecordDao.countTogetherRoom(Constants.DELETED_FALSE, cir.getOrderNum(), cir.getGuestRoom().getId());
            if (together == 1) {//?????????????????????????????????????????????
                boolean result3 = roomStatisticsService.cancleReserve(new CheckInRecordWrapper(cir));
                if (!result3) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return hr.error("???????????????????????????");
                }
                roomCount = roomCount + 1;
            }
            //????????????roomRecord?????????
            List<RoomRecord> list = roomRecordService.findByHotelCodeAndCheckInRecord(cir.getHotelCode(), cir.getId());
            for (int r = 0; r < list.size(); r++) {
                roomRecordService.deleteTrue(list.get(r).getId());
            }
            cir.setDeleted(Constants.DELETED_TRUE);
            cir.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_CANCLE_BOOK);
            modify(cir);
        }
        if (main != null) {
            main.setRoomCount(main.getRoomCount() - roomCount);
            main.setHumanCount(main.getHumanCount() - humanCount);
            checkInRecordDao.saveAndFlush(main);
        }
//		updateCount(reserveIds, mainRecordId);
        return hr.ok();
    }

    //?????????????????????
    @Override
    @Transactional
    public HttpResponse callOffReserveAudit(String[] ids) {
        HttpResponse hr = new HttpResponse();
        for (int i = 0; i < ids.length; i++) {
            CheckInRecord main = null;
            int roomCount = 0;
            int humanCount = 0;
            CheckInRecord cir = checkInRecordDao.getOne(ids[i]);
            if (cir.getMainRecord() != null) {
                main = cir.getMainRecord();
            }
            // ????????????????????????????????????????????????????????????
            if (("G").equals(cir.getType())) {
                return hr.error("????????????????????????");
            }
            if (("R").equals(cir.getType())) {//???????????????????????????
                humanCount = cir.getHumanCount();
                roomCount = cir.getRoomCount();
                cir.setDeleted(Constants.DELETED_TRUE);
                cir.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_CANCLE_BOOK);
                modify(cir);
                boolean result = roomStatisticsService.cancleReserve(new CheckInRecordWrapper(cir));//?????????
                if (!result) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return hr.error("???????????????????????????");
                }
            } else if (("C").equals(cir.getType())) {//????????????????????????????????????
                boolean b = accountService.accountCheck(cir.getAccount().getId());
                if (!b) {
                    return hr.error(cir.getName() + "??????????????????");
                }
                humanCount = 1;
                if ((Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION).equals(cir.getStatus())) {//????????????????????????????????????
                    boolean result2 = roomStatisticsService.cancleAssign(new CheckInRecordWrapper(cir));//????????????
                    if (!result2) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return hr.error("???????????????????????????");
                    }
                }
                int together = checkInRecordDao.countTogetherRoom(Constants.DELETED_FALSE, cir.getOrderNum(), cir.getGuestRoom().getId());
                if (together == 1) {//?????????????????????????????????????????????
                    boolean result3 = roomStatisticsService.cancleReserve(new CheckInRecordWrapper(cir));
                    if (!result3) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return hr.error("???????????????????????????");
                    }
                    roomCount = roomCount + 1;
                }
                //????????????roomRecord?????????
                List<RoomRecord> list = roomRecordService.findByHotelCodeAndCheckInRecord(cir.getHotelCode(), cir.getId());
                for (int r = 0; r < list.size(); r++) {
                    roomRecordService.deleteTrue(list.get(r).getId());
                }
                cir.setDeleted(Constants.DELETED_TRUE);
                cir.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_CANCLE_BOOK);
                modify(cir);
            }
            if (main != null) {
                main.setRoomCount(main.getRoomCount() - roomCount);
                main.setHumanCount(main.getHumanCount() - humanCount);
                checkInRecordDao.saveAndFlush(main);
            }
        }
        return hr.ok();
    }

    //?????????????????????
    @Override
    @Transactional
    public HttpResponse callOffG(String id) {
        HttpResponse hr = new HttpResponse();
        CheckInRecord cir = checkInRecordDao.getOne(id);
        if (Constants.Type.CHECK_IN_RECORD_GROUP.equals(cir.getType())) {//???????????????
            List<String> list = checkInRecordDao.listIdByType(id, Constants.Type.CHECK_IN_RECORD_CUSTOMER, Constants.DELETED_FALSE);
            if (list != null && !list.isEmpty()) {
                return hr.error("??????????????????????????????????????????");
            } else {//????????????????????????????????????????????????????????????
                List<String> rList = checkInRecordDao.listIdByType(id, Constants.Type.CHECK_IN_RECORD_RESERVE, Constants.DELETED_FALSE);
//                int hcount = 0;
//                int rcount = 0;
                for (int i = 0; i < rList.size(); i++) {
                    hr = offReserve(rList.get(i));//???????????????????????????????????????????????????????????????????????????
//                    CheckInRecord r = checkInRecordDao.getOne(rList.get(i));
//                    hcount = hcount + r.getHumanCount();
//                    rcount = rcount + r.getRoomCount();
                }
                //??????????????????????????????
                cir.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_CANCLE_BOOK);
                cir.setDeleted(Constants.DELETED_TRUE);
//                cir.setHumanCount(cir.getHumanCount() - hcount);
//                cir.setRoomCount(cir.getRoomCount() - rcount);
                update(cir);
            }
        } else if (Constants.Type.CHECK_IN_RECORD_RESERVE.equals(cir.getType())) {//??????????????????
            return hr.error("?????????????????????????????????");
        } else {
            if (Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION.equals(cir.getStatus())) {
                String[] ids = new String[1];
                ids[0] = id;
                hr = callOffReserve(ids);
            } else {
                return hr.error("?????????????????????");
            }
        }
        return hr;
    }

    //????????????
    @Override
    public HttpResponse recovery(String id) {
        HttpResponse hr = new HttpResponse();
        CheckInRecord cir = checkInRecordDao.getOne(id);
        if (Constants.Type.CHECK_IN_RECORD_GROUP.equals(cir.getType())) {//???????????????
            if (cir.getDeleted() == 0) {
                return hr.error("????????????????????????");
            } else {
                cir.setDeleted(Constants.DELETED_FALSE);
                cir.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);
                boolean re = roomStatisticsService.reserve(new CheckInRecordWrapper(cir));
                if (re) {
                    update(cir);
                } else {
                    return hr.error("????????????");
                }
            }
        } else {
            return hr.error("??????????????????");
        }
        return hr;
    }

    @Override
    @Transactional
    public HttpResponse offReserve(String id) {
        HttpResponse hr = new HttpResponse();
        List<String> reserveIds = new ArrayList<>();
        String mainRecordId = null;
        CheckInRecord cir = checkInRecordDao.getOne(id);
        if (cir.getMainRecord() != null) {
            mainRecordId = cir.getMainRecord().getId();
        }
        List<CheckInRecord> list = checkInRecordDao.findByReserveIdAndDeleted(cir.getId(), Constants.DELETED_FALSE);
        if (list.size() > 0) {//???????????????????????????
            return hr.error("????????????????????????????????????????????????");
        }
        cir.setDeleted(Constants.DELETED_TRUE);
        cir.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_CANCLE_BOOK);
        modify(cir);
        boolean result = roomStatisticsService.cancleReserve(new CheckInRecordWrapper(cir));//?????????
        if (!result) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return hr.error("???????????????????????????");
        }
        if (mainRecordId != null) {
            CheckInRecord mainRecord = checkInRecordDao.getOne(mainRecordId);//??????????????????
            mainRecord.setRoomCount(mainRecord.getRoomCount() - cir.getRoomCount());
            mainRecord.setHumanCount(mainRecord.getHumanCount() - cir.getHumanCount());
            update(mainRecord);//?????????????????????????????????????????????????????????
        }
        return hr.ok();
    }

    @Override
    public HttpResponse updateCount(List<String> reserveIds, String mainRecordId) {
        HttpResponse hr = new HttpResponse();
        // ????????????????????????????????????
        for (int i = 0; i < reserveIds.size(); i++) {
            int count = checkInRecordDao.getReserveIdCount(reserveIds.get(i), Constants.DELETED_FALSE);
            CheckInRecord cir = findById(reserveIds.get(i));
            cir.setCheckInCount(count);
            modify(cir);
        }
        // ???????????????????????????
        CheckInRecord cir = findById(mainRecordId);
        int count = checkInRecordDao.getMainRecordIdCount(mainRecordId, Constants.DELETED_FALSE);
        cir.setCheckInCount(count);
        modify(cir);
        return hr.ok();
    }

    // ????????????????????????
    @Override
    public PageResponse<CheckInRecord> getNRoomLink(int pageIndex, int pageSize, String name, String roomNum,
                                                    String hotelCode, String groupType, String corp, String status, String account) {
        Pageable page = org.springframework.data.domain.PageRequest.of(pageIndex - 1, pageSize);
        Page<CheckInRecord> pageList = checkInRecordDao.findAll(new Specification<CheckInRecord>() {
            @Override
            public Predicate toPredicate(Root<CheckInRecord> root, CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                if (hotelCode != null) {
                    list.add(criteriaBuilder.equal(root.get("hotelCode"), hotelCode));
                }
                if (groupType != null) {//?????????N?????????Y??????
                    list.add(criteriaBuilder.equal(root.get("groupType"), groupType));
                }
                if (roomNum != null) {//?????????
                    list.add(criteriaBuilder.equal(root.join("guestRoom").get("roomNum"), roomNum));
                }
                if (status != null) {//??????
                    list.add(criteriaBuilder.equal(root.get("status"), status));
                }
                if (name != null) {//??????
                    // ??????????????????????????????join???get
                    list.add(criteriaBuilder.like(root.join("customer").get("name"), "%" + name + "%"));
                }
                if (corp != null) {//??????
                    // ??????????????????????????????join???get
                    list.add(criteriaBuilder.like(root.join("corp").get("name"), "%" + corp + "%"));
                }
                if (account != null) {//??????
                    // ??????????????????????????????join???get
                    list.add(criteriaBuilder.equal(root.join("account").get("code"), account));
                }
                list.add(criteriaBuilder.isNull(root.get("roomLinkId")));
                list.add(criteriaBuilder.isNull(root.get("mainRecord")));
                list.add(criteriaBuilder.equal(root.get("type"), Constants.Type.CHECK_IN_RECORD_CUSTOMER));   // ??????????????????????????????????????????????????????????????????????????????
                list.add(criteriaBuilder.equal(root.get("deleted"), Constants.DELETED_FALSE));
                Predicate[] array = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(array));
            }
        }, page);
        return convent(pageList);
    }

    // ??????roomlinkId???????????????????????????
    @Override
    public List<CheckInRecord> getRoomLinkList(String roomLinkId) {
        List<CheckInRecord> list = new ArrayList<>();
        if (roomLinkId != null) {
            list = checkInRecordDao.findByRoomLinkId(roomLinkId);
        }
        return list;
    }

    // ??????roomlinkId???????????????????????????,????????????????????????????????????
    @Override
    public List<CheckInRecord> getRoomLinkListTo(String id, String orderNum) {
        CheckInRecord cir = checkInRecordDao.getOne(id);
        String roomLinkId = cir.getRoomLinkId();
        List<CheckInRecord> list = new ArrayList<>();
        if (roomLinkId != null && !"".equals(roomLinkId)) {
            list = checkInRecordDao.findByRoomLinkId(roomLinkId);
        } else {
            list = checkInRecordDao.findByOrderNumAndGuestRoomAndDeleted(orderNum, cir.getGuestRoom(), Constants.DELETED_FALSE);
        }
        return list;
    }

    // ????????????
    @Override
    public void deleteRoomLink(String[] ids) {
        for (int i = 0; i < ids.length; i++) {
            CheckInRecord cir = checkInRecordDao.getOne(ids[i]);
            cir.setRoomLinkId(null);
            update(cir);
        }
    }

    @Override
    public List<CheckInRecord> getByRoomNum(String roomNum, String hotelCode, String groupType) {
        List<CheckInRecord> list = checkInRecordDao.findAll(new Specification<CheckInRecord>() {
            @Override
            public Predicate toPredicate(Root<CheckInRecord> root, CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                if (roomNum != null) {
                    list.add(criteriaBuilder.equal(root.join("guestRoom").get("roomNum"), roomNum));
                }
                if (hotelCode != null) {
                    list.add(criteriaBuilder.equal(root.get("hotelCode"), hotelCode));
                }
                if (groupType != null) {
                    list.add(criteriaBuilder.equal(root.get("groupType"), groupType));
                }
                list.add(criteriaBuilder.isNull(root.get("roomLinkId")));
                Predicate[] array = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(array));
            }
        });
        return list;
    }

    @Override
    public List<CheckInRecord> checkInTogether(String hotelCode, String orderNum, String guestRoomId) {
        GuestRoom guestRoom = guestRoomService.findById(guestRoomId);
        List<CheckInRecord> list = checkInRecordDao.findAll(new Specification<CheckInRecord>() {
            @Override
            public Predicate toPredicate(Root<CheckInRecord> root, CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                if (hotelCode != null) {
                    list.add(criteriaBuilder.equal(root.get("hotelCode"), hotelCode));
                }
                if (orderNum != null) {
                    list.add(criteriaBuilder.equal(root.get("orderNum"), orderNum));
                }
                if (guestRoom != null) {
                    list.add(criteriaBuilder.equal(root.join("guestRoom"), guestRoom));
                }
                list.add(criteriaBuilder.equal(root.get("deleted"), Constants.DELETED_FALSE));
//				list.add(criteriaBuilder.isNotNull(root.get("guestRoom")));
                Predicate[] array = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(array));
            }
        });
        return list;
    }

    @Override
    public List<CheckInRecord> checkInTogetherByStatus(String hotelCode, String orderNum, String guestRoomId, List<String> status) {
        GuestRoom guestRoom = guestRoomService.findById(guestRoomId);
        List<CheckInRecord> list = checkInRecordDao.findAll(new Specification<CheckInRecord>() {
            @Override
            public Predicate toPredicate(Root<CheckInRecord> root, CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                if (hotelCode != null) {
                    list.add(criteriaBuilder.equal(root.get("hotelCode"), hotelCode));
                }
                if (orderNum != null) {
                    list.add(criteriaBuilder.equal(root.get("orderNum"), orderNum));
                }
                if (guestRoom != null) {
                    list.add(criteriaBuilder.equal(root.join("guestRoom"), guestRoom));
                }
                list.add(criteriaBuilder.equal(root.get("deleted"), Constants.DELETED_FALSE));
//				list.add(criteriaBuilder.isNotNull(root.get("guestRoom")));
                List<Predicate> predicateListOr = new ArrayList<Predicate>();
                for (int i = 0; i < status.size(); i++) {
                    predicateListOr.add(criteriaBuilder.equal(root.get("status"), status.get(i)));
                }
                list.add(criteriaBuilder.or(predicateListOr.toArray(new Predicate[predicateListOr.size()])));
                Predicate[] array = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(array));
            }
        });
        return list;
    }

    @Override
    public List<CheckInRecord> findByTogetherCode(String hotelCode, String togetherCod) {
        List<CheckInRecord> list = checkInRecordDao.findAll(new Specification<CheckInRecord>() {
            @Override
            public Predicate toPredicate(Root<CheckInRecord> root, CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                if (hotelCode != null) {
                    list.add(criteriaBuilder.equal(root.get("hotelCode"), hotelCode));
                }
                if (togetherCod != null) {
                    list.add(criteriaBuilder.equal(root.get("togetherCode"), togetherCod));
                }
                list.add(criteriaBuilder.equal(root.get("deleted"), Constants.DELETED_FALSE));
                list.add(criteriaBuilder.isNotNull(root.get("guestRoom")));
                Predicate[] array = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(array));
            }
        });
        return list;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public HttpResponse addTogether(String hotelCode, String orderNum, String customerId, String status, String guestRoomId) {
        HttpResponse hr = new HttpResponse();
        String[] statusList = new String[]{"R", "I"};
//        List<CheckInRecord> list = checkInTogether(hotelCode, orderNum, guestRoomId);
        List<CheckInRecord> list = checkInTogetherByStatus(hotelCode, orderNum, guestRoomId, Arrays.asList(statusList));
        if (list == null || list.isEmpty()) {
            return hr.error("????????????????????????");
        }
        CheckInRecord cir = list.get(0);
        CheckInRecord checkInRecord = new CheckInRecord();
        BeanUtils.copyProperties(cir, checkInRecord);
        //????????????????????????????????????????????????????????????????????????????????????????????????,??????????????????
        List<String> roomLayout = null;
        if (cir.getRoomLayout() != null && !cir.getRoomLayout().isEmpty()) {
            roomLayout = new ArrayList<>();
            roomLayout.addAll(cir.getRoomLayout());
        }
        List<String> requirement = null;
        if (cir.getRoomRequirement() != null && !cir.getRoomRequirement().isEmpty()) {
            requirement = new ArrayList<>();
            requirement.addAll(cir.getRoomRequirement());
        }
        checkInRecord.setRoomLayout(roomLayout);
        checkInRecord.setRoomRequirement(requirement);
        checkInRecord.setSubRecords(null);
        Customer customer = customerService.findById(customerId);
        Account account = new Account(0, 0);
        account.setRoomNum(cir.getGuestRoom().getRoomNum());
        account.setRoomId(cir.getGuestRoom().getId());
        account.setName(customer.getName());
        account.setCustomer(customer);
        account.setCode(businessSeqService.fetchNextSeqNum(cir.getHotelCode(),
                Constants.Key.BUSINESS_BUSINESS_CUSTOMER_ACCOUNT_SEQ_KEY));
        account.setType(Constants.Type.ACCOUNT_CUSTOMER);
        checkInRecord.setAccount(null);
        checkInRecord.setAccount(account);
        checkInRecord.setId(null);
        if (status != null && status != "") {
            checkInRecord.setStatus(status);
        }
        // ??????????????????????????????
        checkInRecord.setOriginalPrice(0.0);
        checkInRecord.setPersonalPrice(0.0);
        checkInRecord.setPersonalPercentage(0.0);
        checkInRecord.setCustomer(customer);
        checkInRecord.setName(customer.getName());
        checkInRecord.setActualTimeOfArrive(LocalDateTime.now());
        if (cir.getTogetherCode() == null) {
            String togetherNum = businessSeqService.fetchNextSeqNum(hotelCode, Constants.Key.TOGETHER_NUM_KEY);
            for (int i = 0; i < list.size(); i++) {
                CheckInRecord cirT = list.get(i);
                cirT.setTogetherCode(togetherNum);
                update(cirT);
            }
            checkInRecord.setTogetherCode(togetherNum);
        } else {
            checkInRecord.setTogetherCode(cir.getTogetherCode());
        }
        checkInRecordDao.save(checkInRecord);
        roomStatisticsService.addTogether(new CheckInRecordWrapper(checkInRecord));
        roomRecordService.createRoomRecord(checkInRecord);
        List<CheckInRecord> main = checkInRecordDao.findByOrderNumAndTypeAndDeleted(orderNum, Constants.Type.CHECK_IN_RECORD_GROUP, Constants.DELETED_FALSE);
        if (main != null && !main.isEmpty()) {
            CheckInRecord cm = main.get(0);
            cm.setHumanCount(cm.getHumanCount() + 1);
            update(cm);
        }
        return hr.ok();
    }

    @Override
    public CheckInRecord queryByAccountId(String id) {
        return checkInRecordDao.findTopByAccountIdAndDeleted(id, Constants.DELETED_FALSE);
    }

    // ????????????
    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public HttpResponse roomPriceAllocation(String hotelCode, String orderNum, String checkInRecordId, String guestRoomId) {
        HttpResponse hr = new HttpResponse();
        LocalDate date = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String recordDate = date.format(fmt);
        String[] statusList = new String[]{"R", "I"};
        CheckInRecord tcir = checkInRecordDao.getOne(checkInRecordId);
        if (!Arrays.asList(statusList).contains(tcir.getStatus())) {
            return hr.error("??????/?????????????????????????????????");
        }
//        List<CheckInRecord> list = checkInTogether(hotelCode, orderNum, guestRoomId);
        List<CheckInRecord> list = checkInTogetherByStatus(hotelCode, orderNum, guestRoomId, Arrays.asList(statusList));
        for (int i = 0; i < list.size(); i++) {
            CheckInRecord cir = list.get(i);
//            String custId = cir.getCustomer().getId();
            if (!checkInRecordId.equals(cir.getId())) {
                cir.setOriginalPrice(0.0);
                cir.setPersonalPrice(0.0);
                cir.setPersonalPercentage(0.0);
            } else {
                cir.setOriginalPrice(cir.getRoomType().getPrice());
                cir.setPersonalPrice(cir.getPurchasePrice());
                cir.setPersonalPercentage(1.0);// ??????????????????????????????1
            }
            update(cir);
            List<RoomRecord> roomRecords = roomRecordService.getByTimeAndCheckId(recordDate, cir.getId());
            for (int j = 0; j < roomRecords.size(); j++) {
                RoomRecord rr = roomRecords.get(j);
                rr.setCost(cir.getPersonalPrice());
                rr.setCostRatio(cir.getPersonalPercentage());
                roomRecordService.modify(rr);
            }

        }
        return hr.ok();
    }

    // ????????????
    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public HttpResponse roomPriceAvg(String hotelCode, String orderNum, String guestRoomId) {
        HttpResponse hr = new HttpResponse();
        LocalDate date = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String recordDate = date.format(fmt);
//        List<CheckInRecord> list = checkInTogether(hotelCode, orderNum, guestRoomId);
        String[] status = new String[]{"R", "I"};
        List<CheckInRecord> list = checkInTogetherByStatus(hotelCode, orderNum, guestRoomId, Arrays.asList(status));
        if (list != null && !list.isEmpty()) {
            Double peopleCount = Double.valueOf(list.size());
            Double roomPrice = list.get(0).getPurchasePrice();
            Double oPrice = list.get(0).getRoomType().getPrice();//????????????
            DecimalFormat df = new DecimalFormat("####0.00");
            Double avg = roomPrice / peopleCount;
            Double personalPrice = Double.parseDouble(df.format(avg));
            Double personalPercentage = Double.parseDouble(df.format(1.0 / peopleCount));
            Double originalPrice = Double.parseDouble(df.format(oPrice / peopleCount));
            for (int i = 0; i < list.size(); i++) {
                CheckInRecord cir = list.get(i);
                cir.setPersonalPrice(personalPrice);
                cir.setPersonalPercentage(personalPercentage);
                cir.setOriginalPrice(originalPrice);
                update(cir);
                List<RoomRecord> roomRecords = roomRecordService.getByTimeAndCheckId(recordDate, cir.getId());
                for (int j = 0; j < roomRecords.size(); j++) {
                    RoomRecord rr = roomRecords.get(j);
                    rr.setCostRatio(cir.getPersonalPercentage());
                    rr.setCost(cir.getPersonalPrice());
                    roomRecordService.modify(rr);
                }
            }
        }
        return hr.ok();
    }

    @Override
    public List<CheckInRecord> findByGuestRoomAndStatusAndDeleted(GuestRoom guestRoom, String status, int deleted) {
        return checkInRecordDao.findByGuestRoomAndStatusAndDeleted(guestRoom, status, deleted);
    }

    @Override
    public List<String> findRoomNumByLink(String roomLinkId) {
        return checkInRecordDao.findRoomNumByLink(roomLinkId);
    }

    @Override
    public List<CheckInRecord> findTodayCheckInRecord(GuestRoom guestRoom, String status) {
        LocalDate busDate = businessSeqService.getBuinessDate(guestRoom.getHotelCode());
        List<CheckInRecord> ar = checkInRecordDao.findByGuestRoomIdAndStatusAndDeletedAndStartDate(guestRoom.getId(), status, Constants.DELETED_FALSE, busDate);
//        List<CheckInRecord> ar = checkInRecordDao.findByGuestRoomIdAndStatusAndDeletedAndStartDate(guestRoom.getId(), status, Constants.DELETED_FALSE, LocalDate.now());
        return ar;
    }

    @Override
    public List<CheckInRecord> findByLinkId(String id) {
        return checkInRecordDao.findByRoomLinkId(id);
    }

    @Override
    public List<CheckInRecord> findByGuestRoomAndStatusAndDeleted(String guestRoomId, String status, int deleted) {
        if (status == null) {
            return findByGuestRoomIdAndStatus(guestRoomId, deleted);
        }
        return checkInRecordDao.findByGuestRoomIdAndStatusAndDeleted(guestRoomId, status, deleted);
    }

    private List<CheckInRecord> findByGuestRoomIdAndStatus(String guestRoomId, int deleted) {
        return checkInRecordDao.findByGuestRoomIdAndDeleted(guestRoomId, deleted);
    }

    @Override
    public List<Map<String, Object>> getGroup(String hotelCode, String arriveTime, String leaveTime, String name_,
                                              String code_) {
        List<Map<String, Object>> list = checkInRecordDao.getGroup(hotelCode, arriveTime, leaveTime, name_, code_);
        return list;
    }

    @Override
    public HttpResponse inGroup(String[] cId, String gId, Boolean isFollowGroup) {
        Map<String, Object> returnMap = new HashMap<>();
        CheckInRecord cirG = checkInRecordDao.getOne(gId);
        returnMap.put("orderNum", cirG.getOrderNum());
        returnMap.put("ids", cId);
        List<String> roomNums = new ArrayList<>();
        int checkInCount = 0;
        int hCount = cId.length;
        for (int i = 0; i < cId.length; i++) {
            String id = cId[i];
            CheckInRecord cir = checkInRecordDao.getOne(id);
            if (!roomNums.contains(cir.getGuestRoom().getRoomNum())) {
                roomNums.add(cir.getGuestRoom().getRoomNum());
            }
            cir.setOrderNumOld(cir.getOrderNum());
            Map<String, Object> map = roomPriceSchemeDao.roomTypeAndPriceScheme(cir.getRoomType().getId(), cirG.getRoomPriceScheme().getId());
            Double price = MapUtils.getDouble(map, "price");
            //??????????????????????????????????????????
            if (cir.getGuestRoom() != null) {
                GuestRoom gr = cir.getGuestRoom();
                String orderNum = cir.getOrderNum();

                List<CheckInRecord> list = checkInRecordDao.findByOrderNumAndGuestRoomAndDeleted(orderNum, gr, Constants.DELETED_FALSE);
                List<String> ids = Arrays.asList(cId);
                for (int m = 0; m < list.size(); m++) {
                    if (!ids.contains(list.get(m).getId())) {
                        hCount = hCount + 1;
                        CheckInRecord cirRoomNum = list.get(m);
                        cirRoomNum.setMainRecord(cirG);
                        cirRoomNum.setOrderNumOld(cir.getOrderNum());
                        cirRoomNum.setOrderNum(cirG.getOrderNum());
                        cirRoomNum.setGroupType(Constants.Type.CHECK_IN_RECORD_GROUP_TYPE_YES);
                        if (isFollowGroup) {
                            cirRoomNum.setMarketingSources(cirG.getMarketingSources());
                            cirRoomNum.setRoomPriceScheme(cirG.getRoomPriceScheme());
                            cirRoomNum.setDistributionChannel(cirG.getDistributionChannel());
                            cirRoomNum.setSetMeal(cirG.getSetMeal());
                            cirRoomNum.setPersonalPrice(price * cirRoomNum.getPersonalPercentage());
                        }
                        update(cirRoomNum);
                    }
                }
            }

            cir.setMainRecord(cirG);
            cir.setOrderNum(cirG.getOrderNum());
            cir.setGroupType(Constants.Type.CHECK_IN_RECORD_GROUP_TYPE_YES);
            if (isFollowGroup) {
                cir.setDistributionChannel(cirG.getDistributionChannel());
                cir.setSetMeal(cirG.getSetMeal());
                cir.setMarketingSources(cirG.getMarketingSources());
                cir.setRoomPriceScheme(cirG.getRoomPriceScheme());
                cir.setPersonalPrice(price * cir.getPersonalPercentage());
            }

            // ????????????????????????????????????1
            if (("I").equals(cir.getStatus())) {
                checkInCount += 1;
            }
            checkInRecordDao.save(cir);
        }
        cirG.setRoomCount(cirG.getRoomCount() + roomNums.size());
        cirG.setHumanCount(cirG.getHumanCount() + hCount);
        cirG.setCheckInCount(cirG.getCheckInCount() + checkInCount);
        checkInRecordDao.save(cirG);
        HttpResponse hr = new HttpResponse();
        hr.setData(returnMap);
        return hr;
    }

    @Override
    public HttpResponse outGroup(String[] cId, String gId, Boolean isFollowGroup, String roomPriceId) {
        HttpResponse hr = new HttpResponse();
        CheckInRecord cirG = checkInRecordDao.getOne(gId);
        List<String> roomNums = new ArrayList<>();
        int checkInCount = 0;
        int hCount = cId.length;
        for (int i = 0; i < cId.length; i++) {
            String id = cId[i];
            CheckInRecord cir = checkInRecordDao.getOne(id);
            String orderNum = null;
            if (cir.getOrderNumOld() != null) {
                orderNum = cir.getOrderNumOld();
            } else {
                orderNum = businessSeqService.fetchNextSeqNum(cir.getHotelCode(),
                        Constants.Key.BUSINESS_ORDER_NUM_SEQ_KEY);
            }
            Map<String, Object> map = roomPriceSchemeDao.roomTypeAndPriceScheme(cir.getRoomType().getId(), roomPriceId);
            String setMealId = MapUtils.getString(map, "setMealId");
            Double price = MapUtils.getDouble(map, "price");//???????????????????????????
            if (cir.getGuestRoom() != null) {
                if (price == null) {
                    price = cir.getRoomType().getPrice();
                }
                if (!roomNums.contains(cir.getGuestRoom().getRoomNum())) {
                    roomNums.add(cir.getGuestRoom().getRoomNum());

                    GuestRoom gr = cir.getGuestRoom();
                    String orderNumOld = cir.getOrderNum();
                    List<CheckInRecord> list = checkInRecordDao.findByOrderNumAndGuestRoomAndDeleted(orderNumOld, gr, Constants.DELETED_FALSE);
                    List<String> ids = Arrays.asList(cId);
                    for (int m = 0; m < list.size(); m++) {
                        if (!ids.contains(list.get(m).getId())) {
                            hCount = hCount + 1;
                        }
                        CheckInRecord cirRoomNum = list.get(m);
                        cirRoomNum.setMainRecord(null);
                        cirRoomNum.setGroupType(Constants.Type.CHECK_IN_RECORD_GROUP_TYPE_NO);
                        cirRoomNum.setOrderNum(orderNum);
                        // ????????????????????????????????????1
                        if (("I").equals(cirRoomNum.getStatus())) {
                            checkInCount += 1;
                        }
                        cirRoomNum.setMarketingSources(cirG.getMarketingSources());
                        if (roomPriceId != null) {
                            RoomPriceScheme rps = new RoomPriceScheme();
                            rps.setId(roomPriceId);
                            cirRoomNum.setRoomPriceScheme(rps);
                        } else {
                            cirRoomNum.setRoomPriceScheme(cirG.getRoomPriceScheme());
                        }
//                            cirRoomNum.setRoomPriceScheme(cirG.getRoomPriceScheme());
                        cirRoomNum.setDistributionChannel(cirG.getDistributionChannel());
                        cirRoomNum.setGroupName(null);
                        if (setMealId != null) {
                            SetMeal sm = new SetMeal();
                            sm.setId(setMealId);
                            cirRoomNum.setSetMeal(sm);
                        } else {
                            cirRoomNum.setSetMeal(null);
                        }
                        cirRoomNum.setPersonalPrice(price * cirRoomNum.getPersonalPercentage());
                        checkInRecordDao.saveAndFlush(cirRoomNum);
                    }
                }
            }
        }
        cirG.setHumanCount(cirG.getHumanCount() - hCount);
        cirG.setRoomCount(cirG.getRoomCount() - roomNums.size());
        cirG.setCheckInCount(cirG.getCheckInCount() - checkInCount);
        checkInRecordDao.saveAndFlush(cirG);

        return hr.ok();
    }

    @Override
    public HttpResponse updateGroup(String[] cId, String gId, String uId, Boolean isFollowGroup) {
        HttpResponse hr = new HttpResponse();
        CheckInRecord cirG = checkInRecordDao.getOne(gId);
        CheckInRecord cirU = checkInRecordDao.getOne(uId);
        List<String> roomNums = new ArrayList<>();
        int hCount = cId.length;
        int checkInCount = 0;
        for (int i = 0; i < cId.length; i++) {
            String id = cId[i];
            CheckInRecord cir = checkInRecordDao.getOne(id);
            if (!roomNums.contains(cir.getGuestRoom().getRoomNum())) {
                roomNums.add(cir.getGuestRoom().getRoomNum());
            }
            Map<String, Object> map = roomPriceSchemeDao.roomTypeAndPriceScheme(cir.getRoomType().getId(), cirU.getRoomPriceScheme().getId());
            Double price = MapUtils.getDouble(map, "price");//?????????????????????????????????
            //???????????????????????????
            if (cir.getGuestRoom() != null) {
                GuestRoom gr = cir.getGuestRoom();
                String orderNum = cir.getOrderNum();
                List<CheckInRecord> list = checkInRecordDao.findByOrderNumAndGuestRoomAndDeleted(orderNum, gr, Constants.DELETED_FALSE);
                List<String> ids = Arrays.asList(cId);
                for (int m = 0; m < list.size(); m++) {
                    if (!ids.contains(list.get(m).getId())) {
                        hCount = hCount + 1;
                        CheckInRecord cirRoomNum = list.get(m);
                        cirRoomNum.setMainRecord(cirG);
                        cirRoomNum.setOrderNum(cirG.getOrderNum());
                        cirRoomNum.setGroupType(Constants.Type.CHECK_IN_RECORD_GROUP_TYPE_YES);
                        if (isFollowGroup) {
                            cirRoomNum.setMarketingSources(cirG.getMarketingSources());
                            cirRoomNum.setRoomPriceScheme(cirG.getRoomPriceScheme());
                            cirRoomNum.setDistributionChannel(cirG.getDistributionChannel());
                            cirRoomNum.setPersonalPrice(price * cirRoomNum.getPersonalPercentage());
                            cirRoomNum.setSetMeal(cirG.getSetMeal());
                        }
                        update(cirRoomNum);
                    }
                }
            }

            cir.setMainRecord(cirU);
            cir.setOrderNum(cirU.getOrderNum());
            cir.setGroupType(Constants.Type.CHECK_IN_RECORD_GROUP_TYPE_YES);
            if (isFollowGroup) {
                cir.setMarketingSources(cirG.getMarketingSources());
                cir.setRoomPriceScheme(cirG.getRoomPriceScheme());
                cir.setPersonalPrice(price * cir.getPersonalPercentage());
                cir.setDistributionChannel(cirG.getDistributionChannel());
                cir.setSetMeal(cirG.getSetMeal());
            }
            // ????????????????????????????????????1
            if (("I").equals(cir.getStatus())) {
                checkInCount += 1;
            }
            checkInRecordDao.save(cir);
        }
        cirG.setRoomCount(cirG.getRoomCount() - roomNums.size());
        cirG.setHumanCount(cirG.getHumanCount() - hCount);
        cirG.setCheckInCount(cirG.getCheckInCount() - checkInCount);
        checkInRecordDao.save(cirG);
        cirU.setRoomCount(cirU.getRoomCount() + roomNums.size());
        cirU.setHumanCount(cirG.getHumanCount() + hCount);
        cirU.setCheckInCount(cirU.getCheckInCount() + checkInCount);
        checkInRecordDao.save(cirU);

        return hr.ok();
    }

    @Override
    public Collection<AccountSummaryVo> getAccountSummaryByLinkNum(String hotelCode, String orderNum, String accountCustomer) {
        List<CheckInRecord> data = findByLinkId(orderNum);
        return checkInRecordToAccountSummaryVo(data);
    }

    //????????????
    @Override
    @Transactional
    public HttpResponse updateReserve(CheckInRecord cir) {
        HttpResponse hr = new HttpResponse();
        CheckInRecord oldCir = checkInRecordDao.getOne(cir.getId());
        CheckInRecord main = oldCir.getMainRecord();
        if (main != null) {
            if (cir.getArriveTime().isBefore(main.getArriveTime()) || cir.getLeaveTime().isAfter(main.getLeaveTime())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return hr.error("??????????????????????????????");
            }
        }
        int oldHumanCount = oldCir.getHumanCount();
        int oldRoomCount = oldCir.getRoomCount();
        int newHumanCount = cir.getHumanCount();
        int newRoomCount = cir.getRoomCount();
        CheckInRecordWrapper cirw = new CheckInRecordWrapper(oldCir);
        oldCir.setDeleted(Constants.DELETED_TRUE);
        Account account = null;
        if (oldCir.getAccount() != null) {
            account = oldCir.getAccount();
            oldCir.setAccount(null);
        }
        checkInRecordDao.saveAndFlush(oldCir);
        boolean result = roomStatisticsService.cancleReserve(cirw);//?????????????????????
        if (!result) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return hr.error("???????????????????????????");
        }
        CheckInRecord checkInRecord = new CheckInRecord();
        cir.setId(null);
        cir.setAccount(null);
        BeanUtils.copyProperties(cir, checkInRecord);
        if (account != null) {
            Account a = new Account();
            BeanUtils.copyProperties(account, a);
            a.setId(null);
            checkInRecord.setAccount(a);
            accountService.deleteTrue(account.getId());
        }
        checkInRecord.setMainRecord(oldCir.getMainRecord());
        checkInRecord = checkInRecordDao.saveAndFlush(checkInRecord);
        boolean result2 = roomStatisticsService.reserve(new CheckInRecordWrapper(checkInRecord));//????????????
        if (!result2) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return hr.error("????????????");
        }
        if (main != null) {
            main.setHumanCount(main.getHumanCount() + (newHumanCount - oldHumanCount));
            main.setRoomCount(main.getRoomCount() + (newRoomCount - oldRoomCount));
            checkInRecordDao.saveAndFlush(main);
            hr.setData(checkInRecord);
        }
        return hr;
    }

    @Override
    public List<String> getRoomLayout(String checkInId) {
        return checkInRecordDao.getRoomLayout(checkInId);
    }

    @Override
    public List<String> getRoomRequirement(String checkInId) {
        return checkInRecordDao.getRoomRequirement(checkInId);
    }

    @Override
    public List<Map<String, Object>> resourceStatistics(String orderNum, String arriveTime, String leaveTime) {
        List<Map<String, Object>> list = checkInRecordDao.resourceStatistics(orderNum, arriveTime, leaveTime);
        List<Map<String, Object>> rList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> newMap = new HashMap<>(list.get(i));
            String now = MapUtils.getString(list.get(i), "date");
            String roomTypeId = MapUtils.getString(list.get(i), "roomTypeId");
            Integer humanCount = MapUtils.getInteger(list.get(i), "humanCount");
            Integer roomCount = MapUtils.getInteger(list.get(i), "roomCount");
            Double cost = MapUtils.getDouble(list.get(i), "cost");
            Map<String, Object> map = checkInRecordDao.resourceStatisticsR(orderNum, now, roomTypeId);
            if (map != null && map.size() > 0) {
                Integer hCount = MapUtils.getInteger(map, "humanCount");
                Integer rCount = MapUtils.getInteger(map, "roomCount");
                Double c = MapUtils.getDouble(map, "cost");
                newMap.put("humanCount", humanCount + hCount);
                newMap.put("roomCount", roomCount + rCount);
                newMap.put("cost", cost + c);
            }
            rList.add(newMap);
        }
        return rList;
    }

    @Override
    public List<Map<String, Object>> resourceStatisticsTo(String orderNum, String arriveTime, String leaveTime) {
        List<Map<String, Object>> rlist = new ArrayList<>();
        List<String> list = checkInRecordDao.getTime(arriveTime, leaveTime);
        List<String> roomType = checkInRecordDao.getAllRoomType(orderNum);
        for (int i = 0; i < list.size(); i++) {
            String now = list.get(i);
            for (int j = 0; j < roomType.size(); j++) {
                Map<String, Object> map = new HashMap<>();
                map.put("date", now);
                map.put("roomTypeId", roomType.get(j));
                Map<String, Object> cmap = checkInRecordDao.countMap(orderNum, now, roomType.get(j));
                int humanCount = 0, roomCount = 0;
                Double cost = 0.0;
                if (cmap != null && cmap.size() > 0) {
                    map.put("code", MapUtils.getString(cmap, "code"));
                    map.put("roomType", MapUtils.getString(cmap, "roomType"));
                    Integer hCount = MapUtils.getInteger(cmap, "humanCount");
                    if (hCount != null) {
                        humanCount = humanCount + hCount;
                    }
                    Integer rCount = MapUtils.getInteger(cmap, "roomCount");
                    if (rCount != null) {
                        roomCount = roomCount + rCount;
                    }
                    Double c = MapUtils.getDouble(cmap, "cost");
                    if (c != null) {
                        cost = cost + c;
                    }
                }
                Map<String, Object> rmap = checkInRecordDao.resourceStatisticsR(orderNum, now, roomType.get(j));
                if (rmap != null && rmap.size() > 0) {
                    map.put("code", MapUtils.getString(rmap, "code"));
                    map.put("roomType", MapUtils.getString(rmap, "roomType"));
                    Integer hCount = MapUtils.getInteger(rmap, "humanCount");
                    if (hCount != null) {
                        humanCount = humanCount + hCount;
                    }
                    Integer rCount = MapUtils.getInteger(rmap, "roomCount");
                    if (rCount != null) {
                        roomCount = roomCount + rCount;
                    }
                    Double c = MapUtils.getDouble(rmap, "cost");
                    if (c != null) {
                        cost = cost + c;
                    }
                }
                map.put("humanCount", humanCount);
                map.put("roomCount", roomCount);
                map.put("cost", cost);
                rlist.add(map);
            }
        }

        return rlist;
    }

    @Override
    public CheckInRecord byId(String id) {
        CheckInRecord cir = checkInRecordDao.byId(id);
        return cir;
    }

    private DtoResponse<String> hangUp(CheckInRecord cir, String extFee) {
        DtoResponse<String> rep = new DtoResponse<String>();
        if (Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN.equals(cir.getStatus())) {
            cir.setActualTimeOfLeave(LocalDateTime.now());
            if (Constants.Type.CHECK_IN_RECORD_CUSTOMER.equals(cir.getType())) {
                accountService.enterExtFee(cir, extFee);
                boolean result = roomStatisticsService.checkOut(new CheckInRecordWrapper(cir));
                if (!result) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    rep.setCode(9999);
                    rep.setMessage("????????????");
                    return rep;
                }
                checkRoomRecord(cir);
            }
        }
        cir.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_OUT_UNSETTLED);
        modify(cir);
        return rep;
    }

    private boolean hangUp(List<CheckInRecord> cirs, String extFee) {
        for (CheckInRecord cir : cirs) {
            hangUp(cir, extFee);
        }
        return true;
    }

    @Transactional
    public DtoResponse<String> hangUp(String id, String type, String extFeeType, String orderNum) {
        DtoResponse<String> rep = new DtoResponse<>();
        boolean result = true;
        CheckInRecord cir = null;
        switch (type) {
            case Constants.Type.SETTLE_TYPE_ACCOUNT:
                hangUp(queryByAccountId(id), extFeeType);
                break;
            case Constants.Type.SETTLE_TYPE_GROUP:
                cir = queryByAccountId(id);
                if (cir != null) {
                    hangUp(cir, extFeeType);
                    hangUp(findByOrderNumC(cir.getHotelCode(), cir.getOrderNum()), extFeeType);
                }
                break;
            case Constants.Type.SETTLE_TYPE_IGROUP:
                cir = queryByAccountId(id);
                if (cir != null) {
                    hangUp(cir, extFeeType);
                    hangUp(findByOrderNumC(cir.getHotelCode(), cir.getOrderNum()), extFeeType);
                }
                break;
            case Constants.Type.SETTLE_TYPE_ROOM:
                GuestRoom guestRoom = guestRoomService.findById(id);
                if (guestRoom != null) {
                    hangUp(findByOrderNumAndGuestRoomAndDeleted(orderNum, guestRoom, Constants.DELETED_FALSE), extFeeType);
                }
                break;
            case Constants.Type.SETTLE_TYPE_LINK:
                hangUp(findByLinkId(id), extFeeType);
                break;
            default:
                break;
        }
        return rep;
    }

    @Override
    public boolean hangUp(String checkInRecordId, String extFee) {
        hangUp(findById(checkInRecordId), extFee);
        return true;
    }

    @Override
    public DtoResponse<String> hangUpByAccountId(String id) {
        return null;
    }

    @Override
    public HttpResponse printing(String checkInRecordId) {
        HttpResponse hr = new HttpResponse();
        Map<String, Object> map = checkInRecordDao.printing(checkInRecordId);
        hr.addData(map);
        return hr;
    }

    @Override
    public HttpResponse yesterdayAudit(CheckInRecordAuditBo checkBo, User user) {
        HttpResponse hr = new HttpResponse();
        LocalDate businessDate = businessSeqService.getBuinessDate(user.getHotelCode());
        List<CheckInRecord> list = checkBo.getCirs();
        for (int i = 0; i < list.size(); i++) {
            CheckInRecord cir = list.get(i);
            receptionService.checkInAuditRoomRecord("I", cir, businessDate, user);
        }
        return hr.ok();
    }

    @Override
    public HttpResponse wechatUpdate(CheckInRecord checkInRecord) {
        HttpResponse hr = new HttpResponse();
        HttpResponse httpResponse = new HttpResponse();
        if (Constants.Type.CHECK_IN_RECORD_RESERVE.equals(checkInRecord.getType())) {
            httpResponse = updateReserve(checkInRecord);
            if (httpResponse.getStatus() == 9999) {
                return httpResponse;
            }
        } else {
            if (("G").equals(checkInRecord.getType())) {
                List<String> list = checkInRecordDao.listIdByType(checkInRecord.getId(), Constants.Type.CHECK_IN_RECORD_CUSTOMER, Constants.DELETED_FALSE);
                if (list != null && !list.isEmpty()) {
                    return hr.error("????????????????????????????????????");
                } else {//????????????????????????????????????????????????????????????
                    List<String> rList = checkInRecordDao.listIdByType(checkInRecord.getId(), Constants.Type.CHECK_IN_RECORD_RESERVE, Constants.DELETED_FALSE);
                    for (int i = 0; i < rList.size(); i++) {
                        hr = offReserve(rList.get(i));//???????????????????????????????????????????????????????????????????????????
                    }
                }
                httpResponse = addReserve(checkInRecord.getSubRecords());//????????????
                if (httpResponse.getStatus() == 9999) {
                    return httpResponse;
                }
//                hr = book(cir);
            }
            checkInRecordDao.saveAndFlush(checkInRecord);
        }
        return hr.ok();
    }

    @Override
    public List<Map<String, Object>> printDeposit(String hotelCode, String orderNum) {
        List<Map<String, Object>> list = checkInRecordDao.printDeposit(hotelCode, orderNum);
        return list;
    }

    @Override
    public CheckInRecord findByAccountCode(String hotelCode, String code) {
        return checkInRecordDao.findByAccountCodeAndHotelCode(code,hotelCode);
    }
    @Override
    public int nowLiveIn(String hotelCode){
        int count = checkInRecordDao.nowLiveIn(hotelCode);
        return count;
    }
    @Override
    public int nowCheckOut(String hotelCode, String leaveTime){
        int count = checkInRecordDao.nowCheckOut(hotelCode, leaveTime);
        return count;
    }

    @Override
    @Transactional
    public int updateRemark(String remark, String cirId){
        int count = checkInRecordDao.updateRemark(remark, cirId);
        return count;
    }

}