package com.kry.pms.api.busi;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import com.kry.pms.model.http.request.busi.CheckUpdateItemBo;
import com.kry.pms.model.http.request.busi.CheckUpdateItemTestBo;
import com.kry.pms.model.persistence.sys.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.DtoResponse;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.http.request.busi.TogetherBo;
import com.kry.pms.model.http.response.busi.CheckInRecordListVo;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.service.busi.CheckInRecordService;

@RestController
@RequestMapping(path = "/api/v1/busi/checkInRecord")
public class CheckInRecordController extends BaseController<CheckInRecord> {
	@Autowired
	CheckInRecordService checkInRecordService;
	@PostMapping
	public HttpResponse<CheckInRecord> add(@RequestBody CheckInRecord checkInRecord) {
		return getDefaultResponse().addData(checkInRecordService.add(checkInRecord));
	}

	@PutMapping
	public HttpResponse<CheckInRecord> modify(@RequestBody CheckInRecord checkInRecord) {
		return getDefaultResponse().addData(checkInRecordService.modify(checkInRecord));
	}
	
	@PostMapping(path = "/book")
	public HttpResponse<CheckInRecord> book(@RequestBody CheckInRecord checkInRecord) {
		checkInRecord.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(checkInRecordService.book(checkInRecord));
	}
	
	@PostMapping(path = "/reserve")
	public HttpResponse<List<CheckInRecord>> addReserve(@RequestBody List<CheckInRecord> checkInRecords){
		HttpResponse<List<CheckInRecord>> rep = new HttpResponse();
		return rep.addData(checkInRecordService.addReserve(checkInRecords));

	}
	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		checkInRecordService.delete(id);
		return rep;
	}
	@GetMapping(path = "/book/{id}")
	public HttpResponse<List<CheckInRecord>> findByBookingId(@PathVariable("id") String bookId){
		HttpResponse<List<CheckInRecord>> rep = new HttpResponse<List<CheckInRecord>>();
		rep.addData(checkInRecordService.findByBookId(bookId));
		return rep;
	}
	@GetMapping(path = "/detail/book/{id}")
	public HttpResponse<List<CheckInRecord>> findDetailByBookingId(@PathVariable("id") String bookId){
		HttpResponse<List<CheckInRecord>> rep = new HttpResponse<List<CheckInRecord>>();
		rep.addData(checkInRecordService.findDetailByBookingId(bookId));
		return rep;
	}
	@GetMapping(path = "/detail/{id}")
	public HttpResponse<CheckInRecord> findDetailById(@PathVariable("id") String id){
		HttpResponse<CheckInRecord> rep = new HttpResponse<CheckInRecord>();
		rep.addData(checkInRecordService.findById(id));
		return rep;
	}
	@GetMapping(path="/orderNum/{orderNum}")
	public HttpResponse<List<CheckInRecord>> findDetailByOrderNum(@PathVariable("orderNum") String orderNum){
		HttpResponse<List<CheckInRecord>> rep = new HttpResponse<List<CheckInRecord>>();
		rep.addData(checkInRecordService.findByOrderNum(orderNum));
		return rep;
	}
	@PostMapping(path="/together")
	public HttpResponse<CheckInRecord> addCustomerTogether(@RequestBody TogetherBo togetherBo){
		return getDefaultResponse().addData(checkInRecordService.addTogether(togetherBo));
	}

	/**
	 * 功能描述: <br>批量修改
	 * 〈〉
	 * @Param: [checkUpdateItemBo]
	 * @Return: com.kry.pms.base.HttpResponse
	 * @Author: huanghaibin
	 * @Date: 2019/12/11 15:44
	 */
	@PostMapping(path="/updateItem")
	public HttpResponse updateItem(@RequestBody CheckUpdateItemTestBo checkUpdateItemTestBo){
		HttpResponse hr = new HttpResponse();
		User user = getUser();
		if(user == null){
			return hr.loginError();
		}
		checkInRecordService.updateAll(checkUpdateItemTestBo);
		return hr.ok();
	}
	/**
	 * 功能描述: <br>取消入住
	 * 〈〉
	 * @Param: [ids]
	 * @Return: com.kry.pms.base.HttpResponse
	 * @Author: huanghaibin
	 * @Date: 2019/12/12 18:15
	 */
	@PostMapping(path="/cancelIn")
	public HttpResponse cancelIn(@RequestBody String[] ids){
		HttpResponse hr = new HttpResponse();
		User user = getUser();
		if(user == null){
			return hr.loginError();
		}
		checkInRecordService.cancelIn(ids);
		return hr.ok();
	}
	/**
	 * 功能描述: <br>批量取消排房
	 * 〈〉
	 * @Param: [roomAssignBo]
	 * @Return: com.kry.pms.base.HttpResponse
	 * @Author: huanghaibin
	 * @Date: 2019/12/16 11:09
	 */
	@PostMapping(path = "/callOffAssignRoom")
	public HttpResponse callOffAssignRoom(@RequestBody String[] ids) {
		HttpResponse rep = new HttpResponse();
		rep = checkInRecordService.callOffAssignRoom(ids);
		return rep;
	}
	/**
	 * 功能描述: <br>批量取消预订
	 * 〈〉
	 * @Param: [ids]
	 * @Return: com.kry.pms.base.HttpResponse
	 * @Author: huanghaibin
	 * @Date: 2019/12/17 11:59
	 */
	@PostMapping(path = "/callOffReserve")
	public HttpResponse callOffReserve(@RequestBody String[] ids) {
		HttpResponse rep = new HttpResponse();
		rep = checkInRecordService.callOffReserve(ids);
		return rep;
	}

	public HttpResponse<PageResponse<CheckInRecordListVo>> queryHistory(){
		HttpResponse<PageResponse<CheckInRecordListVo>> rep = new HttpResponse<PageResponse<CheckInRecordListVo>>();
		return rep;
	}
	@GetMapping("/hangUp/account/{accountId}")
	public HttpResponse<String> hangUp(@PathVariable("accountId") String id){
		HttpResponse<String> rep = new HttpResponse<>();
		DtoResponse<String> response = checkInRecordService.hangUpByAccountId(id);
		BeanUtils.copyProperties(response, rep);
		return rep;
	}
	@GetMapping
	public HttpResponse<PageResponse<CheckInRecord>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<CheckInRecord>> rep = new HttpResponse<PageResponse<CheckInRecord>>();
		PageRequest<CheckInRecord> req = parse2PageRequest(request);
		return rep.addData(checkInRecordService.listPage(req));
	}
	@GetMapping(path = "/summary")
	public HttpResponse<PageResponse<CheckInRecordListVo>> querySummaryList(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<CheckInRecordListVo>> rep = new HttpResponse<PageResponse<CheckInRecordListVo>>();
		PageRequest<CheckInRecord> req = parse2PageRequest(request);
		return rep.addData(checkInRecordService.querySummaryList(req));
	}

	/**
	 * 功能描述: <br>同住列表
	 * 〈〉
	 * @Param: [roomLinkId]
	 * @Return: com.kry.pms.base.HttpResponse
	 * @Author: huanghaibin
	 * @Date: 2019/12/23 14:38
	 */
	@GetMapping(value = "/checkInTogether")
	public HttpResponse checkInTogether(String orderNum){
		HttpResponse hr = new HttpResponse();
		User user = getUser();
		List<CheckInRecord> list = checkInRecordService.checkInTogether(user.getHotelCode(),orderNum);
		hr.setData(list);
		return hr;
	}

	/**
	 * 功能描述: <br>同住列表（根据同住编码查询）暂时没用
	 * 〈〉
	 * @Param: [orderNum]
	 * @Return: com.kry.pms.base.HttpResponse
	 * @Author: huanghaibin
	 * @Date: 2019/12/23 16:34
	 */
	@GetMapping(value = "/togetherByCode")
	public HttpResponse togetherByCode(String orderNum){
		HttpResponse hr = new HttpResponse();
		User user = getUser();
//		List<CheckInRecord> list = checkInRecordService.checkInTogether(user.getHotelCode(),orderNum);
//		hr.setData(list);
		return hr;
	}

	/**
	 * 功能描述: <br>添加同住
	 * 〈〉
	 * @Param: [orderNum, customerId]
	 * @Return: com.kry.pms.base.HttpResponse
	 * @Author: huanghaibin
	 * @Date: 2019/12/23 17:38
	 */
	@GetMapping(value = "/addTogether")
	public HttpResponse addTogether(String orderNum, String customerId, String status){
		HttpResponse hr = new HttpResponse();
		User user = getUser();
		checkInRecordService.addTogether(user.getHotelCode(), orderNum, customerId, status);
		return hr.ok();
	}

}
