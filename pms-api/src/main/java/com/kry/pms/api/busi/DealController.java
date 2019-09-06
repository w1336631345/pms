package com.kry.pms.api.busi;

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
import com.kry.pms.model.persistence.busi.Deal;
import com.kry.pms.service.busi.DealService;

@RestController
@RequestMapping(path = "/api/v1/busi/deal")
public class DealController extends BaseController<Deal> {
	@Autowired
	DealService dealService;
 	@PostMapping
	public HttpResponse<Deal> add(@RequestBody Deal deal) {
		return getDefaultResponse().addData(dealService.add(deal));
	}

	@PutMapping
	public HttpResponse<Deal> modify(@RequestBody Deal deal) {
		return getDefaultResponse().addData(dealService.modify(deal));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		dealService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<Deal>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<Deal>> rep = new HttpResponse<PageResponse<Deal>>();
		PageRequest<Deal> req = parse2PageRequest(request);
		return rep.addData(dealService.listPage(req));
	}

}
