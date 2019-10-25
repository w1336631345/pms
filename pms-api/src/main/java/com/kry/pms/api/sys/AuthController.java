package com.kry.pms.api.sys;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.service.sys.AccountService;
import com.kry.pms.utils.MD5Utils;
@RestController
@RequestMapping(path="/auth")
public class AuthController {
	@Autowired
	AccountService accountService;

	@RequestMapping(path = "/admin/login", method = RequestMethod.POST)
	public HttpResponse<String> loginTest(String username, String password) {
		HttpResponse<String> response = new HttpResponse<>();
		password = MD5Utils.encrypt(username, password);
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			String id = (String)subject.getSession().getId();
			response.addData(id);
			return response.ok("登录成功");
		} catch (AuthenticationException e) {
			return response.error(1000,"用户或密码错误");
		}
	}

	@RequestMapping(path = "/admin/logout", method = RequestMethod.POST)
	public HttpResponse<String> logout() {
		HttpResponse<String> response = new HttpResponse<>();
		SecurityUtils.getSubject().logout();
		return response.ok("成功登出");
	}
	/**
     * 未登录，shiro应重定向到登录界面，此处返回未登录状态信息由前端控制跳转页面
     * @return
     */
    @RequestMapping(value = "/unauth")
    @ResponseBody
    public Object unauth() {
    	HttpResponse<String> response = new HttpResponse<>();
        return response.error(403, "未登录");
    }
}
