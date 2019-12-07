package com.kry.pms.dao.report;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.report.FrontEntryReport;
import com.kry.pms.model.persistence.report.FrontReceiveReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface FrontReceiveReportDao extends BaseDao<FrontReceiveReport> {


    @Query(nativeQuery = true,
    value = " select  " +
            "  tb.pay,  " +
            "  tb.operation_employee_id empId, " +
            "  te.`name` empName, " +
            "  tb.shift_code shiftCode, " +
            "  tpt.code_name codeName,  " +
            "  td.`name` dept, " +
            "  td.id deptId " +
            " from t_bill tb   " +
            "  left join t_product tp on tb.product_id = tp.id  " +
            "  left join t_product_type tpt on tp.id = tpt.product_id  " +
            "  left join t_employee te on tb.operation_employee_id = te.id " +
            "  left join t_department td on te.department_id = td.id " +
            " where tb.pay is not null  " +
            "  and if(:hotelCode is not null && :hotelCode != '', tb.hotel_code=:hotelCode, 1=1 ) " +
            "  and if(:businessDate is not null && :businessDate != '', DATE_FORMAT(tb.business_date, '%Y-%m-%d') =:businessDate, 1=1 ) " )
    List<Map<String, Object>> getList(@Param("hotelCode") String hotelCode, @Param("businessDate") String businessDate);

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
    List<Map<String, Object>> getTotal(@Param("hotelCode") String hotelCode, @Param("businessDate") String businessDate);

    List<FrontReceiveReport> findByHotelCodeAndBusinessDate(String hotelCode, LocalDate businessDate);

    @Query(nativeQuery = true, value = " select  " +
            "   code_name, code_num, category_id, sum(cost) cost " +
            "   from t_report_frontentry " +
            "   where 1=1 " +
            "  and if(:hotelCode is not null && :hotelCode != '', hotel_code=:hotelCode, 1=1 ) " +
            "  and if(:businessDate is not null && :businessDate != '', DATE_FORMAT(business_date, '%Y-%m-%d') =:businessDate, 1=1 ) "+
            "  and if(:cashier is not null && :cashier != '', cashier=:cashier, 1=1 ) " +
            "  group by code_name, code_num, category_id ")
    List<Map<String, Object>> getTotal2(@Param("hotelCode") String hotelCode, @Param("businessDate") String businessDate, @Param("cashier") String cashier);

    @Query(nativeQuery = true, value = " select * " +
            "   from t_report_frontentry " +
            "   where 1=1 " +
            "  and if(:hotelCode is not null && :hotelCode != '', hotel_code=:hotelCode, 1=1 ) " +
            "  and if(:businessDate is not null && :businessDate != '', DATE_FORMAT(business_date, '%Y-%m-%d') =:businessDate, 1=1 ) "+
            "  and if(:code_num is not null && :code_num != '', code_num=:code_num, 1=1 ) " +
            "  and if(:cashier is not null && :cashier != '', cashier=:cashier, 1=1 ) ")
    List<FrontEntryReport> getFrontList(@Param("hotelCode") String hotelCode, @Param("businessDate") String businessDate, @Param("code_num") String code_num, @Param("cashier") String cashier);


    @Query(nativeQuery = true, value = " select  " +
            " t.dept_id deptId, " +
            " t.dept, " +
            " t.shift_code shiftCode, " +
            " t.emp_name empName, " +
            " sum(if(t.code_name='人民币现金', pay, 0)) as 'cash',  " +
            "  sum(if(t.code_name='微信扫码', pay, 0)) as 'wechatPay',  " +
            "  sum(if(t.code_name='支付宝收款', pay, 0)) as 'alipay', " +
            "  sum(if(t.code_name='银行卡', pay, 0)) as 'bankCard',  " +
            "  sum(if(t.code_name='支票', pay, 0)) as 'check', " +
            "  sum(if(t.code_name='其它收款', pay, 0)) as 'otherPay', " +
            "  sum(t.pay) total_pay " +
            " from t_report_front_receive t " +
            " where 1=1 " +
            "  and if(:hotelCode is not null && :hotelCode != '', hotel_code=:hotelCode, 1=1 ) " +
            "  and if(:businessDate is not null && :businessDate != '', DATE_FORMAT(business_date, '%Y-%m-%d') =:businessDate, 1=1 ) "+
            "  and if(:deptId is not null && :deptId != '', dept_id=:deptId, 1=1 ) " +
            "  and if(:empName is not null && :empName != '', emp_name=:empName, 1=1 ) " +
            "  group by t.dept_id, t.shift_code, t.emp_name ")
    List<Map<String, Object>> getFrontReceiveList(@Param("hotelCode")String hotelCode, @Param("businessDate")String businessDate,
                                                 @Param("deptId")String deptId, @Param("empName")String empName);
}
