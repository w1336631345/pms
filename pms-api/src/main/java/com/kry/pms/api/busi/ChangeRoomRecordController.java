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
import com.kry.pms.model.persistence.busi.ChangeRoomRecord;
import com.kry.pms.service.busi.ChangeRoomRecordService;

@RestController
@RequestMapping(path = "/api/v1/busi/changeRoomRecord")
public class ChangeRoomRecordController extends BaseController<ChangeRoomRecord> {
	@Autowired
	ChangeRoomRecordService changeRoomRecordService;
	@PostMapping
	public HttpResponse<ChangeRoomRecord> add(@RequestBody ChangeRoomRecord changeRoomRecord) {
		return getDefaultResponse().addData(changeRoomRecordService.add(changeRoomRecord));
	}

	@PutMapping
	public HttpResponse<ChangeRoomRecord> modify(@RequestBody ChangeRoomRecord changeRoomRecord) {
		return getDefaultResponse().addData(changeRoomRecordService.modify(changeRoomRecord));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		changeRoomRecordService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<ChangeRoomRecord>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<ChangeRoomRecord>> rep = new HttpResponse<PageResponse<ChangeRoomRecord>>();
		PageRequest<ChangeRoomRecord> req = parse2PageRequest(request);
		return rep.addData(changeRoomRecordService.listPage(req));
	}

}
