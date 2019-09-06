package com.kry.pms.api.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.service.sys.AccountService;
@RestController
@RequestMapping(path="/auth")
public class AuthController {
	@Autowired
	AccountService accountService;

	@RequestMapping(path = "/admin/login", method = RequestMethod.POST)
	public HttpResponse<String> loginAdmin(String username, String password) {
		HttpResponse<String> response = new HttpResponse<>();
	//	Account account = accountService.findTopByMobileOrUsername(username);
		return response;
	}

	@RequestMapping(path = "/admin/logout", method = RequestMethod.POST)
	public HttpResponse<String> logout() {
		HttpResponse<String> response = new HttpResponse<>();
		return response;
	}
}
