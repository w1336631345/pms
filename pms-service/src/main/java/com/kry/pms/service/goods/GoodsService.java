package com.kry.pms.service.goods;

import com.kry.pms.model.persistence.goods.Goods;
import com.kry.pms.model.persistence.goods.Product;
import com.kry.pms.service.BaseService;

public interface GoodsService extends BaseService<Goods>{

    Goods getById(String id);
}