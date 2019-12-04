package com.kry.pms.api.sys;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.service.org.EmployeeService;
import com.kry.pms.service.sys.AccountService;

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

	@GetMapping
	public HttpResponse<PageResponse<Account>> query(HttpServletRequest request)
			throws InstantiationException, IllegalAccessException {
		HttpResponse<PageResponse<Account>> rep = new HttpResponse<PageResponse<Account>>();
		PageRequest<Account> req = parse2PageRequest(request);
		return rep.addData(accountService.listPage(req));
	}

}
