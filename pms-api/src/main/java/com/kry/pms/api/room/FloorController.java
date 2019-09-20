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
import com.kry.pms.model.persistence.room.Floor;
import com.kry.pms.service.room.FloorService;

@RestController
@RequestMapping(path = "/api/v1/room/floor")
public class FloorController extends BaseController<Floor> {
	@Autowired
	FloorService floorService;
	@PostMapping
	public HttpResponse<Floor> add(@RequestBody @Validated Floor floor) {
		return getDefaultResponse().addData(floorService.add(floor));
	}

	@PutMapping
	public HttpResponse<Floor> modify(@RequestBody @Validated Floor floor) {
		return getDefaultResponse().addData(floorService.modify(floor));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		floorService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<Floor>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<Floor>> rep = new HttpResponse<PageResponse<Floor>>();
		PageRequest<Floor> req = parse2PageRequest(request);
		return rep.addData(floorService.listPage(req));
	}

}
