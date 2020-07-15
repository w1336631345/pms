package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.MemberInfo;

import java.util.List;

public interface MemberInfoDao extends BaseDao<MemberInfo>{

	List<MemberInfo> findByHotelCode(String hotelCode);



}
