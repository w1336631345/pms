package com.kry.pms.dao.goods;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.goods.ProductCategory;

import java.util.List;

public interface ProductCategoryDao extends BaseDao<ProductCategory>{


    List<ProductCategory> findByHotelCodeAndDeleted(String hotelCode, int deleted);
}
