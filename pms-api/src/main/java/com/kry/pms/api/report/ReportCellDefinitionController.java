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
import com.kry.pms.model.persistence.report.ReportCellDefinition;
import com.kry.pms.service.report.ReportCellDefinitionService;

@RestController
@RequestMapping(path = "/api/v1/report/reportCellDefinition")
public class ReportCellDefinitionController extends BaseController<ReportCellDefinition> {
	@Autowired
	ReportCellDefinitionService reportCellDefinitionService;
	@PostMapping
	public HttpResponse<ReportCellDefinition> add(@RequestBody ReportCellDefinition reportCellDefinition) {
		return getDefaultResponse().addData(reportCellDefinitionService.add(reportCellDefinition));
	}

	@PutMapping
	public HttpResponse<ReportCellDefinition> modify(@RequestBody ReportCellDefinition reportCellDefinition) {
		return getDefaultResponse().addData(reportCellDefinitionService.modify(reportCellDefinition));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		reportCellDefinitionService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<ReportCellDefinition>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<ReportCellDefinition>> rep = new HttpResponse<PageResponse<ReportCellDefinition>>();
		PageRequest<ReportCellDefinition> req = parse2PageRequest(request);
		return rep.addData(reportCellDefinitionService.listPage(req));
	}

}
