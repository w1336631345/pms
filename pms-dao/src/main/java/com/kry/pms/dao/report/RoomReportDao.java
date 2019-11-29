package com.kry.pms.dao.report;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.report.BusinessReport;
import com.kry.pms.model.persistence.report.RoomReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface RoomReportDao extends BaseDao<RoomReport> {

    @Query(nativeQuery = true, value = " select * from t_report_business " +
            " where 1=1 " +
            "  and if(:hotelCode is not null && :hotelCode != '', hotel_code=:hotelCode, 1=1 ) " +
            "  and if(:businessDate is not null && :businessDate != '', DATE_FORMAT(business_date, '%Y-%m-%d') =:businessDate, 1=1 ) " +
            " order by sort, number_ desc ")
    List<BusinessReport> getByBusinessDate(@Param("businessDate") String businessDate, @Param("hotelCode") String hotelCode);

    @Query(nativeQuery = true, value = " call CopyRoomStatus(:businessDate) ")
    Integer copyData(@Param("businessDate") String businessDate);

    List<RoomReport> getByHotelCodeAndBusinessDate(String hotelCode, LocalDate businessDate);

    @Query(nativeQuery = true, value = " select count(room_status) totalDay, room_status project " +
            " from t_report_room  where 1=1 " +
            "  and if(:hotelCode is not null && :hotelCode != '', hotel_code=:hotelCode, 1=1 ) " +
            "  and if(:businessDate is not null && :businessDate != '', DATE_FORMAT(business_date, '%Y-%m-%d') =:businessDate, 1=1 ) " +
            "  and if(:roomStatus is not null && :roomStatus != '', room_status =:roomStatus, 1=1 ) " +
            " group by room_status ")
    List<Map<String, Object>> getTotalRoomStatus(@Param("hotelCode") String hotelCode, @Param("businessDate") String businessDate, @Param("roomStatus") String roomStatus);
    @Query(nativeQuery = true, value = " select count(room_status) totalMonth, room_status project " +
            " from t_report_room  where 1=1 " +
            "  and if(:hotelCode is not null && :hotelCode != '', hotel_code=:hotelCode, 1=1 ) " +
            "  and if(:businessDate is not null && :businessDate != '', DATE_FORMAT(business_date, '%Y-%m') =:businessDate, 1=1 ) " +
            "  and if(:roomStatus is not null && :roomStatus != '', room_status =:roomStatus, 1=1 ) " +
            " group by room_status ")
    List<Map<String, Object>> getTotalRoomStatusMonth(@Param("hotelCode") String hotelCode, @Param("businessDate") String businessDate, @Param("roomStatus") String roomStatus);
    @Query(nativeQuery = true, value = " select count(room_status) totalYear, room_status project " +
            " from t_report_room  where 1=1 " +
            "  and if(:hotelCode is not null && :hotelCode != '', hotel_code=:hotelCode, 1=1 ) " +
            "  and if(:businessDate is not null && :businessDate != '', DATE_FORMAT(business_date, '%Y') =:businessDate, 1=1 ) " +
            " group by room_status ")
    List<Map<String, Object>> getTotalRoomStatusYear(@Param("hotelCode") String hotelCode, @Param("businessDate") String businessDate);

    @Query(nativeQuery = true, value = " select sum(if(available_total is null,0, available_total)) available_total, " +
            " sum(if(repair_total is null,0, repair_total)) repair_total, " +
            " sum(if(locked_total is null,0, locked_total)) locked_total, " +
            " 'day' timeType " +
            " from t_room_type_quantity  " +
            " where 1=1 " +
            "  and if(:hotelCode is not null && :hotelCode != '', hotel_code=:hotelCode, 1=1 ) " +
            "  and if(:dayTime is not null && :dayTime != '', DATE_FORMAT(quantity_date, '%Y-%m-%d') =:dayTime, 1=1 ) " +
            " group by quantity_date " +
            " UNION all " +
            " select sum(if(available_total is null,0, available_total)) available_total, " +
            " sum(if(repair_total is null,0, repair_total)) repair_total, " +
            " sum(if(locked_total is null,0, locked_total)) locked_total, " +
            " 'month' timeType " +
            " from t_room_type_quantity  " +
            " where 1=1 " +
            "  and if(:hotelCode is not null && :hotelCode != '', hotel_code=:hotelCode, 1=1 ) " +
            "  and if(:monthTime is not null && :monthTime != '', DATE_FORMAT(quantity_date, '%Y-%m') =:monthTime, 1=1 ) " +
            " group by DATE_FORMAT(quantity_date, '%Y-%m') " +
            " UNION all " +
            " select sum(if(available_total is null,0, available_total)) available_total, " +
            " sum(if(repair_total is null,0, repair_total)) repair_total, " +
            " sum(if(locked_total is null,0, locked_total)) locked_total, " +
            " 'year' timeType " +
            " from t_room_type_quantity  " +
            " where 1=1 " +
            "  and if(:hotelCode is not null && :hotelCode != '', hotel_code=:hotelCode, 1=1 ) " +
            "  and if(:yearTime is not null && :yearTime != '', DATE_FORMAT(quantity_date, '%Y') =:yearTime, 1=1 ) " +
            " group by DATE_FORMAT(quantity_date, '%Y') ")
    List<Map<String, Object>> totalRoomStatusAll(@Param("hotelCode") String hotelCode, @Param("dayTime") String dayTime,
                                                 @Param("monthTime") String monthTime, @Param("yearTime") String yearTime);

    @Query(nativeQuery = true,
    value = " select ty.totalYear, IFNULL(tm.totalMonth,0) totalMonth,  " +
            " IFNULL(td.totalDay,0) totalDay, ty.name " +
            " from  " +
            " ( select count(trr.id) totalYear, tms.id, tms.name " +
            " from t_room_record trr " +
            " left join t_checkin_record tcr on trr.check_in_record_id = tcr.id " +
            " left join t_marketing_sources tms on tcr.marketing_sources_id = tms.id " +
            " where 1=1 " +
            "  and if(:hotelCode is not null && :hotelCode != '', trr.hotel_code=:hotelCode, 1=1 ) " +
            "  and if(:yearTime is not null && :yearTime != '', DATE_FORMAT(trr.record_date,'%Y') =:yearTime, 1=1 ) " +
            " group by tms.id,tms.name ) ty " +
            " left join  " +
            " ( select count(trr.id) totalMonth, tms.id, tms.name " +
            " from t_room_record trr " +
            " left join t_checkin_record tcr on trr.check_in_record_id = tcr.id " +
            " left join t_marketing_sources tms on tcr.marketing_sources_id = tms.id " +
            " where 1=1 " +
            "  and if(:hotelCode is not null && :hotelCode != '', trr.hotel_code=:hotelCode, 1=1 ) " +
            "  and if(:monthTime is not null && :monthTime != '', DATE_FORMAT(trr.record_date,'%Y-%m') =:monthTime, 1=1 ) " +
            " group by tms.id,tms.name ) tm " +
            " on ty.id = tm.id " +
            " left join  " +
            " ( select count(trr.id) totalDay, tms.id, tms.name " +
            " from t_room_record trr " +
            " left join t_checkin_record tcr on trr.check_in_record_id = tcr.id " +
            " left join t_marketing_sources tms on tcr.marketing_sources_id = tms.id " +
            " where 1=1 " +
            "  and if(:hotelCode is not null && :hotelCode != '', trr.hotel_code=:hotelCode, 1=1 ) " +
            "  and if(:dayTime is not null && :dayTime != '', DATE_FORMAT(trr.record_date,'%Y-%m-%d') =:dayTime, 1=1 ) " +
            " group by tms.id,tms.name ) td  " +
            " on ty.id = td.id  ")
    List<Map<String, Object>> totalCheckInType(@Param("hotelCode") String hotelCode, @Param("dayTime") String dayTime,
                                               @Param("monthTime") String monthTime, @Param("yearTime") String yearTime);

    @Query(nativeQuery = true,
            value = " select t1.hotel_code, t1.totalYear, t2.totalMonth, t3.totalDay " +
                    " from " +
                    " (select sum(available_total) totalYear,  " +
                    "      hotel_code, DATE_FORMAT(quantity_date, '%Y') timeYear " +
                    "      from t_room_type_quantity   " +
                    "      where 1=1  " +
                    "       and if(:hotelCode is not null && :hotelCode != '', hotel_code=:hotelCode, 1=1 ) " +
                    "       and if(:yearTime is not null && :yearTime != '', DATE_FORMAT(quantity_date,'%Y') =:yearTime, 1=1 ) " +
                    "      group by  DATE_FORMAT(quantity_date, '%Y'),hotel_code) t1 " +
                    " left join  " +
                    " (select sum(available_total) totalMonth,  " +
                    "      hotel_code, DATE_FORMAT(quantity_date, '%Y-%m') timeMonth " +
                    "      from t_room_type_quantity   " +
                    "      where 1=1  " +
                    "       and if(:hotelCode is not null && :hotelCode != '', hotel_code=:hotelCode, 1=1 ) " +
                    "       and if(:monthTime is not null && :monthTime != '', DATE_FORMAT(quantity_date,'%Y-%m') =:monthTime, 1=1 ) " +
                    "      group by  DATE_FORMAT(quantity_date, '%Y-%m'),hotel_code) t2 " +
                    " on t1.hotel_code = t2.hotel_code " +
                    " left join " +
                    " (select sum(available_total) totalDay,  " +
                    "      hotel_code, DATE_FORMAT(quantity_date, '%Y-%m-%d') timeDay " +
                    "      from t_room_type_quantity  " +
                    "      where 1=1  " +
                    "       and if(:hotelCode is not null && :hotelCode != '', hotel_code=:hotelCode, 1=1 ) " +
                    "       and if(:dayTime is not null && :dayTime != '', DATE_FORMAT(quantity_date,'%Y-%m-%d') =:dayTime, 1=1 ) " +
                    "      group by  DATE_FORMAT(quantity_date, '%Y-%m-%d'),hotel_code) t3 " +
                    " on t1.hotel_code = t3.hotel_code  ")
    Map<String, Object> availableTotal(@Param("hotelCode") String hotelCode, @Param("dayTime") String dayTime,
                                               @Param("monthTime") String monthTime, @Param("yearTime") String yearTime);
}
