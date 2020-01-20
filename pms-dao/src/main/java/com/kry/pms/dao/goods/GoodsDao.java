package com.kry.pms.dao.goods;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.goods.Goods;
import com.kry.pms.model.persistence.goods.Product;

import java.util.List;

public interface GoodsDao extends BaseDao<Goods>{

    Goods getById(String id);

    List<Goods> getByHotelCode(String hotelCode);

}
