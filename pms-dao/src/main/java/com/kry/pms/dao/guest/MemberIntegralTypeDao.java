package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.MemberIntegralType;

import java.util.List;

public interface MemberIntegralTypeDao extends BaseDao<MemberIntegralType>{

	List<MemberIntegralType> findByHotelCode(String hotelCode);


}
