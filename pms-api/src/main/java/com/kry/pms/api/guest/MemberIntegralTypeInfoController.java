package com.kry.pms.api.guest;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.guest.MemberIntegralType;
import com.kry.pms.model.persistence.guest.MemberIntegralTypeInfo;
import com.kry.pms.service.guest.MemberIntegralTypeInfoService;
import com.kry.pms.service.guest.MemberIntegralTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/guest/memberIntegralTypeInfo")
public class MemberIntegralTypeInfoController extends BaseController<MemberIntegralTypeInfo> {
	@Autowired
	MemberIntegralTypeInfoService memberIntegralTypeInfoService;
	@PostMapping
	public HttpResponse<MemberIntegralTypeInfo> add(@RequestBody MemberIntegralTypeInfo memberIntegralTypeInfo) {
		memberIntegralTypeInfo.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(memberIntegralTypeInfoService.add(memberIntegralTypeInfo));
	}

	@PutMapping
	public HttpResponse<MemberIntegralTypeInfo> modify(@RequestBody MemberIntegralTypeInfo memberIntegralTypeInfo) {
		return getDefaultResponse().addData(memberIntegralTypeInfoService.modify(memberIntegralTypeInfo));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		memberIntegralTypeInfoService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<MemberIntegralTypeInfo>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<MemberIntegralTypeInfo>> rep = new HttpResponse<PageResponse<MemberIntegralTypeInfo>>();
		PageRequest<MemberIntegralTypeInfo> req = parse2PageRequest(request);
		return rep.addData(memberIntegralTypeInfoService.listPage(req));
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<MemberIntegralTypeInfo>> getByHotelCode(HttpServletRequest request){
		HttpResponse<List<MemberIntegralTypeInfo>> rep = new HttpResponse<List<MemberIntegralTypeInfo>>();
		List<MemberIntegralTypeInfo> list = memberIntegralTypeInfoService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

}
