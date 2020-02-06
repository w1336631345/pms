package com.kry.pms.api.guest;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.guest.ProtocolType;
import com.kry.pms.model.persistence.guest.VipType;
import com.kry.pms.service.guest.ProtocolTypeService;
import com.kry.pms.service.guest.VipTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api/v1/guest/protocolType")
public class ProtocolTypeController extends BaseController<ProtocolType> {
	@Autowired
	ProtocolTypeService protocolTypeService;
	@PostMapping
	public HttpResponse<ProtocolType> add(@RequestBody ProtocolType vipType) {
		vipType.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(protocolTypeService.add(vipType));
	}

	@PutMapping
	public HttpResponse<ProtocolType> modify(@RequestBody ProtocolType vipType) {
		return getDefaultResponse().addData(protocolTypeService.modify(vipType));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		protocolTypeService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<ProtocolType>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<ProtocolType>> rep = new HttpResponse<PageResponse<ProtocolType>>();
		PageRequest<ProtocolType> req = parse2PageRequest(request);
		return rep.addData(protocolTypeService.listPage(req));
	}

}
