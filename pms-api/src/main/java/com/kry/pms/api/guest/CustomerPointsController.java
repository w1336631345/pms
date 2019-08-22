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
import com.kry.pms.model.persistence.guest.CustomerPoints;
import com.kry.pms.service.guest.CustomerPointsService;

@RestController
@RequestMapping(path = "/api/v1/guest/customerPoints")
public class CustomerPointsController extends BaseController<CustomerPoints> {
	@Autowired
	CustomerPointsService customerPointsService;
	@PostMapping
	public HttpResponse<CustomerPoints> add(@RequestBody CustomerPoints customerPoints) {
		return getDefaultResponse().addData(customerPointsService.add(customerPoints));
	}

	@PutMapping
	public HttpResponse<CustomerPoints> modify(@RequestBody CustomerPoints customerPoints) {
		return getDefaultResponse().addData(customerPointsService.modify(customerPoints));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		customerPointsService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<CustomerPoints>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<CustomerPoints>> rep = new HttpResponse<PageResponse<CustomerPoints>>();
		PageRequest<CustomerPoints> req = parse2PageRequest(request);
		return rep.addData(customerPointsService.listPage(req));
	}

}
