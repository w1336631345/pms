package com.kry.pms.dao.goods;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.goods.Product;

public interface ProductDao extends BaseDao<Product>{

    Product getById(String id);

}
