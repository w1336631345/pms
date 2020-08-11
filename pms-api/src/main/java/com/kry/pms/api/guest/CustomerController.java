package com.kry.pms.api.guest;

import javax.servlet.http.HttpServletRequest;

import freemarker.template.TemplateException;
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
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.service.guest.CustomerService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/guest/customer")
public class CustomerController extends BaseController<Customer> {
	@Autowired
	CustomerService customerService;
	@PostMapping
	public HttpResponse<Customer> add(@RequestBody Customer customer) {
		customer.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(customerService.add(customer));
	}

	@PutMapping
	public HttpResponse<Customer> modify(@RequestBody Customer customer) {
		return getDefaultResponse().addData(customerService.modify(customer));
	}

	@PutMapping(path = "/salesStrategy")
	public HttpResponse<Customer> salesStrategy(@RequestBody Customer customer) {
		return getDefaultResponse().addData(customerService.salesStrategy(customer));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		customerService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<Customer>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<Customer>> rep = new HttpResponse<PageResponse<Customer>>();
		PageRequest<Customer> req = parse2PageRequest(request);
		return rep.addData(customerService.listPage(req));
	}

	/**
	 * 功能描述: <br>客户档案列表-多条件查询
	 * 〈〉
	 * @Param: [request]
	 * @Return: com.kry.pms.base.HttpResponse<com.kry.pms.base.PageResponse<com.kry.pms.model.persistence.guest.Customer>>
	 * @Author: huanghaibin
	 * @Date: 2020/3/21 11:07
	 */
	@GetMapping(path = "/queryLikeSQL")
	public HttpResponse<PageResponse<Map<String, Object>>> queryLikeSQL(HttpServletRequest request) throws IOException, TemplateException {
		HttpResponse<PageResponse<Map<String, Object>>> rep = new HttpResponse<PageResponse<Map<String, Object>>>();
		return rep.addData(customerService.listPageBySQL(getCurrentHotleCode(), parse2CommonPageRequest(request)));
	}


	/**
	 * 功能描述: <br>客户档案的模糊查询
	 * 〈〉
	 * @Param: [request]
	 * @Return: com.kry.pms.base.HttpResponse<com.kry.pms.base.PageResponse<com.kry.pms.model.persistence.guest.Customer>>
	 * @Author: huanghaibin
	 * @Date: 2020/3/9 17:31
	 */
	@GetMapping(path = "/queryLike")
	public HttpResponse<PageResponse<Customer>> queryLike(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<Customer>> rep = new HttpResponse<PageResponse<Customer>>();
		PageRequest<Customer> req = parse2PageRequest(request);
		return rep.addData(customerService.listPageQuery(req));
	}

	@GetMapping(path = "/getNum")
	public HttpResponse<String> getNum(){
		HttpResponse<String> rep = new HttpResponse<String>();
		String num = customerService.getNum(getCurrentHotleCode());
		return rep.addData(num);
	}

	/**
	 * 功能描述: <br>客户档案-预订信息查询
	 * 〈〉
	 * @Param: [customerId]
	 * @Return: com.kry.pms.base.HttpResponse<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
	 * @Author: huanghaibin
	 * @Date: 2020/3/13 14:41
	 */
	@GetMapping(path = "/getResverInfo")
	public HttpResponse<List<Map<String, Object>>> getResverInfo(String customerId){
		HttpResponse<List<Map<String, Object>>> rep = new HttpResponse<List<Map<String, Object>>>();
		List<Map<String, Object>> list = customerService.getResverInfo(customerId);
		return rep.addData(list);
	}

	/**
	 * 功能描述: <br>查询单位
	 * 〈〉
	 * @Param: [name, customerType]
	 * @Return: com.kry.pms.base.HttpResponse<java.util.List<com.kry.pms.model.persistence.guest.Customer>>
	 * @Author: huanghaibin
	 * @Date: 2020/3/16 17:10
	 */
	@GetMapping(path = "/getCorp")
	public HttpResponse<List<Customer>> findByHotelCodeAndNameAndCustomerType(String name, String customerType){
		HttpResponse<List<Customer>> rep = new HttpResponse<List<Customer>>();
		List<Customer> list = customerService.findByHotelCodeAndNameAndCustomerType(getCurrentHotleCode(),name,customerType);
		return rep.addData(list);
	}
	/**
	 * 功能描述: <br>查询单位(根据客户类型)
	 * 〈〉查找A：宾客，B：公司，C：旅行社，D：订房中心，E：OTA，F：团队，G：其他
	 * @Param: [customerType, name, numCode]
	 * @Return: com.kry.pms.base.HttpResponse<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
	 * @Author: huanghaibin
	 * @Date: 2020/3/16 19:09
	 */
	@GetMapping(path = "/getCorpT")
	public HttpResponse<List<Map<String, Object>>> getCorpT(String customerType, String name, String numCode){
		HttpResponse<List<Map<String, Object>>> rep = new HttpResponse<List<Map<String, Object>>>();
		List<Map<String, Object>> list = customerService.getTypeIsB(getCurrentHotleCode(), customerType, name, numCode);
		return rep.addData(list);
	}
	/**
	 * 功能描述: <br>查询单位（除了（A）宾客外的所有类型）
	 * 〈〉
	 * @Param: [name, numCode]
	 * @Return: com.kry.pms.base.HttpResponse<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
	 * @Author: huanghaibin
	 * @Date: 2020/6/2 17:27
	 */
	@GetMapping(path = "/getCorpList")
	public HttpResponse<List<Map<String, Object>>> getCorpList(String name, String numCode){
		HttpResponse<List<Map<String, Object>>> rep = new HttpResponse<List<Map<String, Object>>>();
		List<Map<String, Object>> list = customerService.getTypeCorp(getCurrentHotleCode(), name, numCode);
		return rep.addData(list);
	}

	/**
	 * 功能描述: <br>启用，停用
	 * 〈〉
	 * @Param: [isUsed, id]
	 * @Return: com.kry.pms.base.HttpResponse
	 * @Author: huanghaibin
	 * @Date: 2020/3/20 11:01
	 */
	@GetMapping(path = "/updateIsUsed")
	public HttpResponse updateIsUsed(String isUsed, String id){
		HttpResponse rep = new HttpResponse();
		int i = customerService.updateIsUsed(isUsed, id);
		return rep.addData(i);
	}
	/**
	 * 功能描述: <br>删除
	 * 〈〉
	 * @Param: [deleted, id]
	 * @Return: com.kry.pms.base.HttpResponse
	 * @Author: huanghaibin
	 * @Date: 2020/3/20 11:02
	 */
	@GetMapping(path = "/updateDeleted")
	public HttpResponse updateDeleted(String deleted, String id){
		HttpResponse rep = new HttpResponse();
		int i = customerService.updateDeleted(deleted, id);
		return rep.addData(i);
	}

	@GetMapping(path = "/getById")
	public HttpResponse getById(String id){
		HttpResponse rep = new HttpResponse();
		Customer customer = customerService.findById(id);
		return rep.addData(customer);
	}

	/**
	 * 功能描述: <br>今日新增客史
	 * 〈〉
	 * @Param: [id]
	 * @Return: com.kry.pms.base.HttpResponse
	 * @Author: huanghaibin
	 * @Date: 2020/8/11 16:06
	 */
	@GetMapping(path = "/toDayCount")
	public HttpResponse toDayCount(String dateTime){
		HttpResponse rep = new HttpResponse();
		Integer count = customerService.toDayCount(getCurrentHotleCode(), dateTime);
		return rep.addData(count);
	}

}
