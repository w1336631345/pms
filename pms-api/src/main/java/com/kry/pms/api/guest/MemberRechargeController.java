package com.kry.pms.api.guest;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.guest.MemberInfo;
import com.kry.pms.model.persistence.guest.MemberRecharge;
import com.kry.pms.service.guest.MemberInfoService;
import com.kry.pms.service.guest.MemberRechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/guest/memberRecharge")
public class MemberRechargeController extends BaseController<MemberRecharge> {
	@Autowired
	MemberRechargeService memberRechargeService;
	@PostMapping
	public HttpResponse<MemberRecharge> add(@RequestBody MemberRecharge memberRecharge) {
		memberRecharge.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(memberRechargeService.add(memberRecharge));
	}

	@PutMapping
	public HttpResponse<MemberRecharge> modify(@RequestBody MemberRecharge memberRecharge) {
		return getDefaultResponse().addData(memberRechargeService.modify(memberRecharge));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		memberRechargeService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<MemberRecharge>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<MemberRecharge>> rep = new HttpResponse<PageResponse<MemberRecharge>>();
		PageRequest<MemberRecharge> req = parse2PageRequest(request);
		return rep.addData(memberRechargeService.listPage(req));
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<MemberRecharge>> getByHotelCode(HttpServletRequest request){
		HttpResponse<List<MemberRecharge>> rep = new HttpResponse<List<MemberRecharge>>();
//		List<MemberRecharge> list = memberRechargeService.getAllByHotelCode(getCurrentHotleCode());
		List<MemberRecharge> list = memberRechargeService.getList(getCurrentHotleCode());
		return rep.addData(list);
	}

}
