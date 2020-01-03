package com.kry.pms.dao.goods;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.goods.ProductCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface ProductCategoryDao extends BaseDao<ProductCategory>{


    List<ProductCategory> findByHotelCodeAndDeleted(String hotelCode, int deleted);

    @Query(nativeQuery = true, value = " select DISTINCT " +
            " tpc.name codeName, tpc.corporation_code codeNum, " +
            " tpc.id productId " +
            " from t_product_type tpt, t_product_category tpc " +
            " where tpt.category_id = tpc.id " +
            " and if(:hotelCode is not null && :hotelCode != '', tpc.hotel_code=:hotelCode, 1=1 ) " +
            " and if(:deleted is not null, tpc.deleted=:deleted, 1=1 ) ")
    List<Map<String, Object>> getTreeAndType(@Param("hotelCode")String hotelCode, @Param("deleted")Integer deleted);

}
