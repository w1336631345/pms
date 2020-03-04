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
import com.kry.pms.model.persistence.report.ReportBaseValueDefinition;
import com.kry.pms.service.report.ReportBaseValueDefinitionService;

@RestController
@RequestMapping(path = "/api/v1/report/reportBaseValueDefinition")
public class ReportBaseValueDefinitionController extends BaseController<ReportBaseValueDefinition> {
	@Autowired
	ReportBaseValueDefinitionService reportBaseValueDefinitionService;
	@PostMapping
	public HttpResponse<ReportBaseValueDefinition> add(@RequestBody ReportBaseValueDefinition reportBaseValueDefinition) {
		return getDefaultResponse().addData(reportBaseValueDefinitionService.add(reportBaseValueDefinition));
	}

	@PutMapping
	public HttpResponse<ReportBaseValueDefinition> modify(@RequestBody ReportBaseValueDefinition reportBaseValueDefinition) {
		return getDefaultResponse().addData(reportBaseValueDefinitionService.modify(reportBaseValueDefinition));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		reportBaseValueDefinitionService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<ReportBaseValueDefinition>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<ReportBaseValueDefinition>> rep = new HttpResponse<PageResponse<ReportBaseValueDefinition>>();
		PageRequest<ReportBaseValueDefinition> req = parse2PageRequest(request);
		return rep.addData(reportBaseValueDefinitionService.listPage(req));
	}

}
