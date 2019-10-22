package com.kry.pms.service.busi;

import com.kry.pms.base.DtoResponse;
import com.kry.pms.model.http.request.busi.BookOperationBo;
import com.kry.pms.model.persistence.busi.BookingRecord;
import com.kry.pms.service.BaseService;

public interface BookingRecordService extends BaseService<BookingRecord> {
	public DtoResponse<BookingRecord> book(BookingRecord record);

	public DtoResponse<BookingRecord> operation(BookOperationBo bookOperationBo);

	BookingRecord findByBookingItemId(String bookingItemId);

}