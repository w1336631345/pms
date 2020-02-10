package com.kry.pms.api.guest;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.guest.VipRoomType;
import com.kry.pms.model.persistence.guest.VipType;
import com.kry.pms.service.guest.VipRoomTypeService;
import com.kry.pms.service.guest.VipTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/guest/vipRoomType")
public class VipRoomTypeController extends BaseController<VipRoomType> {
	@Autowired
	VipRoomTypeService vipRoomTypeService;
	@PostMapping
	public HttpResponse<VipRoomType> add(@RequestBody VipRoomType vipRoomType) {
		vipRoomType.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(vipRoomTypeService.add(vipRoomType));
	}

	@PutMapping
	public HttpResponse<VipRoomType> modify(@RequestBody VipRoomType vipRoomType) {
		return getDefaultResponse().addData(vipRoomTypeService.modify(vipRoomType));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		vipRoomTypeService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<VipRoomType>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<VipRoomType>> rep = new HttpResponse<PageResponse<VipRoomType>>();
		PageRequest<VipRoomType> req = parse2PageRequest(request);
		return rep.addData(vipRoomTypeService.listPage(req));
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<VipRoomType>> getByHotelCode(HttpServletRequest request){
		HttpResponse<List<VipRoomType>> rep = new HttpResponse<List<VipRoomType>>();
		List<VipRoomType> list = vipRoomTypeService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

}
