package com.kry.pms.api.sys;

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
import com.kry.pms.model.persistence.sys.OperationLog;
import com.kry.pms.service.sys.OperationLogService;

@RestController
@RequestMapping(path = "/api/v1/sys/operationLog")
public class OperationLogController extends BaseController<OperationLog> {
	@Autowired
	OperationLogService operationLogService;
	@PostMapping
	public HttpResponse<OperationLog> add(@RequestBody OperationLog operationLog) {
		return getDefaultResponse().addData(operationLogService.add(operationLog));
	}

	@PutMapping
	public HttpResponse<OperationLog> modify(@RequestBody OperationLog operationLog) {
		return getDefaultResponse().addData(operationLogService.modify(operationLog));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		operationLogService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<OperationLog>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<OperationLog>> rep = new HttpResponse<PageResponse<OperationLog>>();
		PageRequest<OperationLog> req = parse2PageRequest(request);
		return rep.addData(operationLogService.listPage(req));
	}

}
