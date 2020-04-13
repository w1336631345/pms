package com.kry.pms.api.pay;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.pay.WechatPayRecord;
import com.kry.pms.model.persistence.pay.WechatRefundRecord;
import com.kry.pms.service.pay.WechatPayRecordService;
import com.kry.pms.service.pay.WechatRefundRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/pay/wechatRefundRecord")
public class WechatRefundRecordController extends BaseController<WechatRefundRecord> {
	@Autowired
	WechatRefundRecordService wechatRefundRecordService;
	@PostMapping
	public HttpResponse<WechatRefundRecord> add(@RequestBody WechatRefundRecord wechatRefundRecord) {
		wechatRefundRecord.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(wechatRefundRecordService.add(wechatRefundRecord));
	}

	@PutMapping
	public HttpResponse<WechatRefundRecord> modify(@RequestBody WechatRefundRecord wechatRefundRecord) {
		return getDefaultResponse().addData(wechatRefundRecordService.modify(wechatRefundRecord));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		wechatRefundRecordService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<WechatRefundRecord>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<WechatRefundRecord>> rep = new HttpResponse<PageResponse<WechatRefundRecord>>();
		PageRequest<WechatRefundRecord> req = parse2PageRequest(request);
		return rep.addData(wechatRefundRecordService.listPage(req));
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<WechatRefundRecord>> getByHotelCode(HttpServletRequest request){
		HttpResponse<List<WechatRefundRecord>> rep = new HttpResponse<List<WechatRefundRecord>>();
		List<WechatRefundRecord> list = wechatRefundRecordService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

}
