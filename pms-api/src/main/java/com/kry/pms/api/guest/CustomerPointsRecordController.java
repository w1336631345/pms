package com.kry.pms.api.guest;

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
import com.kry.pms.model.persistence.guest.CustomerPointsRecord;
import com.kry.pms.service.guest.CustomerPointsRecordService;

@RestController
@RequestMapping(path = "/api/v1/guest/customerPointsRecord")
public class CustomerPointsRecordController extends BaseController<CustomerPointsRecord> {
	@Autowired
	CustomerPointsRecordService customerPointsRecordService;
	@PostMapping
	public HttpResponse<CustomerPointsRecord> add(@RequestBody CustomerPointsRecord customerPointsRecord) {
		return getDefaultResponse().addData(customerPointsRecordService.add(customerPointsRecord));
	}

	@PutMapping
	public HttpResponse<CustomerPointsRecord> modify(@RequestBody CustomerPointsRecord customerPointsRecord) {
		return getDefaultResponse().addData(customerPointsRecordService.modify(customerPointsRecord));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		customerPointsRecordService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<CustomerPointsRecord>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<CustomerPointsRecord>> rep = new HttpResponse<PageResponse<CustomerPointsRecord>>();
		PageRequest<CustomerPointsRecord> req = parse2PageRequest(request);
		return rep.addData(customerPointsRecordService.listPage(req));
	}

}
