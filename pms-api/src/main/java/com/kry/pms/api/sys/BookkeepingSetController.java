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
import com.kry.pms.model.persistence.sys.BookkeepingSet;
import com.kry.pms.service.sys.BookkeepingSetService;

@RestController
@RequestMapping(path = "/api/v1/sys/bookkeepingSet")
public class BookkeepingSetController extends BaseController<BookkeepingSet> {
	@Autowired
	BookkeepingSetService bookkeepingSetService;
	@PostMapping
	public HttpResponse<BookkeepingSet> add(@RequestBody BookkeepingSet bookkeepingSet) {
		return getDefaultResponse().addData(bookkeepingSetService.add(bookkeepingSet));
	}

	@PutMapping
	public HttpResponse<BookkeepingSet> modify(@RequestBody BookkeepingSet bookkeepingSet) {
		return getDefaultResponse().addData(bookkeepingSetService.modify(bookkeepingSet));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		bookkeepingSetService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<BookkeepingSet>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<BookkeepingSet>> rep = new HttpResponse<PageResponse<BookkeepingSet>>();
		PageRequest<BookkeepingSet> req = parse2PageRequest(request);
		return rep.addData(bookkeepingSetService.listPage(req));
	}

}
