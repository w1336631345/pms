package com.kry.pms.service.busi;

import com.kry.pms.base.DtoResponse;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.http.request.busi.CheckInBo;
import com.kry.pms.model.http.request.busi.CheckInRecordListBo;
import com.kry.pms.model.http.request.busi.CheckUpdateItemTestBo;
import com.kry.pms.model.http.request.busi.TogetherBo;
import com.kry.pms.model.http.response.busi.AccountSummaryVo;
import com.kry.pms.model.http.response.busi.CheckInRecordListVo;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.BaseService;
import freemarker.template.TemplateException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CheckInRecordService extends BaseService<CheckInRecord> {

	Collection<AccountSummaryVo> getAccountSummaryByOrderNum2(String orderNum, String type);

	public PageResponse<CheckInRecordListVo> querySummaryList(PageRequest<CheckInRecord> req);

    @Transactional
    HttpResponse modifyInfo(CheckInRecord checkInRecord);

	CheckInRecord update(CheckInRecord checkInRecord);

	CheckInRecord updateAll(CheckUpdateItemTestBo checkUpdateItemTestBo);

	HttpResponse cancelIn(String[] ids);

	public void checkIn(CheckInBo checkInBo, DtoResponse<List<CheckInRecord>> rep);

	public List<CheckInRecord> checkOut(String roomId);

	public CheckInRecord checkInByTempName(String string, String roomId, DtoResponse<String> response);

	public List<CheckInRecord> checkInByTempName(int humanCount, CheckInRecord cir, GuestRoom gr,
			DtoResponse<String> response);

	public List<CheckInRecord> findByBookId(String bookId);

	public List<CheckInRecord> findDetailByBookingId(String bookId);

	public CheckInRecord book(CheckInRecord checkInRecord);

    List<CheckInRecord> bookByRoomList(CheckInRecordListBo cirlb);

    @Transactional
	CheckInRecord bookByRoomTypeTest(CheckInRecord checkInRecord);

    //单房预订
    CheckInRecord singleRoom(CheckInRecord checkInRecord);

    PageResponse<CheckInRecord> notYet(int pageCount, int pageSize, String status, User user);

	PageResponse<CheckInRecord> accountEntryList(int pageIndex, int pageSize, User user);

	List<CheckInRecord> accountEntryListAll(String hotelCode);

	PageResponse<Map<String, Object>> unreturnedGuests(int pageIndex, int pageSize, String status, User user);

	List<Map<String, Object>> getStatistics(User user);

	public Collection<AccountSummaryVo> getAccountSummaryByOrderNum(String orderNum, String accountCustomer);

	public Collection<AccountSummaryVo> getAccountSummaryByLinkNum(String orderNum, String accountCustomer);

	public List<CheckInRecord> findByOrderNumC(String orderNum);

    PageResponse<Map<String, Object>> querySummaryListTo(PageRequest<CheckInRecord> prq);

    public List<CheckInRecord> findByOrderNum(String orderNum);

    List<Map<String, Object>> findByOrderNumC2(String hotelCode, String orderNum) throws IOException, TemplateException;

    List<Map<String, Object>> findByOrderNum2(String hotelCode, String orderNum);

	List<Map<String, Object>> sqlOrderNum(String orderNum);

	public CheckInRecord addReserve(CheckInRecord checkInRecord);

	public List<CheckInRecord> addReserve(List<CheckInRecord> checkInRecords);

	public CheckInRecord addTogether(TogetherBo togetherBo);

	public List<CheckInRecord> findRoomTogetherRecord(CheckInRecord cir, String status);

	public DtoResponse<String> hangUp(String id);

	public DtoResponse<String> hangUpByAccountId(String id);

	List<CheckInRecord> findByOrderNumAndGuestRoomAndDeleted(String orderNum, GuestRoom guestRoom, int delete);

	HttpResponse callOffAssignRoom(String[] ids);

	// 取消预订
	HttpResponse callOffReserve(String[] ids);

    @Transactional
    HttpResponse offReserve(String id);

    HttpResponse updateCount(List<String> reserveIds, String mainRecordId);

	PageResponse<CheckInRecord> getNRoomLink(int pageIndex, int pageSize, String name, String roomNum, String hotelCode,
			String groupType, String corp, String status, String account);

	List<CheckInRecord> getRoomLinkList(String roomLinkId);

	void deleteRoomLink(String[] ids);

	List<CheckInRecord> getByRoomNum(String roomNum, String hotelCode, String groupType);

	List<CheckInRecord> checkInTogether(String hotelCode, String orderNum, String guestRoomId);

	List<CheckInRecord> findByTogetherCode(String hotelCode, String togetherCod);

	void addTogether(String hotelCode, String orderNum, String customerId, String status, String guestRoomId);

	// 独单房价
	@org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
	void roomPriceAllocation(String hotelCode, String orderNum, String customerId, String guestRoomId);

	// 平均房价
	@org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
	void roomPriceAvg(String hotelCode, String orderNum, String guestRoomId);

	public CheckInRecord queryByAccountId(String id);

	public List<CheckInRecord> findByGuestRoomAndStatusAndDeleted(GuestRoom guestRoom,
			String checkinRecordStatusCheckIn, int deletedFalse);
	
	public List<CheckInRecord> findByGuestRoomAndStatusAndDeleted(String guestRoomId,
			String checkinRecordStatusCheckIn, int deletedFalse);

	public List<CheckInRecord> findTodayCheckInRecord(GuestRoom guestRoom, String status);

	public List<CheckInRecord> findByLinkId(String id);

    List<Map<String, Object>> getGroup(String hotelCode, String arriveTime, String leaveTime, String name_, String code_);
    void inGroup(String[] cId, String gId, Boolean isFollowGroup);

    void outGroup(String[] cId, String gId, Boolean isFollowGroup);

	void updateGroup(String[] cId, String gId, String uId, Boolean isFollowGroup);

	//修改预留
	CheckInRecord updateReserve(CheckInRecord cir);

    List<CheckInRecord> getRoomLinkListTo(String id, String orderNum);

    List<String> getRoomLayout(String checkInId);

	List<String> getRoomRequirement(String checkInId);
}