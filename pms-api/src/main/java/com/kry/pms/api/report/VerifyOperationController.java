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
import com.kry.pms.model.persistence.report.VerifyOperation;
import com.kry.pms.service.report.VerifyOperationService;

@RestController
@RequestMapping(path = "/api/v1/report/verifyOperation")
public class VerifyOperationController extends BaseController<VerifyOperation> {
	@Autowired
	VerifyOperationService verifyOperationService;
	@PostMapping
	public HttpResponse<VerifyOperation> add(@RequestBody VerifyOperation verifyOperation) {
		return getDefaultResponse().addData(verifyOperationService.add(verifyOperation));
	}

	@PutMapping
	public HttpResponse<VerifyOperation> modify(@RequestBody VerifyOperation verifyOperation) {
		return getDefaultResponse().addData(verifyOperationService.modify(verifyOperation));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		verifyOperationService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<VerifyOperation>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<VerifyOperation>> rep = new HttpResponse<PageResponse<VerifyOperation>>();
		PageRequest<VerifyOperation> req = parse2PageRequest(request);
		return rep.addData(verifyOperationService.listPage(req));
	}

}
