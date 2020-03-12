package com.kry.pms.api.guest;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.guest.CustFeatures;
import com.kry.pms.model.persistence.guest.CustVehicle;
import com.kry.pms.service.guest.CustFeaturesService;
import com.kry.pms.service.guest.CustVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/guest/custFeatures")
public class CustFeaturesController extends BaseController<CustFeatures> {
	@Autowired
	CustFeaturesService custFeaturesService;
	@PostMapping
	public HttpResponse<CustFeatures> add(@RequestBody CustFeatures CustFeatures) {
		CustFeatures.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(custFeaturesService.add(CustFeatures));
	}

	@PutMapping
	public HttpResponse<CustFeatures> modify(@RequestBody CustFeatures CustFeatures) {
		return getDefaultResponse().addData(custFeaturesService.modify(CustFeatures));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		custFeaturesService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<CustFeatures>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<CustFeatures>> rep = new HttpResponse<PageResponse<CustFeatures>>();
		PageRequest<CustFeatures> req = parse2PageRequest(request);
		return rep.addData(custFeaturesService.listPage(req));
	}

	@GetMapping(path = "/hotel")
	public HttpResponse<List<CustFeatures>> getByHotelCode(HttpServletRequest request){
		HttpResponse<List<CustFeatures>> rep = new HttpResponse<List<CustFeatures>>();
		List<CustFeatures> list = custFeaturesService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<CustFeatures>> getByCustomerId(String customerId){
			HttpResponse<List<CustFeatures>> rep = new HttpResponse<List<CustFeatures>>();
		List<CustFeatures> list = custFeaturesService.getByCustomerId(customerId);
		return rep.addData(list);
	}

}
