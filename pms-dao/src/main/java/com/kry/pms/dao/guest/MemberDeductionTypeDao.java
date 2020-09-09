package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.MemberDeductionType;

import java.util.List;

public interface MemberDeductionTypeDao extends BaseDao<MemberDeductionType>{

	List<MemberDeductionType> findByHotelCode(String hotelCode);


}
