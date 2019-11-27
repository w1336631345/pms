package com.kry.pms.service.sys.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.sys.ShiftDao;
import com.kry.pms.model.persistence.sys.Shift;
import com.kry.pms.service.sys.ShiftService;

@Service
public class  ShiftServiceImpl implements  ShiftService{
	@Autowired
	 ShiftDao shiftDao;
	 
	 @Override
	public Shift add(Shift shift) {
		return shiftDao.saveAndFlush(shift);
	}

	@Override
	public void delete(String id) {
		Shift shift = shiftDao.findById(id).get();
		if (shift != null) {
			shift.setDeleted(Constants.DELETED_TRUE);
		}
		modify(shift);
	}

	@Override
	public Shift modify(Shift shift) {
		return shiftDao.saveAndFlush(shift);
	}

	@Override
	public Shift findById(String id) {
		return shiftDao.getOne(id);
	}

	@Override
	public List<Shift> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return shiftDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<Shift> listPage(PageRequest<Shift> prq) {
		Example<Shift> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(shiftDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
