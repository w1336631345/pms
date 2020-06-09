package com.kry.pms.api.busi;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.DtoResponse;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.annotation.OperationLog;
import com.kry.pms.model.http.request.busi.*;
import com.kry.pms.model.http.response.busi.AccountSummaryVo;
import com.kry.pms.model.persistence.busi.BookingRecord;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.service.busi.ReceptionService;
import com.kry.pms.service.org.EmployeeService;
import com.kry.pms.service.room.GuestRoomService;
import com.kry.pms.service.sys.AccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 预定接待
 *
 * @author Louis
 *
 */
@RestController
@RequestMapping(path = "/api/v1/busi/reception")
public class ReceptionController extends BaseController<String> {
	@Autowired
	ReceptionService receptionService;
	@Autowired
	GuestRoomService guestRoomService;
	@Autowired
	EmployeeService employeeService;
	@Autowired
	AccountService accountService;

	/**
	 * 入住
	 *
	 * @param checkIn
	 * @return
	 */
	@PostMapping(path = "/in")
	public HttpResponse<String> checkIn(@RequestBody @Valid CheckInBo checkIn) {
		HttpResponse<String> rep = new HttpResponse<String>();
		BeanUtils.copyProperties(receptionService.checkIn(checkIn), rep);
		return rep;
	}

	/**
	 * 出
	 *
	 * @param
	 * @return
	 */
	@PostMapping(path = "/checkout")
	public HttpResponse<String> checkOut(@RequestBody @Valid CheckOutBo checkOutIn) {
		HttpResponse<String> rep = new HttpResponse<String>();
		return rep;
	}

	/**
	 * 功能描述: <br>批量入住
	 * 〈〉
	 * @Param: [ids]
	 * @Return: com.kry.pms.base.HttpResponse
	 * @Author: huanghaibin
	 * @Date: 2019/12/17 15:52
	 */
	@PostMapping(path = "/checkInAll")
	@OperationLog(remark = "批量入住")
	public HttpResponse checkIn(@RequestBody String[] ids) {
		HttpResponse<String> rep = new HttpResponse<String>();
		BeanUtils.copyProperties(receptionService.checkInAll(ids,getUser()), rep);
		return rep;
	}
	/**
	 * 功能描述: <br>入住（在用）
	 * 〈〉
	 * @Param: [id]
	 * @Return: com.kry.pms.base.HttpResponse<java.lang.String>
	 * @Author: huanghaibin
	 * @Date: 2020/2/28 10:42
	 */
	@GetMapping(path = "/checkIn/{id}")
	@OperationLog(remark = "入住")
	public HttpResponse<String> checkIn(@PathVariable String id) {
		HttpResponse<String> rep = new HttpResponse<String>();
		BeanUtils.copyProperties(receptionService.checkInM(id, getUser()), rep);
		return rep;
	}

	@GetMapping(path="/account/summary/{id}")
	public HttpResponse<AccountSummaryVo> getAccountSummary(@PathVariable String id){
		HttpResponse<AccountSummaryVo> rep = new HttpResponse<>();
		AccountSummaryVo accountSummaryVo = receptionService.getAccountSummaryByCheckRecordId(id);
		rep.setData(accountSummaryVo);
		return rep;
	}

	/**
	 * 结账确认
	 * @return
	 */
	@GetMapping(path="/bill/check/confirm/group/{id}")
	@OperationLog(remark = "结账确认")
	public HttpResponse<List<AccountSummaryVo>> groupCheckBillConfirm(@PathVariable String id){
		HttpResponse<List<AccountSummaryVo>> rep = new HttpResponse<List<AccountSummaryVo>>();
		DtoResponse<List<AccountSummaryVo>> data = receptionService.groupCheckBillConfirm(id);
		BeanUtils.copyProperties(data, rep);
		rep.setData(data.getData());
		return rep;
	}
	/**
	 * 团队结账
	 * @return
	 */
	@GetMapping(path="/bill/check/group/{id}")
	@OperationLog(remark = "团队结账")
	public HttpResponse<String> groupCheckBill(){
		HttpResponse<String> rep = new HttpResponse<String>();
		return rep;
	}
	/**
	 * 宾客结账
	 *
	 * @return
	 */
	@PostMapping(path="/bill/check")
	@OperationLog(remark = "宾客结账")
	public HttpResponse<AccountSummaryVo> customerCheckBill(@RequestBody BillCheckBo	billCheckBo){
		billCheckBo.setHotelCode(getCurrentHotleCode());
		billCheckBo.setShiftCode(getShiftCode());
		billCheckBo.setOperationEmployee(getCurrentEmployee());
		HttpResponse<AccountSummaryVo> rep = new HttpResponse<AccountSummaryVo>();
		DtoResponse<Account> data = accountService.checkCustomerBill(billCheckBo);
		BeanUtils.copyProperties(data, rep);
		if(data.getData()!=null){
			rep.setData(new AccountSummaryVo(data.getData()));
		}
		return rep;
	}

	/**
	 * 排房
	 *
	 * @param
	 * @return
	 */
	@PostMapping(path = "/assign")
	@OperationLog(remark = "排房")
	public HttpResponse<String> assignRoom(@RequestBody @Valid RoomAssignBo roomAssignBo) {
		HttpResponse<String> rep = new HttpResponse<String>();
		BeanUtils.copyProperties(receptionService.assignRoom(roomAssignBo), rep);
		return rep;
	}

	/**
	 * 预定
	 *
	 * @param
	 * @return
	 */
	@PostMapping(path = "/newBook")
	public HttpResponse<String> book(@RequestBody BookingRecord br) {
		br.setHotelCode(getCurrentHotleCode());
		br.setOperationEmployee(getCurrentEmployee());
		DtoResponse<BookingRecord> dtoRep = receptionService.groupBook(br);
		HttpResponse<String> rep = new HttpResponse<String>(dtoRep);
		return rep;
	}

	/**
	 * 续住
	 *
	 * @param renew
	 * @return
	 */
	@PostMapping(path = "/renew")
	public HttpResponse<String> renew(@RequestBody RenewBo renew) {
		HttpResponse<String> rep = new HttpResponse<String>();
		return rep;
	}

}
