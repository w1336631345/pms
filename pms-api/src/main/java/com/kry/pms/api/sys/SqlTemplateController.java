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
import com.kry.pms.model.persistence.sys.SqlTemplate;
import com.kry.pms.service.sys.SqlTemplateService;

@RestController
@RequestMapping(path = "/api/v1/sys/sqlTemplate")
public class SqlTemplateController extends BaseController<SqlTemplate> {
	@Autowired
	SqlTemplateService sqlTemplateService;
	@PostMapping
	public HttpResponse<SqlTemplate> add(@RequestBody SqlTemplate sqlTemplate) {
		return getDefaultResponse().addData(sqlTemplateService.add(sqlTemplate));
	}

	@PutMapping
	public HttpResponse<SqlTemplate> modify(@RequestBody SqlTemplate sqlTemplate) {
		return getDefaultResponse().addData(sqlTemplateService.modify(sqlTemplate));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		sqlTemplateService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<SqlTemplate>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<SqlTemplate>> rep = new HttpResponse<PageResponse<SqlTemplate>>();
		PageRequest<SqlTemplate> req = parse2PageRequest(request);
		return rep.addData(sqlTemplateService.listPage(req));
	}

}
