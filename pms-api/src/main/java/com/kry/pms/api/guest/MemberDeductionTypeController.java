package com.kry.pms.api.guest;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.guest.MemberDeductionType;
import com.kry.pms.model.persistence.guest.MemberIntegralTypeInfo;
import com.kry.pms.service.guest.MemberDeductionTypeService;
import com.kry.pms.service.guest.MemberIntegralTypeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/guest/memberDeductionType")
public class MemberDeductionTypeController extends BaseController<MemberDeductionType> {
	@Autowired
	MemberDeductionTypeService memberDeductionTypeService;
	@PostMapping
	public HttpResponse<MemberDeductionType> add(@RequestBody MemberDeductionType memberDeductionType) {
		memberDeductionType.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(memberDeductionTypeService.add(memberDeductionType));
	}

	@PutMapping
	public HttpResponse<MemberDeductionType> modify(@RequestBody MemberDeductionType memberDeductionType) {
		return getDefaultResponse().addData(memberDeductionTypeService.modify(memberDeductionType));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		memberDeductionTypeService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<MemberDeductionType>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<MemberDeductionType>> rep = new HttpResponse<PageResponse<MemberDeductionType>>();
		PageRequest<MemberDeductionType> req = parse2PageRequest(request);
		return rep.addData(memberDeductionTypeService.listPage(req));
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<MemberDeductionType>> getByHotelCode(HttpServletRequest request){
		HttpResponse<List<MemberDeductionType>> rep = new HttpResponse<List<MemberDeductionType>>();
		List<MemberDeductionType> list = memberDeductionTypeService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

}
