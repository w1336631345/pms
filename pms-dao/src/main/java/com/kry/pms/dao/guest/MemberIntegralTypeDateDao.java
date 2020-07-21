package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.MemberIntegralTypeDate;

import java.util.List;

public interface MemberIntegralTypeDateDao extends BaseDao<MemberIntegralTypeDate>{

	List<MemberIntegralTypeDate> findByHotelCode(String hotelCode);


}
