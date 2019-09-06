package com.kry.pms.service.busi.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.ChangeRoomRecordDao;
import com.kry.pms.model.persistence.busi.ChangeRoomRecord;
import com.kry.pms.service.busi.ChangeRoomRecordService;

@Service
public class  ChangeRoomRecordServiceImpl implements  ChangeRoomRecordService{
	@Autowired
	 ChangeRoomRecordDao changeRoomRecordDao;
	 
	 @Override
	public ChangeRoomRecord add(ChangeRoomRecord changeRoomRecord) {
		return changeRoomRecordDao.saveAndFlush(changeRoomRecord);
	}

	@Override
	public void delete(String id) {
		ChangeRoomRecord changeRoomRecord = changeRoomRecordDao.findById(id).get();
		if (changeRoomRecord != null) {
			changeRoomRecord.setDeleted(Constants.DELETED_TRUE);
		}
		changeRoomRecordDao.saveAndFlush(changeRoomRecord);
	}

	@Override
	public ChangeRoomRecord modify(ChangeRoomRecord changeRoomRecord) {
		return changeRoomRecordDao.saveAndFlush(changeRoomRecord);
	}

	@Override
	public ChangeRoomRecord findById(String id) {
		return changeRoomRecordDao.getOne(id);
	}

	@Override
	public List<ChangeRoomRecord> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return changeRoomRecordDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<ChangeRoomRecord> listPage(PageRequest<ChangeRoomRecord> prq) {
		Example<ChangeRoomRecord> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(changeRoomRecordDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
