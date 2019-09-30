package com.kry.pms.api.dict;

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
import com.kry.pms.model.persistence.dict.RoomLockReason;
import com.kry.pms.service.dict.RoomLockReasonService;

@RestController
@RequestMapping(path = "/api/v1/dict/roomLockReason")
public class RoomLockReasonController extends BaseController<RoomLockReason> {
	@Autowired
	RoomLockReasonService roomLockReasonService;
	@PostMapping
	public HttpResponse<RoomLockReason> add(@RequestBody RoomLockReason roomLockReason) {
		return getDefaultResponse().addData(roomLockReasonService.add(roomLockReason));
	}

	@PutMapping
	public HttpResponse<RoomLockReason> modify(@RequestBody RoomLockReason roomLockReason) {
		return getDefaultResponse().addData(roomLockReasonService.modify(roomLockReason));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		roomLockReasonService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<RoomLockReason>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<RoomLockReason>> rep = new HttpResponse<PageResponse<RoomLockReason>>();
		PageRequest<RoomLockReason> req = parse2PageRequest(request);
		return rep.addData(roomLockReasonService.listPage(req));
	}
	@GetMapping(path = "/all")
	public HttpResponse<List<RoomLockReason>> all(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<List<RoomLockReason>> rep = new HttpResponse<List<RoomLockReason>>();
		return rep.addData(roomLockReasonService.getAllByHotelCode(getCurrentHotleCode()));
	}

}
