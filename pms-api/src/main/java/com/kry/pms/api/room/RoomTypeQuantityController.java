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
import com.kry.pms.model.persistence.room.RoomTypeQuantity;
import com.kry.pms.service.room.RoomTypeQuantityService;

@RestController
@RequestMapping(path = "/api/v1/room/roomTypeQuantity")
public class RoomTypeQuantityController extends BaseController<RoomTypeQuantity> {
	@Autowired
	RoomTypeQuantityService roomTypeQuantityService;
	@PostMapping
	public HttpResponse<RoomTypeQuantity> add(@RequestBody RoomTypeQuantity roomTypeQuantity) {
		return getDefaultResponse().addData(roomTypeQuantityService.add(roomTypeQuantity));
	}

	@PutMapping
	public HttpResponse<RoomTypeQuantity> modify(@RequestBody RoomTypeQuantity roomTypeQuantity) {
		return getDefaultResponse().addData(roomTypeQuantityService.modify(roomTypeQuantity));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		roomTypeQuantityService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<RoomTypeQuantity>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<RoomTypeQuantity>> rep = new HttpResponse<PageResponse<RoomTypeQuantity>>();
		PageRequest<RoomTypeQuantity> req = parse2PageRequest(request);
		return rep.addData(roomTypeQuantityService.listPage(req));
	}

}
