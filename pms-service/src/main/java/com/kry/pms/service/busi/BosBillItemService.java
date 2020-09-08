package com.kry.pms.service.busi;

import com.kry.pms.model.persistence.busi.BosBill;
import com.kry.pms.model.persistence.busi.BosBillItem;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.service.BaseService;

import java.time.LocalDate;
import java.util.List;

public interface BosBillItemService extends BaseService<BosBillItem> {

	BosBillItem createAndSaveBillItem(BosBill bosBill, RoomRecord rr, LocalDate recordDate);

	BosBillItem checkAndPayBill(String id);

    void deleteTrue(String id);

    List<BosBillItem> findByBosBillId(String billId);

	List<BosBillItem> findByIds(List<String> ids);

}