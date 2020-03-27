package com.kry.pms.api.goods;

import javax.servlet.http.HttpServletRequest;

import com.kry.pms.base.Constants;
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
import com.kry.pms.model.persistence.goods.Product;
import com.kry.pms.service.goods.ProductService;

@RestController
@RequestMapping(path = "/api/v1/goods/product")
public class ProductController extends BaseController<Product> {
	@Autowired
	ProductService productService;
	@PostMapping
	public HttpResponse<Product> add(@RequestBody Product product) {
		HttpResponse<Product> rep = getDefaultResponse();
		product = productService.add(product);
		if(product==null){
			rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
			rep.setMessage("编码重复");
		}else{
			rep.setData(product);
		}
		return rep;
	}

	@PutMapping
	public HttpResponse<Product> modify(@RequestBody Product product) {
		return getDefaultResponse().addData(productService.modify(product));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		productService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<Product>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<Product>> rep = new HttpResponse<PageResponse<Product>>();
		PageRequest<Product> req = parse2PageRequest(request);
		return rep.addData(productService.listPage(req));
	}

}
