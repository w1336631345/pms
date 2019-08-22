package com.kry.pms.service.marketing.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.marketing.RoomPriceSchemeDao;
import com.kry.pms.model.persistence.marketing.RoomPriceScheme;
import com.kry.pms.service.marketing.RoomPriceSchemeService;

@Service
public class  RoomPriceSchemeServiceImpl implements  RoomPriceSchemeService{
	@Autowired
	 RoomPriceSchemeDao roomPriceSchemeDao;
	 
	 @Override
	public RoomPriceScheme add(RoomPriceScheme roomPriceScheme) {
		return roomPriceSchemeDao.saveAndFlush(roomPriceScheme);
	}

	@Override
	public void delete(String id) {
		RoomPriceScheme roomPriceScheme = roomPriceSchemeDao.findById(id).get();
		if (roomPriceScheme != null) {
			roomPriceScheme.setDeleted(true);
		}
		roomPriceSchemeDao.saveAndFlush(roomPriceScheme);
	}

	@Override
	public RoomPriceScheme modify(RoomPriceScheme roomPriceScheme) {
		return roomPriceSchemeDao.saveAndFlush(roomPriceScheme);
	}

	@Override
	public RoomPriceScheme findById(String id) {
		return roomPriceSchemeDao.getOne(id);
	}

	@Override
	public List<RoomPriceScheme> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return roomPriceSchemeDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<RoomPriceScheme> listPage(PageRequest<RoomPriceScheme> prq) {
		Example<RoomPriceScheme> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(roomPriceSchemeDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
