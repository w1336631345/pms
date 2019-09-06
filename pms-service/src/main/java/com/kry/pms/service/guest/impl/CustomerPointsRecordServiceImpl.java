package com.kry.pms.service.guest.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.CustomerPointsRecordDao;
import com.kry.pms.model.persistence.guest.CustomerPointsRecord;
import com.kry.pms.service.guest.CustomerPointsRecordService;

@Service
public class  CustomerPointsRecordServiceImpl implements  CustomerPointsRecordService{
	@Autowired
	 CustomerPointsRecordDao customerPointsRecordDao;
	 
	 @Override
	public CustomerPointsRecord add(CustomerPointsRecord customerPointsRecord) {
		return customerPointsRecordDao.saveAndFlush(customerPointsRecord);
	}

	@Override
	public void delete(String id) {
		CustomerPointsRecord customerPointsRecord = customerPointsRecordDao.findById(id).get();
		if (customerPointsRecord != null) {
			customerPointsRecord.setDeleted(Constants.DELETED_TRUE);
		}
		customerPointsRecordDao.saveAndFlush(customerPointsRecord);
	}

	@Override
	public CustomerPointsRecord modify(CustomerPointsRecord customerPointsRecord) {
		return customerPointsRecordDao.saveAndFlush(customerPointsRecord);
	}

	@Override
	public CustomerPointsRecord findById(String id) {
		return customerPointsRecordDao.getOne(id);
	}

	@Override
	public List<CustomerPointsRecord> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return customerPointsRecordDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<CustomerPointsRecord> listPage(PageRequest<CustomerPointsRecord> prq) {
		Example<CustomerPointsRecord> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(customerPointsRecordDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
