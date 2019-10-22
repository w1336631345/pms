package com.kry.pms.api.busi;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.http.request.busi.BookOperationBo;
import com.kry.pms.model.persistence.busi.BookingRecord;
import com.kry.pms.service.busi.BookingRecordService;

@RestController
@RequestMapping(path = "/api/v1/busi/bookingRecord")
public class BookingRecordController extends BaseController<BookingRecord> {
	@Autowired
	BookingRecordService bookingRecordService;

	@PostMapping
	public HttpResponse<BookingRecord> add(@RequestBody BookingRecord bookingRecord) {
		return getDefaultResponse().addData(bookingRecordService.add(bookingRecord));
	}

	@PutMapping
	public HttpResponse<BookingRecord> modify(@RequestBody BookingRecord bookingRecord) {
		return getDefaultResponse().addData(bookingRecordService.modify(bookingRecord));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		bookingRecordService.delete(id);
		return rep;
	}

	@PostMapping("/operation")
	public HttpResponse<BookingRecord> operation(@RequestBody @Valid BookOperationBo bob) {
		HttpResponse<BookingRecord> rep = new HttpResponse<BookingRecord>();
		BeanUtils.copyProperties(bookingRecordService.operation(bob), rep);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<BookingRecord>> query(HttpServletRequest request)
			throws InstantiationException, IllegalAccessException {
		HttpResponse<PageResponse<BookingRecord>> rep = new HttpResponse<PageResponse<BookingRecord>>();
		PageRequest<BookingRecord> req = parse2PageRequest(request);
		return rep.addData(bookingRecordService.listPage(req));
	}

}
