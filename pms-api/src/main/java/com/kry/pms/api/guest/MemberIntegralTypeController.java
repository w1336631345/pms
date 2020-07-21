package com.kry.pms.api.guest;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.guest.MemberIntegral;
import com.kry.pms.model.persistence.guest.MemberIntegralType;
import com.kry.pms.service.guest.MemberIntegralService;
import com.kry.pms.service.guest.MemberIntegralTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/guest/memberIntegralType")
public class MemberIntegralTypeController extends BaseController<MemberIntegralType> {
	@Autowired
	MemberIntegralTypeService memberIntegralTypeService;
	@PostMapping
	public HttpResponse<MemberIntegralType> add(@RequestBody MemberIntegralType memberIntegralType) {
		memberIntegralType.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(memberIntegralTypeService.addAll(memberIntegralType));
	}

	@PutMapping
	public HttpResponse<MemberIntegralType> modify(@RequestBody MemberIntegralType memberIntegralType) {
		return getDefaultResponse().addData(memberIntegralTypeService.modify(memberIntegralType));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		memberIntegralTypeService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<MemberIntegralType>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<MemberIntegralType>> rep = new HttpResponse<PageResponse<MemberIntegralType>>();
		PageRequest<MemberIntegralType> req = parse2PageRequest(request);
		return rep.addData(memberIntegralTypeService.listPage(req));
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<MemberIntegralType>> getByHotelCode(HttpServletRequest request){
		HttpResponse<List<MemberIntegralType>> rep = new HttpResponse<List<MemberIntegralType>>();
		List<MemberIntegralType> list = memberIntegralTypeService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

}
