package com.kry.pms.api.audit;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.audit.AuditNightStep;
import com.kry.pms.service.audit.AuditNightStepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/busi/auditNightStep")
public class AuditNightStepController extends BaseController<AuditNightStep> {
	@Autowired
	AuditNightStepService auditNightStepService;

	@PostMapping
	public HttpResponse<AuditNightStep> add(@RequestBody AuditNightStep auditNightStep) {
		auditNightStep.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(auditNightStepService.add(auditNightStep));
	}

	@PutMapping
	public HttpResponse<AuditNightStep> modify(@RequestBody AuditNightStep auditNightStep) {
		return getDefaultResponse().addData(auditNightStepService.modify(auditNightStep));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		auditNightStepService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<AuditNightStep>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<AuditNightStep>> rep = new HttpResponse<PageResponse<AuditNightStep>>();
		PageRequest<AuditNightStep> req = parse2PageRequest(request);
		return rep.addData(auditNightStepService.listPage(req));
	}

	/**
	 * 功能描述: <br>查询酒店夜审步骤详情
	 * 〈〉
	 * @Param: []
	 * @Return: com.kry.pms.base.HttpResponse<java.util.List<com.kry.pms.model.persistence.audit.AuditNightStep>>
	 * @Author: huanghaibin
	 * @Date: 2020/5/25 14:17
	 */
	@GetMapping(path = "/list")
	public HttpResponse<List<AuditNightStep>> list() {
		HttpResponse<List<AuditNightStep>> rep = new HttpResponse<List<AuditNightStep>>();
		List<AuditNightStep> list = auditNightStepService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

	/**
	 * 功能描述: <br>查询步骤执行过程
	 * 〈〉
	 * @Param: [request]
	 * @Return: com.kry.pms.base.HttpResponse<java.util.List<com.kry.pms.model.persistence.report.AuditNightStep>>
	 * @Author: huanghaibin
	 * @Date: 2020/5/22 16:42
	 */
	@GetMapping(path = "/stepStart")
	public HttpResponse<List<AuditNightStep>> stepStart(HttpServletRequest request) {
		HttpResponse hr = new HttpResponse();
		hr = auditNightStepService.findByHotelCodeAndBusinessDate(getCurrentHotleCode());
		return hr;
	}
}
