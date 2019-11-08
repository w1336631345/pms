package com.kry.pms.service.busi;

import java.time.LocalDate;
import java.util.List;

import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.DailyVerify;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.BaseService;

public interface RoomRecordService extends BaseService<RoomRecord> {

	void createRoomRecord(CheckInRecord cir);
	void dailyVerify(String hotelCode, LocalDate recordDate, DailyVerify dv);
	void checkOut(List<CheckInRecord> crs);

    PageResponse<RoomRecord> accountEntryList(int pageIndex, int pageSize, User user);

    PageResponse<RoomRecord> accountEntryListTest(int pageIndex, int pageSize, User user);

    List<RoomRecord> accountEntryListAll(String hotelCode);
}