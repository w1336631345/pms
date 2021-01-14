package com.kry.pms.api.audit;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.audit.AuditNightStep;
import com.kry.pms.model.persistence.audit.AuditNightStepHis;
import com.kry.pms.service.audit.AuditNightStepHisService;
import com.kry.pms.service.audit.AuditNightStepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/busi/auditNightStepHis")
public class AuditNightStepHisController extends BaseController<AuditNightStepHis> {
	@Autowired
	AuditNightStepHisService auditNightStepHisService;

	@PostMapping
	public HttpResponse<AuditNightStepHis> add(@RequestBody AuditNightStepHis auditNightStep) {
		return getDefaultResponse().addData(auditNightStepHisService.add(auditNightStep));
	}

	@PutMapping
	public HttpResponse<AuditNightStepHis> modify(@RequestBody AuditNightStepHis auditNightStep) {
		return getDefaultResponse().addData(auditNightStepHisService.modify(auditNightStep));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		auditNightStepHisService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<AuditNightStepHis>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<AuditNightStepHis>> rep = new HttpResponse<PageResponse<AuditNightStepHis>>();
		PageRequest<AuditNightStepHis> req = parse2PageRequest(request);
		return rep.addData(auditNightStepHisService.listPage(req));
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<AuditNightStepHis>> list(HttpServletRequest request){
		HttpResponse<List<AuditNightStepHis>> rep = new HttpResponse<List<AuditNightStepHis>>();
		List<AuditNightStepHis> list = auditNightStepHisService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

	/**
	 * 功能描述: <br>查询执行步骤列表
	 * 〈〉
	 * @Param: [request]
	 * @Return: com.kry.pms.base.HttpResponse<java.util.List<com.kry.pms.model.persistence.report.AuditNightStep>>
	 * @Author: huanghaibin
	 * @Date: 2020/5/22 16:42
	 */
	@GetMapping(path = "/stepList")
	public HttpResponse<List<AuditNightStepHis>> stepList(HttpServletRequest request) {
		HttpResponse<List<AuditNightStepHis>> hr = new HttpResponse<List<AuditNightStepHis>>();
		hr = auditNightStepHisService.findByHotelCodeAndBusinessDate(getCurrentHotleCode());
		return hr;
	}

	/**
	 * 功能描述: <br>查询结果1
	 * 〈〉
	 * @Param: [id]
	 * @Return: com.kry.pms.base.HttpResponse<com.kry.pms.model.persistence.audit.AuditNightStepHis>
	 * @Author: huanghaibin
	 * @Date: 2021/1/14 11:25
	 */
	@GetMapping(path = "/refreshOne")
	public HttpResponse<AuditNightStepHis> refreshOne(String id) {
		HttpResponse hr = new HttpResponse();
		AuditNightStepHis auditNightStepHis = auditNightStepHisService.findById(id);
		hr.setData(auditNightStepHis);
		return hr;
	}
	/**
	 * 功能描述: <br>查询结果all
	 * 〈〉
	 * @Param:
	 * @Return:
	 * @Author: huanghaibin
	 * @Date: 2021/1/14 11:25
	 */
	@GetMapping(path = "/refreshAll")
	public HttpResponse<List<AuditNightStepHis>> refreshAll(String businessDate) {
		HttpResponse hr = new HttpResponse();
		List<AuditNightStepHis> list = auditNightStepHisService.resultRefresh(getCurrentHotleCode(), businessDate);
		hr.setData(list);
		return hr;
	}

}
