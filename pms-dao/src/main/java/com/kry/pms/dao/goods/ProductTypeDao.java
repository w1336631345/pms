package com.kry.pms.dao.goods;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.goods.ProductType;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ProductTypeDao extends BaseDao<ProductType> {

    @Query(nativeQuery = true, value = " select * from  t_product_type")
    List<Map<String, Object>> getTypeList();

    List<ProductType> findByCategoryId(String categoryId);

}
