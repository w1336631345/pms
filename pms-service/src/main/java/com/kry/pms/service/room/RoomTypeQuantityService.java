package com.kry.pms.service.room;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.kry.pms.model.http.response.busi.RoomTypeQuantityPredictableVo;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.RoomType;
import com.kry.pms.model.persistence.room.RoomTypeQuantity;
import com.kry.pms.service.BaseService;

public interface RoomTypeQuantityService extends BaseService<RoomTypeQuantity>{
	public List<RoomTypeQuantity> findByQuantityDate(RoomType roomType,LocalDate startDate,int days);
	public List<RoomTypeQuantity> findQuantitysForUpdate(RoomType roomType,LocalDate startDate,LocalDate endDate);
	public List<RoomTypeQuantity> updateQuantitys(List<RoomTypeQuantity> data);
	public RoomTypeQuantity findByRoomTypeAndQuantityDateForUpdate(RoomType roomType,LocalDate quantityDate);
	public boolean bookCheck(RoomType roomType,LocalDate quantityDate,int quantity);
	public void checkIn(GuestRoom gr, LocalDate startDate, Integer days);
	public List<RoomTypeQuantityPredictableVo> queryPredictable(String currentHotleCode, LocalDate startDate, LocalDate endDate);
}