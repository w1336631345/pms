package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.MemberRecharge;

import java.util.List;

public interface MemberRechargeDao extends BaseDao<MemberRecharge>{

	List<MemberRecharge> findByHotelCode(String hotelCode);



}
