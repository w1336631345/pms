package com.kry.pms.api.marketing;

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
import com.kry.pms.model.persistence.marketing.SalesMen;
import com.kry.pms.service.marketing.SalesMenService;

@RestController
@RequestMapping(path = "/api/v1/marketing/salesMen")
public class SalesMenController extends BaseController<SalesMen> {
	@Autowired
	SalesMenService salesMenService;
	@PostMapping
	public HttpResponse<SalesMen> add(@RequestBody SalesMen salesMen) {
		return getDefaultResponse().addData(salesMenService.add(salesMen));
	}

	@PutMapping
	public HttpResponse<SalesMen> modify(@RequestBody SalesMen salesMen) {
		return getDefaultResponse().addData(salesMenService.modify(salesMen));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		salesMenService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<SalesMen>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<SalesMen>> rep = new HttpResponse<PageResponse<SalesMen>>();
		PageRequest<SalesMen> req = parse2PageRequest(request);
		return rep.addData(salesMenService.listPage(req));
	}

}
