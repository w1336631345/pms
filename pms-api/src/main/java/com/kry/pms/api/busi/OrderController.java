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
import com.kry.pms.model.persistence.busi.Order;
import com.kry.pms.service.busi.OrderService;

@RestController
@RequestMapping(path = "/api/v1/busi/order")
public class OrderController extends BaseController<Order> {
	@Autowired
	OrderService orderService;
	@PostMapping
	public HttpResponse<Order> add(@RequestBody Order order) {
		return getDefaultResponse().addData(orderService.add(order));
	}

	@PutMapping
	public HttpResponse<Order> modify(@RequestBody Order order) {
		return getDefaultResponse().addData(orderService.modify(order));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		orderService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<Order>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<Order>> rep = new HttpResponse<PageResponse<Order>>();
		PageRequest<Order> req = parse2PageRequest(request);
		return rep.addData(orderService.listPage(req));
	}

}
