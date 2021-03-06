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
import com.kry.pms.dao.busi.CheckInRecordDao;
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
	@Autowired
	CheckInRecordDao checkInRecordDao;

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
	 * ??????????????????
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
				// ??????????????? ??? ?????????????????????????????????????????????
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
//				if (roomAssignBo.getRoomId().length <= (cir.getRoomCount() - cir.getCheckInCount())) {//?????????????????????????????????????????????
				if (roomAssignBo.getRoomId().length <= cir.getRoomCount()) {
					for (String roomId : roomAssignBo.getRoomId()) {
						GuestRoom gr = guestRoomService.findById(roomId);
						HttpResponse hr = checkInRecordService.checkInByTempName(cir.getSingleRoomCount(), cir, gr, response);
						if(hr.getStatus() == 9999){
							TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
							response.setStatus(Constants.BusinessCode.CODE_RESOURCE_NOT_ENOUGH);
							response.setMessage("????????????");
							return response;
						}
					}
				} else {
					response.setStatus(Constants.BusinessCode.CODE_RESOURCE_NOT_ENOUGH);
					response.setMessage("???????????????????????????????????????");
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
			response.setMessage("???????????????????????????????????????");
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
			rep.error(Constants.BusinessCode.CODE_PARAMETER_INVALID,"???????????????>1????????????????????????????????????");
			return rep;
		}
		LocalTime criticalTime = systemConfigService.getCriticalTime(user.getHotelCode());
		if(now.toLocalDate().isEqual(cir.getArriveTime().toLocalDate())){
			if(cir.getArriveTime().toLocalTime().isAfter(criticalTime)){
				if(now.toLocalTime().isBefore(criticalTime)){
					rep.error(Constants.BusinessCode.CODE_PARAMETER_INVALID,"???????????????????????????????????????????????????");
					return rep;
				}
			}
		}
		if(now.isAfter(cir.getLeaveTime())){
			rep.error(Constants.BusinessCode.CODE_PARAMETER_INVALID,"?????????????????????????????????");
			return rep;
		}
		if(now.toLocalDate().isAfter(cir.getArriveTime().toLocalDate())){
			if(now.toLocalTime().isAfter(criticalTime)){
				rep.error(Constants.BusinessCode.CODE_PARAMETER_INVALID,"??????????????????????????????????????????????????????????????????");
				return rep;
			}
		}
		if (cir != null) {
			// ?????????????????????
			if ((Constants.Type.CHECK_IN_RECORD_RESERVE).equals(cir.getType())) {
				rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
				rep.setMessage("?????????????????????");
				return rep;
			}
			rep = checkIn(cir);
			// ???????????????????????????????????????????????????????????????
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
			rep.setMessage("?????????????????????????????????");
		}
		LocalDate businessDate = businessSeqService.getBuinessDate(cir.getHotelCode());
		//(????????????????????????????????????????????????commit??????R)
		checkInAuditRoomRecord("R",cir, businessDate, user);
		return rep;
	}

	@Override
	public DtoResponse<String> checkIn(CheckInRecord cir) {
		DtoResponse<String> rep = new DtoResponse<>();
		LocalDateTime now = LocalDateTime.now();
//		LocalDateTime now = dateTimeService.getBusinessDateTime(cir.getHotelCode());
		//????????????S????????????????????????????????????????????????????????????????????????Null??????S????????????????????????????????????
		if(Constants.Status.CHECKIN_RECORD_STATUS_OUT_UNSETTLED.equals(cir.getStatus())){
			cir.setActualTimeOfLeave(null);
		}else {
			cir.setActualTimeOfArrive(now);
		}
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
			boolean resule = roomStatisticsService.checkIn(new CheckInRecordWrapper(cir));
			if(!resule){
				rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
				rep.setMessage("???????????????????????????");
			}
		}
		return rep;
	}
	//?????????????????????????????????
	@Override
	public HttpResponse getRoomStatus(String guestRoomId){
		HttpResponse hr = new HttpResponse();
		GuestRoom gr = new GuestRoom();
		gr.setId(guestRoomId);
		GuestRoomStatus guestRoomStatus = guestRoomStatusService.findGuestRoomStatusByGuestRoom(gr);
		hr.setData(guestRoomStatus.getRoomStatus());
		return hr;
	}
	//????????????
	@Override
	public HttpResponse overCheckId(String cirId){
		HttpResponse hr = new HttpResponse();
		CheckInRecord cir = checkInRecordDao.getOne(cirId);
		if(!cir.getActualTimeOfLeave().toLocalDate().isEqual(LocalDate.now())){
			return hr.error("???????????????????????????????????????");
		}
		cir.setActualTimeOfLeave(null);
		if(cir.getMainRecord() != null){//?????????
			CheckInRecord main = cir.getMainRecord();
			if("O".equals(main.getStatus())){
				main.setStatus("I");
				main.setActualTimeOfLeave(null);
				checkInRecordDao.save(main);
			}
			cir.setStatus("I");
			cir.setActualTimeOfLeave(null);
//				cir.setArriveTime(LocalDateTime.now());
			checkInRecordDao.save(cir);
			roomStatisticsService.updateGuestRoomStatus(new CheckInRecordWrapper(cir));
		}else {
			cir.setStatus("I");
			cir.setActualTimeOfLeave(null);
//				cir.setArriveTime(LocalDateTime.now());
			checkInRecordDao.save(cir);
			roomStatisticsService.updateGuestRoomStatus(new CheckInRecordWrapper(cir));
		}
		//??????????????? RoomTypeQuantity ?????????
		return hr;
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
				asv.setName("??????????????????");
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
				asv.setName("????????????");
				asv.setType("link");
				asv.setOrderNum(cir.getOrderNum());
				asv.setId(cir.getRoomLinkId());
				asv.setSettleType(Constants.Type.SETTLE_TYPE_LINK);
				asv.setChildren(data);
			}else if("R".equals(cir.getType()) && "P".equals(cir.getFitType())){//?????????
				Collection<AccountSummaryVo> data = checkInRecordService
						.getAccountSummaryByOrderNum3(cir.getHotelCode(),cir.getOrderNum(), Constants.Type.CHECK_IN_RECORD_RESERVE, "P");
				if (data != null && !data.isEmpty()) {
					asv = (AccountSummaryVo) data.toArray()[0];
				}
			}else{
				Collection<AccountSummaryVo> data = checkInRecordService
						.getAccountSummaryByOrderNum2(cir.getHotelCode(),cir.getOrderNum(), Constants.Type.CHECK_IN_RECORD_CUSTOMER);
				if (data != null && !data.isEmpty()) {
					asv = (AccountSummaryVo) data.toArray()[0];
				}
			}
		} else {
			// ??????
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
				rep.setMessage("??????????????????????????????????????????");
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
//				rep.setMessage("????????????????????????????????????????????????");
//				return rep;
//			}
//		}
		// ???????????????????????????????????????????????????????????????(?????????????????????????????????)
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
				rep.error(Constants.BusinessCode.CODE_PARAMETER_INVALID,"????????????????????????????????????????????????????????????");
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return rep;
			}
			if(now.isAfter(cir.getLeaveTime())){
				rep.error(Constants.BusinessCode.CODE_PARAMETER_INVALID,"?????????????????????????????????");
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return rep;
			}
			if(now.toLocalDate().isEqual(cir.getArriveTime().toLocalDate())){
				LocalTime criticalTime = systemConfigService.getCriticalTime(user.getHotelCode());
				if(cir.getArriveTime().toLocalTime().isAfter(criticalTime)){
					if(now.toLocalTime().isBefore(criticalTime)){
						rep.error(Constants.BusinessCode.CODE_PARAMETER_INVALID,"???????????????????????????????????????????????????");
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return rep;
					}
				}
			}
			// ?????????????????????R
			if ((Constants.Type.CHECK_IN_RECORD_RESERVE).equals(cir.getType())) {
				rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
				rep.setMessage("?????????????????????");
				return rep;
			}
			cir.setActualTimeOfArrive(LocalDateTime.now());
			rep = checkIn(cir);
			//??????????????????????????????(????????????????????????????????????????????????commit??????R)
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
		LocalDateTime actualTimeOfArrive = cir.getActualTimeOfArrive();
		if(actualTimeOfArrive == null){
			actualTimeOfArrive = now;
		}
		LocalDate ar2 = actualTimeOfArrive.toLocalDate();//??????????????????
		LocalTime aTime2 = actualTimeOfArrive.toLocalTime();//??????????????????

		LocalDateTime arriveTime = cir.getArriveTime();
		LocalDate ar = arriveTime.toLocalDate();//??????????????????
		LocalTime aTime = arriveTime.toLocalTime();//??????????????????

		LocalTime t = systemConfigService.getCriticalTime(cir.getHotelCode());//????????????????????????

		LocalDate start = cir.getStartDate();

		//roomReocrd??????????????? startDate ??? leaveTime
		//???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		//1??????????????????????????????????????? actualTimeOfArrive.plusDays(-1) ??? LeaveTime
		//2?????????????????????????????? actualTimeOfArrive ??? LeaveTime
		if(start.isBefore(ar2)){//?????????????????????>?????????????????????????????????????????????????????????roomRecord
			if(aTime2.isBefore(t)){//????????????????????????????????????????????????06:00:00???,????????????????????????
				//????????????????????????????????????????????? 2 ?????????????????????roomRecord
				if(start.isBefore(ar2.plusDays(-1))){
					roomRecordService.deletedRecordBefor(cir.getId(), ar2.plusDays(-2));
				}
			}else{
				roomRecordService.deletedRecordBefor(cir.getId(), ar2.plusDays(-1));
			}
		}

		if(aTime2.isBefore(t)){//????????????????????????????????????????????????06:00:00???,????????????????????????

			//?????????????????????????????????????????????????????????
			DailyVerify dailyVerify = dailyVerifyService.findByHotelCodeAndBusinessDate(user.getHotelCode(), ar2.plusDays(-1));
			if(dailyVerify != null){//?????????????????????????????????????????????????????????????????????????????????????????????????????????-1???????????????-1
				List<Map<String, Object>> list = roomRecordService.checkInAuditRoomRecord(status, ar2.plusDays(-1), cir.getId(),  cir.getHotelCode(), "NO");
				Employee emp = employeeService.findByUser(user);
				billService.putAcountMap(list, ar2, emp, "3", cir.getHotelCode());
			}
		}
		return rep;
	}

}
