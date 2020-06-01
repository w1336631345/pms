package com.kry.pms.api.busi;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.Constants;
import com.kry.pms.base.DtoResponse;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.http.request.busi.BillOperationBo;
import com.kry.pms.model.http.request.busi.BillQueryBo;
import com.kry.pms.model.persistence.busi.Bill;
import com.kry.pms.service.busi.BillService;

@RestController
@RequestMapping(path = "/api/v1/busi/bill")
public class BillController extends BaseController<Bill> {
	@Autowired
	BillService billService;
	@PostMapping
	public HttpResponse<Bill> add(@RequestBody Bill bill) {
		bill.setShiftCode(getShiftCode());
		bill.setOperationEmployee(getCurrentEmployee());
		HttpResponse<Bill> rep = getDefaultResponse();
		bill = billService.add(bill);
		if(bill==null) {
			rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
			rep.setMessage("消费项目不合法");
		}
		return rep.addData(bill);
	}

	@PutMapping
	public HttpResponse<Bill> modify(@RequestBody Bill bill) {
		return getDefaultResponse().addData(billService.modify(bill));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		billService.delete(id);
		return rep;
	}
	
	@GetMapping(path = "/account/{id}")
	public HttpResponse<List<Bill>> queryByAccountId(@PathVariable String id) throws InstantiationException, IllegalAccessException{
		HttpResponse<List<Bill>> rep = new HttpResponse<List<Bill>>();
		List<Bill> data = billService.findByAccountId(id);
		rep.setData(data);
		return rep;
	}
	@GetMapping(path = "/group/{id}")
	public HttpResponse<List<Bill>> queryByGroupId(@PathVariable String id) throws InstantiationException, IllegalAccessException{
		HttpResponse<List<Bill>> rep = new HttpResponse<List<Bill>>();
		List<Bill> data = billService.findByAccountId(id);
		rep.setData(data);
		return rep;
	}
	@GetMapping
	public HttpResponse<PageResponse<Bill>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<Bill>> rep = new HttpResponse<PageResponse<Bill>>();
		PageRequest<Bill> req = parse2PageRequest(request);
		return rep.addData(billService.listPage(req));
	}
//	@PostMapping(path="/all")
//	public HttpResponse<List> queryByBo(@RequestBody BillQueryBo query) throws InstantiationException, IllegalAccessException{
//		query.setHotelCode(getCurrentHotleCode());
//		HttpResponse<List> rep = new HttpResponse<List>();
//		return rep.addData(billService.queryByBo(query));
//	}
	
	@GetMapping(path = "/offset/{id}")
	public HttpResponse<String> offset(@PathVariable String id) throws InstantiationException, IllegalAccessException{
		HttpResponse<String> rep = new HttpResponse<String>();
		DtoResponse<Bill> response = billService.offset(id,getCurrentEmployee(),getShiftCode());
		BeanUtils.copyProperties(response, rep);
		return rep;
	}
	@PostMapping(path="/item/operation")
	public HttpResponse<String> itemOp(@RequestBody BillOperationBo bob){
		bob.setShiftCode(getShiftCode());
		bob.setOperationEmployee(getCurrentEmployee());
		DtoResponse<Bill> response =  billService.operation(bob);
		HttpResponse<String> rep = new HttpResponse<String>();
		BeanUtils.copyProperties(response, rep);
		return rep;
	}

	/**
	 * 功能描述: <br>根据账号id 查询已结账和未结账统计
	 * 〈〉
	 * @Param: [accountId]
	 * @Return: com.kry.pms.base.HttpResponse<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
	 * @Author: huanghaibin
	 * @Date: 2020/4/28 11:48
	 */
	@GetMapping(path="/getStatusTotal")
	public HttpResponse<List<Map<String, Object>>> getStatusTotal(String accountId){
		HttpResponse<List<Map<String, Object>>> rep = new HttpResponse<List<Map<String, Object>>>();
		rep.addData(billService.getStatusTotal(getCurrentHotleCode(), accountId));
		return rep;
	}
}
