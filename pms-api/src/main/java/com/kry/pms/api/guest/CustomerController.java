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
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.service.guest.CustomerService;

@RestController
@RequestMapping(path = "/api/v1/guest/customer")
public class CustomerController extends BaseController<Customer> {
	@Autowired
	CustomerService customerService;
	@PostMapping
	public HttpResponse<Customer> add(@RequestBody Customer customer) {
		customer.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(customerService.add(customer));
	}

	@PutMapping
	public HttpResponse<Customer> modify(@RequestBody Customer customer) {
		return getDefaultResponse().addData(customerService.modify(customer));
	}

	@PutMapping(path = "/salesStrategy")
	public HttpResponse<Customer> salesStrategy(@RequestBody Customer customer) {
		return getDefaultResponse().addData(customerService.salesStrategy(customer));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		customerService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<Customer>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<Customer>> rep = new HttpResponse<PageResponse<Customer>>();
		PageRequest<Customer> req = parse2PageRequest(request);
		return rep.addData(customerService.listPage(req));
	}

}
