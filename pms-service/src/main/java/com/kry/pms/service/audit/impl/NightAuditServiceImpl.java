package com.kry.pms.service.audit.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.CheckInRecordDao;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.DailyVerify;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.report.GenerateReportsLog;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.audit.NightAuditService;
import com.kry.pms.service.busi.BillService;
import com.kry.pms.service.busi.DailyVerifyService;
import com.kry.pms.service.busi.RoomRecordService;
import com.kry.pms.service.guest.MemberIntegralService;
import com.kry.pms.service.org.EmployeeService;
import com.kry.pms.service.report.*;
import com.kry.pms.service.sys.BusinessSeqService;
import com.kry.pms.service.sys.SqlTemplateService;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NightAuditServiceImpl implements NightAuditService {

    @Autowired
    BusinessSeqService businessSeqService;
    @Autowired
    RoomRecordService roomRecordService;
    @Autowired
    BillService billService;
    @Autowired
    DailyVerifyService dailyVerifyService;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    BusinessReportService businessReportService;
    @Autowired
    RoomReportService roomReportService;
    @Autowired
    GenerateReportsLogService generateReportsLogService;
    @Autowired
    ReceivablesReportService receivablesReportService;
    @Autowired
    FrontEntryReportService frontEntryReportService;
    @Autowired
    FrontReceiveReportService frontReceiveReportService;
    @Autowired
    CheckInRecordDao checkInRecordDao;
    @Autowired
    SqlTemplateService sqlTemplateService;
    @Autowired
    MemberIntegralService memberIntegralService;

    @Override
    public RoomRecord add(RoomRecord entity) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public RoomRecord modify(RoomRecord roomRecord) {
        return null;
    }

    @Override
    public RoomRecord findById(String id) {
        return null;
    }

    @Override
    public List<RoomRecord> getAllByHotelCode(String code) {
        return null;
    }

    @Override
    public PageResponse<RoomRecord> listPage(PageRequest<RoomRecord> prq) {
        return null;
    }

    /**
     * ????????????: <br>??????????????????(??????????????????AutomaticNightTrial)
     * ??????
     * @Param: [user, ids]??????ids?????????????????????????????????????????????????????????
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/11/23 15:20
     */
    @Override
    @Transactional
    public HttpResponse manualAdd(User loginUser, String[] ids, String shiftCode) {
        HttpResponse hr = new HttpResponse();
        //????????????????????????????????????
        LocalDate businessDate = businessSeqService.getBuinessDate(loginUser.getHotelCode());
        LocalDateTime time = LocalDateTime.of(businessDate, LocalTime.now().withNano(0));
        List<Map<String, Object>> listCount = checkInRecordDao.getStatistics(loginUser.getHotelCode(), time, businessDate);
        int s = 0, n = 0, i = 0, x = 0, r=0;
        for(int l=0; l<listCount.size(); l++){
            Map<String, Object> map = listCount.get(l);
            String status = MapUtils.getString(map,"status");
            if(Constants.Status.CHECKIN_RECORD_STATUS_OUT_UNSETTLED.equals(status)){
                s = MapUtils.getInteger(map, "scount");
            }else if(Constants.Status.CHECKIN_RECORD_STATUS_NO_SHOW.equals(status)){
                n = MapUtils.getInteger(map, "scount");
            }else if(Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN.equals(status)){
                i = MapUtils.getInteger(map, "scount");
            }else if(Constants.Status.CHECKIN_RECORD_STATUS_CANCLE_BOOK.equals(status)){
                x= MapUtils.getInteger(map, "scount");
            }else if(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION.equals(status)){
                r= MapUtils.getInteger(map, "scount");
            }
        }
        if(r+i>0){// ???????????? + ????????????
            return hr.error("????????????????????????");
        }
        LocalDate now = LocalDate.now();
        if(businessDate.isAfter(now)){
            return hr.error("??????????????????????????????");
        }
        if(businessDate.isEqual(now)){
            return hr.error("????????????0?????????????????????");
        }
        DailyVerify dailyVerify = dailyVerifyService.findByHotelCodeAndBusinessDate(loginUser.getHotelCode(), businessDate);
        if(dailyVerify != null){
            return hr.error("???????????????????????????????????????????????????");
        }
        //???????????????????????????????????????????????????(????????????)
//        List<RoomRecord> list = roomRecordService.accountEntryListAll(loginUser.getHotelCode(), businessDate);
        List<Map<String, Object>> list = roomRecordService.accountEntryListAllMap(businessDate, loginUser.getHotelCode(), "NO");
        Employee emp = employeeService.findByUser(loginUser);
//        billService.putAcount(list, businessDate, emp, shiftCode);
        billService.putAcountMap(list, businessDate, emp, shiftCode, loginUser.getHotelCode());
//        memberIntegralService.auditInInteger(list, businessDate, loginUser);//?????????????????????????????????
        //?????????????????????????????????
        DailyVerify dv = new DailyVerify();
        dv.setOperationEmployee(emp);
        dv.setBusinessDate(businessDate);
        dv.setType(Constants.auditNightMode.NIGHT_AUDIT_MANUAL);//?????????????????????
        dv.setHotelCode(loginUser.getHotelCode());
        dv.setVerifyDate(now);
        dv.setCreateDate(LocalDateTime.now());
        dv.setStatus("success");
        dailyVerifyService.add(dv);
        return hr.ok("????????????");
    }

    //????????????????????????-??????
    @Override
    public HttpResponse addReportAll(User loginUser) {
        HttpResponse hr = new HttpResponse();
        LocalDate businessDate = businessSeqService.getBuinessDate(loginUser.getHotelCode());

        DailyVerify dailyVerify = dailyVerifyService.findByHotelCodeAndBusinessDate(loginUser.getHotelCode(), businessDate);
        if(dailyVerify == null){
            return hr.error(99999, "??????????????????");
        }
        GenerateReportsLog grl = new GenerateReportsLog();
        grl.setHotelCode(loginUser.getHotelCode());
        grl.setAuditDate(LocalDateTime.now());
        grl.setBusinessDate(businessDate);
        grl.setUserId(loginUser.getId());
        grl.setUsername(loginUser.getUsername());
        grl.setType(Constants.auditNightMode.NIGHT_AUDIT_MANUAL);
        //????????????????????????????????????
        try {
//            hr = businessReportService.saveReport(loginUser.getHotelCode(), null, businessDate.toString());//???????????????-??????
//            roomReportService.totalRoomStatusAll(loginUser.getHotelCode(), businessDate.toString());//???????????????-a???????????????
//            roomReportService.totalCheckInType(loginUser.getHotelCode(), businessDate.toString());//???????????????-b???????????????
//            roomReportService.availableTotal(loginUser.getHotelCode(), businessDate.toString());//???????????????-c????????????
//            businessReportService.costByGroupType(loginUser.getHotelCode(), businessDate.toString());//???????????????-d???????????????
//            businessReportService.costByGroupTypeAvg(loginUser.getHotelCode(), businessDate.toString());//???????????????-e???????????????
//
//            receivablesReportService.totalByTypeName(loginUser.getHotelCode(), businessDate.toString());//????????????
//            frontEntryReportService.frontEntryList2(loginUser.getHotelCode(), businessDate.toString());//??????????????????
//            frontReceiveReportService.receiveList(loginUser.getHotelCode(), businessDate.toString());//??????????????????
            //?????????????????????...
            //...
            grl.setAuditStatus("success");
            generateReportsLogService.add(grl);
            if(businessDate.isBefore(LocalDate.now())){
                businessSeqService.plusBuinessDate(loginUser.getHotelCode());//????????????+1
            }
        } catch (Exception e) {
            e.printStackTrace();
            grl.setAuditStatus("error");
            grl.setReason(e.getMessage());
            generateReportsLogService.add(grl);
        }
        return hr.ok("??????????????????");
    }

    //????????????????????????-??????
    @Override
    public HttpResponse addReportAllAuto(String hotelCode) {
        HttpResponse hr = new HttpResponse();
        LocalDate businessDate = businessSeqService.getBuinessDate(hotelCode);
        DailyVerify dailyVerify = dailyVerifyService.findByHotelCodeAndBusinessDate(hotelCode, businessDate);
        if(dailyVerify == null){
            return hr.error(99999, "??????????????????");
        }
        GenerateReportsLog grl = new GenerateReportsLog();
        grl.setHotelCode(hotelCode);
        grl.setAuditDate(LocalDateTime.now());
        grl.setBusinessDate(businessDate);
        grl.setType(Constants.auditNightMode.NIGHT_AUDIT_AUTO);
        //????????????????????????????????????
        try {
            hr = businessReportService.saveReport(hotelCode, null, businessDate.toString());//???????????????-??????
            roomReportService.totalRoomStatusAll(hotelCode, businessDate.toString());//???????????????-a???????????????
            roomReportService.totalCheckInType(hotelCode, businessDate.toString());//???????????????-b???????????????
            roomReportService.availableTotal(hotelCode, businessDate.toString());//???????????????-c????????????
            businessReportService.costByGroupType(hotelCode, businessDate.toString());//???????????????-d???????????????
            businessReportService.costByGroupTypeAvg(hotelCode, businessDate.toString());//???????????????-e???????????????

            receivablesReportService.totalByTypeName(hotelCode, businessDate.toString());//????????????
            frontEntryReportService.frontEntryList2(hotelCode, businessDate.toString());//??????????????????
            frontReceiveReportService.receiveList(hotelCode, businessDate.toString());//??????????????????
            //?????????????????????...
            //...
            grl.setAuditStatus("success");
            generateReportsLogService.add(grl);
            businessSeqService.plusBuinessDate(hotelCode);//????????????+1
        } catch (Exception e) {
            e.printStackTrace();
            grl.setAuditStatus("error");
            grl.setReason(e.getMessage());
            generateReportsLogService.add(grl);
        }
        return hr.ok("??????????????????");
    }

    @Override
    @Transactional
    public HttpResponse storedProcedure(String hotelCode){
        LocalDate businessDate = businessSeqService.getBuinessDate(hotelCode);
        HttpResponse hr = new HttpResponse();
        Map<String, Object> map = new HashMap<>();
//        map.put("cId", id);
        List<Map<String, Object>> list = sqlTemplateService.storedProcedure(hotelCode, businessDate, "CheckInRecordHis", map);
//        List<CheckInRecord> cirs = checkInRecordDao.findByHotelCodeAndStatus(hotelCode, Constants.Status.CHECKIN_RECORD_STATUS_CHECK_OUT);
        List<CheckInRecord> cirs = checkInRecordDao.getObybusinssDate(hotelCode, Constants.Status.CHECKIN_RECORD_STATUS_CHECK_OUT, businessDate);
        for(int i=0; i<cirs.size(); i++){
            CheckInRecord cir = cirs.get(i);
            String orderNum = cir.getOrderNum();
            int nO = checkInRecordDao.isNotCheckOut(orderNum, hotelCode);
            if(nO > 0){
                //????????????????????????
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return hr.error("?????????????????????O????????????");
            }
            cir.setStatus("D");
            checkInRecordDao.save(cir);
        }
        hr.setData(list);
        return hr;
    }
}
