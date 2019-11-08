package com.kry.pms.api.sys;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.controller.SessionController;
import com.kry.pms.model.http.response.sys.UserOnlineVO;
import com.kry.pms.model.persistence.org.Hotel;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.org.HotelService;
import com.kry.pms.service.sys.AccountService;
import com.kry.pms.service.sys.UserService;
import com.kry.pms.utils.MD5Utils;
import com.kry.pms.utils.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping(path="/auth")
public class AuthController {
	@Autowired
	AccountService accountService;
	@Autowired
	SessionDAO sessionDAO;
	@Autowired
    HotelService hotelService;
	@Autowired
    UserService userService;
	@Autowired
	SessionController sessionController;

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

	/**
	 * 功能描述: <br>查询所有在线用户
	 * 〈〉
	 * @Param: []
	 * @Return: com.kry.pms.base.HttpResponse<java.util.List<com.kry.pms.model.http.response.sys.UserOnlineVO>>
	 * @Author: huanghaibin
	 * @Date: 2019/10/25 17:26
	 */
	@GetMapping("/sessionList")
	@ResponseBody
	public HttpResponse<List<UserOnlineVO>> sessionList(){
		List<UserOnlineVO> list = sessionController.getSysLoginUser();
		HttpResponse<List<UserOnlineVO>> rep = new HttpResponse<>();
		rep.addData(list);
		return rep;
	}

	/**
	 * 功能描述: <br>下线在线用户
	 * 〈〉
	 * @Param: [sessionId]
	 * @Return: com.kry.pms.base.HttpResponse
	 * @Author: huanghaibin
	 * @Date: 2019/10/25 17:26
	 */
    @GetMapping("/sessionLoginOut")
    @ResponseBody
    public HttpResponse sessionLoginOut(String sessionId){
        HttpResponse rep = sessionController.sessionLoginOut(sessionId);
        return rep;
    }
}
