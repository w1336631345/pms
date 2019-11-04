
package com.kry.pms.api.busi;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.DtoResponse;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.http.request.busi.BillCheckBo;
import com.kry.pms.model.http.request.busi.BillItemBo;
import com.kry.pms.model.http.request.busi.BillSettleBo;
import com.kry.pms.model.http.request.busi.BookingBo;
import com.kry.pms.model.http.request.busi.CheckInBo;
import com.kry.pms.model.http.request.busi.CheckOutBo;
import com.kry.pms.model.http.request.busi.RenewBo;
import com.kry.pms.model.http.request.busi.RoomAssignBo;
import com.kry.pms.model.http.response.busi.AccountSummaryVo;
import com.kry.pms.model.persistence.busi.BookingRecord;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.service.busi.ReceptionService;
import com.kry.pms.service.org.EmployeeService;
import com.kry.pms.service.room.GuestRoomService;
import com.kry.pms.service.sys.AccountService;

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
	
	@GetMapping(path = "/checkIn/{id}")
	public HttpResponse<String> checkIn(@PathVariable String id) {
		HttpResponse<String> rep = new HttpResponse<String>();
		BeanUtils.copyProperties(receptionService.checkIn(id), rep);
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
	 * 入住
	 * 
	 * @param checkIn
	 * @return
	 */
	@PostMapping(path = "/out")
	public HttpResponse<String> checkOut(@RequestBody @Valid CheckOutBo checkOut) {
		HttpResponse<String> rep = new HttpResponse<String>();
		BeanUtils.copyProperties(receptionService.checkOut(checkOut), rep);
		return rep;
	}
	/**
	 * 结账确认
	 * @return
	 */
	@GetMapping(path="/bill/check/confirm/group/{id}")
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
	public HttpResponse<Account> customerCheckBill(@RequestBody BillCheckBo	billCheckBo){
		billCheckBo.setHotelCode(getCurrentHotleCode());
		billCheckBo.setOperationEmployee(getCurrentEmployee());
		HttpResponse<Account> rep = new HttpResponse<Account>();
		DtoResponse<Account> data = accountService.checkCustomerBill(billCheckBo);
		BeanUtils.copyProperties(data, rep);
		return rep;
	}

	/**
	 * 入住
	 * 
	 * @param checkIn
	 * @return
	 */
	@PostMapping(path = "/assign")
	public HttpResponse<String> assignRoom(@RequestBody @Valid RoomAssignBo roomAssignBo) {
		HttpResponse<String> rep = new HttpResponse<String>();
		BeanUtils.copyProperties(receptionService.assignRoom(roomAssignBo), rep);
		return rep;
	}

	/**
	 * 预定
	 * 
	 * @param book
	 * @return
	 */
	@PostMapping(path = "/book")
	public HttpResponse<String> apply(@RequestBody @Valid BookingBo book) {
		book.setHotelCode(getCurrentHotleCode());
		book.setOperationId(getCurrentEmployee().getId());
		DtoResponse<BookingRecord> dtoRep = receptionService.book(book);
		HttpResponse<String> rep = new HttpResponse<String>(dtoRep);
		return rep;
	}
	
	/**
	 * 预定
	 * 
	 * @param book
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

	/**
	 * 入账
	 * 
	 * @param billItemBo
	 * @return
	 */
	@PostMapping(path = "/account_entry")
	public HttpResponse<String> accountEntry(@RequestBody BillItemBo billItemBo) {
		HttpResponse<String> rep = new HttpResponse<String>();
		return rep;
	}

	/**
	 * 拆分账单
	 * 
	 * @return
	 */
	@PostMapping(path = "/split_bill")
	public HttpResponse<String> splitBiil() {
		HttpResponse<String> rep = new HttpResponse<String>();
		return rep;
	}

	/**
	 * 转移账单
	 * 
	 * @return
	 */
	@PostMapping(path = "/shift_bill")
	public HttpResponse<String> shiftBill() {
		HttpResponse<String> rep = new HttpResponse<String>();
		return rep;
	}

	@PostMapping(path = "/settle_bill")
	public HttpResponse<String> settleBill(BillSettleBo bsb) {
		HttpResponse<String> rep = new HttpResponse<String>();
		BeanUtils.copyProperties(receptionService.billSettle(bsb), rep);
		return rep;
	}

}
