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
import com.kry.pms.model.persistence.guest.CustomerInvoiceInfo;
import com.kry.pms.service.guest.CustomerInvoiceInfoService;

@RestController
@RequestMapping(path = "/api/v1/guest/customerInvoiceInfo")
public class CustomerInvoiceInfoController extends BaseController<CustomerInvoiceInfo> {
	@Autowired
	CustomerInvoiceInfoService customerInvoiceInfoService;
	@PostMapping
	public HttpResponse<CustomerInvoiceInfo> add(@RequestBody CustomerInvoiceInfo customerInvoiceInfo) {
		return getDefaultResponse().addData(customerInvoiceInfoService.add(customerInvoiceInfo));
	}

	@PutMapping
	public HttpResponse<CustomerInvoiceInfo> modify(@RequestBody CustomerInvoiceInfo customerInvoiceInfo) {
		return getDefaultResponse().addData(customerInvoiceInfoService.modify(customerInvoiceInfo));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		customerInvoiceInfoService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<CustomerInvoiceInfo>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<CustomerInvoiceInfo>> rep = new HttpResponse<PageResponse<CustomerInvoiceInfo>>();
		PageRequest<CustomerInvoiceInfo> req = parse2PageRequest(request);
		return rep.addData(customerInvoiceInfoService.listPage(req));
	}

}
