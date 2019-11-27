package com.kry.pms.api.sys;

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
import com.kry.pms.model.persistence.sys.Shift;
import com.kry.pms.service.sys.ShiftService;

@RestController
@RequestMapping(path = "/api/v1/sys/shift")
public class ShiftController extends BaseController<Shift> {
	@Autowired
	ShiftService shiftService;
	@PostMapping
	public HttpResponse<Shift> add(@RequestBody Shift shift) {
		return getDefaultResponse().addData(shiftService.add(shift));
	}

	@PutMapping
	public HttpResponse<Shift> modify(@RequestBody Shift shift) {
		return getDefaultResponse().addData(shiftService.modify(shift));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		shiftService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<Shift>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<Shift>> rep = new HttpResponse<PageResponse<Shift>>();
		PageRequest<Shift> req = parse2PageRequest(request);
		return rep.addData(shiftService.listPage(req));
	}

}
