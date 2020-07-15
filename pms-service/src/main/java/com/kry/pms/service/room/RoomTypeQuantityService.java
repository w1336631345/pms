package com.kry.pms.service.room;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kry.pms.base.DtoResponse;
import com.kry.pms.model.func.UseInfoAble;
import com.kry.pms.model.http.response.busi.RoomTypeQuantityPredictableVo;
import com.kry.pms.model.http.response.room.RoomTypeQuantityVo;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.RoomType;
import com.kry.pms.model.persistence.room.RoomTypeQuantity;
import com.kry.pms.service.BaseService;
import freemarker.template.TemplateException;

public interface RoomTypeQuantityService extends BaseService<RoomTypeQuantity> {
    public List<RoomTypeQuantity> findByQuantityDate(RoomType roomType, LocalDate startDate, int days);

    public List<RoomTypeQuantity> findQuantitysForUpdate(RoomType roomType, LocalDate startDate, LocalDate endDate);

    public List<RoomTypeQuantity> updateQuantitys(List<RoomTypeQuantity> data);

    public RoomTypeQuantity findByRoomTypeAndQuantityDateForUpdate(RoomType roomType, LocalDate quantityDate);

    public boolean bookCheck(RoomType roomType, LocalDate quantityDate, int quantity);

    public void checkIn(GuestRoom gr, LocalDate startDate, Integer days);

    public List<RoomTypeQuantityPredictableVo> queryPredictable(String currentHotleCode, LocalDate startDate, LocalDate endDate, String roomPriceSchemeId);

    List<RoomTypeQuantityPredictableVo> predictableWchat(String hotelCode, LocalDate startDate, LocalDate endDate);

    public RoomTypeQuantityPredictableVo queryPredic(String currentHotleCode, String roomTypeId, LocalDate startDate, LocalDate endDate, String roomPriceSchemeId);

    public List<RoomTypeQuantityVo> queryByDay(String currentHotleCode, LocalDate parse, LocalDate parse2);

    public boolean useRoomType(RoomType roomType, LocalDate startDate, LocalDate endDate, String useType);

    public void unUseRoomType(RoomType roomType, LocalDate startDate, LocalDate startDate2, String roomUsageBook);

    public boolean useRoomType(UseInfoAble info, String userType);

    public boolean useRoomType(UseInfoAble info, LocalDateTime extendTime);

    public List<RoomTypeQuantityVo> queryOneDay(String currentHotleCode, LocalDate now);

    public void checkInRoomTypeWithoutBook(RoomType roomType, LocalDate startDate, LocalDate endDate, int quantity);

    boolean changeRoomTypeQuantity(RoomType roomType, LocalDateTime startDate, LocalDateTime endDate, String oldUsageStatus,
                                String newUsageStatus, int quantity);

    List<Map<String, Object>> getByTimeAndRoomType(String hotelCode, String times, String roomTypeId);

    public void initNewType(RoomType roomType);

    public void addRoomQuantity(RoomType roomType, int quantity);

    List<Map<String, Object>> getByTimeAndRoomType2(String hotelCode, String startTimes, String[] buildIds, String[] roomTypeIds);

    boolean changeRoom(RoomType roomType, RoomType newRoomType, String status, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime changeTime);

    boolean extendTime(RoomType roomType, String roomStatus, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime newStartTime, LocalDateTime newEndTime,int quantity);

    List<Map<String, Object>> resourcesInfo(String currentHotleCode, Map<String, Object> parse2Map) throws IOException, TemplateException;

    DtoResponse<String> recount(String currentHotleCode);
}