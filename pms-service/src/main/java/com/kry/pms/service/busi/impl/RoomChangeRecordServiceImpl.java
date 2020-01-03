package com.kry.pms.service.busi.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.RoomChangeRecordDao;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.RoomChangeRecord;
import com.kry.pms.service.busi.CheckInRecordService;
import com.kry.pms.service.busi.RoomChangeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RoomChangeRecordServiceImpl implements RoomChangeRecordService {

	@Autowired
	RoomChangeRecordDao roomChangeRecordDao;
	@Autowired
	CheckInRecordService checkInRecordService;

	@Override
	public RoomChangeRecord add(RoomChangeRecord entity) {
		return roomChangeRecordDao.save(entity);
	}

	@Override
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
				cirs.setGuestRoom(entity.getNewGuestRoom());//修改所有同住人员房间号为新房间
				cirs.setPurchasePrice(entity.getNewPrice());
				//同住 承担房费占比*房价=承担房费
				if(cir.getPersonalPercentage() != null){
					cirs.setPersonalPrice(roomPrice*cirs.getPersonalPercentage());
				}else {
					cirs.setPersonalPrice(roomPrice);//承担全部房费
				}
				checkInRecordService.update(cirs);
			}
		}else {
			cir.setGuestRoom(entity.getNewGuestRoom());
			cir.setPurchasePrice(entity.getNewPrice());
			cir.setPersonalPrice(roomPrice);
			cir.setPersonalPercentage(1.0);
			checkInRecordService.update(cir);
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
