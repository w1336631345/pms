package com.kry.pms.service.busi;

import java.time.LocalDate;
import java.util.List;

import com.kry.pms.model.http.request.busi.BillSettleBo;
import com.kry.pms.model.persistence.busi.Bill;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.service.BaseService;

public interface BillService extends BaseService<Bill> {

	public boolean billEntry(RoomRecord rr, LocalDate recordDate);

	/**
	 * 检查并支付账单
	 * @param crs
	 * @param total
	 * @return
	 */
	public boolean checkAndPayBill(List<CheckInRecord> crs, double total);

	public boolean checkAndPayBill(BillSettleBo bsb);

}