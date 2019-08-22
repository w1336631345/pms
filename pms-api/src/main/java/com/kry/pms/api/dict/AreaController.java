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
import com.kry.pms.model.persistence.dict.Area;
import com.kry.pms.service.dict.AreaService;

@RestController
@RequestMapping(path = "/api/v1/dict/area")
public class AreaController extends BaseController<Area> {
	@Autowired
	AreaService areaService;
	@PostMapping
	public HttpResponse<Area> add(@RequestBody Area area) {
		return getDefaultResponse().addData(areaService.add(area));
	}

	@PutMapping
	public HttpResponse<Area> modify(@RequestBody Area area) {
		return getDefaultResponse().addData(areaService.modify(area));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		areaService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<Area>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<Area>> rep = new HttpResponse<PageResponse<Area>>();
		PageRequest<Area> req = parse2PageRequest(request);
		return rep.addData(areaService.listPage(req));
	}

}
