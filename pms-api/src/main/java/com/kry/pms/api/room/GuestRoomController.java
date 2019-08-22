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
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.service.room.GuestRoomService;

@RestController
@RequestMapping(path = "/api/v1/room/guestRoom")
public class GuestRoomController extends BaseController<GuestRoom> {
	@Autowired
	GuestRoomService guestRoomService;
	@PostMapping
	public HttpResponse<GuestRoom> add(@RequestBody GuestRoom guestRoom) {
		return getDefaultResponse().addData(guestRoomService.add(guestRoom));
	}

	@PutMapping
	public HttpResponse<GuestRoom> modify(@RequestBody GuestRoom guestRoom) {
		return getDefaultResponse().addData(guestRoomService.modify(guestRoom));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		guestRoomService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<GuestRoom>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<GuestRoom>> rep = new HttpResponse<PageResponse<GuestRoom>>();
		PageRequest<GuestRoom> req = parse2PageRequest(request);
		return rep.addData(guestRoomService.listPage(req));
	}

}
