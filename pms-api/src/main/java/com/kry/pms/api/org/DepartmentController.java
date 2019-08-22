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
import com.kry.pms.model.persistence.org.Department;
import com.kry.pms.service.org.DepartmentService;

@RestController
@RequestMapping(path = "/api/v1/org/department")
public class DepartmentController extends BaseController<Department> {
	@Autowired
	DepartmentService departmentService;
	@PostMapping
	public HttpResponse<Department> add(@RequestBody Department department) {
		return getDefaultResponse().addData(departmentService.add(department));
	}

	@PutMapping
	public HttpResponse<Department> modify(@RequestBody Department department) {
		return getDefaultResponse().addData(departmentService.modify(department));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		departmentService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<Department>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<Department>> rep = new HttpResponse<PageResponse<Department>>();
		PageRequest<Department> req = parse2PageRequest(request);
		return rep.addData(departmentService.listPage(req));
	}

}
