package com.kry.pms.dao.goods;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.goods.ProductType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface ProductTypeDao extends BaseDao<ProductType> {

    @Query(nativeQuery = true, value = " select tp.id productId, tp.name codeName, tp.code_ codeNum " +
            " from t_product_type tpt, t_product tp, t_product_category tpc " +
            " where tp.id = tpt.product_id and tpt.category_id = tpc.id " +
            " and if(:categoryId is not null && :categoryId != '', tpc.id=:categoryId, 1=1 ) ")
    List<Map<String, Object>> getTypeList(@Param("categoryId")String categoryId);

    List<ProductType> findByCategoryId(String categoryId);

}
