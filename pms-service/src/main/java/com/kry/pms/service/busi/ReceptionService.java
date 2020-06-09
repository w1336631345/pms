package com.kry.pms.service.busi;

import java.time.LocalDate;
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
import com.kry.pms.model.persistence.sys.User;

public interface ReceptionService {

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
	 * 结账
	 * @param bsb
	 * @return
	 */

	public DtoResponse<String> billSettle(BillSettleBo bsb);

	public DtoResponse<String> assignRoom(@Valid RoomAssignBo roomAssignBo);

	public DtoResponse<BookingRecord> groupBook(BookingRecord br);

	DtoResponse<String> checkInM(String id, User user);

	DtoResponse<String> checkIn(CheckInRecord cir);

    public AccountSummaryVo getAccountSummaryByCheckRecordId(String id);

	public DtoResponse<List<AccountSummaryVo>> groupCheckBillConfirm(String id);

    DtoResponse<String> checkInAll(String[] ids,User user);

	DtoResponse<String> checkInAuditRoomRecord(CheckInRecord cir, LocalDate businessDate, User user);
}
