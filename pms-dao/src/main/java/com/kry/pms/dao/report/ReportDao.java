package com.kry.pms.dao.report;

import com.kry.pms.model.persistence.busi.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface ReportDao extends JpaRepository<Bill, Integer> {
    @Query(nativeQuery = true, value = " select t.* from (select t1.`name`, IFNULL(sum(t1.totalYear),0) totalYear,  " +
            " IFNULL(sum(t2.totalMonth),0) totalMonth, IFNULL(sum(t3.totalDay),0) totalDay from " +
            " (select  " +
            " sum(tb.cost) totalYear, tms.`name` " +
            " from t_bill tb " +
            " left join t_account ta on tb.account_id = ta.id " +
            " left join t_checkin_record tcr on tcr.account_id = ta.id " +
            " left join t_marketing_sources tms on tcr.marketing_sources_id = tms.id " +
            " where tb.cost is not null " +
            "  and if(:hotelCode is not null && :hotelCode != '', tb.hotel_code=:hotelCode, 1=1 ) " +
            "  and if(:yearTime is not null && :yearTime != '', DATE_FORMAT(tb.business_date,'%Y') =:yearTime, 1=1 ) " +
            " group by tms.`name`) t1  " +
            " left join  " +
            " (select  " +
            " sum(tb.cost) totalMonth, tms.`name` " +
            " from t_bill tb " +
            " left join t_account ta on tb.account_id = ta.id " +
            " left join t_checkin_record tcr on tcr.account_id = ta.id " +
            " left join t_marketing_sources tms on tcr.marketing_sources_id = tms.id " +
            " where tb.cost is not null " +
            "  and if(:hotelCode is not null && :hotelCode != '', tb.hotel_code=:hotelCode, 1=1 ) " +
            "  and if(:monthTime is not null && :monthTime != '', DATE_FORMAT(tb.business_date,'%Y-%m') =:monthTime, 1=1 ) " +
            " group by tms.`name`) t2 " +
            " on t1.`name` = t2.`name`  " +
            " left join  " +
            " (select  " +
            " sum(tb.cost) totalDay, tms.`name` " +
            " from t_bill tb " +
            " left join t_account ta on tb.account_id = ta.id " +
            " left join t_checkin_record tcr on tcr.account_id = ta.id " +
            " left join t_marketing_sources tms on tcr.marketing_sources_id = tms.id " +
            " where tb.cost is not null " +
            "  and if(:hotelCode is not null && :hotelCode != '', tb.hotel_code=:hotelCode, 1=1 ) " +
            "  and if(:dayTime is not null && :dayTime != '', DATE_FORMAT(tb.business_date,'%Y-%m-%d') =:dayTime, 1=1 ) " +
            " group by tms.`name`) t3 " +
            " on t1.`name` = t3.`name` " +
            " group by t1.`name` WITH ROLLUP ) t order by t.`name` asc")
    List<Map<String, Object>> costByGroupType(@Param("hotelCode") String hotelCode, @Param("dayTime") String dayTime,
                                              @Param("monthTime") String monthTime, @Param("yearTime") String yearTime);

    @Query(nativeQuery = true, value = " select t.* from (select t1.`name`, IFNULL(sum(t1.totalYear),0) totalYear,  " +
            " IFNULL(sum(t2.totalMonth),0) totalMonth, IFNULL(sum(t3.totalDay),0) totalDay from " +
            " (select  " +
            " truncate(avg(tb.cost),2) totalYear, tms.`name` " +
            " from t_bill tb " +
            " left join t_account ta on tb.account_id = ta.id " +
            " left join t_checkin_record tcr on tcr.account_id = ta.id " +
            " left join t_marketing_sources tms on tcr.marketing_sources_id = tms.id " +
            " where tb.cost is not null " +
            "  and if(:hotelCode is not null && :hotelCode != '', tb.hotel_code=:hotelCode, 1=1 ) " +
            "  and if(:yearTime is not null && :yearTime != '', DATE_FORMAT(tb.business_date,'%Y') =:yearTime, 1=1 ) " +
            " group by tms.`name`) t1  " +
            " left join  " +
            " (select  " +
            " truncate(avg(tb.cost),2) totalMonth, tms.`name` " +
            " from t_bill tb " +
            " left join t_account ta on tb.account_id = ta.id " +
            " left join t_checkin_record tcr on tcr.account_id = ta.id " +
            " left join t_marketing_sources tms on tcr.marketing_sources_id = tms.id " +
            " where tb.cost is not null " +
            "  and if(:hotelCode is not null && :hotelCode != '', tb.hotel_code=:hotelCode, 1=1 ) " +
            "  and if(:monthTime is not null && :monthTime != '', DATE_FORMAT(tb.business_date,'%Y-%m') =:monthTime, 1=1 ) " +
            " group by tms.`name`) t2 " +
            " on t1.`name` = t2.`name`  " +
            " left join  " +
            " (select  " +
            " truncate(avg(tb.cost),2) totalDay, tms.`name` " +
            " from t_bill tb " +
            " left join t_account ta on tb.account_id = ta.id " +
            " left join t_checkin_record tcr on tcr.account_id = ta.id " +
            " left join t_marketing_sources tms on tcr.marketing_sources_id = tms.id " +
            " where tb.cost is not null " +
            "  and if(:hotelCode is not null && :hotelCode != '', tb.hotel_code=:hotelCode, 1=1 ) " +
            "  and if(:dayTime is not null && :dayTime != '', DATE_FORMAT(tb.business_date,'%Y-%m-%d') =:dayTime, 1=1 ) " +
            " group by tms.`name`) t3 " +
            " on t1.`name` = t3.`name` " +
            " group by t1.`name` WITH ROLLUP ) t order by t.`name` asc")
    List<Map<String, Object>> costByGroupTypeAvg(@Param("hotelCode") String hotelCode, @Param("dayTime") String dayTime,
                                              @Param("monthTime") String monthTime, @Param("yearTime") String yearTime);

}
