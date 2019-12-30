package com.kry.pms.api.goods;

import javax.servlet.http.HttpServletRequest;

import com.kry.pms.model.persistence.sys.User;
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
import com.kry.pms.model.persistence.goods.ProductCategory;
import com.kry.pms.service.goods.ProductCategoryService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/goods/productCategory")
public class ProductCategoryController extends BaseController<ProductCategory> {
	@Autowired
	ProductCategoryService productCategoryService;
	@PostMapping
	public HttpResponse<ProductCategory> add(@RequestBody ProductCategory productCategory) {
		return getDefaultResponse().addData(productCategoryService.add(productCategory));
	}

	@PutMapping
	public HttpResponse<ProductCategory> modify(@RequestBody ProductCategory productCategory) {
		return getDefaultResponse().addData(productCategoryService.modify(productCategory));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		productCategoryService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<ProductCategory>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<ProductCategory>> rep = new HttpResponse<PageResponse<ProductCategory>>();
		PageRequest<ProductCategory> req = parse2PageRequest(request);
		return rep.addData(productCategoryService.listPage(req));
	}

	@GetMapping(path="/treeAndType")
	public HttpResponse treeAndType(){

		HttpResponse hr = new HttpResponse();
		User user = getUser();
		List<ProductCategory> list = productCategoryService.treeAndType(user.getHotelCode());
		hr.setData(list);
		return hr;
	}

}
