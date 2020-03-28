package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.MemberType;

import java.util.List;

public interface MemberTypeDao extends BaseDao<MemberType>{

	List<MemberType> findByHotelCode(String hotelCode);



}
