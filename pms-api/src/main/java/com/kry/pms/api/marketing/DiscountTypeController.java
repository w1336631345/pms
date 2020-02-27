package com.kry.pms.api.marketing;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.marketing.DiscountType;
import com.kry.pms.service.marketing.DiscountTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/marketing/discountType")
public class DiscountTypeController extends BaseController<DiscountType> {
	@Autowired
	DiscountTypeService discountTypeService;
	@PostMapping
	public HttpResponse<DiscountType> add(@RequestBody DiscountType discountType) {
		return getDefaultResponse().addData(discountTypeService.add(discountType));
	}

	@PutMapping
	public HttpResponse<DiscountType> modify(@RequestBody DiscountType discountType) {
		return getDefaultResponse().addData(discountTypeService.modify(discountType));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		discountTypeService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<DiscountType>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<DiscountType>> rep = new HttpResponse<PageResponse<DiscountType>>();
		PageRequest<DiscountType> req = parse2PageRequest(request);
		return rep.addData(discountTypeService.listPage(req));
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<DiscountType>> list(HttpServletRequest request) {
		HttpResponse hr = new HttpResponse();
		HttpResponse<List<DiscountType>> rep = new HttpResponse<List<DiscountType>>();
		List<DiscountType> list = discountTypeService.getAllByHotelCode(getCurrentHotleCode());
		hr.setData(list);
		return hr;
	}

}
