package com.kry.pms.service.busi.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.BillItemDao;
import com.kry.pms.model.persistence.busi.Bill;
import com.kry.pms.model.persistence.busi.BillItem;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.service.busi.BillItemService;

@Service
public class BillItemServiceImpl implements BillItemService {
	@Autowired
	BillItemDao billItemDao;

	@Override
	public BillItem add(BillItem billItem) {
		return billItemDao.saveAndFlush(billItem);
	}

	@Override
	public void delete(String id) {
		BillItem billItem = billItemDao.findById(id).get();
		if (billItem != null) {
			billItem.setDeleted(Constants.DELETED_TRUE);
		}
		modify(billItem);
	}

	@Override
	public BillItem modify(BillItem billItem) {
		return billItemDao.saveAndFlush(billItem);
	}

	@Override
	public BillItem findById(String id) {
		return billItemDao.findById(id).orElse(null);
	}

	@Override
	public List<BillItem> getAllByHotelCode(String code) {
		return null;// 默认不实现
		// return billItemDao.findByHotelCode(code);
	}

	@Override
	public List<BillItem> findByIds(List<String> ids) {
		return billItemDao.findAllById(ids);
	}

	@Override
	public PageResponse<BillItem> listPage(PageRequest<BillItem> prq) {
		Example<BillItem> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(billItemDao.findAll(ex, req));
	}

	@Override
	public BillItem createAndSaveBillItem(Bill bill, RoomRecord rr, LocalDate recordDate) {
		BillItem billItem = new BillItem();
		billItem.setBill(bill);
		billItem.setRoomRecord(rr);
		billItem.setType(Constants.Type.BILL_ITEM_ROOM_RECORD);
		billItem.setTotal(rr.getCost());
		if (billItem.getTotal() != null && billItem.getTotal() > 0) {
			billItem.setStatusPayment(Constants.Status.BILL_TO_BE_PAID);
		}
		billItem.setBillDate(recordDate);
		billItem.setItemSeq(bill.getCurrentItemSeq() + 1);
		return add(billItem);
	}

	@Override
	public BillItem checkAndPayBill(String id) {
		BillItem item = findById(id);
		if (item != null) {
			item.setStatusPayment(Constants.Status.BILL_PAYMENTED);
		}
		return null;
	}

}
