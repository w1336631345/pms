package com.kry.pms.api.dict;

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
import com.kry.pms.model.persistence.dict.CountryOrRegion;
import com.kry.pms.service.dict.CountryOrRegionService;

@RestController
@RequestMapping(path = "/api/v1/dict/countryOrRegion")
public class CountryOrRegionController extends BaseController<CountryOrRegion> {
	@Autowired
	CountryOrRegionService countryOrRegionService;
	@PostMapping
	public HttpResponse<CountryOrRegion> add(@RequestBody CountryOrRegion countryOrRegion) {
		return getDefaultResponse().addData(countryOrRegionService.add(countryOrRegion));
	}

	@PutMapping
	public HttpResponse<CountryOrRegion> modify(@RequestBody CountryOrRegion countryOrRegion) {
		return getDefaultResponse().addData(countryOrRegionService.modify(countryOrRegion));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		countryOrRegionService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<CountryOrRegion>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<CountryOrRegion>> rep = new HttpResponse<PageResponse<CountryOrRegion>>();
		PageRequest<CountryOrRegion> req = parse2PageRequest(request);
		return rep.addData(countryOrRegionService.listPage(req));
	}

}
