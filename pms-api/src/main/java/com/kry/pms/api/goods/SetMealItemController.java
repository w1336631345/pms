package com.kry.pms.api.goods;

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
import com.kry.pms.model.persistence.goods.SetMealItem;
import com.kry.pms.service.goods.SetMealItemService;

@RestController
@RequestMapping(path = "/api/v1/goods/setMealItem")
public class SetMealItemController extends BaseController<SetMealItem> {
	@Autowired
	SetMealItemService setMealItemService;
	@PostMapping
	public HttpResponse<SetMealItem> add(@RequestBody SetMealItem setMealItem) {
		return getDefaultResponse().addData(setMealItemService.add(setMealItem));
	}

	@PutMapping
	public HttpResponse<SetMealItem> modify(@RequestBody SetMealItem setMealItem) {
		return getDefaultResponse().addData(setMealItemService.modify(setMealItem));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		setMealItemService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<SetMealItem>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<SetMealItem>> rep = new HttpResponse<PageResponse<SetMealItem>>();
		PageRequest<SetMealItem> req = parse2PageRequest(request);
		return rep.addData(setMealItemService.listPage(req));
	}

}
