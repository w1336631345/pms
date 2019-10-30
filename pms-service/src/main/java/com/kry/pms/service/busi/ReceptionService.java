package com.kry.pms.service.busi;

import java.util.List;

import javax.validation.Valid;

import com.kry.pms.base.DtoResponse;
import com.kry.pms.model.http.request.busi.BillItemBo;
import com.kry.pms.model.http.request.busi.BillSettleBo;
import com.kry.pms.model.http.request.busi.BookingBo;
import com.kry.pms.model.http.request.busi.CheckInBo;
import com.kry.pms.model.http.request.busi.CheckOutBo;
import com.kry.pms.model.http.request.busi.RoomAssignBo;
import com.kry.pms.model.http.response.busi.AccountSummaryVo;
import com.kry.pms.model.http.response.busi.CheckOutVo;
import com.kry.pms.model.persistence.busi.BillItem;
import com.kry.pms.model.persistence.busi.BookingRecord;
import com.kry.pms.model.persistence.busi.CheckInRecord;

public interface ReceptionService {
	/**
	 * 
	 * @param book
	 * @return
	 */
	public DtoResponse<BookingRecord> book(BookingBo book);

	/**
	 * 
	 * @param checkInBo
	 * @return
	 */
	public DtoResponse<List<CheckInRecord>> checkIn(CheckInBo checkInBo);

	/**
	 * 
	 * @param billItemBo
	 * @return
	 */
	public DtoResponse<List<BillItem>> accountEntry(BillItemBo billItemBo);

	/**
	 * 简易退房&结账
	 * 
	 * @param checkOut
	 * @return
	 */
	public DtoResponse<CheckOutVo> checkOut(CheckOutBo checkOut);

	/**
	 * 结账
	 * @param bsb
	 * @return
	 */

	public DtoResponse<String> billSettle(BillSettleBo bsb);

	public DtoResponse<String> assignRoom(@Valid RoomAssignBo roomAssignBo);

	DtoResponse<BookingRecord> groupBook(BookingRecord br);

	public DtoResponse<String> checkIn(String id);

	public AccountSummaryVo getAccountSummaryByCheckRecordId(String id);
}
