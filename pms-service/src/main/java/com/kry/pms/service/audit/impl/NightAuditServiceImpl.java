package com.kry.pms.service.audit.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.CheckInRecordDao;
import com.kry.pms.model.persistence.busi.DailyVerify;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.report.GenerateReportsLog;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.audit.NightAuditService;
import com.kry.pms.service.busi.BillService;
import com.kry.pms.service.busi.DailyVerifyService;
import com.kry.pms.service.busi.RoomRecordService;
import com.kry.pms.service.org.EmployeeService;
import com.kry.pms.service.report.*;
import com.kry.pms.service.sys.BusinessSeqService;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
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
     * 功能描述: <br>手动夜审入账
     * 〈〉
     * @Param: [user, ids]——ids暂时没有用到，为前端可以选着性入账准备
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/11/23 15:20
     */
    @Override
    @Transactional
    public HttpResponse manualAdd(User loginUser, String[] ids, String shiftCode) {
        HttpResponse hr = new HttpResponse();
        //入账只入当前营业日期的账
        LocalDate businessDate = businessSeqService.getBuinessDate(loginUser.getHotelCode());
        List<Map<String, Object>> listCount = checkInRecordDao.getStatistics(loginUser.getHotelCode());
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
        if(r+i>0){// 应到未到 + 应退未退
            return hr.error("还有未处理订单！");
        }
        LocalDate now = LocalDate.now();
        if(businessDate.isAfter(now)){
            return hr.error("营业日期大于自然日期");
        }
        DailyVerify dailyVerify = dailyVerifyService.findByHotelCodeAndBusinessDate(loginUser.getHotelCode(), businessDate);
        if(dailyVerify != null){
            return hr.error("今日入账完成，请明日再进行入账操作");
        }
        //默认手动点击入账为当前营业日期的账
//        List<RoomRecord> list = roomRecordService.accountEntryListAll(loginUser.getHotelCode(), businessDate);
        List<Map<String, Object>> list = roomRecordService.accountEntryListAllMap(businessDate, loginUser.getHotelCode(), "NO");
        Employee emp = employeeService.findByUser(loginUser);
//        billService.putAcount(list, businessDate, emp, shiftCode);
        billService.putAcountMap(list, businessDate, emp, shiftCode);
        //入账完成，记录入账记录
        DailyVerify dv = new DailyVerify();
        dv.setOperationEmployee(emp);
        dv.setBusinessDate(businessDate);
        dv.setType(Constants.auditNightMode.NIGHT_AUDIT_MANUAL);//夜审方式，手动
        dv.setHotelCode(loginUser.getHotelCode());
        dv.setVerifyDate(now);
        dv.setCreateDate(LocalDateTime.now());
        dv.setStatus("success");
        dailyVerifyService.add(dv);
        return hr.ok("入账成功");
    }

    //报表导入各个统计-手动
    @Override
    public HttpResponse addReportAll(User loginUser) {
        HttpResponse hr = new HttpResponse();
        LocalDate businessDate = businessSeqService.getBuinessDate(loginUser.getHotelCode());

        DailyVerify dailyVerify = dailyVerifyService.findByHotelCodeAndBusinessDate(loginUser.getHotelCode(), businessDate);
        if(dailyVerify == null){
            return hr.error(99999, "请先夜审入账");
        }
        GenerateReportsLog grl = new GenerateReportsLog();
        grl.setHotelCode(loginUser.getHotelCode());
        grl.setAuditDate(LocalDateTime.now());
        grl.setBusinessDate(businessDate);
        grl.setUserId(loginUser.getId());
        grl.setUsername(loginUser.getUsername());
        grl.setType(Constants.auditNightMode.NIGHT_AUDIT_MANUAL);
        //入账只入当前营业日期的账
        try {
//            hr = businessReportService.saveReport(loginUser.getHotelCode(), null, businessDate.toString());//保存到报表-房费
//            roomReportService.totalRoomStatusAll(loginUser.getHotelCode(), businessDate.toString());//保存到报表-a、客房总数
//            roomReportService.totalCheckInType(loginUser.getHotelCode(), businessDate.toString());//保存到报表-b、出租总数
//            roomReportService.availableTotal(loginUser.getHotelCode(), businessDate.toString());//保存到报表-c、售卖率
//            businessReportService.costByGroupType(loginUser.getHotelCode(), businessDate.toString());//保存到报表-d、房租收入
//            businessReportService.costByGroupTypeAvg(loginUser.getHotelCode(), businessDate.toString());//保存到报表-e、平均房价
//
//            receivablesReportService.totalByTypeName(loginUser.getHotelCode(), businessDate.toString());//收款汇总
//            frontEntryReportService.frontEntryList2(loginUser.getHotelCode(), businessDate.toString());//前台入账报表
//            frontReceiveReportService.receiveList(loginUser.getHotelCode(), businessDate.toString());//前台收款汇总
            //后续继续添加添...
            //...
            grl.setAuditStatus("success");
            generateReportsLogService.add(grl);
            if(businessDate.isBefore(LocalDate.now())){
                businessSeqService.plusBuinessDate(loginUser.getHotelCode());//营业日期+1
            }
        } catch (Exception e) {
            e.printStackTrace();
            grl.setAuditStatus("error");
            grl.setReason(e.getMessage());
            generateReportsLogService.add(grl);
        }
        return hr.ok("报表导入成功");
    }

    //报表导入各个统计-自动
    @Override
    public HttpResponse addReportAllAuto(String hotelCode) {
        HttpResponse hr = new HttpResponse();
        LocalDate businessDate = businessSeqService.getBuinessDate(hotelCode);
        DailyVerify dailyVerify = dailyVerifyService.findByHotelCodeAndBusinessDate(hotelCode, businessDate);
        if(dailyVerify == null){
            return hr.error(99999, "请先夜审入账");
        }
        GenerateReportsLog grl = new GenerateReportsLog();
        grl.setHotelCode(hotelCode);
        grl.setAuditDate(LocalDateTime.now());
        grl.setBusinessDate(businessDate);
        grl.setType(Constants.auditNightMode.NIGHT_AUDIT_AUTO);
        //入账只入当前营业日期的账
        try {
            hr = businessReportService.saveReport(hotelCode, null, businessDate.toString());//保存到报表-房费
            roomReportService.totalRoomStatusAll(hotelCode, businessDate.toString());//保存到报表-a、客房总数
            roomReportService.totalCheckInType(hotelCode, businessDate.toString());//保存到报表-b、出租总数
            roomReportService.availableTotal(hotelCode, businessDate.toString());//保存到报表-c、售卖率
            businessReportService.costByGroupType(hotelCode, businessDate.toString());//保存到报表-d、房租收入
            businessReportService.costByGroupTypeAvg(hotelCode, businessDate.toString());//保存到报表-e、平均房价

            receivablesReportService.totalByTypeName(hotelCode, businessDate.toString());//收款汇总
            frontEntryReportService.frontEntryList2(hotelCode, businessDate.toString());//前台入账报表
            frontReceiveReportService.receiveList(hotelCode, businessDate.toString());//前台收款汇总
            //后续继续添加添...
            //...
            grl.setAuditStatus("success");
            generateReportsLogService.add(grl);
            businessSeqService.plusBuinessDate(hotelCode);//营业日期+1
        } catch (Exception e) {
            e.printStackTrace();
            grl.setAuditStatus("error");
            grl.setReason(e.getMessage());
            generateReportsLogService.add(grl);
        }
        return hr.ok("报表导入成功");
    }
}
