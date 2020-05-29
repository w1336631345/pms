package com.kry.pms.api.sys;

import java.util.List;

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
import com.kry.pms.model.persistence.sys.Function;
import com.kry.pms.service.sys.FunctionService;

@RestController
@RequestMapping(path = "/api/v1/sys/function")
public class FunctionController extends BaseController<Function> {
	@Autowired
	FunctionService functionService;
	@PostMapping
	public HttpResponse<Function> add(@RequestBody Function function) {
		return getDefaultResponse().addData(functionService.add(function));
	}

	@PutMapping
	public HttpResponse<Function> modify(@RequestBody Function function) {
		return getDefaultResponse().addData(functionService.modify(function));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		functionService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<Function>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<Function>> rep = new HttpResponse<PageResponse<Function>>();
		PageRequest<Function> req = parse2PageRequest(request);
		req.getExb().setHotelCode(null);
		return rep.addData(functionService.listPage(req));
	}
	@GetMapping(path="/all")
	public HttpResponse<List<Function>> all() throws InstantiationException, IllegalAccessException{
		HttpResponse<List<Function>> rep = new HttpResponse<List<Function>>();
		return rep.addData(functionService.listAll());
	}

}
