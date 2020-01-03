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
import com.kry.pms.model.persistence.dict.RoomRepairReason;
import com.kry.pms.service.dict.RoomRepairReasonService;

@RestController
@RequestMapping(path = "/api/v1/dict/roomRepairReason")
public class RoomRepairReasonController extends BaseController<RoomRepairReason> {
	@Autowired
	RoomRepairReasonService roomRepairReasonService;
	@PostMapping
	public HttpResponse<RoomRepairReason> add(@RequestBody RoomRepairReason roomRepairReason) {
		return getDefaultResponse().addData(roomRepairReasonService.add(roomRepairReason));
	}

	@PutMapping
	public HttpResponse<RoomRepairReason> modify(@RequestBody RoomRepairReason roomRepairReason) {
		return getDefaultResponse().addData(roomRepairReasonService.modify(roomRepairReason));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		roomRepairReasonService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<RoomRepairReason>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<RoomRepairReason>> rep = new HttpResponse<PageResponse<RoomRepairReason>>();
		PageRequest<RoomRepairReason> req = parse2PageRequest(request);
		return rep.addData(roomRepairReasonService.listPage(req));
	}
	@GetMapping(path = "/all")
	public HttpResponse<List<RoomRepairReason>> all(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<List<RoomRepairReason>> rep = new HttpResponse<List<RoomRepairReason>>();
		return rep.addData(roomRepairReasonService.getAllByHotelCode(getCurrentHotleCode()));
	}

}
