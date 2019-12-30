package com.kry.pms.api.goods;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.goods.Product;
import com.kry.pms.model.persistence.goods.ProductType;
import com.kry.pms.service.goods.ProductService;
import com.kry.pms.service.goods.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

//项目分类管理
@RestController
@RequestMapping(path = "/api/v1/goods/productType")
public class ProductTypeController extends BaseController<ProductType> {
	@Autowired
	ProductTypeService productTypeService;
	@PostMapping
	public HttpResponse<ProductType> add(@RequestBody ProductType productType) {
		return getDefaultResponse().addData(productTypeService.add(productType));
	}

	@PutMapping
	public HttpResponse<ProductType> modify(@RequestBody ProductType productType) {
		return getDefaultResponse().addData(productTypeService.modify(productType));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		productTypeService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<ProductType>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<ProductType>> rep = new HttpResponse<PageResponse<ProductType>>();
		PageRequest<ProductType> req = parse2PageRequest(request);
		return rep.addData(productTypeService.listPage(req));
	}

}
