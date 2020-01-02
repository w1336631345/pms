package com.kry.pms.service.busi;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.kry.pms.base.DtoResponse;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.http.request.busi.CheckInBo;
import com.kry.pms.model.http.request.busi.CheckUpdateItemTestBo;
import com.kry.pms.model.http.request.busi.TogetherBo;
import com.kry.pms.model.http.response.busi.AccountSummaryVo;
import com.kry.pms.model.http.response.busi.CheckInRecordListVo;
import com.kry.pms.model.persistence.busi.BookingItem;
import com.kry.pms.model.persistence.busi.BookingRecord;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.BaseService;

import javax.transaction.Transactional;

public interface CheckInRecordService extends BaseService<CheckInRecord> {
	
	public PageResponse<CheckInRecordListVo> querySummaryList(PageRequest<CheckInRecord> req);

    @Transactional
    HttpResponse modifyInfo(CheckInRecord checkInRecord);

    CheckInRecord update(CheckInRecord checkInRecord);

	CheckInRecord updateAll(CheckUpdateItemTestBo checkUpdateItemTestBo);

	HttpResponse cancelIn(String[] ids);

	public void checkIn(CheckInBo checkInBo, DtoResponse<List<CheckInRecord>> rep);

	public List<CheckInRecord> checkOut(String roomId);

	public CheckInRecord checkInByTempName(String string, String roomId, DtoResponse<String> response);

	public List<CheckInRecord> checkInByTempName(int humanCount, BookingRecord br, BookingItem item, GuestRoom gr,
			DtoResponse<String> response);

	public List<CheckInRecord> checkInByTempName(int humanCount, CheckInRecord cir, GuestRoom gr,
			DtoResponse<String> response);

	public List<CheckInRecord> findByBookId(String bookId);

	public List<CheckInRecord> findDetailByBookingId(String bookId);

	public CheckInRecord book(CheckInRecord checkInRecord);

    PageResponse<CheckInRecord> notYet(int pageCount, int pageSize,String status, User user);

    PageResponse<CheckInRecord> accountEntryList(int pageIndex, int pageSize, User user);

    List<CheckInRecord> accountEntryListAll(String hotelCode);

    PageResponse<Map<String, Object>> unreturnedGuests(int pageIndex, int pageSize, String status, User user);

	List<Map<String, Object>> getStatistics(User user);

	public Collection<AccountSummaryVo> getAccountSummaryByOrderNum(String orderNum, String accountCustomer);

	public List<CheckInRecord> findByOrderNum(String orderNum);

	public CheckInRecord addReserve(CheckInRecord checkInRecord);

	public List<CheckInRecord> addReserve(List<CheckInRecord> checkInRecords);

	public CheckInRecord addTogether(TogetherBo togetherBo);

	public List<CheckInRecord> findRoomTogetherRecord(CheckInRecord cir,String status);

	public DtoResponse<String> hangUp(String id);

	public DtoResponse<String> hangUpByAccountId(String id);

	List<CheckInRecord> findByOrderNumAndGuestRoomAndDeleted(String orderNum, GuestRoom guestRoom, int delete);

	HttpResponse callOffAssignRoom(String[] ids);

	//取消预订
	HttpResponse callOffReserve(String[] ids);

	HttpResponse updateCount(List<String> reserveIds, String mainRecordId);

    PageResponse<CheckInRecord> getNRoomLink(int pageIndex, int pageSize, String name, String roomNum, String hotelCode, String groupType);

    List<CheckInRecord> getRoomLinkList(String roomLinkId);

    void deleteRoomLink(String[] ids);

    List<CheckInRecord> getByRoomNum(String roomNum, String hotelCode, String groupType);

    List<CheckInRecord> checkInTogether(String hotelCode, String orderNum);

    List<CheckInRecord> findByTogetherCode(String hotelCode, String togetherCod);

    void addTogether(String hotelCode, String orderNum, String customerId, String status);

	//独单房价
	@org.springframework.transaction.annotation.Transactional(rollbackFor=Exception.class)
	void roomPriceAllocation(String hotelCode, String orderNum, String customerId);

    //平均房价
    @org.springframework.transaction.annotation.Transactional(rollbackFor=Exception.class)
    void roomPriceAvg(String hotelCode, String orderNum);

	public CheckInRecord queryByAccountId(String id);
}