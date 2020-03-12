package com.kry.pms.api.guest;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.guest.CustProposal;
import com.kry.pms.model.persistence.guest.CustVehicle;
import com.kry.pms.service.guest.CustProposalService;
import com.kry.pms.service.guest.CustVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/guest/custProposal")
public class CustProposalController extends BaseController<CustProposal> {
	@Autowired
	CustProposalService custProposalService;
	@PostMapping
	public HttpResponse<CustProposal> add(@RequestBody CustProposal custProposal) {
		custProposal.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(custProposalService.add(custProposal));
	}

	@PutMapping
	public HttpResponse<CustProposal> modify(@RequestBody CustProposal custProposal) {
		return getDefaultResponse().addData(custProposalService.modify(custProposal));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		custProposalService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<CustProposal>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<CustProposal>> rep = new HttpResponse<PageResponse<CustProposal>>();
		PageRequest<CustProposal> req = parse2PageRequest(request);
		return rep.addData(custProposalService.listPage(req));
	}

	@GetMapping(path = "/hotel")
	public HttpResponse<List<CustProposal>> getByHotelCode(HttpServletRequest request){
		HttpResponse<List<CustProposal>> rep = new HttpResponse<List<CustProposal>>();
		List<CustProposal> list = custProposalService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<CustProposal>> getByCustomerId(String customerId){
			HttpResponse<List<CustProposal>> rep = new HttpResponse<List<CustProposal>>();
		List<CustProposal> list = custProposalService.getByCustomerId(customerId);
		return rep.addData(list);
	}

}
