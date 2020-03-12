package com.kry.pms.api.guest;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.guest.CustAddress;
import com.kry.pms.model.persistence.guest.CustPassenger;
import com.kry.pms.service.guest.CustAddressService;
import com.kry.pms.service.guest.CustPassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/guest/custAddress")
public class CustAddressController extends BaseController<CustAddress> {
	@Autowired
	CustAddressService custAddressService;
	@PostMapping
	public HttpResponse<CustAddress> add(@RequestBody CustAddress custAddress) {
		custAddress.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(custAddressService.add(custAddress));
	}

	@PutMapping
	public HttpResponse<CustAddress> modify(@RequestBody CustAddress custAddress) {
		return getDefaultResponse().addData(custAddressService.modify(custAddress));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		custAddressService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<CustAddress>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<CustAddress>> rep = new HttpResponse<PageResponse<CustAddress>>();
		PageRequest<CustAddress> req = parse2PageRequest(request);
		return rep.addData(custAddressService.listPage(req));
	}

	@GetMapping(path = "/hotel")
	public HttpResponse<List<CustAddress>> getByHotelCode(HttpServletRequest request){
		HttpResponse<List<CustAddress>> rep = new HttpResponse<List<CustAddress>>();
		List<CustAddress> list = custAddressService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<CustAddress>> getByCustomerId(String customerId){
		HttpResponse<List<CustAddress>> rep = new HttpResponse<List<CustAddress>>();
		List<CustAddress> list = custAddressService.getByCustomerId(customerId);
		return rep.addData(list);
	}


}
