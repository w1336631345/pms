package com.kry.pms.service.busi;

import java.time.LocalDate;
import java.util.List;

import com.kry.pms.model.persistence.busi.Bill;
import com.kry.pms.model.persistence.busi.BillItem;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.service.BaseService;

public interface BillItemService extends BaseService<BillItem> {

	BillItem createAndSaveBillItem(Bill bill, RoomRecord rr, LocalDate recordDate);

	BillItem checkAndPayBill(String id);

	List<BillItem> findByIds(List<String> ids);

}