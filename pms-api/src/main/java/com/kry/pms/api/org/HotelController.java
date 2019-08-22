package com.kry.pms.api.org;

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
import com.kry.pms.model.persistence.org.Hotel;
import com.kry.pms.service.org.HotelService;

@RestController
@RequestMapping(path = "/api/v1/org/hotel")
public class HotelController extends BaseController<Hotel> {
	@Autowired
	HotelService hotelService;
	@PostMapping
	public HttpResponse<Hotel> add(@RequestBody Hotel hotel) {
		return getDefaultResponse().addData(hotelService.add(hotel));
	}

	@PutMapping
	public HttpResponse<Hotel> modify(@RequestBody Hotel hotel) {
		return getDefaultResponse().addData(hotelService.modify(hotel));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		hotelService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<Hotel>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<Hotel>> rep = new HttpResponse<PageResponse<Hotel>>();
		PageRequest<Hotel> req = parse2PageRequest(request);
		return rep.addData(hotelService.listPage(req));
	}

}
