package com.kry.pms.service.room;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kry.pms.model.func.UseInfoAble;
import com.kry.pms.model.http.response.busi.RoomTypeQuantityPredictableVo;
import com.kry.pms.model.http.response.room.RoomTypeQuantityVo;
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
	public List<RoomTypeQuantityPredictableVo> queryPredictable(String currentHotleCode, LocalDate startDate, LocalDate endDate, String roomPriceSchemeId);
	public RoomTypeQuantityPredictableVo queryPredic(String currentHotleCode,String roomTypeId, LocalDate startDate, LocalDate endDate);
	public List<RoomTypeQuantityVo> queryByDay( String currentHotleCode,LocalDate parse, LocalDate parse2);
	public void useRoomType(RoomType roomType, LocalDate startDate, LocalDate endDate, String useType);
	public void unUseRoomType(RoomType roomType, LocalDate startDate, LocalDate startDate2, String roomUsageBook);
	public void useRoomType(UseInfoAble info, String userType);
	public List<RoomTypeQuantityVo> queryOneDay(String currentHotleCode, LocalDate now);
	public void checkInRoomTypeWithoutBook(RoomType roomType, LocalDate startDate, LocalDate endDate, int quantity);
	void changeRoomTypeQuantity(RoomType roomType, LocalDate startDate, LocalDate endDate, String oldUsageStatus,
			String newUsageStatus, int quantity);

    List<Map<String, Object>> getByTimeAndRoomType(String hotelCode, String times, String roomTypeId);

    public void initNewType(RoomType roomType);
    public void addRoomQuantity(RoomType roomType, int quantity);

    List<Map<String, Object>> getByTimeAndRoomType2(String hotelCode, String startTimes, String[] buildIds, String[] roomTypeIds);
}