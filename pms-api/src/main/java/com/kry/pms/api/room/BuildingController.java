package com.kry.pms.api.room;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
import com.kry.pms.model.persistence.room.Building;
import com.kry.pms.service.room.BuildingService;

@RestController
@RequestMapping(path = "/api/v1/room/building")
public class BuildingController extends BaseController<Building> {
	@Autowired
	BuildingService buildingService;
	@PostMapping
	public HttpResponse<Building> add(@RequestBody @Validated Building building) {
		return getDefaultResponse().addData(buildingService.add(building));
	}

	@PutMapping
	public HttpResponse<Building> modify(@RequestBody @Validated Building building) {
		return getDefaultResponse().addData(buildingService.modify(building));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		buildingService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<Building>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<Building>> rep = new HttpResponse<PageResponse<Building>>();
		PageRequest<Building> req = parse2PageRequest(request);
		return rep.addData(buildingService.listPage(req));
	}

}
