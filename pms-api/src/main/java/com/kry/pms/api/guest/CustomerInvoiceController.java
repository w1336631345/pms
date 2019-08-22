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
import com.kry.pms.model.persistence.guest.CustomerInvoice;
import com.kry.pms.service.guest.CustomerInvoiceService;

@RestController
@RequestMapping(path = "/api/v1/guest/customerInvoice")
public class CustomerInvoiceController extends BaseController<CustomerInvoice> {
	@Autowired
	CustomerInvoiceService customerInvoiceService;
	@PostMapping
	public HttpResponse<CustomerInvoice> add(@RequestBody CustomerInvoice customerInvoice) {
		return getDefaultResponse().addData(customerInvoiceService.add(customerInvoice));
	}

	@PutMapping
	public HttpResponse<CustomerInvoice> modify(@RequestBody CustomerInvoice customerInvoice) {
		return getDefaultResponse().addData(customerInvoiceService.modify(customerInvoice));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		customerInvoiceService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<CustomerInvoice>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<CustomerInvoice>> rep = new HttpResponse<PageResponse<CustomerInvoice>>();
		PageRequest<CustomerInvoice> req = parse2PageRequest(request);
		return rep.addData(customerInvoiceService.listPage(req));
	}

}
