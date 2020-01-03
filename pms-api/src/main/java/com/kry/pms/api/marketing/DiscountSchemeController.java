package com.kry.pms.api.marketing;

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
import com.kry.pms.model.persistence.marketing.DiscountScheme;
import com.kry.pms.service.marketing.DiscountSchemeService;

@RestController
@RequestMapping(path = "/api/v1/marketing/discountScheme")
public class DiscountSchemeController extends BaseController<DiscountScheme> {
	@Autowired
	DiscountSchemeService discountSchemeService;
	@PostMapping
	public HttpResponse<DiscountScheme> add(@RequestBody DiscountScheme discountScheme) {
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

}
