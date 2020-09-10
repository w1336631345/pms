package com.kry.pms.manager.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.sys.SystemConfig;
import com.kry.pms.service.sys.SystemConfigService;

@RestController
@RequestMapping(path = "/api/v1/sys/systemConfig")
public class SystemConfigController extends BaseController<SystemConfig> {
	@Autowired
	SystemConfigService systemConfigService;
	@PostMapping
	public HttpResponse<SystemConfig> add(@RequestBody SystemConfig systemConfig) {
		return getDefaultResponse().addData(systemConfigService.add(systemConfig));
	}

	@PutMapping
	public HttpResponse<SystemConfig> modify(@RequestBody SystemConfig systemConfig) {
		return getDefaultResponse().addData(systemConfigService.modify(systemConfig));
	}
	@GetMapping("/{hotelCode}")
	public HttpResponse<PageResponse<SystemConfig>> query(HttpServletRequest request,@PathVariable("hotelCode") String hotelCode) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<SystemConfig>> rep = new HttpResponse<PageResponse<SystemConfig>>();
		PageRequest<SystemConfig> req = parse2PageRequest(request);
		req.getExb().setHotelCode(hotelCode);
		return rep.addData(systemConfigService.listPage(req));
	}

}
