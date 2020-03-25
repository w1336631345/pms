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
import com.kry.pms.model.persistence.report.VerifyStep;
import com.kry.pms.service.report.VerifyStepService;

@RestController
@RequestMapping(path = "/api/v1/report/verifyStep")
public class VerifyStepController extends BaseController<VerifyStep> {
	@Autowired
	VerifyStepService verifyStepService;
	@PostMapping
	public HttpResponse<VerifyStep> add(@RequestBody VerifyStep verifyStep) {
		return getDefaultResponse().addData(verifyStepService.add(verifyStep));
	}

	@PutMapping
	public HttpResponse<VerifyStep> modify(@RequestBody VerifyStep verifyStep) {
		return getDefaultResponse().addData(verifyStepService.modify(verifyStep));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		verifyStepService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<VerifyStep>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<VerifyStep>> rep = new HttpResponse<PageResponse<VerifyStep>>();
		PageRequest<VerifyStep> req = parse2PageRequest(request);
		return rep.addData(verifyStepService.listPage(req));
	}

}
