package com.kry.pms.api.marketing;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.marketing.SalesMen;
import com.kry.pms.service.marketing.SalesMenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/marketing/salesMen")
public class SalesMenController extends BaseController<SalesMen> {
	@Autowired
	SalesMenService salesMenService;
	@PostMapping
	public HttpResponse<SalesMen> add(@RequestBody SalesMen salesMen) {
		return getDefaultResponse().addData(salesMenService.add(salesMen));
	}

	@PutMapping
	public HttpResponse<SalesMen> modify(@RequestBody SalesMen salesMen) {
		return getDefaultResponse().addData(salesMenService.modify(salesMen));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		salesMenService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<SalesMen>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<SalesMen>> rep = new HttpResponse<PageResponse<SalesMen>>();
		PageRequest<SalesMen> req = parse2PageRequest(request);
		return rep.addData(salesMenService.listPage(req));
	}
	@GetMapping(path = "/list2")
	public HttpResponse<PageResponse<SalesMen>> query2(@RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
													   @RequestParam(value = "pageSize", defaultValue = "20")Integer pageSize,
													   String type, String status, String name, String contactMobile) {
		HttpResponse rep = new HttpResponse();
		return rep.addData(salesMenService.listPage2(pageNum, pageSize,type, status, name, contactMobile, getCurrentHotleCode()));
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<SalesMen>> getByHotelCode(HttpServletRequest request){
		HttpResponse<List<SalesMen>> rep = new HttpResponse<List<SalesMen>>();
		List<SalesMen> list = salesMenService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

}
