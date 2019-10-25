package com.kry.pms.service.marketing.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.marketing.RoomPriceSchemeItemDao;
import com.kry.pms.model.persistence.marketing.RoomPriceSchemeItem;
import com.kry.pms.service.marketing.RoomPriceSchemeItemService;

@Service
public class  RoomPriceSchemeItemServiceImpl implements  RoomPriceSchemeItemService{
	@Autowired
	 RoomPriceSchemeItemDao roomPriceSchemeItemDao;
	 
	 @Override
	public RoomPriceSchemeItem add(RoomPriceSchemeItem roomPriceSchemeItem) {
		return roomPriceSchemeItemDao.saveAndFlush(roomPriceSchemeItem);
	}

	@Override
	public void delete(String id) {
		RoomPriceSchemeItem roomPriceSchemeItem = roomPriceSchemeItemDao.findById(id).get();
		if (roomPriceSchemeItem != null) {
			roomPriceSchemeItem.setDeleted(Constants.DELETED_TRUE);
		}
		roomPriceSchemeItemDao.saveAndFlush(roomPriceSchemeItem);
	}

	@Override
	public RoomPriceSchemeItem modify(RoomPriceSchemeItem roomPriceSchemeItem) {
		return roomPriceSchemeItemDao.saveAndFlush(roomPriceSchemeItem);
	}

	@Override
	public RoomPriceSchemeItem findById(String id) {
		return roomPriceSchemeItemDao.getOne(id);
	}

	@Override
	public List<RoomPriceSchemeItem> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return roomPriceSchemeItemDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<RoomPriceSchemeItem> listPage(PageRequest<RoomPriceSchemeItem> prq) {
		Example<RoomPriceSchemeItem> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(roomPriceSchemeItemDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
