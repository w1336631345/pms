package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.MemberIntegralTypeInfo;

import java.util.List;

public interface MemberIntegralTypeInfoDao extends BaseDao<MemberIntegralTypeInfo>{

	List<MemberIntegralTypeInfo> findByHotelCode(String hotelCode);


}
