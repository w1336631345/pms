package com.kry.pms.api.sys;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.sys.Nation;
import com.kry.pms.model.persistence.sys.Nationality;
import com.kry.pms.service.sys.NationService;
import com.kry.pms.service.sys.NationalityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/sys/nationality")
public class NationalityController extends BaseController<Nationality> {
	@Autowired
	NationalityService nationalityService;
	@PostMapping
	public HttpResponse<Nationality> add(@RequestBody Nationality nationality) {
		return getDefaultResponse().addData(nationalityService.add(nationality));
	}

	@PutMapping
	public HttpResponse<Nationality> modify(@RequestBody Nationality nationality) {
		return getDefaultResponse().addData(nationalityService.modify(nationality));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		nationalityService.delete(id);
		return rep;
	}


	@GetMapping(path = "/list")
	public HttpResponse<List<Nationality>> getList(){
		HttpResponse<List<Nationality>> rep = new HttpResponse<List<Nationality>>();
		return rep.addData(nationalityService.getAll());
	}

}
