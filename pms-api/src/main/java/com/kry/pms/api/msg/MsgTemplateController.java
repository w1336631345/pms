package com.kry.pms.api.msg;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.msg.MsgAccount;
import com.kry.pms.model.persistence.msg.MsgTemplate;
import com.kry.pms.service.msg.MsgAccountService;
import com.kry.pms.service.msg.MsgTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/msg/msgTemplate")
public class MsgTemplateController extends BaseController<MsgTemplate> {
	@Autowired
	MsgTemplateService msgTemplateService;
	@PostMapping
	public HttpResponse<MsgTemplate> add(@RequestBody MsgTemplate msgTemplate) {
		return getDefaultResponse().addData(msgTemplateService.add(msgTemplate));
	}

	@PutMapping
	public HttpResponse<MsgTemplate> modify(@RequestBody MsgTemplate msgTemplate) {
		return getDefaultResponse().addData(msgTemplateService.modify(msgTemplate));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		msgTemplateService.deleteTrue(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<MsgTemplate>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<MsgTemplate>> rep = new HttpResponse<PageResponse<MsgTemplate>>();
		PageRequest<MsgTemplate> req = parse2PageRequest(request);
		return rep.addData(msgTemplateService.listPage(req));
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<MsgTemplate>> getByCustomerId(){
		HttpResponse<List<MsgTemplate>> rep = new HttpResponse<List<MsgTemplate>>();
		List<MsgTemplate> list = msgTemplateService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

}
