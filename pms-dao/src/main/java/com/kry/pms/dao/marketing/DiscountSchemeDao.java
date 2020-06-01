package com.kry.pms.dao.marketing;

import java.util.List;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.marketing.DiscountScheme;

public interface DiscountSchemeDao extends BaseDao<DiscountScheme>{

	List<DiscountScheme> findByHotelCodeAndDeleted(String currentHotleCode, int deleted);

    List<DiscountScheme> findByHotelCode(String code);

    DiscountScheme findByHotelCodeAndDeletedAndCode(String hotelCode, int deleted, String code);

    List<DiscountScheme> findByHotelCodeAndIsUsedAndDeleted(String currentHotleCode, String isUsed, int deleted);
}
