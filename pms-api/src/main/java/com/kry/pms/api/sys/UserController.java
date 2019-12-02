package com.kry.pms.api.sys;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
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
import com.kry.pms.model.http.response.org.EmployeeSummaryVo;
import com.kry.pms.model.http.response.org.HotelInfoVo;
import com.kry.pms.model.http.response.org.HotelSummaryVo;
import com.kry.pms.model.http.response.sys.UserInfoVo;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.org.Hotel;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.org.EmployeeService;
import com.kry.pms.service.org.HotelService;
import com.kry.pms.service.sys.BusinessSeqService;
import com.kry.pms.service.sys.ShiftService;
import com.kry.pms.service.sys.UserService;
import com.kry.pms.utils.MD5Utils;

@RestController
@RequestMapping(path = "/api/v1/sys/user")
public class UserController extends BaseController<User> {
	@Autowired
	UserService userService;
	@Autowired
	ShiftService shiftService;
	@Autowired
	HotelService hotelService;
	@Autowired
	EmployeeService employeeService;
	@Autowired
	BusinessSeqService businessSeqService;

	@PostMapping
	public HttpResponse<User> add(@RequestBody User user) {
		return getDefaultResponse().addData(userService.add(user));
	}

	@PutMapping
	public HttpResponse<User> modify(@RequestBody User user) {
		return getDefaultResponse().addData(userService.modify(user));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		userService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<User>> query(HttpServletRequest request)
			throws InstantiationException, IllegalAccessException {
		HttpResponse<PageResponse<User>> rep = new HttpResponse<PageResponse<User>>();
		PageRequest<User> req = parse2PageRequest(request);
		return rep.addData(userService.listPage(req));
	}

	@GetMapping(path = "/admin/info")
	public HttpResponse<UserInfoVo> info() {
		HttpResponse<UserInfoVo> rep = new HttpResponse<UserInfoVo>();
		String userId = getCurrentUserId();
		String userid = getUserId();
		System.out.println(getShiftCode());
		if(userid == null) {
			return rep.error(403, "登录过期，请重新登录");
		}
		UserInfoVo userInfoVo = new UserInfoVo();
		User user = userService.findById(userid);
		BeanUtils.copyProperties(user, userInfoVo);
		Employee employee = getCurrentEmployee();
		userInfoVo.setShiftCode(getShiftCode());
		employee.setUser(user);
		EmployeeSummaryVo esv = new EmployeeSummaryVo();
		BeanUtils.copyProperties(employee, esv);
		Hotel hotel = hotelService.getByHotelCode(user.getHotelCode());
		HotelSummaryVo hsv = new HotelSummaryVo();
		BeanUtils.copyProperties(hotel, hsv);
		userInfoVo.setHotel(hsv);
		userInfoVo.setEmployee(esv);
		userInfoVo.setBusinessDate(businessSeqService.getBuinessDate(user.getHotelCode()));
		userInfoVo.setRole(user.getRoles().get(0));
		return rep.addData(userInfoVo);
	}
	@GetMapping(path = "/admin/hotelInfo")
	public HttpResponse<HotelInfoVo> hotelInfo(){
		HttpResponse<HotelInfoVo> rep = new HttpResponse<HotelInfoVo>();
		rep.setData(hotelService.getHotelInfo(getCurrentHotleCode()));
		return rep;
		
	}
	
	/**
	 * 功能描述: 修改密码
	 * 〈〉
	 * @Param: [oldpwd, newpwd, topwd]
	 * @Return: com.kry.pms.base.HttpResponse<java.lang.String>
	 * @Author: huanghaibin
	 * @Date: 2019/10/18 16:25
	 */
	@PostMapping(path = "/reset_by_oldpwd")
	public HttpResponse<String> updatePassword(String oldPassword, String newPassword) {
		HttpResponse<String> rep = new HttpResponse<String>();
		String userid = getUserId();
		if(userid == null) {
			return rep.error(403, "登录过期，请重新登录");
		}
		User user = userService.findById(userid);
		if(oldPassword == null || newPassword == null){
			return rep.error(400, "密码不能为空");
		}
		String pwd = MD5Utils.encrypt(user.getUsername(),getCurrentHotleCode(), oldPassword);
		if(!pwd.equals(user.getPassword())){
			return rep.error(400, "原密码错误");
		}
		user.setPassword(MD5Utils.encrypt(user.getUsername(),getCurrentHotleCode(), newPassword));
		userService.modify(user);
		return rep.ok("密码修改成功");
	}

}
