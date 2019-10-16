package com.kry.pms.api.room;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
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
import com.kry.pms.model.http.request.busi.RoomLockBo;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.service.room.GuestRoomService;

@RestController
@RequestMapping(path = "/api/v1/room/guestRoom")
public class GuestRoomController extends BaseController<GuestRoom> {
	@Autowired
	GuestRoomService guestRoomService;
	@PostMapping
	public HttpResponse<GuestRoom> add(@RequestBody GuestRoom guestRoom) {
		HttpResponse<GuestRoom> rep = getDefaultResponse();
		BeanUtils.copyProperties(guestRoomService.addWithDto(guestRoom), rep);
		return rep;
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
	@PostMapping(path="/batch")
	public HttpResponse<List<GuestRoom>> batchAdd(@RequestBody GuestRoom guestRoom) {
		HttpResponse<List<GuestRoom>> rep = new HttpResponse<List<GuestRoom>>();
		BeanUtils.copyProperties(guestRoomService.batchAdd(guestRoom), rep);
		return rep;
	}
	@PostMapping(path = "/lock")
	public HttpResponse<String> lock(@Valid @RequestBody RoomLockBo rlb) {
		HttpResponse<String> rep = new HttpResponse<String>();
		BeanUtils.copyProperties(guestRoomService.locked(rlb), rep);
		return rep;
	}
	
	@GetMapping
	public HttpResponse<PageResponse<GuestRoom>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<GuestRoom>> rep = new HttpResponse<PageResponse<GuestRoom>>();
		PageRequest<GuestRoom> req = parse2PageRequest(request);
		return rep.addData(guestRoomService.listPage(req));
	}

}
