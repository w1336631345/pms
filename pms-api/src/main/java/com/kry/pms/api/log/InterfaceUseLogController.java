package com.kry.pms.api.log;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.annotation.OperationLog;
import com.kry.pms.model.persistence.guest.CustomerPointsRecord;
import com.kry.pms.model.persistence.log.InterfaceUseLog;
import com.kry.pms.service.guest.CustomerPointsRecordService;
import com.kry.pms.service.log.InterfaceUseLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api/v1/log/interfaceUseLog")
public class InterfaceUseLogController extends BaseController<InterfaceUseLog> {
	@Autowired
	InterfaceUseLogService interfaceUseLogService;
	@PostMapping
	public HttpResponse<InterfaceUseLog> add(@RequestBody InterfaceUseLog interfaceUseLog) {
		return getDefaultResponse().addData(interfaceUseLogService.add(interfaceUseLog));
	}

	@PutMapping
	public HttpResponse<InterfaceUseLog> modify(@RequestBody InterfaceUseLog interfaceUseLog) {
		return getDefaultResponse().addData(interfaceUseLogService.modify(interfaceUseLog));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		interfaceUseLogService.delete(id);
		return rep;
	}

	@GetMapping
	@OperationLog(remark = "测试卷哦")
	public HttpResponse<PageResponse<InterfaceUseLog>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<InterfaceUseLog>> rep = new HttpResponse<PageResponse<InterfaceUseLog>>();
		PageRequest<InterfaceUseLog> req = parse2PageRequest(request);
		PageResponse<InterfaceUseLog> page = interfaceUseLogService.listPage(req);
		rep.addData(page);
		return rep;
	}

}
