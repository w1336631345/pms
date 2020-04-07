package com.kry.pms.api.marketing;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.marketing.DiscountScheme;
import com.kry.pms.service.marketing.DiscountSchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/marketing/discountScheme")
public class DiscountSchemeController extends BaseController<DiscountScheme> {
	@Autowired
	DiscountSchemeService discountSchemeService;
	@PostMapping
	public HttpResponse<DiscountScheme> add(@RequestBody DiscountScheme discountScheme) {
		HttpResponse hr = new HttpResponse();
		discountScheme.setHotelCode(getCurrentHotleCode());
		if(discountSchemeService.add(discountScheme) == null){
			return hr.error("代码重复");
		}
		return getDefaultResponse().addData(discountSchemeService.add(discountScheme));
	}

	@PutMapping
	public HttpResponse<DiscountScheme> modify(@RequestBody DiscountScheme discountScheme) {
		return getDefaultResponse().addData(discountSchemeService.modify(discountScheme));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		discountSchemeService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<DiscountScheme>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<DiscountScheme>> rep = new HttpResponse<PageResponse<DiscountScheme>>();
		PageRequest<DiscountScheme> req = parse2PageRequest(request);
		return rep.addData(discountSchemeService.listPage(req));
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<DiscountScheme>> list(HttpServletRequest request) {
		HttpResponse hr = new HttpResponse();
		HttpResponse<List<DiscountScheme>> rep = new HttpResponse<List<DiscountScheme>>();
		List<DiscountScheme> list = discountSchemeService.getAllByHotelCode(getCurrentHotleCode());
		hr.setData(list);
		return hr;
	}

}
