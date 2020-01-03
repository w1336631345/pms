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
import com.kry.pms.model.persistence.sys.Role;
import com.kry.pms.service.sys.RoleService;

@RestController
@RequestMapping(path = "/api/v1/sys/role")
public class RoleController extends BaseController<Role> {
	@Autowired
	RoleService roleService;
	@PostMapping
	public HttpResponse<Role> add(@RequestBody Role role) {
		return getDefaultResponse().addData(roleService.add(role));
	}

	@PutMapping
	public HttpResponse<Role> modify(@RequestBody Role role) {
		return getDefaultResponse().addData(roleService.modify(role));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		roleService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<Role>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<Role>> rep = new HttpResponse<PageResponse<Role>>();
		PageRequest<Role> req = parse2PageRequest(request);
		return rep.addData(roleService.listPage(req));
	}

}
