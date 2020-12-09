package com.kry.pms.api.org;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kry.pms.model.persistence.marketing.SalesMen;
import com.kry.pms.model.persistence.sys.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
	public HttpResponse<String> add(@RequestBody Employee employee) {
		employee.setHotelCode(getCurrentHotleCode());
		HttpResponse<String> response = new HttpResponse<>();
		BeanUtils.copyProperties(employeeService.createEmployee(employee),response);
		return response;
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
	@GetMapping(path = "/list2")
	public HttpResponse<PageResponse<SalesMen>> query2(@RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
													   @RequestParam(value = "pageSize", defaultValue = "20")Integer pageSize,
													   String name, String code, String mobile, String department_id) {
		HttpResponse rep = new HttpResponse();
		return rep.addData(employeeService.listPage2(pageNum, pageSize,name, code, mobile, department_id, getCurrentHotleCode()));
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
