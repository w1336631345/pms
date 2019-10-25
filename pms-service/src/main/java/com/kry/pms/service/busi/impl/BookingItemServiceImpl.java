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
import com.kry.pms.dao.busi.BookingItemDao;
import com.kry.pms.model.persistence.busi.BookingItem;
import com.kry.pms.service.busi.BookingItemService;

@Service
public class  BookingItemServiceImpl implements  BookingItemService{
	@Autowired
	 BookingItemDao bookingItemDao;
	 
	 @Override
	public BookingItem add(BookingItem bookingItem) {
		return bookingItemDao.saveAndFlush(bookingItem);
	}

	@Override
	public void delete(String id) {
		BookingItem bookingItem = bookingItemDao.findById(id).get();
		if (bookingItem != null) {
			bookingItem.setDeleted(Constants.DELETED_TRUE);
		}
		modify(bookingItem);
	}

	@Override
	public BookingItem modify(BookingItem bookingItem) {
		return bookingItemDao.saveAndFlush(bookingItem);
	}

	@Override
	public BookingItem findById(String id) {
		return bookingItemDao.getOne(id);
	}

	@Override
	public List<BookingItem> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return bookingItemDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<BookingItem> listPage(PageRequest<BookingItem> prq) {
		Example<BookingItem> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(bookingItemDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
