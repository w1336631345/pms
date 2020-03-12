package com.kry.pms.api.guest;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.guest.CustPassenger;
import com.kry.pms.model.persistence.guest.VipType;
import com.kry.pms.service.guest.CustPassengerService;
import com.kry.pms.service.guest.VipTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/guest/custPassenger")
public class CustPassengerController extends BaseController<CustPassenger> {
	@Autowired
	CustPassengerService custPassengerService;
	@PostMapping
	public HttpResponse<CustPassenger> add(@RequestBody CustPassenger custPassenger) {
		return getDefaultResponse().addData(custPassengerService.add(custPassenger));
	}

	@PutMapping
	public HttpResponse<CustPassenger> modify(@RequestBody CustPassenger custPassenger) {
		return getDefaultResponse().addData(custPassengerService.modify(custPassenger));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		custPassengerService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<CustPassenger>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<CustPassenger>> rep = new HttpResponse<PageResponse<CustPassenger>>();
		PageRequest<CustPassenger> req = parse2PageRequest(request);
		return rep.addData(custPassengerService.listPage(req));
	}

	@GetMapping(path = "/hotel")
	public HttpResponse<List<CustPassenger>> getByHotelCode(HttpServletRequest request){
		HttpResponse<List<CustPassenger>> rep = new HttpResponse<List<CustPassenger>>();
		List<CustPassenger> list = custPassengerService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<CustPassenger>> getByCustomerId(String customerId){
			HttpResponse<List<CustPassenger>> rep = new HttpResponse<List<CustPassenger>>();
		List<CustPassenger> list = custPassengerService.getByCustomerId(customerId);
		return rep.addData(list);
	}

}
