package com.kry.pms.api.report;

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
import com.kry.pms.model.persistence.report.ReportBaseValue;
import com.kry.pms.service.report.ReportBaseValueService;

@RestController
@RequestMapping(path = "/api/v1/report/reportBaseValue")
public class ReportBaseValueController extends BaseController<ReportBaseValue> {
	@Autowired
	ReportBaseValueService reportBaseValueService;
	@PostMapping
	public HttpResponse<ReportBaseValue> add(@RequestBody ReportBaseValue reportBaseValue) {
		return getDefaultResponse().addData(reportBaseValueService.add(reportBaseValue));
	}

	@PutMapping
	public HttpResponse<ReportBaseValue> modify(@RequestBody ReportBaseValue reportBaseValue) {
		return getDefaultResponse().addData(reportBaseValueService.modify(reportBaseValue));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		reportBaseValueService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<ReportBaseValue>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<ReportBaseValue>> rep = new HttpResponse<PageResponse<ReportBaseValue>>();
		PageRequest<ReportBaseValue> req = parse2PageRequest(request);
		return rep.addData(reportBaseValueService.listPage(req));
	}

}
