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
import com.kry.pms.model.http.request.busi.BookOperationBo;
import com.kry.pms.model.persistence.busi.BookingItem;
import com.kry.pms.model.persistence.busi.BookingRecord;
import com.kry.pms.model.persistence.busi.CheckInRecord;
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
	public BookingRecord findByBookingItemId(String bookingItemId) {
		return bookingRecordDao.findByBookingItemId(bookingItemId);
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
	public DtoResponse<BookingRecord> operation(BookOperationBo bookOperationBo) {
		String bookId = bookOperationBo.getBookId();
		BookingRecord br = findById(bookId);
		DtoResponse<BookingRecord> rep = new DtoResponse<>();
		if (br == null) {
			rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
			rep.setMessage("找不到该订单数据");
		} else {
			switch (bookOperationBo.getOperation()) {
			case Constants.Operation.BOOK_CANCEL:
				cancleBook(rep, bookOperationBo, br);
				break;
			case Constants.Operation.BOOK_VERIFY_PASS:
				verify(rep, bookOperationBo, br, true);
				break;
			case Constants.Operation.BOOK_VERIFY_REFUSE:
				verify(rep, bookOperationBo, br, false);
				break;
			default:
				break;
			}
		}
		return rep;
	}

	private void cancleBook(DtoResponse<BookingRecord> rep, BookOperationBo bookOperationBo, BookingRecord br) {
		br.setStatus(Constants.Status.BOOKING_CANCLE);
		modify(br);
		rep.setData(br);
	}

	private void verify(DtoResponse<BookingRecord> rep, BookOperationBo bookOperationBo, BookingRecord br,
			boolean success) {
		if (success) {
			br.setStatus(Constants.Status.BOOKING_SUCCESS);
		} else {
			br.setStatus(Constants.Status.BOOKING_VERIFY_REFUSE);
		}
		modify(br);
		rep.setData(br);
	}

}
