package com.kry.pms.api.org;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kry.pms.model.persistence.sys.User;
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
import com.kry.pms.base.Constants;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.service.org.EmployeeService;

@RestController
@RequestMapping(path = "/api/v1/org/employee")
public class EmployeeController extends BaseController<Employee> {
	@Autowired
	EmployeeService employeeService;
	@PostMapping
	public HttpResponse<Employee> add(@RequestBody Employee employee) {
		return getDefaultResponse().addData(employeeService.add(employee));
	}

	@PutMapping
	public HttpResponse<Employee> modify(@RequestBody Employee employee) {
		return getDefaultResponse().addData(employeeService.modify(employee));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		employeeService.delete(id);
		return rep;
	}
	@GetMapping(path="/marketing")
	public HttpResponse<List<Employee>> findMarketingEmplyee(){
		HttpResponse<List<Employee>> rep = new HttpResponse<List<Employee>>();
		List<Employee> emps = employeeService.findEmployeeByDeptCode(Constants.BusinessCode.DEPT_MARKETING_DEFAULT_CODE,getCurrentHotleCode());
		if(emps==null||emps.isEmpty()) {
			rep.setStatus(Constants.BusinessCode.CODE_RESOURCE_NOT_ENOUGH);
			rep.setMessage("请先添加销售部人员");
		}else {
			rep.setData(emps);
		}
		return rep;
	}
	@GetMapping(path="/dept/{code}")
	public HttpResponse<List<Employee>> findEmplyeeByDeptCode(@PathVariable("code") String code){
		HttpResponse<List<Employee>> rep = new HttpResponse<List<Employee>>();
		List<Employee> emps = employeeService.findEmployeeByDeptCode(code,getCurrentHotleCode());
		if(emps==null||emps.isEmpty()) {
			rep.setStatus(Constants.BusinessCode.CODE_RESOURCE_NOT_ENOUGH);
			rep.setMessage("请先添加销售部人员");
		}else {
			rep.setData(emps);
		}
		return rep;
	}
	@GetMapping
	public HttpResponse<PageResponse<Employee>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<Employee>> rep = new HttpResponse<PageResponse<Employee>>();
		PageRequest<Employee> req = parse2PageRequest(request);
		return rep.addData(employeeService.listPage(req));
	}

	/**
	 * 功能描述: <br>查询未被删除的员工
	 * 〈〉
	 * @Param: []
	 * @Return: com.kry.pms.base.HttpResponse
	 * @Author: huanghaibin
	 * @Date: 2019/12/7 14:38
	 */
	@GetMapping(path="/getEmpList")
	public HttpResponse getByHotelCodeAndDelete(){
		HttpResponse<List<Employee>> rep = new HttpResponse<List<Employee>>();
		User user = getUser();
		if(user == null){
			return rep.loginError();
		}
		List<Employee> list = employeeService.getByHotelCodeAndDelete(user.getHotelCode());
		rep.setData(list);
		return rep;
	}

	/**
	 * 功能描述: <br>操作员的下拉框列表
	 * 〈〉
	 * @Param: []
	 * @Return: com.kry.pms.base.HttpResponse
	 * @Author: huanghaibin
	 * @Date: 2020/3/19 20:21
	 */
	@GetMapping(path="/getListMap")
	public HttpResponse getListMap(){
		HttpResponse<List<Map<String, Object>>> rep = new HttpResponse<List<Map<String, Object>>>();
		User user = getUser();
		if(user == null){
			return rep.loginError();
		}
		List<Map<String, Object>> list = employeeService.getListMap(user.getHotelCode());
		rep.setData(list);
		return rep;
	}

}
