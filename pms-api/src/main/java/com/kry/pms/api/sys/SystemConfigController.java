package com.kry.pms.api.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.kry.pms.model.persistence.sys.User;
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

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		systemConfigService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<SystemConfig>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<SystemConfig>> rep = new HttpResponse<PageResponse<SystemConfig>>();
		PageRequest<SystemConfig> req = parse2PageRequest(request);
		return rep.addData(systemConfigService.listPage(req));
	}

	@GetMapping(path = "/byKey")
	public HttpResponse<SystemConfig> getByHotelCodeAndKey(String key) {
		HttpResponse hr = new HttpResponse();
		User user = getUser();
		SystemConfig sc = systemConfigService.getByHotelCodeAndKey(getCurrentHotleCode(), key);
		hr.setData(sc);
		return hr;
	}


}
