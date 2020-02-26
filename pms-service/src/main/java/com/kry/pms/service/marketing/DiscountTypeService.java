package com.kry.pms.service.marketing;

import com.kry.pms.model.persistence.marketing.DiscountType;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface DiscountTypeService extends BaseService<DiscountType>{

	List<DiscountType> getAllByHotelCode(String currentHotleCode, int deletedFalse);

}