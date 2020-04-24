package com.kry.pms.api.busi;

import javax.servlet.http.HttpServletRequest;

import com.kry.pms.service.sys.BusinessSeqService;
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
import com.kry.pms.model.persistence.busi.DailyVerify;
import com.kry.pms.service.busi.DailyVerifyService;

import java.time.LocalDate;

@RestController
@RequestMapping(path = "/api/v1/busi/dailyVerify")
public class DailyVerifyController extends BaseController<DailyVerify> {
	@Autowired
	DailyVerifyService dailyVerifyService;
	@Autowired
	BusinessSeqService businessSeqService;

	@GetMapping(path="/test")
	public HttpResponse<String> testVerify() {
		dailyVerifyService.autoDailyVerify(getCurrentHotleCode());
		return new HttpResponse<>();
	}

	@GetMapping
	public HttpResponse<PageResponse<DailyVerify>> query(HttpServletRequest request)
			throws InstantiationException, IllegalAccessException {
		HttpResponse<PageResponse<DailyVerify>> rep = new HttpResponse<PageResponse<DailyVerify>>();
		PageRequest<DailyVerify> req = parse2PageRequest(request);
		return rep.addData(dailyVerifyService.listPage(req));
	}
	/**
	 * 功能描述: <br>查询是否入账
	 * 〈〉
	 * @Param: []
	 * @Return: com.kry.pms.base.HttpResponse<java.lang.String>
	 * @Author: huanghaibin
	 * @Date: 2020/4/24 14:19
	 */
	@GetMapping(path="/isAudit")
	public HttpResponse<String> isAudit() {
		HttpResponse hr = new HttpResponse();
		LocalDate businessDate = businessSeqService.getBuinessDate(getCurrentHotleCode());
		DailyVerify dailyVerify = dailyVerifyService.findByHotelCodeAndBusinessDate(getCurrentHotleCode(), businessDate);
		if(dailyVerify != null){
			hr.addData("Y");
		}else {
			hr.addData("N");
		}
		return hr;
	}

}
