package com.kry.pms.service.busi.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.BillItemDao;
import com.kry.pms.dao.busi.BosBillItemDao;
import com.kry.pms.model.persistence.busi.*;
import com.kry.pms.service.busi.BillItemService;
import com.kry.pms.service.busi.BosBillItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BosBillItemServiceImpl implements BosBillItemService {
	@Autowired
	BosBillItemDao bosBillItemDao;

	@Override
	public BosBillItem add(BosBillItem bosBillItem) {
		return bosBillItemDao.saveAndFlush(bosBillItem);
	}

	@Override
	public void delete(String id) {
		BosBillItem bosBillItem = bosBillItemDao.findById(id).get();
		if (bosBillItem != null) {
			bosBillItem.setDeleted(Constants.DELETED_TRUE);
		}
		modify(bosBillItem);
	}
	@Override
	public void deleteTrue(String id) {
		bosBillItemDao.deleteById(id);
	}

	@Override
	public BosBillItem modify(BosBillItem bosBillItem) {
		return bosBillItemDao.saveAndFlush(bosBillItem);
	}

	@Override
	public BosBillItem findById(String id) {
		return bosBillItemDao.findById(id).orElse(null);
	}

	@Override
	public List<BosBillItem> getAllByHotelCode(String code) {
		return null;// 默认不实现
		// return billItemDao.findByHotelCode(code);
	}
	@Override
	public List<BosBillItem> findByBosBillId(String billId) {
		List<BosBillItem> list = bosBillItemDao.findByBosBillId(billId);
		return list;
	}


	@Override
	public List<BosBillItem> findByIds(List<String> ids) {
		return bosBillItemDao.findAllById(ids);
	}

	@Override
	public PageResponse<BosBillItem> listPage(PageRequest<BosBillItem> prq) {
		Example<BosBillItem> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(bosBillItemDao.findAll(ex, req));
	}

	@Override
	public BosBillItem createAndSaveBillItem(BosBill bosBill, RoomRecord rr, LocalDate recordDate) {
		BosBillItem bosBillItem = new BosBillItem();
		bosBillItem.setBosBill(bosBill);
		bosBillItem.setRoomRecord(rr);
		bosBillItem.setType(Constants.Type.BILL_ITEM_ROOM_RECORD);
		bosBillItem.setTotal(rr.getCost());
		if (bosBillItem.getTotal() != null && bosBillItem.getTotal() > 0) {
			bosBillItem.setStatusPayment(Constants.Status.BILL_NEED_SETTLED);
		}
		bosBillItem.setBillDate(recordDate);
		bosBillItem.setItemSeq(bosBill.getCurrentItemSeq() + 1);
		return add(bosBillItem);
	}

	@Override
	public BosBillItem checkAndPayBill(String id) {
		BosBillItem item = findById(id);
		if (item != null) {
			item.setStatusPayment(Constants.Status.BILL_SETTLED);
		}
		return null;
	}

}
