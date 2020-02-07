package com.kry.pms.api.guest;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.guest.VipType;
import com.kry.pms.service.guest.VipTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/guest/vipType")
public class VipTypeController extends BaseController<VipType> {
	@Autowired
	VipTypeService vipTypeService;
	@PostMapping
	public HttpResponse<VipType> add(@RequestBody VipType vipType) {
		vipType.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(vipTypeService.add(vipType));
	}

	@PutMapping
	public HttpResponse<VipType> modify(@RequestBody VipType vipType) {
		return getDefaultResponse().addData(vipTypeService.modify(vipType));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		vipTypeService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<VipType>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<VipType>> rep = new HttpResponse<PageResponse<VipType>>();
		PageRequest<VipType> req = parse2PageRequest(request);
		return rep.addData(vipTypeService.listPage(req));
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<VipType>> getByHotelCode(HttpServletRequest request){
		HttpResponse<List<VipType>> rep = new HttpResponse<List<VipType>>();
		List<VipType> list = vipTypeService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

}
