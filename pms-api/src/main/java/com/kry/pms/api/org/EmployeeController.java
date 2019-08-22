package com.kry.pms.api.org;

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

	@GetMapping
	public HttpResponse<PageResponse<Employee>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<Employee>> rep = new HttpResponse<PageResponse<Employee>>();
		PageRequest<Employee> req = parse2PageRequest(request);
		return rep.addData(employeeService.listPage(req));
	}

}
