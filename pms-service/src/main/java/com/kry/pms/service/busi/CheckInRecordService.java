package com.kry.pms.service.busi;

import java.util.List;
import java.util.Map;

import com.kry.pms.base.DtoResponse;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.http.request.busi.CheckInBo;
import com.kry.pms.model.persistence.busi.BookingItem;
import com.kry.pms.model.persistence.busi.BookingRecord;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.BaseService;
import org.springframework.data.domain.Page;

public interface CheckInRecordService extends BaseService<CheckInRecord>{

	public void checkIn(CheckInBo checkInBo, DtoResponse<List<CheckInRecord>> rep);

	public List<CheckInRecord> checkOut(String roomId);

	public CheckInRecord checkInByTempName(String string, String roomId,DtoResponse<String> response);

	public List<CheckInRecord> checkInByTempName(int humanCount,BookingRecord br, BookingItem item, GuestRoom gr,
			DtoResponse<String> response);

	public List<CheckInRecord> findByBookId(String bookId);

	public List<CheckInRecord> findDetailByBookingId(String bookId);

    PageResponse<CheckInRecord> notYet(int pageCount, int pageSize,String status, User user);

	PageResponse<Map<String, Object>> unreturnedGuests(int pageIndex, int pageSize, String status, User user);

	List<Map<String, Object>> getStatistics(User user);
}