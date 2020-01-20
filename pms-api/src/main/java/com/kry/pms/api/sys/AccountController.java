package com.kry.pms.api.sys;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.DtoResponse;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.http.response.busi.SettleInfoVo;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.service.org.EmployeeService;
import com.kry.pms.service.sys.AccountService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/sys/account")
public class AccountController extends BaseController<Account> {
	@Autowired
	AccountService accountService;
	@Autowired
	EmployeeService employeeService;

	@PostMapping
	public HttpResponse<Account> add(@RequestBody Account account) {
		return getDefaultResponse().addData(accountService.add(account));
	}

	@PutMapping
	public HttpResponse<Account> modify(@RequestBody Account account) {
		return getDefaultResponse().addData(accountService.modify(account));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		accountService.delete(id);
		return rep;
	}

	@GetMapping(path = "/personPrice")
	public HttpResponse<Double> queryRoomPrice(String id) {
		HttpResponse<Double> rep = new HttpResponse<>();
		DtoResponse<Double> response = accountService.queryRoomPrice(id);
		BeanUtils.copyProperties(response, rep);
		return rep;
	}

	@GetMapping("/settleInfo/{type}/{id}")
	public HttpResponse<SettleInfoVo> getSettleInfo(@PathVariable String type,@PathVariable String id) {
		 HttpResponse<SettleInfoVo> response = new HttpResponse<SettleInfoVo>();
		 SettleInfoVo settleInfoVo = accountService.getSettleInfo(type,id);
		 return response.addData(settleInfoVo);
	}

	@GetMapping
	public HttpResponse<PageResponse<Account>> query(HttpServletRequest request)
			throws InstantiationException, IllegalAccessException {
		HttpResponse<PageResponse<Account>> rep = new HttpResponse<PageResponse<Account>>();
		PageRequest<Account> req = parse2PageRequest(request);
		return rep.addData(accountService.listPage(req));
	}

	/**
	 * 功能描述: <br>查询包价所需的入账账户
	 * 〈〉
	 * @Param: [request]
	 * @Return: com.kry.pms.base.HttpResponse<java.util.List<com.kry.pms.model.persistence.sys.Account>>
	 * @Author: huanghaibin
	 * @Date: 2020/1/20 16:35
	 */
	@GetMapping(path = "/getInner")
	public HttpResponse<List<Account>> list(HttpServletRequest request){
		HttpResponse<List<Account>> rep = new HttpResponse<List<Account>>();
		return rep.addData(accountService.findByHotelCodeAndType(getCurrentHotleCode()));
	}


}
