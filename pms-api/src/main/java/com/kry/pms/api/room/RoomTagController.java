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
import com.kry.pms.model.persistence.room.RoomTag;
import com.kry.pms.service.room.RoomTagService;

@RestController
@RequestMapping(path = "/api/v1/room/roomTag")
public class RoomTagController extends BaseController<RoomTag> {
	@Autowired
	RoomTagService roomTagService;
	@PostMapping
	public HttpResponse<RoomTag> add(@RequestBody RoomTag roomTag) {
		return getDefaultResponse().addData(roomTagService.add(roomTag));
	}

	@PutMapping
	public HttpResponse<RoomTag> modify(@RequestBody RoomTag roomTag) {
		return getDefaultResponse().addData(roomTagService.modify(roomTag));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		roomTagService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<RoomTag>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<RoomTag>> rep = new HttpResponse<PageResponse<RoomTag>>();
		PageRequest<RoomTag> req = parse2PageRequest(request);
		return rep.addData(roomTagService.listPage(req));
	}

}
