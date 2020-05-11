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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

	@Override
	public RoomChangeRecord add(RoomChangeRecord entity) {
		return roomChangeRecordDao.save(entity);
	}

	@Override
	@Transactional
	public HttpResponse save(RoomChangeRecord entity) {
		LocalDateTime now = LocalDateTime.now();
		HttpResponse hr = new HttpResponse();
		Double roomPrice = 0.0;
		//补差价
		if(Constants.Type.CHANGE_ROOM_PAY_B.equals(entity.getHandleType())){
			roomPrice = entity.getNewPrice();
		}else {//免费升级
			roomPrice = entity.getOldPrice();
		}
		CheckInRecord cir = checkInRecordService.findById(entity.getCheckInRecordId());
		GuestRoom newgr = guestRoomDao.getOne(entity.getNewGuestRoom().getId());
		//资源调整
		boolean cresult = roomStatisticsService.changeRoom(new CheckInRecordWrapper(cir), newgr, LocalDateTime.now());
		if(!Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN.equals(cir.getStatus()) && !Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION.equals(cir.getStatus())){
			return hr.error("退房/结账等房间不能换房");
		}
		if(now.isAfter(cir.getLeaveTime())){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return hr.error("已经过了离店时间，禁止换房操作");
		}
		//有同住记录,把所有同房间的都一起换房
//			List<CheckInRecord> list = checkInRecordService.findByTogetherCode(hotelCode, cir.getTogetherCode());
		List<CheckInRecord> list = checkInRecordDao.findByOrderNumAndGuestRoomAndDeleted(cir.getOrderNum(), cir.getGuestRoom(), Constants.DELETED_FALSE);
		for(int i=0; i<list.size(); i++){
			CheckInRecord cirs = list.get(i);
			if(!Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN.equals(cirs.getStatus()) && !Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION.equals(cirs.getStatus())){
				System.out.println("不是入住或者预订状态");
				System.out.println(cirs.getStatus());
				continue;
			}

			LocalDateTime arriveTime = cirs.getArriveTime();
			LocalDateTime leaveTime = cirs.getLeaveTime();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(now.isBefore(leaveTime)){//当前时间在离店时间之前
				List<RoomRecord> roomRecords = roomRecordDao.recordDateAndCheckInRecord(LocalDate.now(), cirs.getId());
				for(int r=0; r<roomRecords.size(); r++){
					RoomRecord rr = roomRecords.get(r);
					rr.setGuestRoom(entity.getNewGuestRoom());
					if(cirs.getPersonalPercentage() != null){
						rr.setCost(entity.getNewPrice()*cirs.getPersonalPercentage());
					}else {
						rr.setCost(entity.getNewPrice());//承担全部房费
					}
//						rr.setCost(entity.getNewPrice());
					rr.setCostRatio(null);
					roomRecordService.modify(rr);
				}
			}
			//修改roomRecord完毕
			cirs.setGuestRoom(entity.getNewGuestRoom());//修改所有同住人员房间号为新房间
			cirs.setRoomType(newgr.getRoomType());//修改新房型
			cirs.setPurchasePrice(entity.getNewPrice());
			//同住 承担房费占比*房价=承担房费
			if(cir.getPersonalPercentage() != null){
				cirs.setPersonalPrice(roomPrice*cirs.getPersonalPercentage());
			}else {
				cirs.setPersonalPrice(roomPrice);//承担全部房费
			}
			cirs = checkInRecordService.update(cirs);
			UpdateLog updateLog = new UpdateLog();
			updateLog.setProduct("换房");
			updateLog.setProductName("订单号");
			updateLog.setProductValue(cirs.getOrderNum());
			updateLog.setOldValue(entity.getOldRoomNum());
			updateLog.setNewValue(entity.getNewRoomNum());
			updateLog.setHotelCode(entity.getHotelCode());
			updateLogService.add(updateLog);//记录换房日志
		}
		hr.setData(roomChangeRecordDao.save(entity));
		return hr;
	}

	//换房老接口
	@Override
	@Transactional
	public HttpResponse saveOld(String hotelCode, RoomChangeRecord entity) {
		HttpResponse hr = new HttpResponse();
		entity.setHotelCode(hotelCode);
		Double roomPrice = 0.0;
		//补差价
		if(Constants.Type.CHANGE_ROOM_PAY_B.equals(entity.getHandleType())){
			roomPrice = entity.getNewPrice();
		}else {//免费升级
			roomPrice = entity.getOldPrice();
		}
		CheckInRecord cir = checkInRecordService.findById(entity.getCheckInRecordId());
		if(!Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN.equals(cir.getStatus()) && !Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION.equals(cir.getStatus())){
			return hr.error("退房/结账等房间不能换房");
		}
		//有同住记录,把所有同房间的都一起换房
//			List<CheckInRecord> list = checkInRecordService.findByTogetherCode(hotelCode, cir.getTogetherCode());
		List<CheckInRecord> list = checkInRecordDao.findByOrderNumAndGuestRoomAndDeleted(cir.getOrderNum(), cir.getGuestRoom(), Constants.DELETED_FALSE);
		for(int i=0; i<list.size(); i++){
			CheckInRecord cirs = list.get(i);
			if(!Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN.equals(cirs.getStatus()) && !Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION.equals(cirs.getStatus())){
				System.out.println("不是入住或者预订状态");
				System.out.println(cirs.getStatus());
				continue;
			}

			LocalDateTime arriveTime = cirs.getArriveTime();
			LocalDateTime leaveTime = cirs.getLeaveTime();
			LocalDateTime now = LocalDateTime.now();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(now.isBefore(leaveTime)){//当前时间在离店时间之前
				if(Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN.equals(cirs.getStatus())){//入住状态
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
							rr.setCost(entity.getNewPrice());//承担全部房费
						}
//						rr.setCost(entity.getNewPrice());
						rr.setCostRatio(null);
						roomRecordService.modify(rr);
					}
				}
			}
			if(now.isAfter(leaveTime)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return hr.error("已经过了离店时间，禁止换房操作");
			}

			//修改roomRecord完毕
			GuestRoom newgr = guestRoomDao.getOne(entity.getNewGuestRoom().getId());
			cirs.setGuestRoom(entity.getNewGuestRoom());//修改所有同住人员房间号为新房间
			cirs.setRoomType(newgr.getRoomType());//修改新房型
			cirs.setPurchasePrice(entity.getNewPrice());
			//同住 承担房费占比*房价=承担房费
			if(cir.getPersonalPercentage() != null){
				cirs.setPersonalPrice(roomPrice*cirs.getPersonalPercentage());
			}else {
				cirs.setPersonalPrice(roomPrice);//承担全部房费
			}

			cirs = checkInRecordService.update(cirs);
			//对新房间进行资源占用
			if(now.isBefore(leaveTime) && now.isAfter(arriveTime)){//当前时间在到店时间和离店时间之间
				cirs.setArriveTime(now);
				cirs = checkInRecordService.update(cirs);
				roomStatisticsService.reserve(new CheckInRecordWrapper(cirs));//占用房类资源应该是当前时间-离店时间
				if(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION.equals(cirs.getStatus())){
					roomStatisticsService.assignRoom(new CheckInRecordWrapper(cirs));
				}
				if(Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN.equals(cirs.getStatus())){
					roomStatisticsService.checkIn(new CheckInRecordWrapper(cirs));
				}
				cirs.setArriveTime(arriveTime);
				cirs = checkInRecordService.update(cirs);
			}
			if(now.isBefore(arriveTime)){//当前时间在到店时间之前
				//占用房类资源应该是到店时间-离店时间
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
