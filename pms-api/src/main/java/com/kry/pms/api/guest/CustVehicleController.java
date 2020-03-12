package com.kry.pms.api.guest;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.guest.CustInvoice;
import com.kry.pms.model.persistence.guest.CustVehicle;
import com.kry.pms.service.guest.CustInvoiceService;
import com.kry.pms.service.guest.CustVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/guest/custVehicle")
public class CustVehicleController extends BaseController<CustVehicle> {
	@Autowired
	CustVehicleService custVehicleService;
	@PostMapping
	public HttpResponse<CustVehicle> add(@RequestBody CustVehicle custVehicle) {
		custVehicle.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(custVehicleService.add(custVehicle));
	}

	@PutMapping
	public HttpResponse<CustVehicle> modify(@RequestBody CustVehicle custVehicle) {
		return getDefaultResponse().addData(custVehicleService.modify(custVehicle));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		custVehicleService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<CustVehicle>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<CustVehicle>> rep = new HttpResponse<PageResponse<CustVehicle>>();
		PageRequest<CustVehicle> req = parse2PageRequest(request);
		return rep.addData(custVehicleService.listPage(req));
	}

	@GetMapping(path = "/hotel")
	public HttpResponse<List<CustVehicle>> getByHotelCode(HttpServletRequest request){
		HttpResponse<List<CustVehicle>> rep = new HttpResponse<List<CustVehicle>>();
		List<CustVehicle> list = custVehicleService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<CustVehicle>> getByCustomerId(String customerId){
			HttpResponse<List<CustVehicle>> rep = new HttpResponse<List<CustVehicle>>();
		List<CustVehicle> list = custVehicleService.getByCustomerId(customerId);
		return rep.addData(list);
	}

}
