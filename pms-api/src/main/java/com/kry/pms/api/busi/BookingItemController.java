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
import com.kry.pms.model.persistence.busi.BookingItem;
import com.kry.pms.service.busi.BookingItemService;

@RestController
@RequestMapping(path = "/api/v1/busi/bookingItem")
public class BookingItemController extends BaseController<BookingItem> {
	@Autowired
	BookingItemService bookingItemService;
	@PostMapping
	public HttpResponse<BookingItem> add(@RequestBody BookingItem bookingItem) {
		return getDefaultResponse().addData(bookingItemService.add(bookingItem));
	}

	@PutMapping
	public HttpResponse<BookingItem> modify(@RequestBody BookingItem bookingItem) {
		return getDefaultResponse().addData(bookingItemService.modify(bookingItem));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		bookingItemService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<BookingItem>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<BookingItem>> rep = new HttpResponse<PageResponse<BookingItem>>();
		PageRequest<BookingItem> req = parse2PageRequest(request);
		return rep.addData(bookingItemService.listPage(req));
	}

}
