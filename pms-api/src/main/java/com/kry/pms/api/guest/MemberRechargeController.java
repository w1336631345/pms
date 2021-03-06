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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/guest/memberRecharge")
public class MemberRechargeController extends BaseController<MemberRecharge> {
	@Autowired
	MemberRechargeService memberRechargeService;
	@PostMapping
	public HttpResponse<MemberRecharge> add(@RequestBody MemberRecharge memberRecharge) {
		memberRecharge.setHotelCode(getCurrentHotleCode());
		memberRecharge.setCreateDate(LocalDateTime.now());
		memberRecharge.setCreateUser(getUserId());
		memberRecharge.setShiftCode(getShiftCode());
		return getDefaultResponse().addData(memberRechargeService.add(memberRecharge));
	}

	/**
	 * 功能描述: <br>充值
	 * 〈〉
	 * @Param: [memberRecharge]
	 * @Return: com.kry.pms.base.HttpResponse<com.kry.pms.model.persistence.guest.MemberRecharge>
	 * @Author: huanghaibin
	 * @Date: 2020/7/27 16:59
	 */
	@PostMapping(path = "/recharge")
	public HttpResponse<MemberRecharge> recharge(@RequestBody MemberRecharge memberRecharge) {
		HttpResponse hr = new HttpResponse();
		memberRecharge.setHotelCode(getCurrentHotleCode());
		memberRecharge.setCreateDate(LocalDateTime.now());
		memberRecharge.setCreateUser(getUserId());
		memberRecharge.setShiftCode(getShiftCode());
		hr = memberRechargeService.recharge(memberRecharge);
		return hr;
	}

	@PutMapping
	public HttpResponse<MemberRecharge> modify(@RequestBody MemberRecharge memberRecharge) {
		memberRecharge.setUpdateDate(LocalDateTime.now());
		memberRecharge.setUpdateUser(getUserId());
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
	/**
	 * 功能描述: <br>查询充值记录1
	 * 〈〉
	 * @Param: [cardNum]
	 * @Return: com.kry.pms.base.HttpResponse<java.util.List<com.kry.pms.model.persistence.guest.MemberRecharge>>
	 * @Author: huanghaibin
	 * @Date: 2021/3/6 9:39
	 */
	@GetMapping(path = "/cardNum")
	public HttpResponse<List<MemberRecharge>> getByHotelCodeAndCardNum(String cardNum){
		HttpResponse<List<MemberRecharge>> rep = new HttpResponse<List<MemberRecharge>>();
		List<MemberRecharge> list = memberRechargeService.getByHotelCodeAndCardNum(getCurrentHotleCode(), cardNum);
		return rep.addData(list);
	}
	/**
	 * 功能描述: <br>查询充值记录2
	 * 〈〉
	 * @Param: [memberId]
	 * @Return: com.kry.pms.base.HttpResponse<java.util.List<com.kry.pms.model.persistence.guest.MemberRecharge>>
	 * @Author: huanghaibin
	 * @Date: 2021/3/6 9:43
	 */
	@GetMapping(path = "/cardNum2")
	public HttpResponse<List<MemberRecharge>> cardNum2(String memberId){
		HttpResponse<List<MemberRecharge>> rep = new HttpResponse<List<MemberRecharge>>();
		List<MemberRecharge> list = memberRechargeService.findByMemberInfoId(memberId);
		return rep.addData(list);
	}

}
