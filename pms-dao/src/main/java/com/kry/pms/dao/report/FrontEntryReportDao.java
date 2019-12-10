package com.kry.pms.dao.report;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.report.FrontEntryReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface FrontEntryReportDao extends BaseDao<FrontEntryReport> {


    @Query(nativeQuery = true,
    value = "  select  " +
            " ta.`code` accountNum, " +
            " tgr.room_num, " +
            " ta.`name` custName, " +
            " tpt.code_name,   " +
            " tb.cost, " +
            " DATE_FORMAT(tb.create_date,'%Y-%m-%d %T') create_date ," +
            " te.`name` cashier, " +
            " tb.shift_code, " +
            " tpt.code_ codeNum,   " +
            " tpt.category_id, " +
            " tb.business_date "+
            " from t_bill tb   " +
            " left join t_product tp on tb.product_id = tp.id   " +
            " left join t_product_type tpt on tp.id = tpt.product_id   " +
            " left join t_employee te on tb.operation_employee_id = te.id " +
            " left join t_account ta on tb.account_id = ta.id " +
            " left join t_checkin_record tcr on ta.id = tcr.account_id " +
            " left join t_guest_room tgr on tcr.guest_room_id = tgr.id " +
            " where tb.cost is not null   " +
            "  and if(:hotelCode is not null && :hotelCode != '', tb.hotel_code=:hotelCode, 1=1 ) " +
            "  and if(:businessDate is not null && :businessDate != '', DATE_FORMAT(tb.business_date, '%Y-%m-%d') =:businessDate, 1=1 ) " +
            "  and if(:code_ is not null && :code_ != '', tpt.code_=:code_, 1=1 ) " )
    List<Map<String, Object>> getList(@Param("hotelCode")String hotelCode, @Param("businessDate") String businessDate, @Param("code_") String code_);

    @Query(nativeQuery = true, value = " select " +
            " tpt.code_name,   " +
            " tpt.code_ codeNum,   " +
            " tpt.category_id, " +
            " sum(tb.cost) cost " +
            " from t_bill tb    " +
            " left join t_product tp on tb.product_id = tp.id   " +
            " left join t_product_type tpt on tp.id = tpt.product_id   " +
            " where tb.cost is not null   " +
            "  and if(:hotelCode is not null && :hotelCode != '', tb.hotel_code=:hotelCode, 1=1 ) " +
            "  and if(:businessDate is not null && :businessDate != '', DATE_FORMAT(tb.business_date, '%Y-%m-%d') =:businessDate, 1=1 ) "+
            " group by tpt.code_name,  tpt.code_, tpt.category_id ")
    List<Map<String, Object>> getTotal(@Param("hotelCode")String hotelCode, @Param("businessDate") String businessDate);

    List<FrontEntryReport> findByHotelCodeAndBusinessDate(String hotelCode, LocalDate businessDate);

    @Query(nativeQuery = true, value = " select  " +
            "   code_name, code_num, category_id, sum(cost) cost " +
            "   from t_report_frontentry " +
            "   where 1=1 " +
            "  and if(:hotelCode is not null && :hotelCode != '', hotel_code=:hotelCode, 1=1 ) " +
            "  and if(:businessDate is not null && :businessDate != '', DATE_FORMAT(business_date, '%Y-%m-%d') =:businessDate, 1=1 ) "+
            "  and if(:cashier is not null && :cashier != '', cashier=:cashier, 1=1 ) " +
            "  group by code_name, code_num, category_id ")
    List<Map<String, Object>> getTotal2(@Param("hotelCode")String hotelCode, @Param("businessDate") String businessDate, @Param("cashier") String cashier);

    @Query(nativeQuery = true, value = " select * " +
            "   from t_report_frontentry " +
            "   where 1=1 " +
            "  and if(:hotelCode is not null && :hotelCode != '', hotel_code=:hotelCode, 1=1 ) " +
            "  and if(:businessDate is not null && :businessDate != '', DATE_FORMAT(business_date, '%Y-%m-%d') =:businessDate, 1=1 ) "+
            "  and if(:code_num is not null && :code_num != '', code_num=:code_num, 1=1 ) " +
            "  and if(:cashier is not null && :cashier != '', cashier=:cashier, 1=1 ) ")
    List<FrontEntryReport> getFrontList(@Param("hotelCode")String hotelCode, @Param("businessDate")String businessDate, @Param("code_num")String code_num, @Param("cashier")String cashier);
}
