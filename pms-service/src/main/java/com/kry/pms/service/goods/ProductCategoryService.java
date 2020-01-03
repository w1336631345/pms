package com.kry.pms.service.goods;

import com.kry.pms.model.persistence.goods.ProductCategory;
import com.kry.pms.service.BaseService;

import java.util.List;
import java.util.Map;

public interface ProductCategoryService extends BaseService<ProductCategory>{

    List<ProductCategory> treeAndType(String hotelCode);

    List<Map<String, Object>> getTreeAndType(String hotelCode);
}