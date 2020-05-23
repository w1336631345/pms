package com.kry.pms.dao.marketing;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.marketing.RoomPriceScheme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface RoomPriceSchemeDao extends BaseDao<RoomPriceScheme>{

    @Query(nativeQuery = true, value = " select trp.id, trp.code, trp.`name`, tms.type  " +
            " from t_room_price_scheme trp left join t_marketing_sources tms " +
            " on trp.marketing_sources_id = tms.id  where trp.deleted = 0 " +
            " and if(:hotelCode is not null && :hotelCode != '', trp.hotel_code=:hotelCode, 1=1 ) ")
    List<Map<String, Object>> getSql(@Param("hotelCode")String hotelCode);

    @Query(nativeQuery = true, value = " select trp.* " +
            " from t_room_price_scheme trp, t_protocol_corpation_room_price_schemes tpc " +
            " where trp.id = tpc.room_price_schemes_id " +
            " and tpc.protocol_corpation_id = ?1 ")
    List<RoomPriceScheme> getByCorpation(String protocolCId);

    @Query(nativeQuery = true, value = " SELECT trp.id, " +
            " trp.`code`, " +
            " trp.`name`, " +
            " trpt.price, " +
            " trpt.room_type_id, " +
            " trt.`name` roomTypeName, " +
            " trt.`code` typeCode, " +
            " trt.over_reservation " +
            " from  " +
            " t_room_price_scheme trp, t_room_price_scheme_item trpt, t_room_price_scheme_items trps,t_room_type trt  " +
            " where trp.id = trps.room_price_scheme_id and trpt.id = trps.items_id " +
            " and trpt.room_type_id = trt.id " +
            " and if(:schemeId is not null && :schemeId != '', trp.id=:schemeId, 1=1 ) " +
            " and if(:roomTypeId is not null && :roomTypeId != '', trpt.room_type_id=:roomTypeId, 1=1 ) ")
    List<Map<String, Object>> getByRoomType(@Param("schemeId")String schemeId, @Param("roomTypeId")String roomTypeId);
    @Query(nativeQuery = true, value = " SELECT trp.id, " +
            " trp.`code`, " +
            " trp.`name`, " +
            " trpt.price, " +
            " trpt.room_type_id, " +
            " trt.`name` roomTypeName, " +
            " trt.`code` typeCode, " +
            " trt.over_reservation " +
            " from  " +
            " t_room_price_scheme trp, t_room_price_scheme_item trpt, t_room_price_scheme_items trps,t_room_type trt  " +
            " where trp.id = trps.room_price_scheme_id and trpt.id = trps.items_id " +
            " and trpt.room_type_id = trt.id " +
            " and if(:schemeId is not null && :schemeId != '', trp.id=:schemeId, 1=1 ) " +
            " and if(coalesce (:roomTypeIds,null) is not null, trpt.room_type_id in (:roomTypeIds), 1=1 ) ")
    List<Map<String, Object>> getByRoomType2(@Param("schemeId")String schemeId, @Param("roomTypeIds")List<String> roomTypeIds);

    @Query(nativeQuery = true, value = " select code from t_room_price_scheme where hotel_code = ?1 and deleted = ?2 ")
    List<String> getCode(String hotelCode, int delelted);

    List<RoomPriceScheme> findByHotelCodeAndIsShowAndDeleted(String hotelCode, String isShow, int deleted);
    @Query(nativeQuery = true, value = " select  trp.id, trp.`name`, tsm.id setMealId, tsm.`name` setMealName, trpt.room_type_id, trpt.price  \n" +
            " from t_room_price_scheme trp left join t_room_price_scheme_items trpts on trp.id = trpts.room_price_scheme_id \n" +
            " left join t_room_price_scheme_item trpt on trpt.id = trpts.items_id \n" +
            " left join t_set_meal tsm  on trpt.set_meal_id = tsm.id  \n" +
            " where 1=1 " +
            " and if(:roomTypeId is not null && :roomTypeId != '', trpt.room_type_id=:roomTypeId, 1=1 ) " +
            " and if(:roomPriceSchemeId is not null && :roomPriceSchemeId != '', trpts.room_price_scheme_id=:roomPriceSchemeId, 1=1 ) ")
    Map<String, Object> roomTypeAndPriceScheme(@Param("roomTypeId")String roomTypeId, @Param("roomPriceSchemeId")String roomPriceSchemeId);

    @Query(nativeQuery = true, value = " select  trp.id, trp.`name`, tsm.id setMealId, tsm.`name` setMealName, trpt.room_type_id, trpt.price  \n" +
            " from t_room_price_scheme trp left join t_room_price_scheme_items trpts on trp.id = trpts.room_price_scheme_id \n" +
            " left join t_room_price_scheme_item trpt on trpt.id = trpts.items_id \n" +
            " left join t_set_meal tsm  on trpt.set_meal_id = tsm.id  \n" +
            " where 1=1 " +
            " and if(:hotelCode is not null && :hotelCode != '', trp.hotel_code=:hotelCode, 1=1 ) " +
            " and if(:roomTypeId is not null && :roomTypeId != '', trpt.room_type_id=:roomTypeId, 1=1 ) ")
    List<Map<String, Object>> roomTypeAndPriceSchemeList(@Param("hotelCode")String hotelCode, @Param("roomTypeId")String roomTypeId);

    List<RoomPriceScheme> findByHotelCodeAndDeleted(String hotelCode, int deletedFalse);

    @Query(nativeQuery = true, value = " select tr.* " +
            " from t_customer_room_price_schemes t, t_room_price_scheme tr " +
            " where t.room_price_schemes_id = tr.id " +
            " and t.customer_id = ?1")
    List<RoomPriceScheme> getByCustomerId(String custId);
}
