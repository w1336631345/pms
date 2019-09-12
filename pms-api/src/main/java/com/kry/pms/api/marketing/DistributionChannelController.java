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
import com.kry.pms.model.persistence.marketing.DistributionChannel;
import com.kry.pms.service.marketing.DistributionChannelService;

@RestController
@RequestMapping(path = "/api/v1/marketing/distributionChannel")
public class DistributionChannelController extends BaseController<DistributionChannel> {
	@Autowired
	DistributionChannelService distributionChannelService;
	@PostMapping
	public HttpResponse<DistributionChannel> add(@RequestBody DistributionChannel distributionChannel) {
		return getDefaultResponse().addData(distributionChannelService.add(distributionChannel));
	}

	@PutMapping
	public HttpResponse<DistributionChannel> modify(@RequestBody DistributionChannel distributionChannel) {
		return getDefaultResponse().addData(distributionChannelService.modify(distributionChannel));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		distributionChannelService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<DistributionChannel>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<DistributionChannel>> rep = new HttpResponse<PageResponse<DistributionChannel>>();
		PageRequest<DistributionChannel> req = parse2PageRequest(request);
		return rep.addData(distributionChannelService.listPage(req));
	}

}
