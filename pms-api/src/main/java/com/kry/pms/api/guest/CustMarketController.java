package com.kry.pms.api.guest;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.guest.CustMarket;
import com.kry.pms.model.persistence.guest.CustProposal;
import com.kry.pms.service.guest.CustMarketService;
import com.kry.pms.service.guest.CustProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/guest/custMarket")
public class CustMarketController extends BaseController<CustMarket> {
	@Autowired
	CustMarketService custMarketService;
	@PostMapping
	public HttpResponse<CustMarket> add(@RequestBody CustMarket custMarket) {
		custMarket.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(custMarketService.add(custMarket));
	}

	@PutMapping
	public HttpResponse<CustMarket> modify(@RequestBody CustMarket custMarket) {
		return getDefaultResponse().addData(custMarketService.modify(custMarket));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		custMarketService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<CustMarket>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<CustMarket>> rep = new HttpResponse<PageResponse<CustMarket>>();
		PageRequest<CustMarket> req = parse2PageRequest(request);
		return rep.addData(custMarketService.listPage(req));
	}

	@GetMapping(path = "/hotel")
	public HttpResponse<List<CustMarket>> getByHotelCode(HttpServletRequest request){
		HttpResponse<List<CustMarket>> rep = new HttpResponse<List<CustMarket>>();
		List<CustMarket> list = custMarketService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<CustMarket>> getByCustomerId(String customerId){
			HttpResponse<List<CustMarket>> rep = new HttpResponse<List<CustMarket>>();
		List<CustMarket> list = custMarketService.getByCustomerId(customerId);
		return rep.addData(list);
	}

}
