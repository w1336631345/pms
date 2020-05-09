package com.kry.pms.service.log.impl;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.log.InterfaceUseLogDao;
import com.kry.pms.dao.log.UpdateLogDao;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.log.InterfaceUseLog;
import com.kry.pms.model.persistence.log.UpdateLog;
import com.kry.pms.service.busi.CheckInRecordService;
import com.kry.pms.service.log.InterfaceUseLogService;
import com.kry.pms.service.log.UpdateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UpdateLogServiceImpl implements UpdateLogService {
	@Autowired
	UpdateLogDao updateLogDao;
	@Autowired
	CheckInRecordService checkInRecordService;
	 
	 @Override
	public UpdateLog add(UpdateLog updateLog) {
		return updateLogDao.saveAndFlush(updateLog);
	}

	@Override
	public void delete(String id) {
		updateLogDao.deleteById(id);
	}

	@Override
	public UpdateLog modify(UpdateLog updateLog) {
		return updateLogDao.saveAndFlush(updateLog);
	}

	@Override
	public UpdateLog findById(String id) {
		return updateLogDao.getOne(id);
	}

	@Override
	public List<UpdateLog> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return interfaceUseLogDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<UpdateLog> listPage(PageRequest<UpdateLog> prq) {
		Example<UpdateLog> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		Page<UpdateLog> list = updateLogDao.findAll(ex, req);
		return convent(list);
	}


	@Override
	public HttpResponse updateCirAllLog(CheckInRecord checkInRecord){
		HttpResponse hr = new HttpResponse();
		checkInRecordService.updateAllLog(checkInRecord);
		return hr.ok();
	}
	 
}
