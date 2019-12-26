package com.kry.pms.api.busi;

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
import com.kry.pms.model.persistence.busi.RoomRepairRecord;
import com.kry.pms.service.busi.RoomRepairRecordService;

@RestController
@RequestMapping(path = "/api/v1/busi/roomRepairRecord")
public class RoomRepairRecordController extends BaseController<RoomRepairRecord> {
	@Autowired
	RoomRepairRecordService roomRepairRecordService;
	@PostMapping
	public HttpResponse<RoomRepairRecord> add(@RequestBody RoomRepairRecord roomRepairRecord) {
		return getDefaultResponse().addData(roomRepairRecordService.add(roomRepairRecord));
	}

	@PutMapping
	public HttpResponse<RoomRepairRecord> modify(@RequestBody RoomRepairRecord roomRepairRecord) {
		return getDefaultResponse().addData(roomRepairRecordService.modify(roomRepairRecord));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		roomRepairRecordService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<RoomRepairRecord>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<RoomRepairRecord>> rep = new HttpResponse<PageResponse<RoomRepairRecord>>();
		PageRequest<RoomRepairRecord> req = parse2PageRequest(request);
		return rep.addData(roomRepairRecordService.listPage(req));
	}

}
