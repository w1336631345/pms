package com.kry.pms.dao.room;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.room.RoomType;
import com.kry.pms.model.persistence.room.RoomTypeQuantity;

public interface RoomTypeQuantityDao extends BaseDao<RoomTypeQuantity> {

	@Query(value = "select t.* from t_room_type_quantity t where t.room_type =?1 and t.quantity_date between ?2 and ?3 for update", nativeQuery = true)
	List<RoomTypeQuantity> findQuantitysForUpdate(RoomType roomType, LocalDate startDate, LocalDate endDate);
	
	@Query(value = "select t.* from t_room_type_quantity t where t.room_type =?1 and t.quantity_date between ?2 and ?3", nativeQuery = true)
	List<RoomTypeQuantity> findQuantitys(RoomType roomType, LocalDate startDate, LocalDate endDate);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	RoomTypeQuantity findByRoomTypeAndQuantityDate(RoomType roomType,LocalDate quantityDate);

}
