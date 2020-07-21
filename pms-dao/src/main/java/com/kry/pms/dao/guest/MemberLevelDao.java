package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.MemberLevel;

import java.util.List;

public interface MemberLevelDao extends BaseDao<MemberLevel>{

	List<MemberLevel> findByHotelCode(String hotelCode);


}
