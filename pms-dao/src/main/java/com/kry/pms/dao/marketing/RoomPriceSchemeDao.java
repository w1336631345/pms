package com.kry.pms.dao.marketing;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.marketing.RoomPriceScheme;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface RoomPriceSchemeDao extends BaseDao<RoomPriceScheme>{

    @Query(nativeQuery = true, value = " select id, code, `name` from t_room_price_scheme " +
            " where 1=1 " +
            " and if(:hotelCode is not null && :hotelCode != '', hotel_code=:hotelCode, 1=1 ) ")
    List<Map<String, Object>> getSql(@Param("hotelCode")String hotelCode);

    @Query(nativeQuery = true, value = " SELECT trp.id, " +
            " trp.`code`, " +
            " trp.`name`, " +
            " trpt.price, " +
            " trpt.room_type_id, " +
            " trt.`name` roomTypeName, " +
            " trt.`code` typeCode " +
            " from  " +
            " t_room_price_scheme trp, t_room_price_scheme_item trpt, t_room_price_scheme_items trps,t_room_type trt  " +
            " where trp.id = trps.room_price_scheme_id and trpt.id = trps.items_id " +
            " and trpt.room_type_id = trt.id " +
            " and if(:schemeId is not null && :schemeId != '', trp.id=:schemeId, 1=1 ) " +
            " and if(:roomTypeId is not null && :roomTypeId != '', trpt.room_type_id=:roomTypeId, 1=1 ) ")
    List<Map<String, Object>> getByRoomType(@Param("schemeId")String schemeId, @Param("roomTypeId")String roomTypeId);
}
