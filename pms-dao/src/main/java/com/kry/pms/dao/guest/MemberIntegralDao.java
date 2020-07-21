package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.MemberIntegral;

import java.util.List;

public interface MemberIntegralDao extends BaseDao<MemberIntegral>{

	List<MemberIntegral> findByHotelCode(String hotelCode);

	List<MemberIntegral> findByHotelCodeAndCardNum(String hotelCode, String cardNum);

}
