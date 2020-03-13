package com.kry.pms.api.guest;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.guest.CustProposal;
import com.kry.pms.model.persistence.guest.CustReturnVisit;
import com.kry.pms.service.guest.CustProposalService;
import com.kry.pms.service.guest.CustReturnVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 功能描述: <br>回访记录
 * @Author: huanghaibin
 * @Date: 2020/3/13 14:00
 */
@RestController
@RequestMapping(path = "/api/v1/guest/custReturnVisit")
public class CustReturnVisitController extends BaseController<CustReturnVisit> {
	@Autowired
	CustReturnVisitService custReturnVisitService;
	@PostMapping
	public HttpResponse<CustReturnVisit> add(@RequestBody CustReturnVisit custReturnVisit) {
		custReturnVisit.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(custReturnVisitService.add(custReturnVisit));
	}

	@PutMapping
	public HttpResponse<CustReturnVisit> modify(@RequestBody CustReturnVisit custReturnVisit) {
		return getDefaultResponse().addData(custReturnVisitService.modify(custReturnVisit));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		custReturnVisitService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<CustReturnVisit>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<CustReturnVisit>> rep = new HttpResponse<PageResponse<CustReturnVisit>>();
		PageRequest<CustReturnVisit> req = parse2PageRequest(request);
		return rep.addData(custReturnVisitService.listPage(req));
	}

	@GetMapping(path = "/hotel")
	public HttpResponse<List<CustReturnVisit>> getByHotelCode(HttpServletRequest request){
		HttpResponse<List<CustReturnVisit>> rep = new HttpResponse<List<CustReturnVisit>>();
		List<CustReturnVisit> list = custReturnVisitService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<CustReturnVisit>> getByCustomerId(String customerId){
			HttpResponse<List<CustReturnVisit>> rep = new HttpResponse<List<CustReturnVisit>>();
		List<CustReturnVisit> list = custReturnVisitService.getByCustomerId(customerId);
		return rep.addData(list);
	}

}
