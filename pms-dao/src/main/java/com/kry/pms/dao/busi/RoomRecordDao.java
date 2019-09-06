package com.kry.pms.dao.busi;

import java.time.LocalDate;
import java.util.List;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.busi.RoomRecord;

public interface RoomRecordDao extends BaseDao<RoomRecord>{

	List<RoomRecord> findByHotelCodeAndRecordDate(String hotelCode, LocalDate recordDate);

}
