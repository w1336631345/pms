package com.kry.pms.api.guest;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.guest.MemberInfo;
import com.kry.pms.model.persistence.guest.MemberIntegral;
import com.kry.pms.service.guest.MemberInfoService;
import com.kry.pms.service.guest.MemberIntegralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/guest/memberIntegral")
public class MemberIntegralController extends BaseController<MemberIntegral> {
	@Autowired
	MemberIntegralService memberIntegralService;
	@PostMapping
	public HttpResponse<MemberIntegral> add(@RequestBody MemberIntegral memberIntegral) {
		memberIntegral.setHotelCode(getCurrentHotleCode());
		memberIntegral.setCreateDate(LocalDateTime.now());
		memberIntegral.setCreateUser(getUserId());
		return getDefaultResponse().addData(memberIntegralService.add(memberIntegral));
	}
	@PostMapping(path = "/reduce")
	public HttpResponse<MemberIntegral> reduce(@RequestBody MemberIntegral memberIntegral) {
		memberIntegral.setHotelCode(getCurrentHotleCode());
		memberIntegral.setCreateDate(LocalDateTime.now());
		memberIntegral.setCreateUser(getUserId());
		HttpResponse hr = memberIntegralService.reduce(memberIntegral);
		return hr;
	}

	@PutMapping
	public HttpResponse<MemberIntegral> modify(@RequestBody MemberIntegral memberIntegral) {
		return getDefaultResponse().addData(memberIntegralService.modify(memberIntegral));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		memberIntegralService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<MemberIntegral>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<MemberIntegral>> rep = new HttpResponse<PageResponse<MemberIntegral>>();
		PageRequest<MemberIntegral> req = parse2PageRequest(request);
		return rep.addData(memberIntegralService.listPage(req));
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<MemberIntegral>> getByHotelCode(String cardNum){
		HttpResponse<List<MemberIntegral>> rep = new HttpResponse<List<MemberIntegral>>();
		List<MemberIntegral> list = memberIntegralService.getList(getCurrentHotleCode(), cardNum);
		return rep.addData(list);
	}

}
