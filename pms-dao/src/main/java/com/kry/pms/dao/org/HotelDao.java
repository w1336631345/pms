package com.kry.pms.dao.org;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.org.Hotel;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface HotelDao extends BaseDao<Hotel>{

    Hotel findByHotelCode(String hotelCode);

    List<Hotel> findByHotelCodeAndDeleted(String hotelCode, int deletede);

    @Query(nativeQuery = true, value = " select \n" +
            " th.id, th.hotel_code, th.`name` \n" +
            " from t_hotel th, t_user tu \n" +
            " where th.hotel_code = tu.hotel_code \n" +
            " and tu.union_id = ?1 ")
    List<Map<String, Object>> getByUnionId(String unionId);

}
