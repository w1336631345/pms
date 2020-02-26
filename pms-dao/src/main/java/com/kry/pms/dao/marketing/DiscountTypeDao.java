package com.kry.pms.dao.marketing;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.marketing.DiscountType;

import java.util.List;

public interface DiscountTypeDao extends BaseDao<DiscountType>{

	List<DiscountType> findByHotelCodeAndDeleted(String currentHotleCode, int deleted);

}
