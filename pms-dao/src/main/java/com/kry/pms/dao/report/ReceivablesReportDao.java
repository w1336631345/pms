package com.kry.pms.dao.report;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.report.ReceivablesReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReceivablesReportDao extends BaseDao<ReceivablesReport> {

    @Query(nativeQuery = true,
            value = " select  " +
                    "  t.deptId, " +
                    "  t.dept, " +
                    "  t.shift_code, " +
                    "  t.`name`, " +
                    "  sum(if(t.code_name='人民币现金', pay, 0)) as 'cash',  " +
                    "  sum(if(t.code_name='微信扫码', pay, 0)) as 'wechatPay',  " +
                    "  sum(if(t.code_name='支付宝收款', pay, 0)) as 'alipay', " +
                    "  sum(if(t.code_name='银行卡', pay, 0)) as 'bankCard',  " +
                    "  sum(if(t.code_name='其它收款', pay, 0)) as 'otherPay', " +
                    "  sum(t.pay) total_pay " +
                    " from " +
                    " (select  " +
                    "  sum(tb.pay) pay,  " +
                    "  tb.operation_employee_id, " +
                    "  te.`name`, " +
                    "  tb.shift_code, " +
                    "  tpt.code_name,  " +
                    "  td.`name` dept, " +
                    "  td.id deptId " +
                    " from t_bill tb   " +
                    "  left join t_product tp on tb.product_id = tp.id  " +
                    "  left join t_product_type tpt on tp.id = tpt.product_id  " +
                    "  left join t_employee te on tb.operation_employee_id = te.id " +
                    "  left join t_department td on te.department_id = td.id " +
                    " where tb.pay is not null  " +
                    "  and if(:hotelCode is not null && :hotelCode != '', tb.hotel_code=:hotelCode, 1=1 ) " +
                    "  and if(:businessDate is not null && :businessDate != '', DATE_FORMAT(tb.business_date, '%Y-%m-%d') =:businessDate, 1=1 ) " +
                    "  group by tpt.code_, tpt.code_name,tb.operation_employee_id, " +
                    " te.`name`,tb.shift_code, tpt.type_name, td.id ) t " +
                    "  group by t.deptId, t.shift_code, t.`name` order by t.deptId " )
    List<Map<String, Object>> totalByTypeName(@Param("hotelCode")String hotelCode, @Param("businessDate") String businessDate);

    //参数必须与totalByTypeName保持一致
    @Query(nativeQuery = true,
            value = " select  " +
                    "  t.deptId, " +
                    "  t.dept, " +
                    " sum(if(t.code_name='人民币现金', pay, 0)) as 'cash',  " +
                    "  sum(if(t.code_name='微信扫码', pay, 0)) as 'wechatPay',  " +
                    "  sum(if(t.code_name='支付宝收款', pay, 0)) as 'alipay', " +
                    "  sum(if(t.code_name='银行卡', pay, 0)) as 'bankCard',  " +
                    "  sum(if(t.code_name='其它收款', pay, 0)) as 'otherPay', " +
                    "  sum(t.pay) total_pay " +
                    " from " +
                    " (select  " +
                    "  sum(tb.pay) pay,   " +
                    "  tpt.code_name,  " +
                    "  td.`name` dept, " +
                    "  td.id deptId " +
                    " from t_bill tb   " +
                    "  left join t_product tp on tb.product_id = tp.id  " +
                    "  left join t_product_type tpt on tp.id = tpt.product_id  " +
                    "  left join t_employee te on tb.operation_employee_id = te.id " +
                    "  left join t_department td on te.department_id = td.id " +
                    " where tb.pay is not null  " +
                    "  and if(:hotelCode is not null && :hotelCode != '', tb.hotel_code=:hotelCode, 1=1 ) " +
                    "  and if(:businessDate is not null && :businessDate != '', DATE_FORMAT(tb.business_date, '%Y-%m-%d') =:businessDate, 1=1 ) " +
                    "  group by tpt.code_, tpt.code_name, tpt.type_name, td.id ) t " +
                    "  group by t.deptId order by t.deptId " )
    List<Map<String, Object>> totalByTypeNameAll(@Param("hotelCode")String hotelCode, @Param("businessDate") String businessDate);

    List<ReceivablesReport> findByHotelCodeAndBusinessDate(String hotelCode, LocalDate businessDate);
}
