package com.kry.pms.api.goods;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.goods.BosBusinessSite;
import com.kry.pms.model.persistence.goods.BosGoodsInfo;
import com.kry.pms.service.goods.BosBusinessSiteService;
import com.kry.pms.service.goods.BosGoodsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/goods/bosBusinessSite")
public class BosBusinessSiteController extends BaseController<BosBusinessSite> {
	@Autowired
	BosBusinessSiteService bosBusinessSiteService;
	@PostMapping
	public HttpResponse<BosBusinessSite> add(@RequestBody BosBusinessSite bosBusinessSite) {
		bosBusinessSite.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(bosBusinessSiteService.add(bosBusinessSite));
	}

	@PutMapping
	public HttpResponse<BosBusinessSite> modify(@RequestBody BosBusinessSite bosBusinessSite) {
		return getDefaultResponse().addData(bosBusinessSiteService.modify(bosBusinessSite));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		bosBusinessSiteService.delete(id);
		return rep;
	}

	@GetMapping(path = "/deleteAll")
	public HttpResponse deleteAll(String id, String deleteAll) {
		HttpResponse rep = new HttpResponse<>();
		rep = bosBusinessSiteService.deleteAll(id, deleteAll);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<BosBusinessSite>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<BosBusinessSite>> rep = new HttpResponse<PageResponse<BosBusinessSite>>();
		PageRequest<BosBusinessSite> req = parse2PageRequest(request);
		return rep.addData(bosBusinessSiteService.listPage(req));
	}

	@GetMapping(path = "/hotelCode")
	public HttpResponse<List<BosBusinessSite>> getByHotelCode(HttpServletRequest request){
		HttpResponse<List<BosBusinessSite>> rep = new HttpResponse<List<BosBusinessSite>>();
		List<BosBusinessSite> list = bosBusinessSiteService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<BosBusinessSite>> getByCustomerId(String code){
			HttpResponse<List<BosBusinessSite>> rep = new HttpResponse<List<BosBusinessSite>>();
		List<BosBusinessSite> list = bosBusinessSiteService.getByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

}
