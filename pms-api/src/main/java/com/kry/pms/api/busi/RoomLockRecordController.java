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
import com.kry.pms.model.persistence.busi.RoomLockRecord;
import com.kry.pms.service.busi.RoomLockRecordService;

@RestController
@RequestMapping(path = "/api/v1/busi/roomLockRecord")
public class RoomLockRecordController extends BaseController<RoomLockRecord> {
	@Autowired
	RoomLockRecordService roomLockRecordService;
	@PostMapping
	public HttpResponse<RoomLockRecord> add(@RequestBody RoomLockRecord roomLockRecord) {
		return getDefaultResponse().addData(roomLockRecordService.add(roomLockRecord));
	}

	@PutMapping
	public HttpResponse<RoomLockRecord> modify(@RequestBody RoomLockRecord roomLockRecord) {
		return getDefaultResponse().addData(roomLockRecordService.modify(roomLockRecord));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		roomLockRecordService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<RoomLockRecord>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<RoomLockRecord>> rep = new HttpResponse<PageResponse<RoomLockRecord>>();
		PageRequest<RoomLockRecord> req = parse2PageRequest(request);
		return rep.addData(roomLockRecordService.listPage(req));
	}

}
