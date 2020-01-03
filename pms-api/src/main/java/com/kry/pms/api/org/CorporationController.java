package com.kry.pms.api.org;

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
import com.kry.pms.model.persistence.org.Corporation;
import com.kry.pms.service.org.CorporationService;

@RestController
@RequestMapping(path = "/api/v1/org/corporation")
public class CorporationController extends BaseController<Corporation> {
	@Autowired
	CorporationService corporationService;
	@PostMapping
	public HttpResponse<Corporation> add(@RequestBody Corporation corporation) {
		return getDefaultResponse().addData(corporationService.add(corporation));
	}

	@PutMapping
	public HttpResponse<Corporation> modify(@RequestBody Corporation corporation) {
		return getDefaultResponse().addData(corporationService.modify(corporation));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		corporationService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<Corporation>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<Corporation>> rep = new HttpResponse<PageResponse<Corporation>>();
		PageRequest<Corporation> req = parse2PageRequest(request);
		return rep.addData(corporationService.listPage(req));
	}

}
