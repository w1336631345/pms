package com.kry.pms.dao.goods;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.goods.SetMeal;

import java.util.List;

public interface SetMealDao extends BaseDao<SetMeal>{

    List<SetMeal> findByHotelCodeAndDeleted(String hotelCode, int deleted);
}
