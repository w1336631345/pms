package com.kry.pms.api.goods;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.goods.Goods;
import com.kry.pms.model.persistence.goods.Product;
import com.kry.pms.service.goods.GoodsService;
import com.kry.pms.service.goods.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/goods/goods")
public class GoodsController extends BaseController<Goods> {
	@Autowired
	GoodsService goodsService;
	@PostMapping
	public HttpResponse<Goods> add(@RequestBody Goods goods) {
		return getDefaultResponse().addData(goodsService.add(goods));
	}

	@PutMapping
	public HttpResponse<Goods> modify(@RequestBody Goods goods) {
		return getDefaultResponse().addData(goodsService.modify(goods));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		goodsService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<Goods>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<Goods>> rep = new HttpResponse<PageResponse<Goods>>();
		PageRequest<Goods> req = parse2PageRequest(request);
		return rep.addData(goodsService.listPage(req));
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<Goods>> list(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<List<Goods>> rep = new HttpResponse<List<Goods>>();
		List<Goods> list = goodsService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

}
