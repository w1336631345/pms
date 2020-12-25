package com.kry.pms.dao.msg;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.msg.MsgAccount;

import java.util.List;

public interface MsgAccountDao extends BaseDao<MsgAccount>{

    List<MsgAccount> findByHotelCode(String hotelCode);

}
