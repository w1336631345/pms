package com.kry.pms.api.room;

import java.util.List;

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
import com.kry.pms.model.persistence.room.RoomType;
import com.kry.pms.service.room.RoomTypeService;

@RestController
@RequestMapping(path = "/api/v1/room/roomType")
public class RoomTypeController extends BaseController<RoomType> {
	@Autowired
	RoomTypeService roomTypeService;

	@PostMapping
	public HttpResponse<RoomType> add(@RequestBody RoomType roomType) {
		return getDefaultResponse().addData(roomTypeService.add(roomType));
	}

	@PutMapping
	public HttpResponse<RoomType> modify(@RequestBody RoomType roomType) {
		return getDefaultResponse().addData(roomTypeService.modify(roomType));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		roomTypeService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<RoomType>> query(HttpServletRequest request)
			throws InstantiationException, IllegalAccessException {
		HttpResponse<PageResponse<RoomType>> rep = new HttpResponse<PageResponse<RoomType>>();
		PageRequest<RoomType> req = parse2PageRequest(request);
		return rep.addData(roomTypeService.listPage(req));
	}
	@GetMapping(path="/hotel")
	public HttpResponse<List<RoomType>> hotelAllRoomType() {
		HttpResponse<List<RoomType>> rep = new HttpResponse<List<RoomType>>();
		rep.addData(roomTypeService.getAllByHotelCode(getCurrentHotleCode()));
		return rep;
	}

	@GetMapping(path="/list")
	public HttpResponse<List<RoomType>> codeAndDeleted() {
		HttpResponse<List<RoomType>> rep = new HttpResponse<List<RoomType>>();
		rep.addData(roomTypeService.getAllByHotelCode(getCurrentHotleCode(),0));
		return rep;
	}

}
