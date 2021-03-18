package com.kry.pms.api.guest;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.MemberInfoDao;
import com.kry.pms.model.persistence.guest.MemberInfo;
import com.kry.pms.service.guest.MemberInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/guest/memberInfo")
public class MemberInfoController extends BaseController<MemberInfo> {
	@Autowired
	MemberInfoService memberInfoService;
	@Autowired
	MemberInfoDao memberInfoDao;

	@PostMapping
	public HttpResponse<MemberInfo> add(@RequestBody MemberInfo memberInfo) {
		memberInfo.setHotelCode(getCurrentHotleCode());
		memberInfo.setCreateDate(LocalDateTime.now());
		memberInfo.setCreateUser(getUserId());
		memberInfo.setOperator(getCurrentEmployee().getName());

		// 先判重，先判断物理卡号是否存在（手动输入的），然后判断系统卡号是否存在（生成的）
		List<MemberInfo>  macList = memberInfoDao.findByHotelCodeAndMacNum(memberInfo.getHotelCode(),memberInfo.getMacNum());
		if (null != macList && 0 != macList.size()){
			return getDefaultResponse().error("物理卡号已存在，请重新输入");
		}


		MemberInfo result = memberInfoService.add(memberInfo);
		if (null == result){
			return  getDefaultResponse().error("系统卡号已存在，请重新输入");
		}else{
			return getDefaultResponse().addData(result);
		}
	}

	@PutMapping
	public HttpResponse<MemberInfo> modify(@RequestBody MemberInfo memberInfo) {

		// 选择了密码校验，则密码必填
		if (null != memberInfo.getIsUsedPassword() && "T".equals(memberInfo.getIsUsedPassword())
				&& (null == memberInfo.getPassword() || "".equals(memberInfo.getPassword()))){
			HttpResponse hr = new HttpResponse();
			 return hr.error("选择了密码校验请填写密码!");
		}

		// 判断物理卡号是否存在，存在则不允许修改
		List<MemberInfo>  macList = memberInfoDao.findByHotelCodeAndMacNum(memberInfo.getHotelCode(),memberInfo.getMacNum());
		if (null != macList && 0 != macList.size() && macList.size() > 1){  // 有两条以上重复记录
			return getDefaultResponse().error("物理卡号已存在，请重新输入");
		}else if (null != macList && 0 != macList.size() && macList.size() == 1){  //有一条记录，判断是否是它自己，不是的话则不允许修改
			if (!memberInfo.getId().equals(macList.get(0).getId())){  //存在的这条记录不是他自己
				return getDefaultResponse().error("物理卡号已存在，请重新输入");
			}
		}

        memberInfo.setUpdateDate(LocalDateTime.now());
        memberInfo.setUpdateUser(getCurrentUserId());
		return getDefaultResponse().addData(memberInfoService.modify(memberInfo));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		memberInfoService.delete(id);
		return rep;
	}

	@GetMapping(path = "/isPassword")
	public HttpResponse<Boolean> delete(String id, String password) {
		HttpResponse<Boolean> rep = new HttpResponse<>();
		Boolean b = memberInfoService.findByIdAndPassword(id, password);
		rep.setData(b);
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
    @GetMapping(path = "/findByCustomer")
    public HttpResponse<List<MemberInfo>> findByCustomer(String customerId){
        HttpResponse<List<MemberInfo>> rep = new HttpResponse<List<MemberInfo>>();
        List<MemberInfo> list = memberInfoService.findByCustomer(customerId);
        return rep.addData(list);
    }
	/**
	 * 功能描述: <br>会员列表-条件查询
	 * 〈〉
	 * @Param: [type, isUsed, moreParams]
	 * @Return: com.kry.pms.base.HttpResponse<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
	 * @Author: huanghaibin
	 * @Date: 2020/7/29 16:25
	 */
	@GetMapping(path = "/parmsList")
	public HttpResponse<List<MemberInfo>> parmsList(String type, String isUsed, String moreParams){
		HttpResponse<List<MemberInfo>> rep = new HttpResponse<List<MemberInfo>>();
		List<MemberInfo> list = memberInfoService.parmsList(getCurrentHotleCode(), type, isUsed, moreParams);
		return rep.addData(list);
	}

	/**
	 * 功能描述: <br>新办会员报表-列表
	 * 〈〉
	 * @Param: [createDate]
	 * @Return: com.kry.pms.base.HttpResponse<java.util.List<com.kry.pms.model.persistence.guest.MemberInfo>>
	 * @Author: huanghaibin
	 * @Date: 2020/7/28 14:47
	 */
	@GetMapping(path = "/memberReport")
	public HttpResponse<List<MemberInfo>> memberReport(String createDate){
		HttpResponse<List<MemberInfo>> rep = new HttpResponse<List<MemberInfo>>();
		List<MemberInfo> list = memberInfoService.getByCreateDate(getCurrentHotleCode(), createDate);
		return rep.addData(list);
	}
	/**
	 * 功能描述: <br>今日新办会员数量
	 * 〈〉
	 * @Param: [createDate]
	 * @Return: com.kry.pms.base.HttpResponse<java.lang.Integer>
	 * @Author: huanghaibin
	 * @Date: 2020/8/11 16:02
	 */
	@GetMapping(path = "/createDateCount")
	public HttpResponse<Integer> createDateCount(String createDate){
		HttpResponse<Integer> rep = new HttpResponse<Integer>();
		Integer count = memberInfoService.getByCreateDateCount(getCurrentHotleCode(), createDate);
		return rep.addData(count);
	}
	/**
	 * 功能描述: <br>新办会员报表-统计
	 * 〈〉
	 * @Param: [createDate]
	 * @Return: com.kry.pms.base.HttpResponse<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
	 * @Author: huanghaibin
	 * @Date: 2020/7/28 14:47
	 */
	@GetMapping(path = "/memberReportCount")
	public HttpResponse<List<Map<String, Object>>> memberReportCount(String createDate){
		HttpResponse<List<Map<String, Object>>> rep = new HttpResponse<List<Map<String, Object>>>();
		List<Map<String, Object>> list = memberInfoService.countByCreateUser(getCurrentHotleCode(), createDate);
		return rep.addData(list);
	}

	/**
	 * 功能描述: <br>会员过期报表
	 * 〈〉
	 * @Param: [createDate]
	 * @Return: com.kry.pms.base.HttpResponse<java.util.List<com.kry.pms.model.persistence.guest.MemberInfo>>
	 * @Author: huanghaibin
	 * @Date: 2020/7/28 15:25
	 */
	@GetMapping(path = "/byParamsList")
	public HttpResponse<List<MemberInfo>> byParamsList(String startTime, String endTime, String birthDay){
		HttpResponse<List<MemberInfo>> rep = new HttpResponse<List<MemberInfo>>();
		List<MemberInfo> list = memberInfoService.byParamsList(getCurrentHotleCode(), startTime, endTime, birthDay);
		return rep.addData(list);
	}

	/**
	 * 功能描述: <br>会员充值报表
	 * 〈〉
	 * @Param: [rechargeDate]
	 * @Return: com.kry.pms.base.HttpResponse<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
	 * @Author: huanghaibin
	 * @Date: 2020/7/28 16:21
	 */
	@GetMapping(path = "/rechargeReport")
	public HttpResponse<List<Map<String, Object>>> rechargeReport(String rechargeDate){
		HttpResponse<List<Map<String, Object>>> rep = new HttpResponse<List<Map<String, Object>>>();
		List<Map<String, Object>> list = memberInfoService.rechargeReport(getCurrentHotleCode(), rechargeDate);
		return rep.addData(list);
	}
	/**
	 * 功能描述: <br>会员积分报表
	 * 〈〉
	 * @Param: [consDate]
	 * @Return: com.kry.pms.base.HttpResponse<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
	 * @Author: huanghaibin
	 * @Date: 2020/7/28 16:33
	 */
	@GetMapping(path = "/integralReport")
	public HttpResponse<List<Map<String, Object>>> integralReport(String consDate){
		HttpResponse<List<Map<String, Object>>> rep = new HttpResponse<List<Map<String, Object>>>();
		List<Map<String, Object>> list = memberInfoService.integralReport(getCurrentHotleCode(), consDate);
		return rep.addData(list);
	}
	/**
	 * 功能描述: <br>客户档案按条件搜索会员
	 * 〈〉
	 * @Param: [pageNum, pageSize, name, mobile, cardNum, idCardNum]
	 * @Return: com.kry.pms.base.HttpResponse<com.kry.pms.base.PageResponse<com.kry.pms.model.persistence.guest.MemberInfo>>
	 * @Author: huanghaibin
	 * @Date: 2020/7/29 15:26
	 */
	@GetMapping(value = "/getParmsList")
	public HttpResponse<List<MemberInfo>> getParmsList(String name, String mobile,String cardNum, String idCardNum,String isUsed){
		HttpResponse<List<MemberInfo>> rep = new HttpResponse<>();
		List<MemberInfo> list = memberInfoService.getParmsList(name, mobile, cardNum, idCardNum, getCurrentHotleCode(),isUsed);
		rep.addData(list);
		return rep;

	}

	/**
	 * 功能描述: <br>手动计算过期积分、金额
	 * 〈〉
	 * @Param: [request]
	 * @Return: com.kry.pms.base.HttpResponse
	 * @Author: huanghaibin
	 * @Date: 2020/7/24 17:27
	 */
	@GetMapping(path = "/boOverdueList")
	public HttpResponse boOverdueList(HttpServletRequest request){
		HttpResponse hr = new HttpResponse();
		memberInfoService.boOverdueList(getCurrentHotleCode());
		return hr.ok();
	}

	@GetMapping(path = "/accountId")
	public HttpResponse findByAccountId(String accountId){
		HttpResponse hr = new HttpResponse();
		MemberInfo memberInfo = memberInfoService.findByAccountId(getCurrentHotleCode(), accountId);
		hr.setData(memberInfo);
		return hr;
	}

	/**
	 * 功能描述: <br>发送短信查询的会员列表
	 * 〈〉
	 * @Param: [tDay, sDay, leavelIds]
	 * @Return: com.kry.pms.base.HttpResponse<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
	 * @Author: huanghaibin
	 * @Date: 2020/12/24 14:48
	 */
	@GetMapping(path = "/getSendMsgList")
	public HttpResponse<List<Map<String, Object>>> getSendMsgList(String tDay, String sDay, String[] leavelIds){
		HttpResponse<List<Map<String, Object>>> rep = new HttpResponse<List<Map<String, Object>>>();
		List<Map<String, Object>> list = memberInfoService.getSendMsgList(getCurrentHotleCode(), tDay, sDay, leavelIds);
		return rep.addData(list);
	}

}
