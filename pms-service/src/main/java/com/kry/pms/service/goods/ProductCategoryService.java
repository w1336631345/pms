package com.kry.pms.service.goods;

import com.kry.pms.model.persistence.goods.ProductCategory;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface ProductCategoryService extends BaseService<ProductCategory>{

    List<ProductCategory> treeAndType(String hotelCode);
}