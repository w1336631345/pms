package com.kry.pms.api.guest;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.guest.CustInvoice;
import com.kry.pms.model.persistence.guest.CustPassenger;
import com.kry.pms.service.guest.CustInvoiceService;
import com.kry.pms.service.guest.CustPassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/guest/custInvoice")
public class CustInvoiceController extends BaseController<CustInvoice> {
	@Autowired
	CustInvoiceService custInvoiceService;
	@PostMapping
	public HttpResponse<CustInvoice> add(@RequestBody CustInvoice custInvoice) {
		custInvoice.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(custInvoiceService.add(custInvoice));
	}

	@PutMapping
	public HttpResponse<CustInvoice> modify(@RequestBody CustInvoice custInvoice) {
		return getDefaultResponse().addData(custInvoiceService.modify(custInvoice));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		custInvoiceService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<CustInvoice>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<CustInvoice>> rep = new HttpResponse<PageResponse<CustInvoice>>();
		PageRequest<CustInvoice> req = parse2PageRequest(request);
		return rep.addData(custInvoiceService.listPage(req));
	}

	@GetMapping(path = "/hotel")
	public HttpResponse<List<CustInvoice>> getByHotelCode(HttpServletRequest request){
		HttpResponse<List<CustInvoice>> rep = new HttpResponse<List<CustInvoice>>();
		List<CustInvoice> list = custInvoiceService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<CustInvoice>> getByCustomerId(String customerId){
			HttpResponse<List<CustInvoice>> rep = new HttpResponse<List<CustInvoice>>();
		List<CustInvoice> list = custInvoiceService.getByCustomerId(customerId);
		return rep.addData(list);
	}

}
