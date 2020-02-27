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
import com.kry.pms.model.persistence.dict.DictData;
import com.kry.pms.service.dict.DictDataService;

@RestController
@RequestMapping(path = "/api/v1/dict/dictData")
public class DictDataController extends BaseController<DictData> {
	@Autowired
	DictDataService dictDataService;
	@PostMapping
	public HttpResponse<DictData> add(@RequestBody DictData dictData) {
		return getDefaultResponse().addData(dictDataService.add(dictData));
	}

	@PutMapping
	public HttpResponse<DictData> modify(@RequestBody DictData dictData) {
		return getDefaultResponse().addData(dictDataService.modify(dictData));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		dictDataService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<DictData>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<DictData>> rep = new HttpResponse<PageResponse<DictData>>();
		PageRequest<DictData> req = parse2PageRequest(request);
		return rep.addData(dictDataService.listPage(req));
	}

}
