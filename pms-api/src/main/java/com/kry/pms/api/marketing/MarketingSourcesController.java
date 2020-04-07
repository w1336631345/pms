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
import com.kry.pms.model.persistence.marketing.MarketingSources;
import com.kry.pms.service.marketing.MarketingSourcesService;

@RestController
@RequestMapping(path = "/api/v1/marketing/marketingSources")
public class MarketingSourcesController extends BaseController<MarketingSources> {
	@Autowired
	MarketingSourcesService marketingSourcesService;
	@PostMapping
	public HttpResponse<MarketingSources> add(@RequestBody MarketingSources marketingSources) {
		HttpResponse hr = new HttpResponse();
		marketingSources.setHotelCode(getCurrentHotleCode());
		if(marketingSourcesService.add(marketingSources) == null){
			return hr.error("代码重复");
		}
		return getDefaultResponse().addData(marketingSourcesService.add(marketingSources));
	}

	@PutMapping
	public HttpResponse<MarketingSources> modify(@RequestBody MarketingSources marketingSources) {
		return getDefaultResponse().addData(marketingSourcesService.modify(marketingSources));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		marketingSourcesService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<MarketingSources>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<MarketingSources>> rep = new HttpResponse<PageResponse<MarketingSources>>();
		PageRequest<MarketingSources> req = parse2PageRequest(request);
		return rep.addData(marketingSourcesService.listPage(req));
	}

}
