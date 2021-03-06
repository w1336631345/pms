package com.kry.pms.service.busi.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.CheckInRecordDao;
import com.kry.pms.dao.busi.RoomChangeRecordDao;
import com.kry.pms.dao.busi.RoomRecordDao;
import com.kry.pms.dao.room.GuestRoomDao;
import com.kry.pms.model.annotation.PropertyMsg;
import com.kry.pms.model.other.wrapper.CheckInRecordWrapper;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.RoomChangeRecord;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.model.persistence.log.UpdateLog;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.service.busi.CheckInRecordService;
import com.kry.pms.service.busi.RoomChangeRecordService;
import com.kry.pms.service.busi.RoomRecordService;
import com.kry.pms.service.log.UpdateLogService;
import com.kry.pms.service.room.RoomStatisticsService;
import com.kry.pms.service.sys.BusinessSeqService;
import com.kry.pms.service.sys.DateTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Service
public class RoomChangeRecordServiceImpl implements RoomChangeRecordService {

	@Autowired
	RoomChangeRecordDao roomChangeRecordDao;
	@Autowired
	CheckInRecordService checkInRecordService;
	@Autowired
	RoomStatisticsService roomStatisticsService;
	@Autowired
	RoomRecordService roomRecordService;
	@Autowired
	CheckInRecordDao checkInRecordDao;
	@Autowired
	RoomRecordDao roomRecordDao;
	@Autowired
	GuestRoomDao guestRoomDao;
	@Autowired
	UpdateLogService updateLogService;
	@Autowired
	BusinessSeqService businessSeqService;

	@Override
	public RoomChangeRecord add(RoomChangeRecord entity) {
		return roomChangeRecordDao.save(entity);
	}

	@Override
	@Transactional
	public HttpResponse save(RoomChangeRecord entity) {
		LocalDate businessDate = businessSeqService.getBuinessDate(entity.getHotelCode());
		LocalDateTime businessDateTime = LocalDateTime.of(businessDate, LocalTime.now());
		LocalDateTime now = LocalDateTime.now();
		HttpResponse hr = new HttpResponse();
		Double roomPrice = 0.0;
		//?????????
		if(Constants.Type.CHANGE_ROOM_PAY_B.equals(entity.getHandleType())){
			roomPrice = entity.getNewPrice();
		}else {//????????????
			roomPrice = entity.getOldPrice();
		}
		CheckInRecord cir = checkInRecordService.findById(entity.getCheckInRecordId());
		GuestRoom newgr = guestRoomDao.getOne(entity.getNewGuestRoom().getId());
		if(!Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN.equals(cir.getStatus()) && !Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION.equals(cir.getStatus())){
			return hr.error("??????/???????????????????????????");
		}
		if(businessDateTime.isAfter(cir.getLeaveTime())){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return hr.error("?????????????????????????????????????????????");
		}
		//????????????
		boolean cresult = roomStatisticsService.changeRoom(new CheckInRecordWrapper(cir), newgr, businessDateTime);
		if(!cresult){
			return hr.error("???????????????????????????");
		}
		//???????????????,????????????????????????????????????
//			List<CheckInRecord> list = checkInRecordService.findByTogetherCode(hotelCode, cir.getTogetherCode());
		List<CheckInRecord> list = checkInRecordDao.findByOrderNumAndGuestRoomAndDeleted(cir.getOrderNum(), cir.getGuestRoom(), Constants.DELETED_FALSE);
		for(int i=0; i<list.size(); i++){
			CheckInRecord cirs = list.get(i);
			if(!Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN.equals(cirs.getStatus()) && !Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION.equals(cirs.getStatus())){
				System.out.println("??????????????????????????????");
				System.out.println(cirs.getStatus());
				continue;
			}

			LocalDateTime arriveTime = cirs.getArriveTime();
			LocalDateTime leaveTime = cirs.getLeaveTime();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			//***********??????roomRecord??????***********
			cirs.setGuestRoom(entity.getNewGuestRoom());//?????????????????????????????????????????????
			cirs.setRoomType(newgr.getRoomType());//???????????????
			cirs.setOriginalPrice(newgr.getRoomType().getPrice());
			cirs.setPurchasePrice(entity.getNewPrice());
			//?????? ??????????????????*??????=????????????
			if(cir.getPersonalPercentage() != null){
				cirs.setPersonalPrice(roomPrice*cirs.getPersonalPercentage());
			}else {
				cirs.setPersonalPrice(roomPrice);//??????????????????
			}
			cirs = checkInRecordService.update(cirs);
			//***********??????roomRecord??????***********
			if(businessDateTime.isBefore(leaveTime)){//?????????????????????????????????
				List<RoomRecord> roomRecords = roomRecordDao.recordDateAndCheckInRecord(businessDate, cirs.getId());
				for(int r=0; r<roomRecords.size(); r++){
					RoomRecord rr = roomRecords.get(r);
					rr.setGuestRoom(entity.getNewGuestRoom());
					rr.setCost(cirs.getPersonalPrice());
					rr.setCostRatio(cirs.getPersonalPercentage());
					roomRecordService.modify(rr);
				}
			}

			UpdateLog updateLog = new UpdateLog();
			updateLog.setProduct("??????");
			updateLog.setProductName("?????????");
			updateLog.setProductValue(cirs.getOrderNum());
			if(cirs.getAccount() != null){
				updateLog.setIdentifier(cirs.getAccount().getCode());
			}
			updateLog.setOldValue(entity.getOldRoomNum());
			updateLog.setNewValue(entity.getNewRoomNum());
			updateLog.setHotelCode(entity.getHotelCode());
			updateLog.setProductType("GO");
			updateLogService.add(updateLog);//??????????????????
		}
		hr.setData(roomChangeRecordDao.save(entity));
		return hr;
	}

	//???????????????
	@Override
	@Transactional
	public HttpResponse saveOld(String hotelCode, RoomChangeRecord entity) {
		HttpResponse hr = new HttpResponse();
		entity.setHotelCode(hotelCode);
		Double roomPrice = 0.0;
		//?????????
		if(Constants.Type.CHANGE_ROOM_PAY_B.equals(entity.getHandleType())){
			roomPrice = entity.getNewPrice();
		}else {//????????????
			roomPrice = entity.getOldPrice();
		}
		CheckInRecord cir = checkInRecordService.findById(entity.getCheckInRecordId());
		if(!Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN.equals(cir.getStatus()) && !Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION.equals(cir.getStatus())){
			return hr.error("??????/???????????????????????????");
		}
		//???????????????,????????????????????????????????????
//			List<CheckInRecord> list = checkInRecordService.findByTogetherCode(hotelCode, cir.getTogetherCode());
		List<CheckInRecord> list = checkInRecordDao.findByOrderNumAndGuestRoomAndDeleted(cir.getOrderNum(), cir.getGuestRoom(), Constants.DELETED_FALSE);
		for(int i=0; i<list.size(); i++){
			CheckInRecord cirs = list.get(i);
			if(!Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN.equals(cirs.getStatus()) && !Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION.equals(cirs.getStatus())){
				System.out.println("??????????????????????????????");
				System.out.println(cirs.getStatus());
				continue;
			}

			LocalDateTime arriveTime = cirs.getArriveTime();
			LocalDateTime leaveTime = cirs.getLeaveTime();
			LocalDateTime now = LocalDateTime.now();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(now.isBefore(leaveTime)){//?????????????????????????????????
				if(Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN.equals(cirs.getStatus())){//????????????
					roomStatisticsService.checkOut(new CheckInRecordWrapper(cirs));
				}else {
					roomStatisticsService.cancleAssign(new CheckInRecordWrapper(cirs));
					roomStatisticsService.cancleReserve(new CheckInRecordWrapper(cirs));
					List<RoomRecord> roomRecords = roomRecordDao.recordDateAndCheckInRecord(LocalDate.now(), cirs.getId());
					for(int r=0; r<roomRecords.size(); r++){
						RoomRecord rr = roomRecords.get(r);
						rr.setGuestRoom(entity.getNewGuestRoom());
						if(cirs.getPersonalPercentage() != null){
							rr.setCost(entity.getNewPrice()*cirs.getPersonalPercentage());
						}else {
							rr.setCost(entity.getNewPrice());//??????????????????
						}
//						rr.setCost(entity.getNewPrice());
						rr.setCostRatio(null);
						roomRecordService.modify(rr);
					}
				}
			}
			if(now.isAfter(leaveTime)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return hr.error("?????????????????????????????????????????????");
			}

			//??????roomRecord??????
			GuestRoom newgr = guestRoomDao.getOne(entity.getNewGuestRoom().getId());
			cirs.setGuestRoom(entity.getNewGuestRoom());//?????????????????????????????????????????????
			cirs.setRoomType(newgr.getRoomType());//???????????????
			cirs.setPurchasePrice(entity.getNewPrice());
			//?????? ??????????????????*??????=????????????
			if(cir.getPersonalPercentage() != null){
				cirs.setPersonalPrice(roomPrice*cirs.getPersonalPercentage());
			}else {
				cirs.setPersonalPrice(roomPrice);//??????????????????
			}

			cirs = checkInRecordService.update(cirs);
			//??????????????????????????????
			if(now.isBefore(leaveTime) && now.isAfter(arriveTime)){//????????????????????????????????????????????????
				cirs.setArriveTime(now);
				cirs = checkInRecordService.update(cirs);
				roomStatisticsService.reserve(new CheckInRecordWrapper(cirs));//???????????????????????????????????????-????????????
				if(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION.equals(cirs.getStatus())){
					roomStatisticsService.assignRoom(new CheckInRecordWrapper(cirs));
				}
				if(Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN.equals(cirs.getStatus())){
					roomStatisticsService.checkIn(new CheckInRecordWrapper(cirs));
				}
				cirs.setArriveTime(arriveTime);
				cirs = checkInRecordService.update(cirs);
			}
			if(now.isBefore(arriveTime)){//?????????????????????????????????
				//???????????????????????????????????????-????????????
				roomStatisticsService.reserve(new CheckInRecordWrapper(cirs));
				if(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION.equals(cirs.getStatus())){
					roomStatisticsService.assignRoom(new CheckInRecordWrapper(cirs));
				}
				if(Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN.equals(cirs.getStatus())){
					roomStatisticsService.checkIn(new CheckInRecordWrapper(cirs));
				}
			}
		}
		hr.setData(roomChangeRecordDao.save(entity));
		return hr;
	}

	@Override
	public void delete(String id) {

	}

	@Override
	public RoomChangeRecord modify(RoomChangeRecord roomChangeRecord) {
		return roomChangeRecordDao.saveAndFlush(roomChangeRecord);
	}

	@Override
	public RoomChangeRecord findById(String id) {
		return roomChangeRecordDao.getOne(id);
	}

	@Override
	public List<RoomChangeRecord> getAllByHotelCode(String code) {
		return null;
	}

	@Override
	public PageResponse<RoomChangeRecord> listPage(PageRequest<RoomChangeRecord> prq) {
		return null;
	}
}
