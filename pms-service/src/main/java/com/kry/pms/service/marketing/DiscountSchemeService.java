package com.kry.pms.service.marketing;

import java.util.List;

import com.kry.pms.model.persistence.marketing.DiscountScheme;
import com.kry.pms.service.BaseService;

public interface DiscountSchemeService extends BaseService<DiscountScheme>{

	List<DiscountScheme> getAllByHotelCode(String currentHotleCode, int deletedFalse);

}