package com.kry.pms.api.sys;

import com.kry.pms.base.HttpResponse;
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

//	@RequestMapping(path = "/admin/login", method = RequestMethod.POST)
//	public HttpResponse<String> loginAdmin(String username, String password) {
//		HttpResponse<String> response = new HttpResponse<>();
//	//	Account account = accountService.findTopByMobileOrUsername(username);
//		return response;
//	}
	
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
	//	Account account = accountService.findTopByMobileOrUsername(username);
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


	@GetMapping("/sessionList")
	@ResponseBody
	public HttpResponse<List<UserOnlineVO>> sessionList(){
        User loginUser = ShiroUtils.getUser();
        Hotel hotel = hotelService.getByHotelCode(loginUser.getHotelCode());
        List<User> listUser = userService.getAllByHotelCode(loginUser.getHotelCode());
        boolean isHotelUser = false;
		List<UserOnlineVO> list = new ArrayList<>();
		HttpResponse<List<UserOnlineVO>> rep = new HttpResponse<>();
		Collection<Session> sessions =  sessionDAO.getActiveSessions();
		for (Session session : sessions) {
			UserOnlineVO userOnlineVO = new UserOnlineVO();
			User user;
			SimplePrincipalCollection principalCollection;
			if(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY )== null){
				continue;
			}else {
				principalCollection = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
				user = (User) principalCollection.getPrimaryPrincipal();
				//将同旅馆里的在线用户踢出，剩下全是离线用户，在add到List<UserOnlineVO>就是全部在线和离线用户
//				if(listUser.contains(user)){
//                    listUser.remove(user);
//                }
				if(loginUser.getHotelCode().equals(user.getHotelCode())){
                    isHotelUser = true;
                }
				if(isHotelUser){
				    if(loginUser.getId().equals(user.getId())){
                        userOnlineVO.setIsMyself("本机");
                    }
                    userOnlineVO.setHotelName(hotel.getName());
                    userOnlineVO.setUsername(user.getUsername());
                    userOnlineVO.setUserId(user.getId());
                    userOnlineVO.setPhone(user.getMobile());
                    userOnlineVO.setNickename(user.getNickname());
                }
			}
			if(isHotelUser){
                userOnlineVO.setHost(session.getHost());
                userOnlineVO.setSessionId((String) session.getId());
                userOnlineVO.setStartAccessTime(session.getStartTimestamp());
                userOnlineVO.setLastAccessTime(session.getLastAccessTime());
                list.add(userOnlineVO);
            }
		}
		rep.addData(list);
		return rep;
	}

    @GetMapping("/sessionLoginOut")
    @ResponseBody
    public HttpResponse sessionLoginOut(String sessionId){
        HttpResponse rep = new HttpResponse();
        Session session = sessionDAO.readSession(sessionId);
        sessionDAO.delete(session);
        return rep.ok();
    }
}
