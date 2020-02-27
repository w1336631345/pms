package com.kry.pms.api.dict;

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
import com.kry.pms.model.persistence.dict.DictType;
import com.kry.pms.service.dict.DictTypeService;

@RestController
@RequestMapping(path = "/api/v1/dict/dictType")
public class DictTypeController extends BaseController<DictType> {
	@Autowired
	DictTypeService dictTypeService;
	@PostMapping
	public HttpResponse<DictType> add(@RequestBody DictType dictType) {
		return getDefaultResponse().addData(dictTypeService.add(dictType));
	}

	@PutMapping
	public HttpResponse<DictType> modify(@RequestBody DictType dictType) {
		return getDefaultResponse().addData(dictTypeService.modify(dictType));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		dictTypeService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<DictType>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<DictType>> rep = new HttpResponse<PageResponse<DictType>>();
		PageRequest<DictType> req = parse2PageRequest(request);
		return rep.addData(dictTypeService.listPage(req));
	}

}
