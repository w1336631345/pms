package com.kry.pms.service.busi.impl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.aspectj.internal.lang.annotation.ajcDeclareAnnotation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.kry.pms.base.Constants;
import com.kry.pms.base.DtoResponse;
import com.kry.pms.model.http.request.busi.BillItemBo;
import com.kry.pms.model.http.request.busi.BillSettleBo;
import com.kry.pms.model.http.request.busi.BookingBo;
import com.kry.pms.model.http.request.busi.BookingItemBo;
import com.kry.pms.model.http.request.busi.CheckInBo;
import com.kry.pms.model.http.request.busi.CheckInItemBo;
import com.kry.pms.model.http.request.busi.CheckOutBo;
import com.kry.pms.model.http.request.busi.RoomAssignBo;
import com.kry.pms.model.http.response.busi.CheckOutVo;
import com.kry.pms.model.persistence.busi.BillItem;
import com.kry.pms.model.persistence.busi.BookingItem;
import com.kry.pms.model.persistence.busi.BookingRecord;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.marketing.RoomPriceSchemeItem;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.GuestRoomStatus;
import com.kry.pms.model.persistence.room.RoomType;
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
import com.kry.pms.service.room.RoomTypeService;
import com.kry.pms.service.sys.SystemConfigService;

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

	@Transactional
	@Override
	public DtoResponse<BookingRecord> book(BookingBo book) {
		DtoResponse<BookingRecord> rep = new DtoResponse<>();
		BookingRecord br = new BookingRecord();
		BeanUtils.copyProperties(book, br);
		br.setArriveTime(LocalDateTime.of(book.getArriveDate(), LocalTime.parse("14:00:00")));
		br.setLeaveTime(LocalDateTime.of(book.getLeaveDate(),LocalTime.parse("12:00:00")));
		Employee oe = employeeService.findById(book.getOperationId());
		if (oe == null) {
			rep.setStatus(Constants.ErrorCode.REQUIRED_PARAMETER_INVALID);
		}
		Employee me = employeeService.findById(book.getMarketingId());
		if (me == null) {
			rep.setStatus(Constants.ErrorCode.REQUIRED_PARAMETER_INVALID);
		}
		if (book.getRoomTypeId() != null) {
			RoomType rt = roomTypeService.findById(book.getRoomTypeId());
			if (rt == null) {
				rep.setStatus(Constants.ErrorCode.REQUIRED_PARAMETER_INVALID);
			}
			if (rep.getStatus() == 0) {
				br.setHotelCode(book.getHotelCode());
				br.setRoomType(rt);
				br.setOperationEmployee(oe);
				br.setMarketEmployee(me);
				rep = bookingRecordService.book(br);
			}
		} else if (book.getItems() != null && !book.getItems().isEmpty()) {
			ArrayList<CheckInRecord> items = new ArrayList<CheckInRecord>();
			CheckInRecord cir;
			for (BookingItemBo bib : book.getItems()) {
				cir = new CheckInRecord();
				//BeanUtils.copyProperties(item, bib);
				cir.setPurchasePrice(bib.getPurchasePrice());
				cir.setRoomCount(bib.getRoomCount());
				cir.setType(Constants.Type.BOOK_CHECK_IN_TEMP);
				cir.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);
				cir.setArriveTime(br.getArriveTime());
				cir.setLeaveTime(br.getLeaveTime());
				cir.setDays(br.getDays());
				cir.setHoldTime(book.getHoldTime());
				cir.setRoomType(roomTypeService.findById(bib.getRoomTypeId()));
				cir.setPriceSchemeItem(roomPriceSchemeItemService.findById(bib.getPriceSchemeItemId()));
				items.add(cir);
			}
			br.setCheckInRecord(items);
			rep = bookingRecordService.book(br);
		} else {
			rep.setStatus(Constants.ErrorCode.REQUIRED_PARAMETER_INVALID);
			rep.setMessage("请预留房间");
		}
		if (rep.getStatus() != 0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return rep;
	}

	@Transactional
	@Override
	public DtoResponse<List<CheckInRecord>> checkIn(CheckInBo checkInBo) {
		DtoResponse<List<CheckInRecord>> rep = new DtoResponse<List<CheckInRecord>>();
		switch (checkInBo.getType()) {
		case Constants.Type.BOOK_CHECK_IN:
			if (checkInBo.getBookingId() == null) {
				rep.setStatus(Constants.ErrorCode.REQUIRED_PARAMETER_MISSING);
			} else {
				BookingRecord br = bookingRecordService.findById(checkInBo.getBookingId());
				if (br == null) {// 找不到预定记录
					rep.setStatus(Constants.ErrorCode.REQUIRED_PARAMETER_INVALID);
				} else if (roomStatusCheck(checkInBo, rep)) {// 房间状态确认
					checkInRecordService.checkIn(checkInBo, rep);
				} else {
					rep.setStatus(Constants.ErrorCode.REQUIRED_PARAMETER_INVALID);
				}
			}
			break;
		case Constants.Type.NO_BOOK_CHECK_IN:
			if (roomStatusCheck(checkInBo, rep)) {
				checkInRecordService.checkIn(checkInBo, rep);
			} else {
				rep.setStatus(Constants.ErrorCode.REQUIRED_PARAMETER_INVALID);
			}
			break;
		default:
			rep.setStatus(Constants.ErrorCode.REQUIRED_PARAMETER_INVALID);
			break;
		}
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
	public DtoResponse<CheckOutVo> checkOut(@Valid CheckOutBo checkOut) {
		String roomId = checkOut.getRoomId();
		List<CheckInRecord> crs = checkInRecordService.checkOut(roomId);
		billService.checkAndPayBill(crs, checkOut.getTatal());
		roomRecordService.checkOut(crs);
		guestRoomStatusService.checkOut(roomId);
		return null;
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
	public DtoResponse<String> assignRoom(@Valid RoomAssignBo roomAssignBo) {
		DtoResponse<String> response = new DtoResponse<String>();
		String bookId = roomAssignBo.getBookId();
		BookingRecord br = bookingRecordService.findById(bookId);
		String bookItemId = roomAssignBo.getBookItemId();
		BookingItem item = bookingItemService.findById(bookItemId);
		if (item != null && br != null) {
			if (roomAssignBo.getRoomId().length < (item.getRoomCount() - item.getCheckInCount())) {
				for (String roomId : roomAssignBo.getRoomId()) {
					GuestRoom gr = guestRoomService.findById(roomId);
					checkInRecordService.checkInByTempName(roomAssignBo.getHumanCountPreRoom(), br, item, gr, response);
				}
			} else {
				response.setStatus(Constants.BusinessCode.CODE_RESOURCE_NOT_ENOUGH);
				response.setMessage("选择房间数过多，请重新选择");
			}
		} else {
			response.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
			response.setMessage("找不到预定信息，请重新选择");
		}
		if(response.getStatus()==0) {
			item.setCheckInCount(item.getCheckInCount()+roomAssignBo.getRoomId().length);
			bookingItemService.modify(item);
		}else {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return response;
	}
}
