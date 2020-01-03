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
import com.kry.pms.model.persistence.guest.CustomerLevel;
import com.kry.pms.service.guest.CustomerLevelService;

@RestController
@RequestMapping(path = "/api/v1/guest/customerLevel")
public class CustomerLevelController extends BaseController<CustomerLevel> {
	@Autowired
	CustomerLevelService customerLevelService;
	@PostMapping
	public HttpResponse<CustomerLevel> add(@RequestBody CustomerLevel customerLevel) {
		return getDefaultResponse().addData(customerLevelService.add(customerLevel));
	}

	@PutMapping
	public HttpResponse<CustomerLevel> modify(@RequestBody CustomerLevel customerLevel) {
		return getDefaultResponse().addData(customerLevelService.modify(customerLevel));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		customerLevelService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<CustomerLevel>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<CustomerLevel>> rep = new HttpResponse<PageResponse<CustomerLevel>>();
		PageRequest<CustomerLevel> req = parse2PageRequest(request);
		return rep.addData(customerLevelService.listPage(req));
	}

}
