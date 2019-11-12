package com.kry.pms.dao.report;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.busi.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AuditReportDao extends BaseDao<Bill> {

    // " and if(:businessDate is not null && :businessDate != '', tb.business_date=:businessDate, 1=1 ) " +
    @Query(nativeQuery = true,
            value = " select " +
                    " sum(tb.cost) cost,  " +
                    " tb.product_id, " +
                    " tp.`name`, " +
                    " tpt.code_, " +
                    " tpt.code_name, " +
                    " tpt.category_id, " +
                    " tpt.type_, " +
                    " tpt.type_name, " +
                    " tpc.`name` productTypeName " +
                    " from t_bill tb  " +
                    " left join t_product tp on tb.product_id = tp.id " +
                    " left join t_product_type tpt on tp.id = tpt.product_id " +
                    " left join t_product_category tpc on tpt.category_id = tpc.id " +
                    " where tb.cost is not null " +
                    " and if(:hotelCode is not null && :hotelCode != '', tb.hotel_code=:hotelCode, 1=1 ) " +
                    " and if(:businessDate is not null && :businessDate != '', DATE_FORMAT(tb.business_date, '%Y-%m-%d') =:hotelCode, 1=1 ) " +
                    " group by tb.product_id, " +
                    " tp.`name`,  tpt.code_, " +
                    " tpt.code_name, " +
                    " tpt.category_id, " +
                    " tpt.type_, " +
                    " tpt.type_name, " +
                    " tpc.`name` " )
    List<Map<String, Object>> auditNight(@Param("hotelCode")String hotelCode, @Param("businessDate") String businessDate);
    //和auditNight/auditNights接口参数条件必须保持一致
    @Query(nativeQuery = true,
            value = " select  " +
                    " ifnull(t.category_id, 'N/A') category_id, " +
                    " t.productTypeName code_name, " +
                    " sum(if(t.type_='MC', cost, 0)) as 'MC', " +
                    " sum(if(t.type_='RE', cost, 0)) as 'RE', " +
                    " sum(if(t.type_='SE', cost, 0)) as 'SE', " +
                    " sum(if(t.type_='OT', cost, 0)) as 'OT', " +
                    " sum(t.cost) total_cost " +
                    " from " +
                    " (select  " +
                    "  sum(tb.cost) cost,  " +
                    "  tpt.category_id,  " +
                    "  tpt.type_, " +
                    "  tpc.`name` productTypeName  " +
                    " from t_bill tb   " +
                    "  left join t_product tp on tb.product_id = tp.id  " +
                    "  left join t_product_type tpt on tp.id = tpt.product_id  " +
                    "  left join t_product_category tpc on tpt.category_id = tpc.id  " +
                    " where tb.cost is not null  " +
                    "  and if(:hotelCode is not null && :hotelCode != '', tb.hotel_code=:hotelCode, 1=1 ) " +
                    "  and if(:businessDate is not null && :businessDate != '', DATE_FORMAT(tb.business_date, '%Y-%m-%d') =:hotelCode, 1=1 ) " +
                    "  group by  " +
                    "  tpt.category_id,  " +
                    "  tpt.type_, " +
                    "  tpc.`name` ) t " +
                    "  GROUP BY t.category_id WITH ROLLUP " )
    List<Map<String, Object>> totalCostType(@Param("hotelCode")String hotelCode, @Param("businessDate") String businessDate);
    @Query(nativeQuery = true,
            value = " select  " +
                    " t.category_id, " +
                    " t.code_name, " +
                    " sum(if(t.type_='MC', cost, 0)) as 'MC', " +
                    " sum(if(t.type_='RE', cost, 0)) as 'RE', " +
                    " sum(if(t.type_='SE', cost, 0)) as 'SE', " +
                    " sum(if(t.type_='OT', cost, 0)) as 'OT', " +
                    " sum(t.cost) total_cost " +
                    " from " +
                    " (select  " +
                    "  sum(tb.cost) cost,   " +
                    "  tb.product_id,  " +
                    "  tp.`name`,  " +
                    "  tpt.code_,  " +
                    "  tpt.code_name,  " +
                    "  tpt.category_id,  " +
                    "  tpt.type_  " +
                    " from t_bill tb   " +
                    "  left join t_product tp on tb.product_id = tp.id  " +
                    "  left join t_product_type tpt on tp.id = tpt.product_id  " +
                    "  left join t_product_category tpc on tpt.category_id = tpc.id  " +
                    "where tb.cost is not null  " +
                    "  and if(:hotelCode is not null && :hotelCode != '', tb.hotel_code=:hotelCode, 1=1 ) " +
                    "  and if(:businessDate is not null && :businessDate != '', DATE_FORMAT(tb.business_date, '%Y-%m-%d') =:hotelCode, 1=1 ) " +
                    "  group by tb.product_id,  " +
                    "  tp.`name`,  tpt.code_,  " +
                    "  tpt.code_name,  " +
                    "  tpt.category_id,  " +
                    "  tpt.type_ ) t " +
                    "  GROUP BY t.code_name  " )
    List<Map<String, Object>> auditNights(@Param("hotelCode")String hotelCode, @Param("businessDate") String businessDate);

    @Query(nativeQuery = true,
            value = " select   " +
                    "  t.category_id,  " +
                    "  t.code_name,  " +
                    "  sum(if(t.type_='AL', pay, 0)) as 'AL',  " +
                    "  sum(if(t.type_='FB', pay, 0)) as 'FB',  " +
                    "  sum(if(t.type_='BOS', pay, 0)) as 'BOS',  " +
                    "  sum(if(t.type_='AR', pay, 0)) as 'AR',  " +
                    "  sum(if(t.type_='OH', pay, 0)) as 'OH',  " +
                    "  sum(t.pay) total_pay " +
                    " from " +
                    "(select  " +
                    "  sum(tb.pay) pay,   " +
                    "  tb.product_id,   " +
                    "  tp.`name`,  " +
                    "  tpt.code_,  " +
                    "  tpt.code_name,  " +
                    "  tpt.category_id,  " +
                    "  tpt.type_ " +
                    "from t_bill tb   " +
                    "  left join t_product tp on tb.product_id = tp.id  " +
                    "  left join t_product_type tpt on tp.id = tpt.product_id  " +
                    "  left join t_product_category tpc on tpt.category_id = tpc.id  " +
                    "where tb.pay is not null  " +
                    "  and if(:hotelCode is not null && :hotelCode != '', tb.hotel_code=:hotelCode, 1=1 ) " +
                    "  and if(:businessDate is not null && :businessDate != '', DATE_FORMAT(tb.business_date, '%Y-%m-%d') =:hotelCode, 1=1 ) " +
                    "  group by tb.product_id,  " +
                    "  tp.`name`,  tpt.code_,  " +
                    "  tpt.code_name,  " +
                    "  tpt.category_id,  " +
                    "  tpt.type_ ) t " +
                    "  GROUP BY t.code_name " )
    List<Map<String, Object>> receivables(@Param("hotelCode")String hotelCode, @Param("businessDate") String businessDate);
    //参数必须与receivables一致
    @Query(nativeQuery = true,
            value = " select   " +
                    "  ifnull(t.category_id, 'N/A') category_id,  " +
                    "  t.productTypeName code_name,  " +
                    "  sum(if(t.type_='AL', pay, 0)) as 'AL',  " +
                    "  sum(if(t.type_='FB', pay, 0)) as 'FB',  " +
                    "  sum(if(t.type_='BOS', pay, 0)) as 'BOS',  " +
                    "  sum(if(t.type_='AR', pay, 0)) as 'AR',  " +
                    "  sum(if(t.type_='OH', pay, 0)) as 'OH',  " +
                    "  sum(t.pay) total_pay " +
                    " from " +
                    " (select  " +
                    "  sum(tb.pay) pay, " +
                    "  tpt.category_id,  " +
                    "  tpt.type_, " +
                    "  tpc.`name` productTypeName " +
                    "from t_bill tb   " +
                    "  left join t_product tp on tb.product_id = tp.id  " +
                    "  left join t_product_type tpt on tp.id = tpt.product_id  " +
                    "  left join t_product_category tpc on tpt.category_id = tpc.id  " +
                    "where tb.pay is not null  " +
                    "  and if(:hotelCode is not null && :hotelCode != '', tb.hotel_code=:hotelCode, 1=1 ) " +
                    "  and if(:businessDate is not null && :businessDate != '', DATE_FORMAT(tb.business_date, '%Y-%m-%d') =:hotelCode, 1=1 ) " +
                    "  group by   " +
                    "  tpt.category_id,  " +
                    "  tpt.type_, " +
                    "  tpc.`name`) t " +
                    "  GROUP BY t.category_id WITH ROLLUP  " )
    List<Map<String, Object>> totalPayType(@Param("hotelCode")String hotelCode, @Param("businessDate") String businessDate);
}
