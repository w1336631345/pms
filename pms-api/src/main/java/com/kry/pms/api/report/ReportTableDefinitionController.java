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
import com.kry.pms.model.persistence.report.ReportTableDefinition;
import com.kry.pms.service.report.ReportTableDefinitionService;

@RestController
@RequestMapping(path = "/api/v1/report/reportTableDefinition")
public class ReportTableDefinitionController extends BaseController<ReportTableDefinition> {
	@Autowired
	ReportTableDefinitionService reportTableDefinitionService;
	@PostMapping
	public HttpResponse<ReportTableDefinition> add(@RequestBody ReportTableDefinition reportTableDefinition) {
		return getDefaultResponse().addData(reportTableDefinitionService.add(reportTableDefinition));
	}

	@PutMapping
	public HttpResponse<ReportTableDefinition> modify(@RequestBody ReportTableDefinition reportTableDefinition) {
		return getDefaultResponse().addData(reportTableDefinitionService.modify(reportTableDefinition));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		reportTableDefinitionService.delete(id);
		return rep;
	}


	@GetMapping("/build")
	public HttpResponse<String> build(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		reportTableDefinitionService.buildTemplate(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<ReportTableDefinition>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<ReportTableDefinition>> rep = new HttpResponse<PageResponse<ReportTableDefinition>>();
		PageRequest<ReportTableDefinition> req = parse2PageRequest(request);
		return rep.addData(reportTableDefinitionService.listPage(req));
	}

}
