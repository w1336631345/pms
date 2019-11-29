package com.kry.pms.service.report.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.report.BusinessReportDao;
import com.kry.pms.dao.report.RoomReportDao;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.DailyVerify;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.model.persistence.report.BusinessReport;
import com.kry.pms.model.persistence.report.RoomReport;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.busi.RoomRecordService;
import com.kry.pms.service.report.BusinessReportService;
import com.kry.pms.service.report.RoomReportService;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.plaf.basic.BasicScrollPaneUI;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomReportServiceImpl implements RoomReportService {

    @Autowired
    RoomReportDao roomReportDao;
    @Autowired
    BusinessReportDao businessReportDao;

    @Override
    public HttpResponse copyData(String businessDate) {
        //每天一次定时在凌晨2-6点
        HttpResponse hr = new HttpResponse();
        roomReportDao.copyData(businessDate);
        return hr.ok("房间状态数据copy完成");
    }

    @Override
    public RoomReport add(RoomReport entity) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public RoomReport modify(RoomReport roomReport) {
        return null;
    }

    @Override
    public RoomReport findById(String id) {
        return null;
    }

    @Override
    public List<RoomReport> getAllByHotelCode(String code) {
        return null;
    }

    @Override
    public PageResponse<RoomReport> listPage(PageRequest<RoomReport> prq) {
        return null;
    }

    @Override
    public List<RoomReport> getByHotelCodeAndBusinessDate(User user, LocalDate businessDate){
        List<RoomReport> list = roomReportDao.getByHotelCodeAndBusinessDate(user.getHotelCode(), businessDate);
        return list;
    }
    //夜审时，生成营业日报客房统计
    @Override
    @Transactional(rollbackFor=Exception.class)
    public HttpResponse saveRoomStatus(User user, String businessDate){
        HttpResponse hr = new HttpResponse();
        List<BusinessReport> list = businessReportDao.getByBusinessDate(businessDate, user.getHotelCode(),Constants.ReportProjectType.REPORT_ROOM_NUM_A);

        BusinessReport otatlAll = new BusinessReport();
        otatlAll.setHotelCode(user.getHotelCode());
        otatlAll.setBusinessDate(LocalDate.parse(businessDate));
        otatlAll.setSort("2");
        otatlAll.setNumber_("二");
        otatlAll.setProject("客房");
        businessReportDao.save(otatlAll);

        String month = businessDate.substring(0,7);
        String year = businessDate.substring(0,4);
        List<Map<String, Object>> listYear = roomReportDao.getTotalRoomStatusYear(user.getHotelCode(), year);
        for(int i=0; i<listYear.size(); i++){
            Map<String, Object> mapYear = listYear.get(i);
            Map<String, Object> mapDay = new HashMap<>();
            Map<String, Object> mapMonth = new HashMap<>();
            List<Map<String, Object>> listMonth = roomReportDao.getTotalRoomStatus(user.getHotelCode(), month, mapYear.get("project").toString());
            if(listMonth != null && !listMonth.isEmpty()){
                mapMonth = listMonth.get(0);
            }
            List<Map<String, Object>> listDay = roomReportDao.getTotalRoomStatus(user.getHotelCode(), businessDate, mapYear.get("project").toString());
            if(listMonth != null && !listDay.isEmpty()){
                mapDay = listMonth.get(0);
            }
            BusinessReport brTotal = new BusinessReport();
            brTotal.setHotelCode(user.getHotelCode());
            brTotal.setBusinessDate(LocalDate.parse(businessDate));
            brTotal.setSort("2");
//            brTotal.setNumber_("二");
            brTotal.setProject(mapYear.get("project").toString());
            brTotal.setTotalDay(mapDay.get("totalDay").toString());
            brTotal.setTotalMonth(mapMonth.get("totalMonth").toString());
            brTotal.setTotalYear(mapYear.get("totalYear").toString());
            businessReportDao.save(brTotal);
        }

        return hr;
    }
    //导入报表 a、客房总数
    @Override
    @Transactional(rollbackFor=Exception.class)
    public HttpResponse totalRoomStatusAll(User user, String businessDate){
        HttpResponse hr = new HttpResponse();
        List<BusinessReport> isNull = businessReportDao.getByBusinessDate(businessDate, user.getHotelCode(),Constants.ReportProjectType.REPORT_ROOM_NUM_A);
        if(isNull != null && !isNull.isEmpty()){
            return hr.ok("今日已完成");
        }
        String month = businessDate.substring(0,7);
        String year = businessDate.substring(0,4);
        List<Map<String, Object>> list = roomReportDao.totalRoomStatusAll(user.getHotelCode(),businessDate, month, year);
        BusinessReport otatlAll = new BusinessReport();
        otatlAll.setHotelCode(user.getHotelCode());
        otatlAll.setBusinessDate(LocalDate.parse(businessDate));
        otatlAll.setSort(Constants.ReportSort.REPORT_ROOM_CHECKIN_A);
        otatlAll.setProjectType(Constants.ReportProjectType.REPORT_ROOM_NUM_A);
        otatlAll.setNumber_("二");
        otatlAll.setProject("客房");
        BusinessReport total = new BusinessReport();
        BusinessReport available = new BusinessReport();
        BusinessReport repair = new BusinessReport();
        BusinessReport locked = new BusinessReport();
        total.setSort(Constants.ReportSort.REPORT_ROOM_CHECKIN_A);
        total.setProjectType(Constants.ReportProjectType.REPORT_ROOM_NUM_A);
        total.setHotelCode(user.getHotelCode());
        total.setProject(Constants.ReportProject.REPORT_ROOM_CHECKIN_A);
        total.setBusinessDate(LocalDate.parse(businessDate));
        available.setSort(Constants.ReportSort.REPORT_ROOM_CHECKIN_A);
        available.setProjectType(Constants.ReportProjectType.REPORT_ROOM_NUM_A);
        available.setHotelCode(user.getHotelCode());
        available.setProject("可用房");
        available.setBusinessDate(LocalDate.parse(businessDate));
        repair.setSort(Constants.ReportSort.REPORT_ROOM_CHECKIN_A);
        repair.setProjectType(Constants.ReportProjectType.REPORT_ROOM_NUM_A);
        repair.setHotelCode(user.getHotelCode());
        repair.setProject("维修房");
        repair.setBusinessDate(LocalDate.parse(businessDate));
        locked.setSort(Constants.ReportSort.REPORT_ROOM_CHECKIN_A);
        locked.setProjectType(Constants.ReportProjectType.REPORT_ROOM_NUM_A);
        locked.setHotelCode(user.getHotelCode());
        locked.setProject("锁房");
        locked.setBusinessDate(LocalDate.parse(businessDate));
        int totalDay = 0, totalMonth = 0, totalYear = 0;
        for(int i=0; i<list.size(); i++){
            Map<String, Object> map = list.get(i);
            String timeType = map.get("timeType").toString();
            if(("day").equals(timeType)){
                available.setTotalDay(map.get("available_total").toString());
                repair.setTotalDay(map.get("repair_total").toString());
                locked.setTotalDay(map.get("locked_total").toString());
                totalDay = Integer.parseInt(map.get("available_total").toString()) +
                        Integer.parseInt(map.get("repair_total").toString()) + Integer.parseInt(map.get("locked_total").toString()) ;
            }
            if(("month").equals(timeType)){
                available.setTotalMonth(map.get("available_total").toString());
                repair.setTotalMonth(map.get("repair_total").toString());
                locked.setTotalMonth(map.get("locked_total").toString());
                totalMonth = Integer.parseInt(map.get("available_total").toString()) +
                        Integer.parseInt(map.get("repair_total").toString()) + Integer.parseInt(map.get("locked_total").toString()) ;
            }
            if(("year").equals(timeType)){
                available.setTotalYear(map.get("available_total").toString());
                repair.setTotalYear(map.get("repair_total").toString());
                locked.setTotalYear(map.get("locked_total").toString());
                totalYear = Integer.parseInt(map.get("available_total").toString()) +
                        Integer.parseInt(map.get("repair_total").toString()) + Integer.parseInt(map.get("locked_total").toString()) ;
            }
        }
        total.setTotalDay(totalDay+"");
        total.setTotalMonth(totalMonth+"");
        total.setTotalYear(totalYear+"");
        businessReportDao.save(otatlAll);
        businessReportDao.save(total);
        businessReportDao.save(available);
        businessReportDao.save(repair);
        businessReportDao.save(locked);
        return hr;
    }

    //导入报表 b、出租总数
    @Override
    @Transactional(rollbackFor=Exception.class)
    public HttpResponse totalCheckInType(User user, String businessDate){
        HttpResponse hr = new HttpResponse();
        List<BusinessReport> isNull = businessReportDao.getByBusinessDate(businessDate, user.getHotelCode(),Constants.ReportProjectType.REPORT_ROOM_NUM_B);
        if(isNull != null && !isNull.isEmpty()){
            return hr.ok("今日已完成");
        }
        String month = businessDate.substring(0,7);
        String year = businessDate.substring(0,4);
        BusinessReport total = new BusinessReport();
        total.setSort(Constants.ReportSort.REPORT_ROOM_CHECKIN_B);
        total.setProjectType(Constants.ReportProjectType.REPORT_ROOM_NUM_B);
        total.setHotelCode(user.getHotelCode());
        total.setProject(Constants.ReportProject.REPORT_ROOM_CHECKIN_B);
        total.setBusinessDate(LocalDate.parse(businessDate));
        businessReportDao.save(total);
        List<Map<String, Object>> list = roomReportDao.totalCheckInType(user.getHotelCode(),businessDate, month, year);
        for(int i=0; i<list.size(); i++){
            Map<String, Object> map = list.get(i);
            BusinessReport br = new BusinessReport();
            br.setProjectType(Constants.ReportProjectType.REPORT_ROOM_NUM_B);
            br.setHotelCode(user.getHotelCode());
            br.setBusinessDate(LocalDate.parse(businessDate));
            br.setProject(map.get("name").toString());
            br.setTotalDay(map.get("totalDay").toString());
            br.setTotalMonth(map.get("totalMonth").toString());
            br.setTotalYear(map.get("totalYear").toString());
            br.setSort(Constants.ReportSort.REPORT_ROOM_CHECKIN_B);
            businessReportDao.save(br);
        }
        return hr;
    }
    //导入报表 c、售卖率
    @Override
    @Transactional(rollbackFor=Exception.class)
    public HttpResponse availableTotal(User user, String businessDate){
        HttpResponse hr = new HttpResponse();
        List<BusinessReport> isNull = businessReportDao.getByBusinessDate(businessDate, user.getHotelCode(),Constants.ReportProjectType.REPORT_ROOM_NUM_C);
        if(isNull != null && !isNull.isEmpty()){
            return hr.ok("今日已完成");
        }
        String month = businessDate.substring(0,7);
        String year = businessDate.substring(0,4);
        BusinessReport total = new BusinessReport();
        total.setSort(Constants.ReportSort.REPORT_ROOM_CHECKIN_C);
        total.setProjectType(Constants.ReportProjectType.REPORT_ROOM_NUM_C);
        total.setHotelCode(user.getHotelCode());
        total.setProject(Constants.ReportProject.REPORT_ROOM_CHECKIN_C);
        total.setBusinessDate(LocalDate.parse(businessDate));
        businessReportDao.save(total);
        Map<String, Object> mapTotal = roomReportDao.availableTotal(user.getHotelCode(),businessDate, month, year);
        if(mapTotal == null){
            return hr.error("房间可用数统计出错");
        }
        double dayTotal = Double.parseDouble(mapTotal.get("totalDay").toString());
        double monthTotal = Double.parseDouble(mapTotal.get("totalMonth").toString());
        double yearTotal = Double.parseDouble(mapTotal.get("totalYear").toString());
        String dayP = "0",monthP = "0",yearP = "0";
        List<Map<String, Object>> list = roomReportDao.totalCheckInType(user.getHotelCode(),businessDate, month, year);
        DecimalFormat df = new DecimalFormat("##0.00");
        for(int i=0; i<list.size(); i++){
            Map<String, Object> map = list.get(i);
            BusinessReport br = new BusinessReport();
            br.setProjectType(Constants.ReportProjectType.REPORT_ROOM_NUM_C);
            br.setHotelCode(user.getHotelCode());
            br.setBusinessDate(LocalDate.parse(businessDate));
            br.setProject(map.get("name").toString());
            if(dayTotal != 0){
                dayP = df.format(Double.parseDouble(map.get("totalDay").toString())/dayTotal *100) + "%";
                monthP = df.format(Double.parseDouble(map.get("totalMonth").toString())/monthTotal *100) + "%";
                yearP = df.format(Double.parseDouble(map.get("totalYear").toString())/yearTotal *100) + "%";
            }
            br.setTotalDay(dayP);
            br.setTotalMonth(monthP);
            br.setTotalYear(yearP);
            br.setSort(Constants.ReportSort.REPORT_ROOM_CHECKIN_C);
            businessReportDao.save(br);
        }
        return hr;
    }
}
