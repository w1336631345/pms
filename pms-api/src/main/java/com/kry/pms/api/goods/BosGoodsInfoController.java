package com.kry.pms.api.goods;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.goods.BosGoodsInfo;
import com.kry.pms.model.persistence.guest.CustVehicle;
import com.kry.pms.service.goods.BosGoodsInfoService;
import com.kry.pms.service.guest.CustVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/goods/bosGoodsInfo")
public class BosGoodsInfoController extends BaseController<BosGoodsInfo> {
	@Autowired
	BosGoodsInfoService bosGoodsInfoService;
	@PostMapping
	public HttpResponse<BosGoodsInfo> add(@RequestBody BosGoodsInfo bosGoodsInfo) {
		bosGoodsInfo.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(bosGoodsInfoService.add(bosGoodsInfo));
	}

	@PutMapping
	public HttpResponse<BosGoodsInfo> modify(@RequestBody BosGoodsInfo bosGoodsInfo) {
		return getDefaultResponse().addData(bosGoodsInfoService.modify(bosGoodsInfo));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		bosGoodsInfoService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<BosGoodsInfo>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<BosGoodsInfo>> rep = new HttpResponse<PageResponse<BosGoodsInfo>>();
		PageRequest<BosGoodsInfo> req = parse2PageRequest(request);
		return rep.addData(bosGoodsInfoService.listPage(req));
	}

	@GetMapping(path = "/hotelCode")
	public HttpResponse<List<BosGoodsInfo>> getByHotelCode(HttpServletRequest request){
		HttpResponse<List<BosGoodsInfo>> rep = new HttpResponse<List<BosGoodsInfo>>();
		List<BosGoodsInfo> list = bosGoodsInfoService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

	/**
	 * 功能描述: <br>根据类型查询
	 * 〈〉
	 * @Param: [typeId]
	 * @Return: com.kry.pms.base.HttpResponse<java.util.List<com.kry.pms.model.persistence.goods.BosGoodsInfo>>
	 * @Author: huanghaibin
	 * @Date: 2020/7/6 10:40
	 */
	@GetMapping(path = "/list")
	public HttpResponse<List<BosGoodsInfo>> getByCustomerId(String typeId){
			HttpResponse<List<BosGoodsInfo>> rep = new HttpResponse<List<BosGoodsInfo>>();
		List<BosGoodsInfo> list = bosGoodsInfoService.findByBosGoodsTypeId(typeId);
		return rep.addData(list);
	}

	/**
	 * 功能描述: <br>根据营业点查询
	 * 〈〉
	 * @Param: [typeId]
	 * @Return: com.kry.pms.base.HttpResponse<java.util.List<com.kry.pms.model.persistence.goods.BosGoodsInfo>>
	 * @Author: huanghaibin
	 * @Date: 2020/7/6 10:41
	 */
	@GetMapping(path = "/findByBosSiteId")
	public HttpResponse<List<BosGoodsInfo>> findByBosSiteId(String siteId){
		HttpResponse<List<BosGoodsInfo>> rep = new HttpResponse<List<BosGoodsInfo>>();
		List<BosGoodsInfo> list = bosGoodsInfoService.findByBosSiteId(siteId);
		return rep.addData(list);
	}

}
