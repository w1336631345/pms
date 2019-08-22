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
import com.kry.pms.model.persistence.marketing.MarketingChannel;
import com.kry.pms.service.marketing.MarketingChannelService;

@RestController
@RequestMapping(path = "/api/v1/marketing/marketingChannel")
public class MarketingChannelController extends BaseController<MarketingChannel> {
	@Autowired
	MarketingChannelService marketingChannelService;
	@PostMapping
	public HttpResponse<MarketingChannel> add(@RequestBody MarketingChannel marketingChannel) {
		return getDefaultResponse().addData(marketingChannelService.add(marketingChannel));
	}

	@PutMapping
	public HttpResponse<MarketingChannel> modify(@RequestBody MarketingChannel marketingChannel) {
		return getDefaultResponse().addData(marketingChannelService.modify(marketingChannel));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		marketingChannelService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<MarketingChannel>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<MarketingChannel>> rep = new HttpResponse<PageResponse<MarketingChannel>>();
		PageRequest<MarketingChannel> req = parse2PageRequest(request);
		return rep.addData(marketingChannelService.listPage(req));
	}

}
