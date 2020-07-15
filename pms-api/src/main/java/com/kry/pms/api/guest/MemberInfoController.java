package com.kry.pms.api.guest;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.guest.MemberInfo;
import com.kry.pms.service.guest.MemberInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/guest/memberInfo")
public class MemberInfoController extends BaseController<MemberInfo> {
	@Autowired
	MemberInfoService memberInfoService;
	@PostMapping
	public HttpResponse<MemberInfo> add(@RequestBody MemberInfo memberInfo) {
		memberInfo.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(memberInfoService.add(memberInfo));
	}

	@PutMapping
	public HttpResponse<MemberInfo> modify(@RequestBody MemberInfo memberInfo) {
		return getDefaultResponse().addData(memberInfoService.modify(memberInfo));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		memberInfoService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<MemberInfo>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<MemberInfo>> rep = new HttpResponse<PageResponse<MemberInfo>>();
		PageRequest<MemberInfo> req = parse2PageRequest(request);
		return rep.addData(memberInfoService.listPage(req));
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<MemberInfo>> getByHotelCode(HttpServletRequest request){
		HttpResponse<List<MemberInfo>> rep = new HttpResponse<List<MemberInfo>>();
		List<MemberInfo> list = memberInfoService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

}
