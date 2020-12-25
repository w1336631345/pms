package com.kry.pms.api.msg;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.msg.MsgRecords;
import com.kry.pms.service.msg.MsgRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/msg/msgRecords")
public class MsgRecordsController extends BaseController<MsgRecords> {
	@Autowired
	MsgRecordsService msgRecordsService;
	@PostMapping
	public HttpResponse<MsgRecords> add(@RequestBody MsgRecords msgRecords) {
		return getDefaultResponse().addData(msgRecordsService.add(msgRecords));
	}

	@PutMapping
	public HttpResponse<MsgRecords> modify(@RequestBody MsgRecords msgRecords) {
		return getDefaultResponse().addData(msgRecordsService.modify(msgRecords));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		msgRecordsService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<MsgRecords>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<MsgRecords>> rep = new HttpResponse<PageResponse<MsgRecords>>();
		PageRequest<MsgRecords> req = parse2PageRequest(request);
		return rep.addData(msgRecordsService.listPage(req));
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<MsgRecords>> getByCustomerId(){
		HttpResponse<List<MsgRecords>> rep = new HttpResponse<List<MsgRecords>>();
		List<MsgRecords> list = msgRecordsService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

}
