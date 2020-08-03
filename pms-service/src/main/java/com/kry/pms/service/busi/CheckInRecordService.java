package com.kry.pms.service.busi;

import com.kry.pms.base.DtoResponse;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.http.request.busi.*;
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

    Collection<AccountSummaryVo> getAccountSummaryByOrderNum2(String hotelCode,String orderNum, String type);

    public PageResponse<CheckInRecordListVo> querySummaryList(PageRequest<CheckInRecord> req);

    @Transactional
    HttpResponse modifyInfo(CheckInRecord checkInRecord);

    CheckInRecord update(CheckInRecord checkInRecord);

    CheckInRecord updateLog(CheckInRecord checkInRecord);

    HttpResponse updateAll(CheckUpdateItemTestBo checkUpdateItemTestBo);

    //仅仅是为了批量操作记录日志，不做任何处理
//    @UpdateAnnotation(name = "订单号", value = "orderNum", type = "GO")
    HttpResponse updateAllLog(CheckInRecord checkInRecord);

    HttpResponse cancelIn(String[] ids, String hotelCode);

    CheckInRecord logFindById(String id);

    public void checkIn(CheckInBo checkInBo, DtoResponse<List<CheckInRecord>> rep);

    public DtoResponse<List<CheckInRecord>> checkOut(String type, String id, String orderNum);

    public CheckInRecord checkInByTempName(String string, String roomId, DtoResponse<String> response);

    public HttpResponse checkInByTempName(int humanCount, CheckInRecord cir, GuestRoom gr,
                                          DtoResponse<String> response);

    public List<CheckInRecord> findByBookId(String bookId);

    public List<CheckInRecord> findDetailByBookingId(String bookId);

    public HttpResponse book(CheckInRecord checkInRecord);

    HttpResponse bookByRoomList(CheckInRecordListBo cirlb, User user);

    @Transactional
    HttpResponse bookByRoomTypeTest(CheckInRecord checkInRecord, User user);

    //单房预订
    HttpResponse singleRoom(CheckInRecord checkInRecord);

    PageResponse<CheckInRecord> notYet(int pageCount, int pageSize, String status, User user);

    PageResponse<CheckInRecord> accountEntryList(int pageIndex, int pageSize, User user);

    List<CheckInRecord> accountEntryListAll(String hotelCode);

    PageResponse<Map<String, Object>> accountEntryListMap(int pageIndex, int pageSize, User user);

    PageResponse<Map<String, Object>> unreturnedGuests(int pageIndex, int pageSize, String mainNum, String status, User user);

    List<Map<String, Object>> getStatistics(User user);

    Collection<AccountSummaryVo> getAccountSummaryByLinkNum(String hotelCode, String orderNum, String accountCustomer);

    List<CheckInRecord> findByOrderNumC(String hotelCode, String orderNum);

    PageResponse<Map<String, Object>> querySummaryListTo(PageRequest<CheckInRecord> prq);

    PageResponse<Map<String, Object>> querySummaryListToBySql(String hotelCode, PageRequest pageRequest) throws IOException, TemplateException;

    PageResponse<Map<String, Object>> resverListHis(String hotelCode, PageRequest pageRequest) throws IOException, TemplateException;

    List<Map<String, Object>> querySummaryListToBySqlTotal(String hotelCode, Map<String, Object> params) throws IOException, TemplateException;


    public List<CheckInRecord> findByOrderNum(String hotelCode, String orderNum);

    List<Map<String, Object>> findByOrderNumC2(String hotelCode, String orderNum) throws IOException, TemplateException;

    List<Map<String, Object>> findByOrderNum2(String hotelCode, String orderNum) throws IOException, TemplateException;

    List<Map<String, Object>> sqlOrderNum(String orderNum);

    public CheckInRecord addReserve(CheckInRecord checkInRecord);

    public HttpResponse addReserve(List<CheckInRecord> checkInRecords);

    public CheckInRecord addTogether(TogetherBo togetherBo);

    public List<CheckInRecord> findRoomTogetherRecord(CheckInRecord cir, String status);

    /**
     * @param id checkinrecord  的   id
     * @return
     */
    public DtoResponse<String> hangUp(String id, String type, String extFee, String orderNum);

    public boolean hangUp(String checkInRecordId, String extFee);

    public DtoResponse<String> hangUpByAccountId(String id);

    List<CheckInRecord> findByOrderNumAndGuestRoomAndDeleted(String orderNum, GuestRoom guestRoom, int delete);

    HttpResponse callOffAssignRoom(String[] ids);

    // 取消预订
    HttpResponse callOffReserve(String[] ids);

    //夜审的取消预订
    @Transactional
    HttpResponse callOffReserveAudit(String[] ids);

    @Transactional
    HttpResponse offReserve(String id);

    HttpResponse updateCount(List<String> reserveIds, String mainRecordId);

    PageResponse<CheckInRecord> getNRoomLink(int pageIndex, int pageSize, String name, String roomNum, String hotelCode,
                                             String groupType, String corp, String status, String account);

    List<CheckInRecord> getRoomLinkList(String roomLinkId);

    void deleteRoomLink(String[] ids);

    List<CheckInRecord> getByRoomNum(String roomNum, String hotelCode, String groupType);

    List<CheckInRecord> checkInTogether(String hotelCode, String orderNum, String guestRoomId);

    List<CheckInRecord> checkInTogetherByStatus(String hotelCode, String orderNum, String guestRoomId, List<String> status);

    List<CheckInRecord> findByTogetherCode(String hotelCode, String togetherCod);

    HttpResponse addTogether(String hotelCode, String orderNum, String customerId, String status, String guestRoomId);

    // 独单房价
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    HttpResponse roomPriceAllocation(String hotelCode, String orderNum, String checkInRecordId, String guestRoomId);

    // 平均房价
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    HttpResponse roomPriceAvg(String hotelCode, String orderNum, String guestRoomId);

    public CheckInRecord queryByAccountId(String id);

    public List<CheckInRecord> findByGuestRoomAndStatusAndDeleted(GuestRoom guestRoom,
                                                                  String checkinRecordStatusCheckIn, int deletedFalse);

    public List<CheckInRecord> findByGuestRoomAndStatusAndDeleted(String guestRoomId,
                                                                  String checkinRecordStatusCheckIn, int deletedFalse);

    public List<CheckInRecord> findTodayCheckInRecord(GuestRoom guestRoom, String status);

    public List<CheckInRecord> findByLinkId(String id);

    List<Map<String, Object>> getGroup(String hotelCode, String arriveTime, String leaveTime, String name_, String code_);

    HttpResponse inGroup(String[] cId, String gId, Boolean isFollowGroup);

    HttpResponse outGroup(String[] cId, String gId, Boolean isFollowGroup, String roomPriceId);

    HttpResponse updateGroup(String[] cId, String gId, String uId, Boolean isFollowGroup);

    //修改预留
    HttpResponse updateReserve(CheckInRecord cir);

    List<CheckInRecord> getRoomLinkListTo(String id, String orderNum);

    List<String> getRoomLayout(String checkInId);

    List<String> getRoomRequirement(String checkInId);

    List<Map<String, Object>> resourceStatistics(String orderNum, String arriveTime, String leaveTime);

    HttpResponse callOffG(String id);

    HttpResponse recovery(String id);

    List<Map<String, Object>> resourceStatisticsTo(String orderNum, String arriveTime, String leaveTime);

    CheckInRecord byId(String id);

    HttpResponse printing(String checkInRecordId);

    HttpResponse yesterdayAudit(CheckInRecordAuditBo checkBo, User user);

    HttpResponse wechatUpdate(CheckInRecord checkInRecord);

    List<Map<String, Object>> printDeposit(String hotelCode, String orderNum);

    CheckInRecord findByAccountCode(String hotelCode, String code);
}