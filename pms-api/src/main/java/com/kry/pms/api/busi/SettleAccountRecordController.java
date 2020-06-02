package com.kry.pms.api.busi;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

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
import com.kry.pms.model.http.response.busi.SettleInfoVo;
import com.kry.pms.model.persistence.busi.SettleAccountRecord;
import com.kry.pms.service.busi.SettleAccountRecordService;

@RestController
@RequestMapping(path = "/api/v1/busi/settleAccountRecord")
public class SettleAccountRecordController extends BaseController<SettleAccountRecord> {
	@Autowired
	SettleAccountRecordService settleAccountRecordService;

	@PostMapping
	public HttpResponse<SettleAccountRecord> add(@RequestBody SettleAccountRecord settleAccountRecord) {
		return getDefaultResponse().addData(settleAccountRecordService.add(settleAccountRecord));
	}

	@PutMapping
	public HttpResponse<SettleAccountRecord> modify(@RequestBody SettleAccountRecord settleAccountRecord) {
		return getDefaultResponse().addData(settleAccountRecordService.modify(settleAccountRecord));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		settleAccountRecordService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<SettleAccountRecord>> query(HttpServletRequest request)
			throws InstantiationException, IllegalAccessException {
		HttpResponse<PageResponse<SettleAccountRecord>> rep = new HttpResponse<PageResponse<SettleAccountRecord>>();
		PageRequest<SettleAccountRecord> req = parse2PageRequest(request);
		return rep.addData(settleAccountRecordService.listPage(req));
	}

	@GetMapping(path = "/cancle/{id}")
	public HttpResponse<String> cancle(@PathVariable("id") String id) {
		HttpResponse<String> rep = new HttpResponse<String>();
		BeanUtils.copyProperties(settleAccountRecordService.cancle(id, getShiftCode(), getCurrentEmployee()), rep);
		return rep;
	}
	@GetMapping(path = "/account/{id}")
	public HttpResponse account(){
		HttpResponse<String> rep = new HttpResponse<String>();
		return rep;
	}

}
