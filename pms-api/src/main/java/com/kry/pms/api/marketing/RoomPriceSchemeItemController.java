package com.kry.pms.api.marketing;

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
import com.kry.pms.model.persistence.marketing.RoomPriceSchemeItem;
import com.kry.pms.service.marketing.RoomPriceSchemeItemService;

@RestController
@RequestMapping(path = "/api/v1/marketing/roomPriceSchemeItem")
public class RoomPriceSchemeItemController extends BaseController<RoomPriceSchemeItem> {
	@Autowired
	RoomPriceSchemeItemService roomPriceSchemeItemService;
	@PostMapping
	public HttpResponse<RoomPriceSchemeItem> add(@RequestBody RoomPriceSchemeItem roomPriceSchemeItem) {
		return getDefaultResponse().addData(roomPriceSchemeItemService.add(roomPriceSchemeItem));
	}

	@PutMapping
	public HttpResponse<RoomPriceSchemeItem> modify(@RequestBody RoomPriceSchemeItem roomPriceSchemeItem) {
		return getDefaultResponse().addData(roomPriceSchemeItemService.modify(roomPriceSchemeItem));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		roomPriceSchemeItemService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<RoomPriceSchemeItem>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<RoomPriceSchemeItem>> rep = new HttpResponse<PageResponse<RoomPriceSchemeItem>>();
		PageRequest<RoomPriceSchemeItem> req = parse2PageRequest(request);
		return rep.addData(roomPriceSchemeItemService.listPage(req));
	}

}
