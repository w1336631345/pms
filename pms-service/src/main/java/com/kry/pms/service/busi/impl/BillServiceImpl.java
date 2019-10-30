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
import com.kry.pms.dao.busi.BillDao;
import com.kry.pms.model.http.request.busi.BillBo;
import com.kry.pms.model.http.request.busi.BillSettleBo;
import com.kry.pms.model.persistence.busi.Bill;
import com.kry.pms.model.persistence.busi.BillItem;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.model.persistence.goods.Product;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.model.persistence.sys.BusinessSeq;
import com.kry.pms.service.busi.BillItemService;
import com.kry.pms.service.busi.BillService;
import com.kry.pms.service.goods.ProductService;
import com.kry.pms.service.sys.BusinessSeqService;

@Service
public class BillServiceImpl implements BillService {
	@Autowired
	BillDao billDao;
	@Autowired
	BillItemService billItemService;
	@Autowired
	ProductService productService;
	@Autowired
	BusinessSeqService businessSeqService;
	@Override
	public Bill add(Bill bill) {
		if(bill.getProduct()!=null&&bill.getProduct().getId()!=null) {
			Product p = productService.findById(bill.getProduct().getId());
			if(p!=null) {
				if(1==p.getDirection()) {
					bill.setCost(bill.getTotal());
				}else {
					bill.setPay(bill.getTotal());
				}
				bill.setType(p.getType());
				bill.setBusinessDate(businessSeqService.getBuinessDate(p.getHotelCode()));
			}
		}else {
			return null;
		}
		return billDao.saveAndFlush(bill);
	}

	@Override
	public void delete(String id) {
		Bill bill = billDao.findById(id).get();
		if (bill != null) {
			bill.setDeleted(Constants.DELETED_TRUE);
		}
		billDao.saveAndFlush(bill);
	}

	@Override
	public Bill modify(Bill bill) {
		return billDao.saveAndFlush(bill);
	}

	@Override
	public Bill findById(String id) {
		return billDao.findById(id).orElse(null);
	}

	@Override
	public List<Bill> getAllByHotelCode(String code) {
		return null;// 默认不实现
		// return billDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<Bill> listPage(PageRequest<Bill> prq) {
		Example<Bill> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(billDao.findAll(ex, req));
	}

	@Override
	public boolean billEntry(RoomRecord rr, LocalDate recordDate) {
		Account account = rr.getCheckInRecord().getAccount();
//		BillItem billItem = billItemService.createAndSaveBillItem(bill, rr, recordDate);
//		bill.setCurrentItemSeq(billItem.getItemSeq());
//		bill.setTotal(billItem.getTotal() + bill.getTotal());
		return true;
	}

	@Override
	public boolean checkAndPayBill(List<CheckInRecord> crs, double total) {
		double billTatal = 0.0;
		for (CheckInRecord cir : crs) {
//			billTatal += cir.getBill().getTotal();
		}
		if (total == billTatal) {

		}
		return false;

	}

	@Override
	public boolean checkAndPayBill(BillSettleBo bsb) {
		double actualTotal = 0.0;
		for (BillBo bb : bsb.getBills()) {
			Bill bill = findById(bb.getBillId());
			if (bill != null) {
				List<String> itemIds = bb.getItems();
				List<BillItem> bis = billItemService.findByIds(itemIds);
				if (bis != null && !bis.isEmpty()) {
					for (BillItem bi : bis) {
						actualTotal += bi.getTotal();
						bi.setStatus(Constants.Status.BILL_PAYMENTED);
						billItemService.modify(bi);
					}
				}
			} else {
				// TODO
			}
		}
		if (bsb.getTatal() == actualTotal) {
			return true;
		}
		return false;
	}

	@Override
	public List<Bill> findByAccountId(String id) {
		return billDao.findByAccountId(id);
	}

}
