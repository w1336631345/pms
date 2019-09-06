package com.kry.pms.api.busi;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.DtoResponse;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.http.request.busi.BillItemBo;
import com.kry.pms.model.http.request.busi.BillSettleBo;
import com.kry.pms.model.http.request.busi.BookingBo;
import com.kry.pms.model.http.request.busi.CheckInBo;
import com.kry.pms.model.http.request.busi.CheckOutBo;
import com.kry.pms.model.http.request.busi.RenewBo;
import com.kry.pms.model.persistence.busi.BookingRecord;
import com.kry.pms.service.busi.ReceptionService;

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
	 * 预定
	 * 
	 * @param book
	 * @return
	 */
	@PostMapping(path = "/book")
	public HttpResponse<String> apply(@RequestBody @Valid BookingBo book) {
		DtoResponse<BookingRecord> dtoRep = receptionService.book(book);
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
