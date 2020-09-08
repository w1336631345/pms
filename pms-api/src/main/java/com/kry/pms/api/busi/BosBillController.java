package com.kry.pms.api.busi;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.*;
import com.kry.pms.model.http.request.busi.BillOperationBo;
import com.kry.pms.model.http.request.busi.BosBillCheckBo;
import com.kry.pms.model.persistence.busi.Bill;
import com.kry.pms.model.persistence.busi.BosBill;
import com.kry.pms.service.busi.BillService;
import com.kry.pms.service.busi.BosBillService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/busi/bosBill")
public class BosBillController extends BaseController<BosBill> {
	@Autowired
	BosBillService bosBillService;

	/**
	 * 功能描述: <br>bos入账（存盘）
	 * 〈〉
	 * @Param: \
	 * @Return: com.kry.pms.base.HttpResponse<com.kry.pms.model.persistence.busi.BosBill>
	 * @Author: huanghaibin
	 * @Date: 2020/9/4 14:54
	 */
	@PostMapping
	public HttpResponse<BosBill> add(@RequestBody BosBill bosBill) {
		bosBill.setShiftCode(getShiftCode());
		bosBill.setHotelCode(getCurrentHotleCode());
		bosBill.setOperationEmployee(getCurrentEmployee());
		HttpResponse<BosBill> rep = getDefaultResponse();
		bosBill = bosBillService.add(bosBill);
		if(bosBill==null) {
			rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
			rep.setMessage("消费项目不合法");
		}
		return rep.addData(bosBill);
	}
	/**
	 * 功能描述: <br>bos修改
	 * 〈〉
	 * @Param: [bosBill]
	 * @Return: com.kry.pms.base.HttpResponse<com.kry.pms.model.persistence.busi.BosBill>
	 * @Author: huanghaibin
	 * @Date: 2020/9/4 14:55
	 */
	@PutMapping
	public HttpResponse<BosBill> modify(@RequestBody BosBill bosBill) {
		HttpResponse<BosBill> rep = getDefaultResponse();
		bosBill = bosBillService.modify(bosBill);
		return rep.addData(bosBill);
	}

	/**
	 * 功能描述: <br>bos入账查询
	 * 〈〉
	 * @Param: [roomNum, accountCode]
	 * @Return: com.kry.pms.base.HttpResponse<java.util.List<com.kry.pms.model.persistence.busi.BosBill>>
	 * @Author: huanghaibin
	 * @Date: 2020/9/4 14:54
	 */
	@GetMapping(path = "/query")
	public HttpResponse<List<BosBill>> findQuery(String siteId, String roomNum, String accountCode) {
		HttpResponse hr = new HttpResponse();
		List<BosBill> list = bosBillService.findQuery(getCurrentHotleCode(),siteId, roomNum, accountCode);
		hr.setData(list);
		return hr;
	}

	/**
	 * 功能描述: <br>bos转房账
	 * 〈〉
	 * @Param: [roomNum, accountCode]
	 * @Return: com.kry.pms.base.HttpResponse<java.util.List<com.kry.pms.model.persistence.busi.BosBill>>
	 * @Author: huanghaibin
	 * @Date: 2020/9/4 14:54
	 */
	@GetMapping(path = "/transferAccount")
	public HttpResponse<BosBill> transferAccount(String bosBillId, String roomNum, String roomId, String accountId) {
		HttpResponse hr = new HttpResponse();
		BosBill bosBill = bosBillService.transferAccount(bosBillId, roomNum, roomId, accountId);
		hr.setData(bosBill);
		return hr;
	}

	/**
	 * 功能描述: <br>消单
	 * 〈〉
	 * @Param: [bosBill]
	 * @Return: com.kry.pms.base.HttpResponse<com.kry.pms.model.persistence.busi.BosBill>
	 * @Author: huanghaibin
	 * @Date: 2020/9/7 10:50
	 */
	@PutMapping(path = "/cancellation")
	public HttpResponse<BosBill> cancellation(@RequestBody BosBill bosBill) {
		HttpResponse<BosBill> rep = getDefaultResponse();
		rep = bosBillService.cancellation(bosBill);
		return rep.addData(bosBill);
	}
	/**
	 * 功能描述: <br>结账
	 * 〈〉
	 * @Param: [bosBillCheckBo]
	 * @Return: com.kry.pms.base.HttpResponse
	 * @Author: huanghaibin
	 * @Date: 2020/9/8 14:50
	 */
	@PostMapping(path="/check")
	public HttpResponse check(@RequestBody BosBillCheckBo bosBillCheckBo){
		bosBillCheckBo.setHotelCode(getCurrentHotleCode());
		bosBillCheckBo.setShiftCode(getShiftCode());
		bosBillCheckBo.setEmployee(getCurrentEmployee());
		HttpResponse hr = new HttpResponse();
		hr = bosBillService.check(bosBillCheckBo);
		return hr;
	}

}
