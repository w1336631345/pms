package com.kry.pms.service.busi;

import com.kry.pms.base.DtoResponse;
import com.kry.pms.model.persistence.busi.BookingRecord;
import com.kry.pms.service.BaseService;

public interface BookingRecordService extends BaseService<BookingRecord>{
	public DtoResponse<BookingRecord> book(BookingRecord record);

}