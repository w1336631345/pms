package com.kry.pms.api.goods;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.goods.BosGoodsInfo;
import com.kry.pms.model.persistence.goods.BosGoodsType;
import com.kry.pms.service.goods.BosGoodsInfoService;
import com.kry.pms.service.goods.BosGoodsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/goods/bosGoodsType")
public class BosGoodsTypeController extends BaseController<BosGoodsType> {
	@Autowired
	BosGoodsTypeService bosGoodsTypeService;
	@PostMapping
	public HttpResponse<BosGoodsType> add(@RequestBody BosGoodsType bosGoodsType) {
		bosGoodsType.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(bosGoodsTypeService.add(bosGoodsType));
	}

	@PutMapping
	public HttpResponse<BosGoodsType> modify(@RequestBody BosGoodsType bosGoodsType) {
		return getDefaultResponse().addData(bosGoodsTypeService.modify(bosGoodsType));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		bosGoodsTypeService.delete(id);
		return rep;
	}

	@GetMapping(path = "/deleteAll")
	public HttpResponse deleteAll(String id, String deleteAll) {
		HttpResponse rep = new HttpResponse<>();
		rep = bosGoodsTypeService.deleteAll(id, deleteAll);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<BosGoodsType>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<BosGoodsType>> rep = new HttpResponse<PageResponse<BosGoodsType>>();
		PageRequest<BosGoodsType> req = parse2PageRequest(request);
		return rep.addData(bosGoodsTypeService.listPage(req));
	}

	@GetMapping(path = "/hotelCode")
	public HttpResponse<List<BosGoodsType>> getByHotelCode(HttpServletRequest request){
		HttpResponse<List<BosGoodsType>> rep = new HttpResponse<List<BosGoodsType>>();
		List<BosGoodsType> list = bosGoodsTypeService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<BosGoodsType>> getByBosBusinessSiteId(String siteId){
			HttpResponse<List<BosGoodsType>> rep = new HttpResponse<List<BosGoodsType>>();
		List<BosGoodsType> list = bosGoodsTypeService.findByBosBusinessSiteId(siteId);
		return rep.addData(list);
	}

}
