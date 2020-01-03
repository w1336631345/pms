package com.kry.pms.api.busi;

import javax.servlet.http.HttpServletRequest;

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
import com.kry.pms.model.persistence.busi.BillItem;
import com.kry.pms.service.busi.BillItemService;

@RestController
@RequestMapping(path = "/api/v1/busi/billItem")
public class BillItemController extends BaseController<BillItem> {
	@Autowired
	BillItemService billItemService;
	@PostMapping
	public HttpResponse<BillItem> add(@RequestBody BillItem billItem) {
		return getDefaultResponse().addData(billItemService.add(billItem));
	}

	@PutMapping
	public HttpResponse<BillItem> modify(@RequestBody BillItem billItem) {
		return getDefaultResponse().addData(billItemService.modify(billItem));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		billItemService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<BillItem>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<BillItem>> rep = new HttpResponse<PageResponse<BillItem>>();
		PageRequest<BillItem> req = parse2PageRequest(request);
		return rep.addData(billItemService.listPage(req));
	}

}
