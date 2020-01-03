package com.kry.pms.api.busi;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
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
import com.kry.pms.model.persistence.busi.CreditGrantingRecord;
import com.kry.pms.service.busi.CreditGrantingRecordService;

@RestController
@RequestMapping(path = "/api/v1/busi/creditGrantingRecord")
public class CreditGrantingRecordController extends BaseController<CreditGrantingRecord> {
	@Autowired
	CreditGrantingRecordService creditGrantingRecordService;
	@PostMapping
	public HttpResponse<CreditGrantingRecord> add(@RequestBody CreditGrantingRecord creditGrantingRecord) {
		HttpResponse<CreditGrantingRecord> rep = new HttpResponse<CreditGrantingRecord>();
		BeanUtils.copyProperties(creditGrantingRecordService.createRecord(creditGrantingRecord), rep);
		return rep;
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
	@GetMapping(path="/account/{id}")
	public HttpResponse<List<CreditGrantingRecord>> queryByAccountId(@PathVariable("id") String id) throws InstantiationException, IllegalAccessException{
		HttpResponse<List<CreditGrantingRecord>> rep = new HttpResponse<List<CreditGrantingRecord>>();
		return rep.addData(creditGrantingRecordService.queryByAccountId(id));
	}
	@GetMapping(path="/grantingAccount/{id}")
	public HttpResponse<List<CreditGrantingRecord>> queryByGrantingAccountId(@PathVariable("id") String id) throws InstantiationException, IllegalAccessException{
		HttpResponse<List<CreditGrantingRecord>> rep = new HttpResponse<List<CreditGrantingRecord>>();
		return rep.addData(creditGrantingRecordService.queryByGrantingAccountId(id));
	}
}
