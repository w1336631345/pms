package com.kry.pms.api.guest;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.guest.CustomerType;
import com.kry.pms.service.guest.CustomerService;
import com.kry.pms.service.guest.CustomerTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/guest/customerType")
public class CustomerTypeController extends BaseController<CustomerType> {
	@Autowired
	CustomerTypeService customerTypeService;
	@PostMapping
	public HttpResponse<CustomerType> add(@RequestBody CustomerType customerType) {
		return getDefaultResponse().addData(customerTypeService.add(customerType));
	}

	@PutMapping
	public HttpResponse<CustomerType> modify(@RequestBody CustomerType customerType) {
		return getDefaultResponse().addData(customerTypeService.modify(customerType));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		customerTypeService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<CustomerType>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<CustomerType>> rep = new HttpResponse<PageResponse<CustomerType>>();
		PageRequest<CustomerType> req = parse2PageRequest(request);
		return rep.addData(customerTypeService.listPage(req));
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<CustomerType>> getByHotelCode(HttpServletRequest request){
		HttpResponse<List<CustomerType>> rep = new HttpResponse<List<CustomerType>>();
		List<CustomerType> list = customerTypeService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

}
