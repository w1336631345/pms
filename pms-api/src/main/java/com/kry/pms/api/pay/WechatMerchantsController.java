package com.kry.pms.api.pay;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.pay.WechatMerchants;
import com.kry.pms.model.persistence.pay.WechatRefundRecord;
import com.kry.pms.service.pay.WechatMerchantsService;
import com.kry.pms.service.pay.WechatRefundRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/pay/wechatMerchants")
public class WechatMerchantsController extends BaseController<WechatMerchants> {
	@Autowired
	WechatMerchantsService wechatMerchantsService;
	@PostMapping
	public HttpResponse<WechatMerchants> add(@RequestBody WechatMerchants wechatMerchants) {
		wechatMerchants.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(wechatMerchantsService.add(wechatMerchants));
	}

	@PutMapping
	public HttpResponse<WechatMerchants> modify(@RequestBody WechatMerchants wechatMerchants) {
		return getDefaultResponse().addData(wechatMerchantsService.modify(wechatMerchants));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		wechatMerchantsService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<WechatMerchants>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<WechatMerchants>> rep = new HttpResponse<PageResponse<WechatMerchants>>();
		PageRequest<WechatMerchants> req = parse2PageRequest(request);
		return rep.addData(wechatMerchantsService.listPage(req));
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<WechatMerchants>> getByHotelCode(HttpServletRequest request){
		HttpResponse<List<WechatMerchants>> rep = new HttpResponse<List<WechatMerchants>>();
		List<WechatMerchants> list = wechatMerchantsService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

}
