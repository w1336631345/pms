package com.kry.pms.api.room;

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
import com.kry.pms.model.persistence.room.RoomStatusQuantity;
import com.kry.pms.service.room.RoomStatusQuantityService;

@RestController
@RequestMapping(path = "/api/v1/room/roomStatusQuantity")
public class RoomStatusQuantityController extends BaseController<RoomStatusQuantity> {
	@Autowired
	RoomStatusQuantityService roomStatusQuantityService;
	@PostMapping
	public HttpResponse<RoomStatusQuantity> add(@RequestBody RoomStatusQuantity roomStatusQuantity) {
		return getDefaultResponse().addData(roomStatusQuantityService.add(roomStatusQuantity));
	}

	@PutMapping
	public HttpResponse<RoomStatusQuantity> modify(@RequestBody RoomStatusQuantity roomStatusQuantity) {
		return getDefaultResponse().addData(roomStatusQuantityService.modify(roomStatusQuantity));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		roomStatusQuantityService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<RoomStatusQuantity>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<RoomStatusQuantity>> rep = new HttpResponse<PageResponse<RoomStatusQuantity>>();
		PageRequest<RoomStatusQuantity> req = parse2PageRequest(request);
		return rep.addData(roomStatusQuantityService.listPage(req));
	}

}
