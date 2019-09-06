package com.kry.pms.api.busi;

import javax.servlet.http.HttpServletRequest;

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

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		checkInRecordService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<CheckInRecord>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<CheckInRecord>> rep = new HttpResponse<PageResponse<CheckInRecord>>();
		PageRequest<CheckInRecord> req = parse2PageRequest(request);
		return rep.addData(checkInRecordService.listPage(req));
	}

}
