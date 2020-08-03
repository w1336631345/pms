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
import com.kry.pms.model.persistence.report.ReportRowDefinition;
import com.kry.pms.service.report.ReportRowDefinitionService;

@RestController
@RequestMapping(path = "/api/v1/report/reportRowDefinition")
public class ReportRowDefinitionController extends BaseController<ReportRowDefinition> {
	@Autowired
	ReportRowDefinitionService reportRowDefinitionService;
	@PostMapping
	public HttpResponse<ReportRowDefinition> add(@RequestBody ReportRowDefinition reportRowDefinition) {
		return getDefaultResponse().addData(reportRowDefinitionService.add(reportRowDefinition));
	}

	@PutMapping
	public HttpResponse<ReportRowDefinition> modify(@RequestBody ReportRowDefinition reportRowDefinition) {
		return getDefaultResponse().addData(reportRowDefinitionService.modify(reportRowDefinition));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		reportRowDefinitionService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<ReportRowDefinition>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<ReportRowDefinition>> rep = new HttpResponse<PageResponse<ReportRowDefinition>>();
		PageRequest<ReportRowDefinition> req = parse2PageRequest(request);
		return rep.addData(reportRowDefinitionService.listPage(req));
	}

}
