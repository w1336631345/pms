package com.kry.pms.dao.report;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.report.BusinessReport;
import com.kry.pms.model.persistence.report.RoomReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

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

    List<RoomReport> getByHotelCodeAndBusinessDate(String hotelCode, String businessDate);

    @Query(nativeQuery = true, value = " select count(room_status) totalDay, room_status project " +
            " from t_report_room  where 1=1 " +
            "  and if(:hotelCode is not null && :hotelCode != '', hotel_code=:hotelCode, 1=1 ) " +
            "  and if(:businessDate is not null && :businessDate != '', DATE_FORMAT(business_date, '%Y-%m-%d') =:businessDate, 1=1 ) " +
            " group by room_status ")
    List<Map<String, Object>> getTotalRoomStatus(@Param("hotelCode") String hotelCode, @Param("businessDate") String businessDate);

}
