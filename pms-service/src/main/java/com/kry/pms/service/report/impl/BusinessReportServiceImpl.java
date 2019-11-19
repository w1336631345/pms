package com.kry.pms.service.report.impl;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.report.BusinessReportDao;
import com.kry.pms.model.persistence.report.BusinessReport;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.report.BusinessReportService;
import org.hibernate.SQLQuery;
import org.hibernate.query.internal.QueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BusinessReportServiceImpl implements BusinessReportService {

    @Autowired
    BusinessReportDao businessReportDao;
    @Autowired
    EntityManager entityManager;


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
    @Transactional(rollbackFor=Exception.class)
    public HttpResponse saveReport(User user, String projectType, String businessDate){
        HttpResponse hr = new HttpResponse();
        String year = "%Y";
        String month = "%Y-%m";
        String day = "%Y-%m-%d";
        String yearTime = businessDate.substring(0,4);
        String monthTime = businessDate.substring(0,7);
        String dayTime = businessDate;
        String yearSql = getSql(user.getHotelCode(), year, yearTime);
        String monthSql = getSql(user.getHotelCode(), month, monthTime);
        String daySql = getSql(user.getHotelCode(), day, dayTime);
        Query queryYear = entityManager.createNativeQuery(yearSql);
        queryYear.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Query queryMonth = entityManager.createNativeQuery(monthSql);
        queryMonth.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Query queryDay = entityManager.createNativeQuery(daySql);
        queryDay.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> listYear = queryYear.getResultList();
        List<Map<String, Object>> listMonth = queryMonth.getResultList();
        List<Map<String, Object>> listDay = queryDay.getResultList();
        List<BusinessReport> list = businessReportDao.getByBusinessDate(businessDate, user.getHotelCode());
        double totalDay = 0;
        double totalDayRe = 0;
        double totalMonth = 0;
        double totalMonthRe = 0;
        double totalYear = 0;
        double totalYearRe = 0;
        if(list != null && !list.isEmpty()){
            //如果不为空，表明今日已经入报表
        }else {
            for(int i=0; i<listYear.size(); i++){//有日必会有月和有年数据。反推循环
                Map<String, Object> map =listYear.get(i);
                String categoryId = map.get("category_id").toString();

                    totalYear = totalYear + (double)map.get("total_cost");
                    totalYearRe = totalYearRe + (double)map.get("RE");
                    BusinessReport br = new BusinessReport();
                    br.setHotelCode(user.getHotelCode());
                    br.setBusinessDate(LocalDate.parse(businessDate));
                    br.setSort("1");
                    br.setProject(map.get("code_name").toString());
                    br.setTotalYear((double)map.get("total_cost"));
                    br.setRebateYear((double)map.get("RE"));
                    for(int j=0; j<listMonth.size(); j++){
                        Map<String, Object> mapM = listMonth.get(j);
                        String categoryIdM = mapM.get("category_id").toString();
                        if(categoryIdM.equals(categoryId)){
                            br.setTotalMonth((double)mapM.get("total_cost"));
                            br.setRebateMonth((double)mapM.get("RE"));
                            totalMonth = totalMonth + (double)mapM.get("total_cost");
                            totalMonthRe = totalMonthRe + (double)mapM.get("RE");
                        }
                    }
                    for(int j=0; j<listDay.size(); j++){
                        Map<String, Object> mapD = listDay.get(j);
                        String categoryIdD = mapD.get("category_id").toString();
                        if(categoryIdD.equals(categoryId)){
                            br.setTotalDay((double)mapD.get("total_cost"));
                            br.setRebateDay((double)mapD.get("RE"));
                            totalDay = totalDay + (double)mapD.get("total_cost");
                            totalDayRe = totalDayRe + (double)mapD.get("RE");
                        }
                    }
                    businessReportDao.save(br);
            }
            BusinessReport brTotal = new BusinessReport();
            brTotal.setHotelCode(user.getHotelCode());
            brTotal.setBusinessDate(LocalDate.parse(businessDate));
            brTotal.setSort("1");
            brTotal.setNumber_("一");
            brTotal.setProject("营业状况统计");
            brTotal.setTotalDay(totalDay);
            brTotal.setRebateDay(totalDayRe);
            brTotal.setTotalMonth(totalMonth);
            brTotal.setRebateMonth(totalMonthRe);
            brTotal.setTotalYear(totalYear);
            brTotal.setRebateYear(totalYearRe);
            businessReportDao.save(brTotal);
        }
        return hr.ok();
    }

    @Override
    public List<BusinessReport> getByBusinessDate(User user, String businessDate) {
        List<BusinessReport> list = businessReportDao.getByBusinessDate(businessDate, user.getHotelCode());
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
}
