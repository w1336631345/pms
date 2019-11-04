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
import com.kry.pms.model.persistence.busi.CreditGrantingRecord;
import com.kry.pms.service.busi.CreditGrantingRecordService;

@RestController
@RequestMapping(path = "/api/v1/busi/creditGrantingRecord")
public class CreditGrantingRecordController extends BaseController<CreditGrantingRecord> {
	@Autowired
	CreditGrantingRecordService creditGrantingRecordService;
	@PostMapping
	public HttpResponse<CreditGrantingRecord> add(@RequestBody CreditGrantingRecord creditGrantingRecord) {
		return getDefaultResponse().addData(creditGrantingRecordService.add(creditGrantingRecord));
	}

	@PutMapping
	public HttpResponse<CreditGrantingRecord> modify(@RequestBody CreditGrantingRecord creditGrantingRecord) {
		return getDefaultResponse().addData(creditGrantingRecordService.modify(creditGrantingRecord));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		creditGrantingRecordService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<CreditGrantingRecord>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<CreditGrantingRecord>> rep = new HttpResponse<PageResponse<CreditGrantingRecord>>();
		PageRequest<CreditGrantingRecord> req = parse2PageRequest(request);
		return rep.addData(creditGrantingRecordService.listPage(req));
	}

}
