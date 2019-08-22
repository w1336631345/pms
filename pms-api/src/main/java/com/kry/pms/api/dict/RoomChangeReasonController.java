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
import com.kry.pms.model.persistence.dict.RoomChangeReason;
import com.kry.pms.service.dict.RoomChangeReasonService;

@RestController
@RequestMapping(path = "/api/v1/dict/roomChangeReason")
public class RoomChangeReasonController extends BaseController<RoomChangeReason> {
	@Autowired
	RoomChangeReasonService roomChangeReasonService;
	@PostMapping
	public HttpResponse<RoomChangeReason> add(@RequestBody RoomChangeReason roomChangeReason) {
		return getDefaultResponse().addData(roomChangeReasonService.add(roomChangeReason));
	}

	@PutMapping
	public HttpResponse<RoomChangeReason> modify(@RequestBody RoomChangeReason roomChangeReason) {
		return getDefaultResponse().addData(roomChangeReasonService.modify(roomChangeReason));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		roomChangeReasonService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<RoomChangeReason>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<RoomChangeReason>> rep = new HttpResponse<PageResponse<RoomChangeReason>>();
		PageRequest<RoomChangeReason> req = parse2PageRequest(request);
		return rep.addData(roomChangeReasonService.listPage(req));
	}

}
