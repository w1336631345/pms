package com.kry.pms.api.msg;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.msg.MsgAccount;
import com.kry.pms.model.persistence.org.Department;
import com.kry.pms.service.msg.MsgAccountService;
import com.kry.pms.service.org.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/msg/msgAccount")
public class MsgAccountController extends BaseController<MsgAccount> {
	@Autowired
	MsgAccountService msgAccountService;
	@PostMapping
	public HttpResponse<MsgAccount> add(@RequestBody MsgAccount msgAccount) {
		return getDefaultResponse().addData(msgAccountService.add(msgAccount));
	}
	@PostMapping(path = "/add")
	public HttpResponse addTo(@RequestBody MsgAccount msgAccount) {
		HttpResponse hr = msgAccountService.addTo(msgAccount);
		return hr;
	}

	@PutMapping
	public HttpResponse<MsgAccount> modify(@RequestBody MsgAccount msgAccount) {
		return getDefaultResponse().addData(msgAccountService.modify(msgAccount));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		msgAccountService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<MsgAccount>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<MsgAccount>> rep = new HttpResponse<PageResponse<MsgAccount>>();
		PageRequest<MsgAccount> req = parse2PageRequest(request);
		return rep.addData(msgAccountService.listPage(req));
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<MsgAccount>> getByCustomerId(){
		HttpResponse<List<MsgAccount>> rep = new HttpResponse<List<MsgAccount>>();
		List<MsgAccount> list = msgAccountService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

	/**
	 * 功能描述: <br>查询账户余额信息
	 * 〈〉
	 * @Param: []
	 * @Return: com.kry.pms.base.HttpResponse
	 * @Author: huanghaibin
	 * @Date: 2020/12/22 15:17
	 */
	@GetMapping(path = "/getBalance")
	public HttpResponse getBalance(){
		HttpResponse rep = msgAccountService.getBalance(getCurrentHotleCode());
		return rep;
	}

}
