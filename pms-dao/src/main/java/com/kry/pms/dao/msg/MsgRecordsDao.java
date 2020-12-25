package com.kry.pms.dao.msg;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.msg.MsgRecords;

import java.util.List;

public interface MsgRecordsDao extends BaseDao<MsgRecords>{

    List<MsgRecords> findByHotelCode(String hotelCode);
}
