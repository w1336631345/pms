
package com.kry.pms.dao.room;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.http.response.room.RoomTypeQuantityVo;
import com.kry.pms.model.persistence.room.RoomType;
import com.kry.pms.model.persistence.room.RoomTypeQuantity;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface RoomTypeQuantityDao extends BaseDao<RoomTypeQuantity> {

	@Query(value = "select t.* from t_room_type_quantity t where t.room_type =?1 and t.quantity_date between ?2 and ?3 for update", nativeQuery = true)
	List<RoomTypeQuantity> findQuantitysForUpdate(RoomType roomType, LocalDate startDate, LocalDate endDate);
	
	@Query(value = "select t.* from t_room_type_quantity t where t.room_type =?1 and t.quantity_date between ?2 and ?3", nativeQuery = true)
	List<RoomTypeQuantity> findQuantitys(RoomType roomType, LocalDate startDate, LocalDate endDate);
	
//	@Lock(LockModeType.PESSIMISTIC_WRITE)
	RoomTypeQuantity findByRoomTypeAndQuantityDate(RoomType roomType,LocalDate quantityDate);
	
	@Query(value = "select t.* from t_room_type_quantity t where t.room_type_id =?1 and t.quantity_date >= ?2 and t.quantity_date <= ?3 order by predictable_total asc limit 1", nativeQuery = true)
	RoomTypeQuantity queryPredictable(String roomTypeId, LocalDate startDate, LocalDate endDate);
	
	@Query(value = "select 	t.* from t_room_type_quantity t where t.hotel_code =?1 and t.quantity_date >= ?2 and t.quantity_date <= ?3 ", nativeQuery = true)
	List<RoomTypeQuantity> queryByDay(String currentHotleCode, LocalDate startDate, LocalDate endDate);

	List<RoomTypeQuantity> findByHotelCodeAndQuantityDate(String hotleCode, LocalDate date);

	@Query(nativeQuery = true, value = "select  t.room_type_id, t.room_type_name, t.room_type_code, t.predictable_total from t_room_type_quantity t  " +
			" where 1=1 " +
			"  and if(:hotelCode is not null && :hotelCode != '', t.hotel_code=:hotelCode, 1=1 ) " +
			"  and if(:times is not null && :times != '', DATE_FORMAT(t.quantity_date, '%Y-%m-%d') =:times, 1=1 ) " +
			"  and if(:roomTypeId is not null && :roomTypeId != '', t.room_type_id=:roomTypeId, 1=1 ) " )
	List<Map<String, Object>> getByTimeAndRoomType(@Param("hotelCode") String hotelCode, @Param("times")String time, @Param("roomTypeId")String roomTypeId);

	@Query(nativeQuery = true, value = "select  t.room_type_id, t.room_type_name, t.room_type_code, t.predictable_total from t_room_type_quantity t  " +
			" where 1=1 " +
			"  and if(:hotelCode is not null && :hotelCode != '', t.hotel_code=:hotelCode, 1=1 ) " +
			"  and if(:times is not null && :times != '', DATE_FORMAT(t.quantity_date, '%Y-%m-%d') =:times, 1=1 ) " +
			"  and if(:roomTypeId is not null && :roomTypeId != '', t.room_type_id=:roomTypeId, 1=1 ) " )
	Map<String, Object> mapByTimeAndRoomType(@Param("hotelCode") String hotelCode, @Param("times")String time, @Param("roomTypeId")String roomTypeId);
	@Query(nativeQuery = true, value = " select count(t.guest_room_id) total, trt.id, trt.`name` from t_guest_room tgr, t_room_type trt, t_building tb, " +
			" (select DISTINCT guest_room_id, usage_status " +
			" from t_room_usage tru " +
			" where 1=1" +
			" and if(:startTime is not null && :startTime != '', DATE_FORMAT(start_date_time, '%Y-%m-%d %T') <=:startTime, 1=1 ) " +
			" and if(:endTime is not null && :endTime != '', DATE_FORMAT(end_date_time, '%Y-%m-%d %T') >=:endTime, 1=1 ) " +
			" and usage_status = 'F') t " +
			" where tgr.id = t.guest_room_id and tgr.room_type_id = trt.id and tgr.building_id = tb.id " +
			" and if(:hotelCode is not null && :hotelCode != '', tgr.hotel_code=:hotelCode, 1=1 ) " +
			" and if(coalesce(:buildIds, null) is not null, tb.id in (:buildIds), 1=1 ) " +
			" and if(:roomTypeId is not null && :roomTypeId != '', trt.id =:roomTypeId, 1=1 ) " +
			" group by trt.id, trt.`name` " )
	Map<String, Object> mapByTimeAndRoomType2(@Param("hotelCode")String hotelCode, @Param("startTime")String startTime, @Param("endTime") String endTime,
											  @Param("buildIds")List<String> buildIds, @Param("roomTypeId")String roomTypeId);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = " update t_room_type_quantity " +
			" set room_count = room_count + 1, predictable_total = predictable_total + 1, available_total = available_total + 1 " +
			" where room_type_id = :roomTypeId and DATE_FORMAT(quantity_date, '%Y-%m-%d') >= :nowDate ")
	int updateAddTotal(@Param("roomTypeId") String roomTypeId, @Param("nowDate")String nowDate);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = " update t_room_type_quantity " +
			" set room_count = room_count - 1, predictable_total = predictable_total - 1, available_total = available_total - 1 " +
            " where room_type_id = :roomTypeId and DATE_FORMAT(quantity_date, '%Y-%m-%d') >= :nowDate ")
    int deletedAddTotal(@Param("roomTypeId") String roomTypeId, @Param("nowDate")String nowDate);
    
    
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = " update t_room_type_quantity " +
			" set room_count = room_count +:quantity, predictable_total = predictable_total +:quantity, available_total = available_total +:quantity " +
            " where room_type_id = :roomTypeId and DATE_FORMAT(quantity_date, '%Y-%m-%d') >= CURRENT_DATE ")
    int plusRoomTypeQuantity(@Param("roomTypeId") String roomTypeId, @Param("quantity")int quantity);
    

}
