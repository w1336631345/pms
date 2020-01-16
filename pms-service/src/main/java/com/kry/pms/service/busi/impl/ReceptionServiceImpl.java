package com.kry.pms.service.busi.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

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
import com.kry.pms.service.busi.BillService;
import com.kry.pms.service.busi.BookingItemService;
import com.kry.pms.service.busi.BookingRecordService;
import com.kry.pms.service.busi.CheckInRecordService;
import com.kry.pms.service.busi.ReceptionService;
import com.kry.pms.service.busi.RoomRecordService;
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
				if (roomAssignBo.getRoomId().length <= (cir.getRoomCount() - cir.getCheckInCount())) {
					for (String roomId : roomAssignBo.getRoomId()) {
						GuestRoom gr = guestRoomService.findById(roomId);
						checkInRecordService.checkInByTempName(cir.getSingleRoomCount(), cir, gr, response);
					}
				} else {
					response.setStatus(Constants.BusinessCode.CODE_RESOURCE_NOT_ENOUGH);
					response.setMessage("选择房间数过多，请重新选择");
				}

				if (response.getStatus() == 0) {
					cir.setCheckInCount(cir.getCheckInCount() + roomAssignBo.getRoomId().length);
					if (cir.getRoomCount() == cir.getCheckInCount()) {
						cir.setDeleted(Constants.DELETED_TRUE);
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
	public DtoResponse<String> checkInM(String id) {
		DtoResponse<String> rep = new DtoResponse<>();
		CheckInRecord cir = checkInRecordService.findById(id);
		if (cir != null) {
			// 预留单不能入住
			if ((Constants.Type.CHECK_IN_RECORD_RESERVE).equals(cir.getType())) {
				rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
				rep.setMessage("预留单不能入住");
				return rep;
			}
			rep = checkIn(cir);
			// 是主单
			if (!(Constants.Type.CHECK_IN_RECORD_GROUP).equals(cir.getType())) {
				String mainRecordId = cir.getMainRecord().getId();
				CheckInRecord cirMain = checkInRecordService.findById(mainRecordId);
				if (!(Constants.Status.ACCOUNT_IN).equals(cirMain.getStatus())) {
					rep = checkIn(cirMain);
				}
			}
		} else {
			rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
			rep.setMessage("没有找到对应的入住记录");
		}
		return rep;
	}

	@Override
	public DtoResponse<String> checkIn(CheckInRecord cir) {
		DtoResponse<String> rep = new DtoResponse<>();
		cir.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN);
		checkInRecordService.modify(cir);
		List<CheckInRecord> togetherRecord = checkInRecordService.findRoomTogetherRecord(cir,
				Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);
		if (togetherRecord != null && !togetherRecord.isEmpty()) {
			rep.setCode(1);
		}
		roomStatisticsService.checkIn(new CheckInRecordWrapper(cir));
		return rep;
	}

	@Override
	public AccountSummaryVo getAccountSummaryByCheckRecordId(String id) {
		CheckInRecord cir = checkInRecordService.findById(id);
		AccountSummaryVo asv = null;
		if (cir != null && cir.getGroupType() != null) {
			switch (cir.getGroupType()) {
			case Constants.Type.CHECK_IN_RECORD_GROUP_TYPE_YES:
				asv = new AccountSummaryVo();
				asv.setName("团队所有账务");
				asv.setType("temp");
				asv.setSettleType(Constants.Type.SETTLE_TYPE_NONE);
				asv.setChildren(new ArrayList<>());
				Account account = cir.getAccount();
				AccountSummaryVo group = new AccountSummaryVo(account);
				group.setRoomStatus(cir.getStatus());
				group.setSettleType(Constants.Type.SETTLE_TYPE_GROUP);
				asv.getChildren().add(group);
				asv.getChildren().addAll((checkInRecordService.getAccountSummaryByOrderNum(cir.getOrderNum(),
						Constants.Type.CHECK_IN_RECORD_CUSTOMER)));
				break;
			case Constants.Type.CHECK_IN_RECORD_GROUP_TYPE_NO:
				if (cir.getRoomLinkId() == null) {// 散客
					Collection<AccountSummaryVo> data = checkInRecordService
							.getAccountSummaryByOrderNum(cir.getOrderNum(), Constants.Type.CHECK_IN_RECORD_CUSTOMER);
					if (data != null && !data.isEmpty()) {
						asv = (AccountSummaryVo) data.toArray()[0];
					}
				} else {// 联房
					Collection<AccountSummaryVo> data = checkInRecordService
							.getAccountSummaryByLinkNum(cir.getRoomLinkId(), Constants.Type.CHECK_IN_RECORD_CUSTOMER);
					asv = new AccountSummaryVo();
					asv.setName("联房账务");
					asv.setType("link");
					asv.setSettleType(Constants.Type.SETTLE_TYPE_LINK);
					asv.setChildren(data);
				}
				break;
			default:
				break;
			}
		} else {
			// 散客
		}
		return asv;
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
	public DtoResponse<String> checkInAll(String[] ids) {
		DtoResponse<String> rep = new DtoResponse<>();
		for (int i = 0; i < ids.length; i++) {
			CheckInRecord cir = checkInRecordService.findById(ids[i]);
			// 预留单不能入住R
			if ((Constants.Type.CHECK_IN_RECORD_RESERVE).equals(cir.getType())) {
				rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
				rep.setMessage("预留单不能入住");
				return rep;
			}
			rep = checkIn(cir);
			// 是主单G
			if (!(Constants.Type.CHECK_IN_RECORD_GROUP).equals(cir.getType())) {
				String mainRecordId = cir.getMainRecord().getId();
				CheckInRecord cirMain = checkInRecordService.findById(mainRecordId);
				if (!(Constants.Status.ACCOUNT_IN).equals(cirMain.getStatus())) {
					rep = checkIn(cirMain);
				}
			}
		}
		return rep;
	}

	@Override
	public DtoResponse<BookingRecord> groupBook(BookingRecord br) {
		// TODO Auto-generated method stub
		return null;
	}

}
