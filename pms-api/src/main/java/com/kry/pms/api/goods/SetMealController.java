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
import com.kry.pms.model.persistence.goods.SetMeal;
import com.kry.pms.service.goods.SetMealService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/goods/setMeal")
public class SetMealController extends BaseController<SetMeal> {
	@Autowired
	SetMealService setMealService;
	@PostMapping
	public HttpResponse<SetMeal> add(@RequestBody SetMeal setMeal) {
		HttpResponse hr = new HttpResponse();
		setMeal.setHotelCode(getCurrentHotleCode());
		if(setMealService.add(setMeal) == null){
			return hr.error("代码重复");
		}
		return getDefaultResponse().addData(setMealService.add(setMeal));
	}

	@PutMapping
	public HttpResponse<SetMeal> modify(@RequestBody SetMeal setMeal) {
		return getDefaultResponse().addData(setMealService.modify(setMeal));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		setMealService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<SetMeal>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<SetMeal>> rep = new HttpResponse<PageResponse<SetMeal>>();
		PageRequest<SetMeal> req = parse2PageRequest(request);
		return rep.addData(setMealService.listPage(req));
	}
	@GetMapping(path = "/list")
	public HttpResponse<List<SetMeal>> list(HttpServletRequest request){
		HttpResponse<List<SetMeal>> rep = new HttpResponse<List<SetMeal>>();
		return rep.addData(setMealService.getAllByHotelCode(getCurrentHotleCode()));
	}


}
