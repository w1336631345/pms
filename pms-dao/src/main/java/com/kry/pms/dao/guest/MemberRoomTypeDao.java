package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.MemberRoomType;

import java.util.List;

public interface MemberRoomTypeDao extends BaseDao<MemberRoomType>{

	List<MemberRoomType> findByHotelCode(String hotelCode);



}
