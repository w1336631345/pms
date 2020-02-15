package com.kry.pms.api.guest;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.guest.MemberType;
import com.kry.pms.model.persistence.guest.VipType;
import com.kry.pms.service.guest.MemberTypeService;
import com.kry.pms.service.guest.VipTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/guest/memberType")
public class MemberTypeController extends BaseController<MemberType> {
	@Autowired
	MemberTypeService memberTypeService;
	@PostMapping
	public HttpResponse<MemberType> add(@RequestBody MemberType memberType) {
		memberType.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(memberTypeService.add(memberType));
	}

	@PutMapping
	public HttpResponse<MemberType> modify(@RequestBody MemberType memberType) {
		return getDefaultResponse().addData(memberTypeService.modify(memberType));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		memberTypeService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<MemberType>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<MemberType>> rep = new HttpResponse<PageResponse<MemberType>>();
		PageRequest<MemberType> req = parse2PageRequest(request);
		return rep.addData(memberTypeService.listPage(req));
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<MemberType>> getByHotelCode(HttpServletRequest request){
		HttpResponse<List<MemberType>> rep = new HttpResponse<List<MemberType>>();
		List<MemberType> list = memberTypeService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

}
