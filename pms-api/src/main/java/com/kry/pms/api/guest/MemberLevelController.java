package com.kry.pms.api.guest;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.guest.MemberLevel;
import com.kry.pms.service.guest.MemberLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/guest/memberLevel")
public class MemberLevelController extends BaseController<MemberLevel> {
	@Autowired
	MemberLevelService memberLevelService;
	@PostMapping
	public HttpResponse<MemberLevel> add(@RequestBody MemberLevel memberLevel) {
		memberLevel.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(memberLevelService.add(memberLevel));
	}

	@PutMapping
	public HttpResponse<MemberLevel> modify(@RequestBody MemberLevel memberIntegralTypeInfo) {
		return getDefaultResponse().addData(memberLevelService.modify(memberIntegralTypeInfo));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		memberLevelService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<MemberLevel>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<MemberLevel>> rep = new HttpResponse<PageResponse<MemberLevel>>();
		PageRequest<MemberLevel> req = parse2PageRequest(request);
		return rep.addData(memberLevelService.listPage(req));
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<MemberLevel>> getByHotelCode(HttpServletRequest request){
		HttpResponse<List<MemberLevel>> rep = new HttpResponse<List<MemberLevel>>();
		List<MemberLevel> list = memberLevelService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

}
