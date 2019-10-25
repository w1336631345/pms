package com.kry.pms.service.busi.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.internal.lang.annotation.ajcDeclareAnnotation;
import org.jboss.jandex.Main;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.DtoResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.CheckInRecordDao;
import com.kry.pms.model.http.request.busi.CheckInBo;
import com.kry.pms.model.http.request.busi.CheckInItemBo;
import com.kry.pms.model.persistence.busi.BookingItem;
import com.kry.pms.model.persistence.busi.BookingRecord;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.RoomUsage;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.model.persistence.sys.BusinessSeq;
import com.kry.pms.service.busi.CheckInRecordService;
import com.kry.pms.service.busi.RoomRecordService;
import com.kry.pms.service.guest.CustomerService;
import com.kry.pms.service.room.GuestRoomService;
import com.kry.pms.service.room.GuestRoomStatusService;
import com.kry.pms.service.room.RoomStatisticsService;
import com.kry.pms.service.room.RoomStatusQuantityService;
import com.kry.pms.service.room.RoomTypeQuantityService;
import com.kry.pms.service.room.RoomTypeService;
import com.kry.pms.service.room.RoomUsageService;
import com.kry.pms.service.sys.BusinessSeqService;
import com.kry.pms.service.sys.SystemConfigService;

@Service
public class CheckInRecordServiceImpl implements CheckInRecordService {
	@Autowired
	CheckInRecordDao checkInRecordDao;
	@Autowired
	RoomRecordService roomRecordService;
	@Autowired
	GuestRoomService guestRoomService;
	@Autowired
	CustomerService customerService;
	@Autowired
	RoomStatisticsService roomStatisticsService;
	@Autowired
	GuestRoomStatusService guestRoomStatausService;
	@Autowired
	SystemConfigService systemConfigService;
	@Autowired
	RoomTypeQuantityService roomTypeQuantityService;
	@Autowired
	RoomStatusQuantityService roomStatusQuantityService;
	@Autowired
	RoomUsageService roomUsageService;
	@Autowired
	BusinessSeqService businessSeqService;
	@Autowired
	RoomTypeService roomTypeService;

	@Override
	public CheckInRecord add(CheckInRecord checkInRecord) {
		return checkInRecordDao.saveAndFlush(checkInRecord);
	}

	@Override
	public void delete(String id) {
		CheckInRecord checkInRecord = checkInRecordDao.findById(id).get();
		if (checkInRecord != null) {
			checkInRecord.setDeleted(Constants.DELETED_TRUE);
		}
		modify(checkInRecord);
	}

	@Override
	public CheckInRecord modify(CheckInRecord checkInRecord) {
		CheckInRecord dbCir = findById(checkInRecord.getId());
		if(dbCir!=null) {
			checkInRecord.setMainRecord(dbCir.getMainRecord());
			if(checkInRecord.getCustomer()!=null) {
				Customer customer = customerService.modify(checkInRecord.getCustomer());
				checkInRecord.setCustomer(customer);
			}
			return checkInRecordDao.saveAndFlush(checkInRecord);
		}
		return null;
	}

	@Override
	public CheckInRecord findById(String id) {
		return checkInRecordDao.getOne(id);
	}

	@Override
	public List<CheckInRecord> getAllByHotelCode(String code) {
		return null;// 默认不实现
		// return checkInRecordDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<CheckInRecord> listPage(PageRequest<CheckInRecord> prq) {
		Example<CheckInRecord> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(checkInRecordDao.findAll(ex, req));
	}

	@Override
	public void checkIn(CheckInBo checkInBo, DtoResponse<List<CheckInRecord>> rep) {
		for (CheckInItemBo ciib : checkInBo.getItems()) {
			GuestRoom gr = guestRoomService.findById(ciib.getRoomId());
			LocalDateTime time = LocalDateTime.now();
			ArrayList<CheckInRecord> list = createCheckInRecord(ciib, gr, time);
			// TODO check is group
			roomStatisticsService.checkIn(gr, time, ciib.getDays(), false);

		}
	}

	private ArrayList<CheckInRecord> createCheckInRecord(CheckInItemBo ciib, GuestRoom gr, LocalDateTime time) {
		ArrayList<CheckInRecord> list = new ArrayList<>();
		CheckInRecord cir = null;
		Customer customer = null;
		LocalDate startDate = time.toLocalDate();
		StringBuilder sb = new StringBuilder();
		LocalTime criticalTime = systemConfigService.getCriticalTime(gr.getHotelCode());
		if (time.toLocalTime().isBefore(criticalTime)) {
			startDate = startDate.plusDays(-1);
		}
		for (int i = 0; i < ciib.getGuests().size(); i++) {
			cir = new CheckInRecord();
			cir.setArriveTime(time);
			ciib.setHotelCode(gr.getHotelCode());
			BeanUtils.copyProperties(ciib, cir);
			customer = customerService.createOrGetCustomer(ciib.getGuests().get(i));
			cir.setCustomer(customer);
			cir.setGuestRoom(gr);
			cir.setStartDate(startDate);
			list.add(add(cir));
			roomRecordService.createRoomRecord(cir);
			sb.append(customer.getGuestInfo().getName());
			sb.append(" ");
		}
		guestRoomStatausService.checkIn(gr, startDate, sb.toString(), false, false, false, false, false);
		roomTypeQuantityService.checkIn(gr, startDate, ciib.getDays());
		return list;
	}

	@Override
	public List<CheckInRecord> checkOut(String roomId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CheckInRecord checkInByTempName(String tempName, String roomId, DtoResponse<String> response) {
		Customer customer = customerService.createTempCustomer(tempName);
		GuestRoom gr = guestRoomService.findById(roomId);
		if (gr != null) {

		} else {

		}

		return null;
	}

	@Override
	public List<CheckInRecord> checkInByTempName(int humanCount, BookingRecord br, BookingItem item, GuestRoom gr,
			DtoResponse<String> response) {
		List<CheckInRecord> data = new ArrayList<CheckInRecord>();
		LocalTime criticalTime = systemConfigService.getCriticalTime(gr.getHotelCode());
		LocalDate startDate = br.getArriveTime().toLocalDate();
		if (br.getArriveTime().toLocalTime().isBefore(criticalTime)) {
			startDate = startDate.plusDays(-1);
		}
		BusinessSeq bs = businessSeqService.fetchNextSeq(gr.getHotelCode(), Constants.Key.BUSINESS_SEQ_KEY);
		String tempName = br.getName();
		String checkInSn =bs.getCurrentDateStr()+bs.getCurrentSeq();
		roomUsageService.use(gr, Constants.Status.ROOM_USAGE_BOOK, br.getArriveTime(), br.getLeaveTime(), checkInSn,
				tempName, response);
		if (response.getStatus() == 0) {
			for (int i = 1; i <= humanCount; i++) {
				tempName = br.getName() + gr.getRoomNum() + "#" + i;
				Customer customer = customerService.createTempCustomer(tempName);
				CheckInRecord cir = new CheckInRecord();
				cir.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);
				cir.setCustomer(customer);
				cir.setCheckInSn(checkInSn);
				cir.setType(Constants.Type.CHECK_IN_RECORD_GROUP_CUSTOMER);
				cir.setGroup(br.getGroup());
				cir.setArriveTime(br.getArriveTime());
				cir.setLeaveTime(br.getLeaveTime());
				cir.setStartDate(startDate);
				cir.setDays(br.getDays());
				cir.setGuestRoom(gr);
				cir.setHotelCode(gr.getHotelCode());
				Account account = new Account();
				account.setCustomer(customer);
				account.setType(Constants.Type.ACCOUNT_CUSTOMER);
				cir.setAccount(account);
				cir = add(cir);
				data.add(cir);
				roomRecordService.createRoomRecord(cir);
			}
		}
		return data;
	}

	@Override
	public List<CheckInRecord> findByBookId(String bookId) {
		return checkInRecordDao.fingByBookId(bookId);
	}

	@Override
	public List<CheckInRecord> findDetailByBookingId(String bookId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CheckInRecord> checkInByTempName(int humanCount, CheckInRecord cir, GuestRoom gr,
			DtoResponse<String> response) {
		LocalTime criticalTime = systemConfigService.getCriticalTime(gr.getHotelCode());
		LocalDate startDate = cir.getArriveTime().toLocalDate();
		if (cir.getArriveTime().toLocalTime().isBefore(criticalTime)) {
			startDate = startDate.plusDays(-1);
		}
		List<CheckInRecord> data = new ArrayList<CheckInRecord>();
		String tempName = cir.getName()+gr.getRoomNum();
		if (response.getStatus() == 0) {
			for (int i = 1; i <= humanCount; i++) {
				Customer customer = customerService.createTempCustomer(tempName+"#"+i);
				CheckInRecord ncir = null;
				try {
					ncir = (CheckInRecord) cir.clone();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				ncir.setId(null);
				ncir.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);
				ncir.setCustomer(customer);
				ncir.setName(customer.getName());
				ncir.setType(Constants.Type.CHECK_IN_RECORD_GROUP_CUSTOMER);
				ncir.setGuestRoom(gr);
				ncir.setSubRecords(null);
				ncir.setStartDate(startDate);
				ncir.setHotelCode(gr.getHotelCode());
				Account account = new Account();
				account.setCustomer(customer);
				ncir.setCheckInCount(1);
				ncir.setRoomCount(1);
				account.setType(Constants.Type.ACCOUNT_CUSTOMER);
				ncir.setAccount(account);
				ncir = add(ncir);
				data.add(ncir);
				roomRecordService.createRoomRecord(ncir);
			}
		}
		return data;
	}

	@Override
	public CheckInRecord book(CheckInRecord checkInRecord) {
		BusinessSeq bs = businessSeqService.fetchNextSeq(checkInRecord.getHotelCode(), Constants.Key.BUSINESS_ORDER_NUM_SEQ_KEY);
		String orderNum =bs.getCurrentDateStr()+String.format("%04d", bs.getCurrentSeq()); 
		checkInRecord.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);
		checkInRecord.setType(Constants.Type.CHECK_IN_RECORD_GROUP);
		checkInRecord.setOrderNum(orderNum);
		for(CheckInRecord item:checkInRecord.getSubRecords()) {
			item.setOrderNum(orderNum);
			item.setHoldTime(checkInRecord.getHoldTime());
			item.setArriveTime(checkInRecord.getArriveTime());
			item.setLeaveTime(checkInRecord.getLeaveTime());
			item.setDays(checkInRecord.getDays());
			item.setRoomType(roomTypeService.findById(item.getRoomTypeId()));
			item.setContactName(checkInRecord.getContactName());
			item.setContactMobile(checkInRecord.getContactMobile());
			item.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);
			item.setType(Constants.Type.CHECK_IN_RECORD_GROUP_CUSTOMER);
			item.setProtocolCorpation(checkInRecord.getProtocolCorpation());
		}
		return add(checkInRecord);
	}

}
