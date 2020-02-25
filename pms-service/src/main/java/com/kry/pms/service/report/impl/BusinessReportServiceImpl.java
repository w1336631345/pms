package com.kry.pms.service.report.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.report.BusinessReportDao;
import com.kry.pms.dao.report.ReportDao;
import com.kry.pms.model.persistence.report.BusinessReport;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.report.BusinessReportService;
import com.kry.pms.service.report.RoomReportService;
import org.apache.commons.collections4.MapUtils;
import org.hibernate.SQLQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class BusinessReportServiceImpl implements BusinessReportService {

    @Autowired
    BusinessReportDao businessReportDao;
    @Autowired
    EntityManager entityManager;
    @Autowired
    RoomReportService roomReportService;
    @Autowired
    ReportDao reportDao;

    @Override
    public BusinessReport add(BusinessReport entity) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public BusinessReport modify(BusinessReport businessReport) {
        return null;
    }

    @Override
    public BusinessReport findById(String id) {
        return null;
    }

    @Override
    public List<BusinessReport> getAllByHotelCode(String code) {
        return null;
    }

    @Override
    public PageResponse<BusinessReport> listPage(PageRequest<BusinessReport> prq) {
        return null;
    }

    @Override
    public HttpResponse saveReportAll(User user, String projectType, String businessDate) {
        HttpResponse hr = new HttpResponse();
        try {
            //保存房间费用统计信息
            saveReport(user.getHotelCode(),null, businessDate);
            //保存客房数量统计
            roomReportService.saveRoomStatus(user, businessDate);
        }catch (Exception e) {
            //这里保存夜审记录信息是否成功
        }
        return hr;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public HttpResponse saveReport(String hotelCode, String projectType, String businessDate){
        HttpResponse hr = new HttpResponse();
        List<BusinessReport> list = businessReportDao.getByBusinessDate(businessDate, hotelCode, Constants.ReportProjectType.REPORT_ROOM_RATE);
        if(list != null && !list.isEmpty()){
            //如果不为空，表明今日已经入报表
            return hr.ok("今日已完成");
        }
        String year = "%Y";
        String month = "%Y-%m";
        String day = "%Y-%m-%d";
        String yearTime = businessDate.substring(0,4);
        String monthTime = businessDate.substring(0,7);
        String dayTime = businessDate;
        String yearSql = getSql(hotelCode, year, yearTime);
        String monthSql = getSql(hotelCode, month, monthTime);
        String daySql = getSql(hotelCode, day, dayTime);
        Query queryYear = entityManager.createNativeQuery(yearSql);
        queryYear.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Query queryMonth = entityManager.createNativeQuery(monthSql);
        queryMonth.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Query queryDay = entityManager.createNativeQuery(daySql);
        queryDay.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> listYear = queryYear.getResultList();
        List<Map<String, Object>> listMonth = queryMonth.getResultList();
        List<Map<String, Object>> listDay = queryDay.getResultList();

        double totalDay = 0;
        double totalDayRe = 0;
        double totalMonth = 0;
        double totalMonthRe = 0;
        double totalYear = 0;
        double totalYearRe = 0;
        for(int i=0; i<listYear.size(); i++){//有日必会有月和有年数据。反推循环
            Map<String, Object> map =listYear.get(i);
            String categoryId = MapUtils.getString(map,"category_id");

                totalYear = totalYear + MapUtils.getDouble(map,"total_cost");
                totalYearRe = totalYearRe + MapUtils.getDouble(map,"RE");
                BusinessReport br = new BusinessReport();
                br.setHotelCode(hotelCode);
                br.setBusinessDate(LocalDate.parse(businessDate));
                br.setSort(Constants.ReportSort.REPORT_ROOM_RATE);
                br.setProject(MapUtils.getString(map,"code_name"));
                br.setTotalYear(MapUtils.getString(map,"total_cost"));
                br.setRebateYear(MapUtils.getString(map,"RE"));
                for(int j=0; j<listMonth.size(); j++){
                    Map<String, Object> mapM = listMonth.get(j);
                    String categoryIdM = MapUtils.getString(mapM,"category_id");
                    if(categoryIdM.equals(categoryId)){
                        br.setTotalMonth(MapUtils.getString(mapM,"total_cost"));
                        br.setRebateMonth(MapUtils.getString(mapM,"RE"));
                        totalMonth = totalMonth + MapUtils.getDouble(mapM,"total_cost");
                        totalMonthRe = totalMonthRe + MapUtils.getDouble(mapM,"RE");
                    }
                }
                for(int j=0; j<listDay.size(); j++){
                    Map<String, Object> mapD = listDay.get(j);
                    String categoryIdD = (String)mapD.get("category_id");
                    if(categoryIdD.equals(categoryId)){
                        br.setTotalDay(MapUtils.getString(mapD,"total_cost"));
                        br.setRebateDay(MapUtils.getString(mapD,"RE"));
                        totalDay = totalDay + MapUtils.getDouble(mapD,"total_cost");
                        totalDayRe = totalDayRe + MapUtils.getDouble(mapD,"RE");
                    }
                }
                br.setProjectType(Constants.ReportProjectType.REPORT_ROOM_RATE);
                businessReportDao.save(br);
        }
        BusinessReport brTotal = new BusinessReport();
        brTotal.setHotelCode(hotelCode);
        brTotal.setBusinessDate(LocalDate.parse(businessDate));
        brTotal.setSort(Constants.ReportSort.REPORT_ROOM_RATE);
        brTotal.setProjectType(Constants.ReportProjectType.REPORT_ROOM_RATE);
        brTotal.setNumber_("一");
        brTotal.setProject("营业状况统计");
        brTotal.setTotalDay(totalDay+"");
        brTotal.setRebateDay(totalDayRe+"");
        brTotal.setTotalMonth(totalMonth+"");
        brTotal.setRebateMonth(totalMonthRe+"");
        brTotal.setTotalYear(totalYear+"");
        brTotal.setRebateYear(totalYearRe+"");
        businessReportDao.save(brTotal);
        return hr.ok();
    }

    @Override
    public List<BusinessReport> getByBusinessDate(User user, String businessDate) {
        List<BusinessReport> list = businessReportDao.getByBusinessDate(businessDate, user.getHotelCode(), null);
        return list;
    }

    public String getSql(String hotelCode, String dateType, String dateTime){
        String qlString = "select " +
                "  ifnull(t.category_id, 'N/A') category_id,  " +
                "  t.productTypeName code_name, " +
                "  sum(if(t.type_='RE', cost, 0)) as 'RE',  " +
                "  sum(t.cost) total_cost " +
                "  from " +
                "  (select  " +
                "    sum(tb.cost) cost,   " +
                "    if(tpt.category_id='11', '10', tpt.category_id) category_id, " +
                "    tpt.type_, " +
                "    if(tpc.`name`='客房其它收入', '客房房費', tpc.`name`) productTypeName " +
                "  from t_bill tb   " +
                "   left join t_product tp on tb.product_id = tp.id  " +
                "   left join t_product_type tpt on tp.id = tpt.product_id  " +
                "   left join t_product_category tpc on tpt.category_id = tpc.id  " +
                "  where tb.cost is not null  " +
                "   and tb.hotel_code= '" +hotelCode +"'"+
                "   and DATE_FORMAT(tb.business_date, '"+dateType+"') = '" + dateTime +"'"+
                "   group by   " +
                "   tpt.category_id,   " +
                "   tpt.type_,  " +
                "   tpc.`name` ) t  " +
                "   GROUP BY t.category_id ";

        return qlString;
    }

    //稽核报表-营业日报表-d房租收入
    @Override
    public HttpResponse costByGroupType(String hotelCode, String businessDate) {
        HttpResponse hr = new HttpResponse();
        List<BusinessReport> listReport = businessReportDao.getByBusinessDate(businessDate, hotelCode, Constants.ReportProjectType.REPORT_ROOM_NUM_D);
        if(listReport != null && !listReport.isEmpty()){
            //如果不为空，表明今日已经入报表
            return hr.ok("今日已完成");
        }
        String month = businessDate.substring(0,7);
        String year = businessDate.substring(0,4);
        List<Map<String, Object>> list = reportDao.costByGroupType(hotelCode,businessDate, month, year);

        for(int i=0; i<list.size(); i++){
            Map<String, Object> map = list.get(i);
            BusinessReport br = new BusinessReport();
            Object name = map.get("name");
            if(name == null){
                br.setProjectType(Constants.ReportProjectType.REPORT_ROOM_NUM_D);
                br.setHotelCode(hotelCode);
                br.setBusinessDate(LocalDate.parse(businessDate));
                br.setProject(Constants.ReportProject.REPORT_ROOM_CHECKIN_D);
                br.setTotalDay(MapUtils.getString(map,"totalDay"));
                br.setTotalMonth(MapUtils.getString(map,"totalMonth"));
                br.setTotalYear(MapUtils.getString(map,"totalYear"));
                br.setSort(Constants.ReportSort.REPORT_ROOM_CHECKIN_D);
            }else {
                br.setProjectType(Constants.ReportProjectType.REPORT_ROOM_NUM_D);
                br.setHotelCode(hotelCode);
                br.setBusinessDate(LocalDate.parse(businessDate));
                br.setProject(MapUtils.getString(map,"name"));
                br.setTotalDay(MapUtils.getString(map,"totalDay"));
                br.setTotalMonth(MapUtils.getString(map,"totalMonth"));
                br.setTotalYear(MapUtils.getString(map,"totalYear"));
                br.setSort(Constants.ReportSort.REPORT_ROOM_CHECKIN_D);
            }
            businessReportDao.save(br);
        }
        return hr;
    }

    //稽核报表-营业日报表-e、平均房价
    @Override
    public HttpResponse costByGroupTypeAvg(String hotelCode, String businessDate) {
        HttpResponse hr = new HttpResponse();
        List<BusinessReport> listReport = businessReportDao.getByBusinessDate(businessDate, hotelCode, Constants.ReportProjectType.REPORT_ROOM_NUM_E);
        if(listReport != null && !listReport.isEmpty()){
            //如果不为空，表明今日已经入报表
            return hr.ok("今日已完成");
        }
        String month = businessDate.substring(0,7);
        String year = businessDate.substring(0,4);
        List<Map<String, Object>> list = reportDao.costByGroupTypeAvg(hotelCode,businessDate, month, year);

        for(int i=0; i<list.size(); i++){
            Map<String, Object> map = list.get(i);
            BusinessReport br = new BusinessReport();
            Object name = map.get("name");
            if(name == null){
                br.setProjectType(Constants.ReportProjectType.REPORT_ROOM_NUM_E);
                br.setHotelCode(hotelCode);
                br.setBusinessDate(LocalDate.parse(businessDate));
                br.setProject(Constants.ReportProject.REPORT_ROOM_CHECKIN_E);
                br.setTotalDay(MapUtils.getString(map,"totalDay"));
                br.setTotalMonth(MapUtils.getString(map,"totalMonth"));
                br.setTotalYear(MapUtils.getString(map,"totalYear"));
                br.setSort(Constants.ReportSort.REPORT_ROOM_CHECKIN_E);
            }else {
                br.setProjectType(Constants.ReportProjectType.REPORT_ROOM_NUM_E);
                br.setHotelCode(hotelCode);
                br.setBusinessDate(LocalDate.parse(businessDate));
                br.setProject(MapUtils.getString(map,"name"));
                br.setTotalDay(MapUtils.getString(map,"totalDay"));
                br.setTotalMonth(MapUtils.getString(map,"totalMonth"));
                br.setTotalYear(MapUtils.getString(map,"totalYear"));
                br.setSort(Constants.ReportSort.REPORT_ROOM_CHECKIN_E);
            }
            businessReportDao.save(br);
        }
        return hr;
    }
}
