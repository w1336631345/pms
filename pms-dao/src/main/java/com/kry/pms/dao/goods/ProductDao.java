package com.kry.pms.dao.goods;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.goods.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface ProductDao extends BaseDao<Product>{

    Product getById(String id);

    Product findByHotelCodeAndCode(String hotelCode, String code);

    @Query(nativeQuery = true, value = " select \n" +
            " tp.id, tp.code_, tp.`name`, tdd.description, tp.achievement_type \n" +
            " from t_dict_data tdd, t_product tp \n" +
            " where tdd.`code` = tp.achievement_type \n" +
            " and tdd.dict_type_code = 'AchievementType' \n" +
            " and tdd.hotel_code = :hotelCode " +
            " and tp.direction = 1 \n" +
            " and tp.hotel_code = :hotelCode " +
            " order by tp.achievement_type desc ")
    List<Map<String, Object>> getPaySetList(@Param("hotelCode")String hotelCode);

}
