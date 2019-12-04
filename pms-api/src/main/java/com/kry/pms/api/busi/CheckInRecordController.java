package com.kry.pms.api.busi;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.http.request.busi.TogetherBo;
import com.kry.pms.model.http.response.busi.CheckInRecordListVo;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.service.busi.CheckInRecordService;

@RestController
@RequestMapping(path = "/api/v1/busi/checkInRecord")
public class CheckInRecordController extends BaseController<CheckInRecord> {
	@Autowired
	CheckInRecordService checkInRecordService;
	@PostMapping
	public HttpResponse<CheckInRecord> add(@RequestBody CheckInRecord checkInRecord) {
		return getDefaultResponse().addData(checkInRecordService.add(checkInRecord));
	}

	@PutMapping
	public HttpResponse<CheckInRecord> modify(@RequestBody CheckInRecord checkInRecord) {
		return getDefaultResponse().addData(checkInRecordService.modify(checkInRecord));
	}
	
	@PostMapping(path = "/book")
	public HttpResponse<CheckInRecord> book(@RequestBody CheckInRecord checkInRecord) {
		checkInRecord.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(checkInRecordService.book(checkInRecord));
	}
	
	@PostMapping(path = "/reserve")
	public HttpResponse<List<CheckInRecord>> addReserve(@RequestBody List<CheckInRecord> checkInRecords){
		HttpResponse<List<CheckInRecord>> rep = new HttpResponse();
		return rep.addData(checkInRecordService.addReserve(checkInRecords));

	}
	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		checkInRecordService.delete(id);
		return rep;
	}
	@GetMapping(path = "/book/{id}")
	public HttpResponse<List<CheckInRecord>> findByBookingId(@PathVariable("id") String bookId){
		HttpResponse<List<CheckInRecord>> rep = new HttpResponse<List<CheckInRecord>>();
		rep.addData(checkInRecordService.findByBookId(bookId));
		return rep;
	}
	@GetMapping(path = "/detail/book/{id}")
	public HttpResponse<List<CheckInRecord>> findDetailByBookingId(@PathVariable("id") String bookId){
		HttpResponse<List<CheckInRecord>> rep = new HttpResponse<List<CheckInRecord>>();
		rep.addData(checkInRecordService.findDetailByBookingId(bookId));
		return rep;
	}
	@GetMapping(path = "/detail/{id}")
	public HttpResponse<CheckInRecord> findDetailById(@PathVariable("id") String id){
		HttpResponse<CheckInRecord> rep = new HttpResponse<CheckInRecord>();
		rep.addData(checkInRecordService.findById(id));
		return rep;
	}
	@GetMapping(path="/orderNum/{orderNum}")
	public HttpResponse<List<CheckInRecord>> findDetailByOrderNum(@PathVariable("orderNum") String orderNum){
		HttpResponse<List<CheckInRecord>> rep = new HttpResponse<List<CheckInRecord>>();
		rep.addData(checkInRecordService.findByOrderNum(orderNum));
		return rep;
	}
	@PostMapping(path="/together")
	public HttpResponse<CheckInRecord> addCustomerTogether(@RequestBody TogetherBo togetherBo){
		return getDefaultResponse().addData(checkInRecordService.addTogether(togetherBo));
	}
	public HttpResponse<PageResponse<CheckInRecordListVo>> queryHistory(){
		HttpResponse<PageResponse<CheckInRecordListVo>> rep = new HttpResponse<PageResponse<CheckInRecordListVo>>();
		return rep;
	}
	
	@GetMapping
	public HttpResponse<PageResponse<CheckInRecord>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<CheckInRecord>> rep = new HttpResponse<PageResponse<CheckInRecord>>();
		PageRequest<CheckInRecord> req = parse2PageRequest(request);
		return rep.addData(checkInRecordService.listPage(req));
	}
	@GetMapping(path = "/summary")
	public HttpResponse<PageResponse<CheckInRecordListVo>> querySummaryList(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<CheckInRecordListVo>> rep = new HttpResponse<PageResponse<CheckInRecordListVo>>();
		PageRequest<CheckInRecord> req = parse2PageRequest(request);
		return rep.addData(checkInRecordService.querySummaryList(req));
	}

}
