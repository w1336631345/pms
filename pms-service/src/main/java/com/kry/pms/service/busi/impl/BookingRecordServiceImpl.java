package com.kry.pms.service.busi.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.kry.pms.base.Constants;
import com.kry.pms.base.DtoResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.BookingRecordDao;
import com.kry.pms.model.persistence.busi.BookingRecord;
import com.kry.pms.service.busi.BookingRecordService;
import com.kry.pms.service.room.RoomStatisticsService;

@Service
public class BookingRecordServiceImpl implements BookingRecordService {
	@Autowired
	BookingRecordDao bookingRecordDao;
	@Autowired
	RoomStatisticsService roomStatisticsService;

	@Override
	public BookingRecord add(BookingRecord bookingRecord) {
		return bookingRecordDao.saveAndFlush(bookingRecord);
	}

	@Override
	public void delete(String id) {
		BookingRecord bookingRecord = bookingRecordDao.findById(id).get();
		if (bookingRecord != null) {
			bookingRecord.setDeleted(Constants.DELETED_TRUE);
		}
		bookingRecordDao.saveAndFlush(bookingRecord);
	}

	@Override
	public BookingRecord modify(BookingRecord bookingRecord) {
		return bookingRecordDao.saveAndFlush(bookingRecord);
	}

	@Override
	public BookingRecord findById(String id) {
		return bookingRecordDao.getOne(id);
	}

	@Override
	public List<BookingRecord> getAllByHotelCode(String code) {
		return null;// 默认不实现
		// return bookingRecordDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<BookingRecord> listPage(PageRequest<BookingRecord> prq) {
		Example<BookingRecord> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(bookingRecordDao.findAll(ex, req));
	}

	@Override
	public DtoResponse<BookingRecord> book(BookingRecord record) {
		DtoResponse<BookingRecord> rep = new DtoResponse<BookingRecord>();
		if (roomStatisticsService.booking(record.getRoomType(), record.getArriveTime(), record.getRoomCount(),
				record.getDays())) {
			rep.addData(add(record));
		} else {
			rep.setStatus(Constants.BusinessCode.CODE_RESOURCE_NOT_ENOUGH);
		}
		return rep;
	}

}
