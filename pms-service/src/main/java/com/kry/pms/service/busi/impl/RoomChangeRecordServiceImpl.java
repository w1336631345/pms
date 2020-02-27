package com.kry.pms.service.busi.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.RoomChangeRecordDao;
import com.kry.pms.model.other.wrapper.CheckInRecordWrapper;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.RoomChangeRecord;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.service.busi.CheckInRecordService;
import com.kry.pms.service.busi.RoomChangeRecordService;
import com.kry.pms.service.busi.RoomRecordService;
import com.kry.pms.service.room.RoomStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

	@Override
	public RoomChangeRecord add(RoomChangeRecord entity) {
		return roomChangeRecordDao.save(entity);
	}

	@Override
	@Transactional
	public RoomChangeRecord save(String hotelCode, RoomChangeRecord entity) {
		entity.setHotelCode(hotelCode);
		Double roomPrice = 0.0;
		//补差价
		if(Constants.Type.CHANGE_ROOM_PAY_B.equals(entity.getHandleType())){
			roomPrice = entity.getNewPrice();
		}else {//免费升级
			roomPrice = entity.getOldPrice();
		}
		CheckInRecord cir = checkInRecordService.findById(entity.getCheckInRecordId());
		//有同住记录
		if(cir.getTogetherCode() != null){
			List<CheckInRecord> list = checkInRecordService.findByTogetherCode(hotelCode, cir.getTogetherCode());
			for(int i=0; i<list.size(); i++){
				CheckInRecord cirs = list.get(i);
				roomStatisticsService.cancleReserve(new CheckInRecordWrapper(cirs));
				//需要删除roomRecord的记录
				List<RoomRecord> roomRecords = roomRecordService.findByHotelCodeAndCheckInRecord(cirs.getHotelCode(), cirs.getId());
				for(int r=0; r<roomRecords.size(); r++){
					roomRecordService.deleteTrue(roomRecords.get(r).getId());
				}
				//删除完毕
				cirs.setGuestRoom(entity.getNewGuestRoom());//修改所有同住人员房间号为新房间
				cirs.setPurchasePrice(entity.getNewPrice());
				//同住 承担房费占比*房价=承担房费
				if(cir.getPersonalPercentage() != null){
					cirs.setPersonalPrice(roomPrice*cirs.getPersonalPercentage());
				}else {
					cirs.setPersonalPrice(roomPrice);//承担全部房费
				}
				checkInRecordService.update(cirs);
				roomStatisticsService.reserve(new CheckInRecordWrapper(cirs));
				if(Constants.Status.CHECKIN_RECORD_STATUS_ASSIGN.equals(cirs.getStatus())){
					roomStatisticsService.assignRoom(new CheckInRecordWrapper(cirs));
				}
				if(Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN.equals(cirs.getStatus())){
					roomStatisticsService.checkIn(new CheckInRecordWrapper(cirs));
				}
				roomRecordService.createRoomRecord(cirs);
			}
		}else {
			//需要删除roomRecord的记录
			List<RoomRecord> roomRecords = roomRecordService.findByHotelCodeAndCheckInRecord(cir.getHotelCode(), cir.getId());
			for(int r=0; r<roomRecords.size(); r++){
				roomRecordService.deleteTrue(roomRecords.get(r).getId());
			}
			//取消之前的预定，释放资源
			roomStatisticsService.cancleReserve(new CheckInRecordWrapper(cir));
			cir.setGuestRoom(entity.getNewGuestRoom());
			cir.setPurchasePrice(entity.getNewPrice());
			cir.setPersonalPrice(roomPrice);
			cir.setPersonalPercentage(1.0);
			checkInRecordService.update(cir);
			roomRecordService.createRoomRecord(cir);
			//房间新预订，占用资源
			roomStatisticsService.reserve(new CheckInRecordWrapper(cir));
			if(Constants.Status.CHECKIN_RECORD_STATUS_ASSIGN.equals(cir.getStatus())){
				roomStatisticsService.assignRoom(new CheckInRecordWrapper(cir));
			}
			if(Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN.equals(cir.getStatus())){
				roomStatisticsService.checkIn(new CheckInRecordWrapper(cir));
			}
		}
		return roomChangeRecordDao.save(entity);
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
