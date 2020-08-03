package com.kry.pms.service.busi.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.busi.DailyVerify;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.busi.*;
import com.kry.pms.service.sys.DateTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.kry.pms.base.Constants;
import com.kry.pms.base.DtoResponse;
import com.kry.pms.model.http.request.busi.BillItemBo;
import com.kry.pms.model.http.request.busi.BillSettleBo;
import com.kry.pms.model.http.request.busi.CheckInBo;
import com.kry.pms.model.http.request.busi.CheckInItemBo;
import com.kry.pms.model.http.request.busi.RoomAssignBo;
import com.kry.pms.model.http.response.busi.AccountSummaryVo;
import com.kry.pms.model.other.wrapper.CheckInRecordWrapper;
import com.kry.pms.model.persistence.busi.BillItem;
import com.kry.pms.model.persistence.busi.BookingRecord;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.GuestRoomStatus;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.model.persistence.sys.SystemConfig;
import com.kry.pms.service.marketing.RoomPriceSchemeItemService;
import com.kry.pms.service.org.EmployeeService;
import com.kry.pms.service.room.GuestRoomService;
import com.kry.pms.service.room.GuestRoomStatusService;
import com.kry.pms.service.room.RoomStatisticsService;
import com.kry.pms.service.room.RoomTypeService;
import com.kry.pms.service.sys.AccountService;
import com.kry.pms.service.sys.BusinessSeqService;
import com.kry.pms.service.sys.SystemConfigService;

@Transactional
@Service
public class ReceptionServiceImpl implements ReceptionService {
	@Autowired
	BookingRecordService bookingRecordService;
	@Autowired
	EmployeeService employeeService;
	@Autowired
	RoomTypeService roomTypeService;
	@Autowired
	RoomRecordService roomRecordService;
	@Autowired
	CheckInRecordService checkInRecordService;
	@Autowired
	private SystemConfigService systemConfigService;
	@Autowired
	private GuestRoomService guestRoomService;
	@Autowired
	GuestRoomStatusService guestRoomStatusService;
	@Autowired
	BillService billService;
	@Autowired
	BookingItemService bookingItemService;
	@Autowired
	RoomPriceSchemeItemService roomPriceSchemeItemService;
	@Autowired
	BusinessSeqService businessSeqService;
	@Autowired
	AccountService accountService;
	@Autowired
	RoomStatisticsService roomStatisticsService;
	@Autowired
	DateTimeService dateTimeService;
	@Autowired
	DailyVerifyService dailyVerifyService;

	private List<CheckInRecord> createGroupMainCheckInRecord(BookingRecord br) {
		String tempName = br.getName();
		String checkInSn = businessSeqService.fetchNextSeqNum(br.getHotelCode(), Constants.Key.BUSINESS_SEQ_KEY);
		List<CheckInRecord> data = new ArrayList<CheckInRecord>();
		List<CheckInRecord> sub = new ArrayList<CheckInRecord>();
		CheckInRecord mcir = new CheckInRecord();
		mcir.setType(Constants.Type.CHECK_IN_RECORD_GROUP);
		mcir.setGroupType(Constants.Type.CHECK_IN_RECORD_GROUP_TYPE_YES);
		mcir.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);
		mcir.setArriveTime(br.getArriveTime());
		mcir.setLeaveTime(br.getLeaveTime());
		mcir.setDays(br.getDays());
		mcir.setHoldTime(br.getHoldTime());
		for (CheckInRecord cir : br.getCheckInRecords()) {
			try {
				cir = (CheckInRecord) cir.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			cir.setHotelCode(br.getHotelCode());
			cir.setOrderNum(checkInSn);
			cir.setName(tempName);
			cir.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);
			cir.setType(Constants.Type.CHECK_IN_RECORD_RESERVE);
			cir.setArriveTime(br.getArriveTime());
			cir.setLeaveTime(br.getLeaveTime());
			cir.setDays(br.getDays());
			cir.setHoldTime(br.getHoldTime());
			cir.setRoomType(roomTypeService.findById(cir.getRoomTypeId()));
			sub.add(cir);
		}
		mcir.setSubRecords(sub);
		data.add(mcir);
		return data;
	}

	@Transactional
	@Override
	public DtoResponse<List<CheckInRecord>> checkIn(CheckInBo checkInBo) {
		DtoResponse<List<CheckInRecord>> rep = new DtoResponse<List<CheckInRecord>>();
		return rep;
	}

	@Override
	public DtoResponse<List<BillItem>> accountEntry(BillItemBo billItemBo) {
		return null;
	}

	/**
	 * 房间状态确认
	 * 
	 * @param checkInBo
	 * @param rep
	 * @return
	 */
	private boolean roomStatusCheck(CheckInBo checkInBo, DtoResponse<List<CheckInRecord>> rep) {
		SystemConfig sc = systemConfigService.getByHotelCodeAndKey(checkInBo.getHotelCode(),
				Constants.SystemConfig.CODE_VARCANT_DIRTY_CHECK_IN_ABLE);
		boolean vdcia = false;
		if (sc != null) {
			vdcia = Boolean.valueOf(sc.getValue());
		}
		boolean result = true;
		for (CheckInItemBo ciib : checkInBo.getItems()) {
			GuestRoom gr = guestRoomService.findById(ciib.getRoomId());
			if (gr == null) {
				result = false;
			} else {
				GuestRoomStatus grs = guestRoomStatusService.findGuestRoomStatusByGuestRoom(gr);
				// 判断空净房 或 空脏房且系统设置允许空脏房入住
				if (grs == null || !(Constants.Status.ROOM_STATUS_VACANT_CLEAN.equals(grs.getRoomStatus())
						|| (vdcia && Constants.Status.ROOM_STATUS_VACANT_DIRTY.equals(grs.getRoomStatus())))) {
					result = false;
				}
			}
		}
		return result;
	}

	@Transactional
	@Override
	public DtoResponse<String> billSettle(BillSettleBo bsb) {
		billService.checkAndPayBill(bsb);
		DtoResponse<String> rep = new DtoResponse<String>();
		return rep;
	}

	@Transactional
	@Override
	public DtoResponse<String> assignRoom(RoomAssignBo roomAssignBo) {
		DtoResponse<String> response = new DtoResponse<String>();
		String checkInRecordId = roomAssignBo.getCheckInRecordId();
		CheckInRecord cir = checkInRecordService.findById(checkInRecordId);
		if (cir != null) {
			if (cir.getType().equals(Constants.Type.CHECK_IN_RECORD_RESERVE)) {
//				if (roomAssignBo.getRoomId().length <= (cir.getRoomCount() - cir.getCheckInCount())) {//原来是不变预订数，增加已排房数
				if (roomAssignBo.getRoomId().length <= cir.getRoomCount()) {
					for (String roomId : roomAssignBo.getRoomId()) {
						GuestRoom gr = guestRoomService.findById(roomId);
						HttpResponse hr = checkInRecordService.checkInByTempName(cir.getSingleRoomCount(), cir, gr, response);
						if(hr.getStatus() == 9999){
							TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
							response.setStatus(Constants.BusinessCode.CODE_RESOURCE_NOT_ENOUGH);
							response.setMessage("资源不足");
							return response;
						}
					}
				} else {
					response.setStatus(Constants.BusinessCode.CODE_RESOURCE_NOT_ENOUGH);
					response.setMessage("选择房间数过多，请重新选择");
				}

				if (response.getStatus() == 0) {
					cir.setCheckInCount(cir.getCheckInCount() + roomAssignBo.getRoomId().length);
					cir.setRoomCount(cir.getRoomCount() - roomAssignBo.getRoomId().length);
//					if (cir.getRoomCount() == cir.getCheckInCount()) {
					if (cir.getRoomCount() == 0) {
						cir.setDeleted(Constants.DELETED_TRUE);
					}
					if(cir.getSingleRoomCount() != null){
						int hCount = roomAssignBo.getRoomId().length * cir.getSingleRoomCount();
						cir.setHumanCount(cir.getHumanCount() - hCount);
					}
					checkInRecordService.modify(cir);
				} else {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				}
			} else {
				GuestRoom gr = guestRoomService.findById(roomAssignBo.getRoomId()[0]);
				cir.setGuestRoom(gr);
				cir.setCheckInCount(cir.getRoomCount());
				checkInRecordService.modify(cir);
			}
		} else {
			response.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
			response.setMessage("找不到预定信息，请重新选择");
		}
		return response;
	}

	@Override
	@Transactional
	public DtoResponse<String> checkInM(String id, User user) {
		DtoResponse<String> rep = new DtoResponse<>();
		CheckInRecord cir = checkInRecordService.findById(id);
		LocalDateTime now = LocalDateTime.now();
		if(now.toLocalDate().isBefore(cir.getArriveTime().toLocalDate())){
			rep.error(Constants.BusinessCode.CODE_PARAMETER_INVALID,"提前入住“>1天”，请修改时间重算资源");
			return rep;
		}
		LocalTime criticalTime = systemConfigService.getCriticalTime(user.getHotelCode());
		if(now.toLocalDate().isEqual(cir.getArriveTime().toLocalDate())){
			if(cir.getArriveTime().toLocalTime().isAfter(criticalTime)){
				if(now.toLocalTime().isBefore(criticalTime)){
					rep.error(Constants.BusinessCode.CODE_PARAMETER_INVALID,"提前到凌晨入住，请修改时间重算资源");
					return rep;
				}
			}
		}
		if(now.isAfter(cir.getLeaveTime())){
			rep.error(Constants.BusinessCode.CODE_PARAMETER_INVALID,"已过离店时间，无法入住");
			return rep;
		}
		if(now.toLocalDate().isAfter(cir.getArriveTime().toLocalDate())){
			if(now.toLocalTime().isAfter(criticalTime)){
				rep.error(Constants.BusinessCode.CODE_PARAMETER_INVALID,"当前日期已过到店日期，请修改到店日期重算资源");
				return rep;
			}
		}
		if (cir != null) {
			// 预留单不能入住
			if ((Constants.Type.CHECK_IN_RECORD_RESERVE).equals(cir.getType())) {
				rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
				rep.setMessage("预留单不能入住");
				return rep;
			}
			rep = checkIn(cir);
			// 不是主单，只要有人员入住，主单改为入住状态
			if (!(Constants.Type.CHECK_IN_RECORD_GROUP).equals(cir.getType())) {
				if(cir.getMainRecord() != null){
					String mainRecordId = cir.getMainRecord().getId();
					CheckInRecord cirMain = checkInRecordService.findById(mainRecordId);
					if (!(Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN).equals(cirMain.getStatus())) {
						rep = checkIn(cirMain);
					}
				}
			}
		} else {
			rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
			rep.setMessage("没有找到对应的入住记录");
		}
		LocalDate businessDate = businessSeqService.getBuinessDate(cir.getHotelCode());
		//(因为在事务中，所以入住状态还没有commit，是R)
		checkInAuditRoomRecord("R",cir, businessDate, user);
		return rep;
	}

	@Override
	public DtoResponse<String> checkIn(CheckInRecord cir) {
		DtoResponse<String> rep = new DtoResponse<>();
		LocalDateTime now = LocalDateTime.now();
//		LocalDateTime now = dateTimeService.getBusinessDateTime(cir.getHotelCode());
		cir.setActualTimeOfArrive(now);
		cir.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN);
		checkInRecordService.modify(cir);
		if(Constants.Type.CHECK_IN_RECORD_CUSTOMER.equals(cir.getType())){
			List<CheckInRecord> together = checkInRecordService.checkInTogether(cir.getHotelCode(), cir.getOrderNum(), cir.getGuestRoom().getId());
			for(int i=0; i<together.size(); i++){
				CheckInRecord c = together.get(i);
				c.setActualTimeOfArrive(now);
				c.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN);
				checkInRecordService.modify(c);
			}
			roomStatisticsService.checkIn(new CheckInRecordWrapper(cir));
		}
		return rep;
	}
	@Override
	public AccountSummaryVo getAccountSummaryByAccountCode(String hotelCode,String code){
		CheckInRecord cir = checkInRecordService.findByAccountCode(hotelCode,code);
		return getAccountSummaryByCheckRecord(cir);
	}

	private AccountSummaryVo getAccountSummaryByCheckRecord(CheckInRecord cir){
		AccountSummaryVo asv = null;
		if (cir != null && cir.getGroupType() != null) {
			if(cir.getMainRecord()!=null){
				cir = cir.getMainRecord();
			}
			if(Constants.Type.CHECK_IN_RECORD_GROUP_TYPE_YES.equals(cir.getGroupType())||"T".equals(cir.getFitType())){
				asv = new AccountSummaryVo();
				asv.setName("团队所有账务");
				asv.setType("temp");
				asv.setSettleType(Constants.Type.SETTLE_TYPE_NONE);
				asv.setChildren(new ArrayList<>());
				Account account = cir.getAccount();
				AccountSummaryVo group = new AccountSummaryVo(cir);
				group.setRoomStatus(cir.getStatus());
				if(Constants.Type.CHECK_IN_RECORD_GROUP_TYPE_YES.equals(cir.getGroupType())){
					group.setSettleType(Constants.Type.SETTLE_TYPE_GROUP);
				}else{
					group.setSettleType(Constants.Type.SETTLE_TYPE_IGROUP);
				}
				asv.getChildren().add(group);
				asv.getChildren().addAll((checkInRecordService.getAccountSummaryByOrderNum2(cir.getHotelCode(),cir.getOrderNum(),
						Constants.Type.CHECK_IN_RECORD_CUSTOMER)));
			}else if(cir.getRoomLinkId() != null){
				Collection<AccountSummaryVo> data = checkInRecordService
						.getAccountSummaryByLinkNum(cir.getHotelCode(),cir.getRoomLinkId(), Constants.Type.CHECK_IN_RECORD_CUSTOMER);
				asv = new AccountSummaryVo();
				asv.setName("联房账务");
				asv.setType("link");
				asv.setId(cir.getRoomLinkId());
				asv.setSettleType(Constants.Type.SETTLE_TYPE_LINK);
				asv.setChildren(data);
			}else{
				Collection<AccountSummaryVo> data = checkInRecordService
						.getAccountSummaryByOrderNum2(cir.getHotelCode(),cir.getOrderNum(), Constants.Type.CHECK_IN_RECORD_CUSTOMER);
				if (data != null && !data.isEmpty()) {
					asv = (AccountSummaryVo) data.toArray()[0];
				}
			}
		} else {
			// 散客
		}
		return asv;
	}

	@Override
	public AccountSummaryVo getAccountSummaryByCheckRecordId(String id) {
		CheckInRecord cir = checkInRecordService.findById(id);
		return getAccountSummaryByCheckRecord(cir);
	}

	@Override
	public DtoResponse<List<AccountSummaryVo>> groupCheckBillConfirm(String id) {
		DtoResponse<List<AccountSummaryVo>> rep = new DtoResponse<List<AccountSummaryVo>>();
		CheckInRecord cir = checkInRecordService.findById(id);
		if (cir != null) {
			String orderNum = cir.getOrderNum();
			Collection<Account> asvs = accountService.getAccountByOrderNumAndStatusAndCheckInType(orderNum,
					Constants.Type.CHECK_IN_RECORD_CUSTOMER, Constants.Status.ACCOUNT_IN);
			if (asvs != null && !asvs.isEmpty()) {
				rep.setStatus(Constants.BusinessCode.CODE_ILLEGAL_OPERATION);
				rep.setMessage("有未结账的客房，请先结客房帐");
			}
		}
		return rep;
	}

	@Override
	@Transactional
	public DtoResponse<String> checkInAll(String[] ids,User user) {
		LocalDate businessDate = businessSeqService.getBuinessDate(user.getHotelCode());
		DtoResponse<String> rep = new DtoResponse<>();
		CheckInRecord checkInRecord = checkInRecordService.findById(ids[0]);
//		if (checkInRecord!=null&&!(Constants.Type.CHECK_IN_RECORD_GROUP).equals(checkInRecord.getType())) {
//			if (checkInRecord.getMainRecord()!=null&&!(Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN).equals(checkInRecord.getMainRecord().getStatus())){
//				rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
//				rep.setMessage("请先做团队主单入住，再做成员入住");
//				return rep;
//			}
//		}
		// 不是主单，只要有人员入住，主单改为入住状态(与上面注释掉的代码相反)
		if (!(Constants.Type.CHECK_IN_RECORD_GROUP).equals(checkInRecord.getType())) {
			if(checkInRecord.getMainRecord() != null){
				String mainRecordId = checkInRecord.getMainRecord().getId();
				CheckInRecord cirMain = checkInRecordService.findById(mainRecordId);
				if (!(Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN).equals(cirMain.getStatus())) {
					rep = checkIn(cirMain);
				}
			}
		}
		for (int i = 0; i < ids.length; i++) {
			CheckInRecord cir = checkInRecordService.findById(ids[i]);
			LocalDateTime now = LocalDateTime.now();
			if(now.toLocalDate().isBefore(cir.getArriveTime().toLocalDate())){
				rep.error(Constants.BusinessCode.CODE_PARAMETER_INVALID,"提前入住请修改到达时间，确认房类资源足够");
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return rep;
			}
			if(now.isAfter(cir.getLeaveTime())){
				rep.error(Constants.BusinessCode.CODE_PARAMETER_INVALID,"已过离店时间，无法入住");
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return rep;
			}
			if(now.toLocalDate().isEqual(cir.getArriveTime().toLocalDate())){
				LocalTime criticalTime = systemConfigService.getCriticalTime(user.getHotelCode());
				if(cir.getArriveTime().toLocalTime().isAfter(criticalTime)){
					if(now.toLocalTime().isBefore(criticalTime)){
						rep.error(Constants.BusinessCode.CODE_PARAMETER_INVALID,"提前到凌晨入住，请修改时间重算资源");
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return rep;
					}
				}
			}
			// 预留单不能入住R
			if ((Constants.Type.CHECK_IN_RECORD_RESERVE).equals(cir.getType())) {
				rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
				rep.setMessage("预留单不能入住");
				return rep;
			}
			cir.setActualTimeOfArrive(LocalDateTime.now());
			rep = checkIn(cir);
			//判断是否需要直接入账(因为在事务中，所以入住状态还没有commit，是R)
			rep = checkInAuditRoomRecord("R",cir, businessDate, user);
		}
		return rep;
	}

	@Override
	public DtoResponse<BookingRecord> groupBook(BookingRecord br) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DtoResponse<String> checkInAuditRoomRecord(String status, CheckInRecord cir, LocalDate businessDate, User user) {
		DtoResponse<String> rep = new DtoResponse<>();
		LocalDateTime now = LocalDateTime.now();

		LocalDateTime arriveTime = cir.getArriveTime();
		LocalDate ar = arriveTime.toLocalDate();

		LocalTime aTime = arriveTime.toLocalTime();
		LocalTime t = systemConfigService.getCriticalTime(cir.getHotelCode());//过夜审的临界时间
		if(aTime.isBefore(t)){//如果到达时间在临界时间之前（06:00:00）,房费要算在前一天
			//房费要算前一天，查询前一天夜审是否已过
			DailyVerify dailyVerify = dailyVerifyService.findByHotelCodeAndBusinessDate(user.getHotelCode(), ar.plusDays(-1));
			if(dailyVerify != null){//到达时间前一天的夜审已经过了，已经过夜审，要自动入账到前一天。到达时间-1，离开时间-1
				List<Map<String, Object>> list = roomRecordService.checkInAuditRoomRecord(status, ar.plusDays(-1), cir.getId(),  cir.getHotelCode(), "NO");
				Employee emp = employeeService.findByUser(user);
				billService.putAcountMap(list, ar, emp, "3", cir.getHotelCode());
			}
		}
		return rep;
	}

}
