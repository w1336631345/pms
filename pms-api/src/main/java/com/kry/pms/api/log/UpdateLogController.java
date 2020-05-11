package com.kry.pms.api.log;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.annotation.OperationLog;
import com.kry.pms.model.persistence.log.InterfaceUseLog;
import com.kry.pms.model.persistence.log.UpdateLog;
import com.kry.pms.service.log.InterfaceUseLogService;
import com.kry.pms.service.log.UpdateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api/v1/log/updateLog")
public class UpdateLogController extends BaseController<UpdateLog> {
	@Autowired
	UpdateLogService updateLogService;
	@PostMapping
	public HttpResponse<UpdateLog> add(@RequestBody UpdateLog updateLog) {
		return getDefaultResponse().addData(updateLogService.add(updateLog));
	}

	@PutMapping
	public HttpResponse<UpdateLog> modify(@RequestBody UpdateLog updateLog) {
		return getDefaultResponse().addData(updateLogService.modify(updateLog));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		updateLogService.delete(id);
		return rep;
	}

	@GetMapping
	@OperationLog(remark = "测试卷哦")
	public HttpResponse<PageResponse<UpdateLog>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<UpdateLog>> rep = new HttpResponse<PageResponse<UpdateLog>>();
		PageRequest<UpdateLog> req = parse2PageRequest(request);
		PageResponse<UpdateLog> page = updateLogService.listPage(req);
		rep.addData(page);
		return rep;
	}

}
