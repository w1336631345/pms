package com.kry.pms.api.sys;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.sys.Nation;
import com.kry.pms.model.persistence.sys.Nationality;
import com.kry.pms.model.persistence.sys.Role;
import com.kry.pms.service.sys.NationService;
import com.kry.pms.service.sys.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/sys/nation")
public class NationController extends BaseController<Nation> {
	@Autowired
	NationService nationService;
	@PostMapping
	public HttpResponse<Nation> add(@RequestBody Nation nation) {
		return getDefaultResponse().addData(nationService.add(nation));
	}

	@PutMapping
	public HttpResponse<Nation> modify(@RequestBody Nation nation) {
		return getDefaultResponse().addData(nationService.modify(nation));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		nationService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<Nation>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<Nation>> rep = new HttpResponse<PageResponse<Nation>>();
		PageRequest<Nation> req = parse2PageRequest(request);
		return rep.addData(nationService.listPage(req));
	}

}
