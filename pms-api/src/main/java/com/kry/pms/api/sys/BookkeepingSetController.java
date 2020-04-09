package com.kry.pms.api.sys;

import javax.servlet.http.HttpServletRequest;

import com.kry.pms.model.persistence.sys.User;
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
import com.kry.pms.model.persistence.sys.BookkeepingSet;
import com.kry.pms.service.sys.BookkeepingSetService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/sys/bookkeepingSet")
public class BookkeepingSetController extends BaseController<BookkeepingSet> {
	@Autowired
	BookkeepingSetService bookkeepingSetService;
	@PostMapping
	public HttpResponse<BookkeepingSet> add(@RequestBody BookkeepingSet bookkeepingSet) {
		return getDefaultResponse().addData(bookkeepingSetService.add(bookkeepingSet));
	}

	/**
	 * 功能描述: <br>团付、记账设置批量添加
	 * 〈〉
	 * @Param: [bookkeepingSet]
	 * @Return: com.kry.pms.base.HttpResponse
	 * @Author: huanghaibin
	 * @Date: 2019/12/30 17:31
	 */
	@PostMapping(path = "/addList")
	public HttpResponse addList(@RequestBody List<BookkeepingSet> list, String accountId) {
		HttpResponse hr = new HttpResponse();
		User user = getUser();
		hr = bookkeepingSetService.addList(list,accountId, user.getHotelCode());
		return hr;
	}

	@PutMapping
	public HttpResponse<BookkeepingSet> modify(@RequestBody BookkeepingSet bookkeepingSet) {
		return getDefaultResponse().addData(bookkeepingSetService.modify(bookkeepingSet));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		bookkeepingSetService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<BookkeepingSet>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<BookkeepingSet>> rep = new HttpResponse<PageResponse<BookkeepingSet>>();
		PageRequest<BookkeepingSet> req = parse2PageRequest(request);
		return rep.addData(bookkeepingSetService.listPage(req));
	}

	/**
	 * 功能描述: <br>查询是否团付设置(记账设置)
	 * 〈〉
	 * @Param: [accountId, productId]
	 * @Return: com.kry.pms.base.HttpResponse
	 * @Author: huanghaibin
	 * @Date: 2019/12/30 17:22
	 */
	@GetMapping(path = "/isExist")
	public HttpResponse isExist(String accountId, String productId){
		HttpResponse hr = new HttpResponse();
		User user = getUser();
		BookkeepingSet bs = bookkeepingSetService.isExist(user.getHotelCode(), accountId, productId);
		if(bs == null){
			hr.setData(false);
		}else{
			hr.setData(true);
		}
		return hr;
	}

	/**
	 * 功能描述: <br>查询已经设置的数据
	 * 〈〉
	 * @Param: [accountId]
	 * @Return: com.kry.pms.base.HttpResponse
	 * @Author: huanghaibin
	 * @Date: 2019/12/31 11:13
	 */
	@GetMapping(path = "/findSet")
	public HttpResponse findSet(String accountId){
		HttpResponse hr = new HttpResponse();
		User user = getUser();
		List<BookkeepingSet> list = bookkeepingSetService.findSet(user.getHotelCode(), accountId);
		hr.setData(list);
		return hr;
	}

}
