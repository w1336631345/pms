package com.kry.pms.api.pay;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.pay.WechatPayRecord;
import com.kry.pms.service.pay.WechatPayRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/pay/wechatPayRecord")
public class WechatPayRecordController extends BaseController<WechatPayRecord> {
	@Autowired
	WechatPayRecordService wechatPayRecordService;
	@PostMapping
	public HttpResponse<WechatPayRecord> add(@RequestBody WechatPayRecord wechatPayRecord) {
		wechatPayRecord.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(wechatPayRecordService.add(wechatPayRecord));
	}

	@PutMapping
	public HttpResponse<WechatPayRecord> modify(@RequestBody WechatPayRecord wechatPayRecord) {
		return getDefaultResponse().addData(wechatPayRecordService.modify(wechatPayRecord));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		wechatPayRecordService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<WechatPayRecord>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<WechatPayRecord>> rep = new HttpResponse<PageResponse<WechatPayRecord>>();
		PageRequest<WechatPayRecord> req = parse2PageRequest(request);
		return rep.addData(wechatPayRecordService.listPage(req));
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<WechatPayRecord>> getByHotelCode(HttpServletRequest request){
		HttpResponse<List<WechatPayRecord>> rep = new HttpResponse<List<WechatPayRecord>>();
		List<WechatPayRecord> list = wechatPayRecordService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

	/**
	 * 功能描述: <br>用户输入支付密码后查询结果，更改数据库支付数据结果
	 * 〈〉
	 * @Param: [out_trade_no, trade_state, trade_state_desc]
	 * @Return: com.kry.pms.base.HttpResponse
	 * @Author: huanghaibin
	 * @Date: 2020/4/15 15:43
	 */
	@GetMapping(path = "/resultUpdate")
	public HttpResponse resultUpdate(String out_trade_no, String trade_state, String trade_state_desc, String transaction_id){
		HttpResponse rep = new HttpResponse();
		rep = wechatPayRecordService.resultUpdate(out_trade_no, trade_state, trade_state_desc, transaction_id, getCurrentHotleCode());
		return rep;
	}

}
