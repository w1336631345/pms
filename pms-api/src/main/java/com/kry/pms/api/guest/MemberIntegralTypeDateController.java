package com.kry.pms.api.guest;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.guest.MemberIntegralTypeDate;
import com.kry.pms.model.persistence.guest.MemberIntegralTypeInfo;
import com.kry.pms.service.guest.MemberIntegralTypeDateService;
import com.kry.pms.service.guest.MemberIntegralTypeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/guest/memberIntegralTypeDate")
public class MemberIntegralTypeDateController extends BaseController<MemberIntegralTypeDate> {
	@Autowired
	MemberIntegralTypeDateService memberIntegralTypeDateService;
	@PostMapping
	public HttpResponse<MemberIntegralTypeDate> add(@RequestBody MemberIntegralTypeDate memberIntegralTypeDate) {
		memberIntegralTypeDate.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(memberIntegralTypeDateService.add(memberIntegralTypeDate));
	}

	@PutMapping
	public HttpResponse<MemberIntegralTypeDate> modify(@RequestBody MemberIntegralTypeDate memberIntegralTypeDate) {
		return getDefaultResponse().addData(memberIntegralTypeDateService.modify(memberIntegralTypeDate));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		memberIntegralTypeDateService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<MemberIntegralTypeDate>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<MemberIntegralTypeDate>> rep = new HttpResponse<PageResponse<MemberIntegralTypeDate>>();
		PageRequest<MemberIntegralTypeDate> req = parse2PageRequest(request);
		return rep.addData(memberIntegralTypeDateService.listPage(req));
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<MemberIntegralTypeDate>> getByHotelCode(HttpServletRequest request){
		HttpResponse<List<MemberIntegralTypeDate>> rep = new HttpResponse<List<MemberIntegralTypeDate>>();
		List<MemberIntegralTypeDate> list = memberIntegralTypeDateService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

}
