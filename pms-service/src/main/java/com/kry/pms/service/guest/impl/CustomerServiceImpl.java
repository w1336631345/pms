package com.kry.pms.service.guest.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.CustomerDao;
import com.kry.pms.model.http.request.busi.GuestInfoBo;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.guest.GuestInfo;
import com.kry.pms.service.guest.CustomerService;
import com.kry.pms.service.guest.GuestInfoService;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	CustomerDao customerDao;
	@Autowired
	GuestInfoService guestInfoService;

	@Override
	public Customer add(Customer customer) {
		return customerDao.saveAndFlush(customer);
	}

	@Override
	public void delete(String id) {
		Customer customer = customerDao.findById(id).get();
		if (customer != null) {
			customer.setDeleted(Constants.DELETED_TRUE);
		}
		customerDao.saveAndFlush(customer);
	}

	@Override
	public Customer modify(Customer customer) {
		if(customer.getIdCardNum()!=null) {
			Customer dbc = customerDao.findByIdCardNum(customer.getIdCardNum());
			if(dbc!=null) {
				return dbc;
			}
		}
		return customerDao.saveAndFlush(customer);
	}

	@Override
	public Customer findById(String id) {
		return customerDao.getOne(id);
	}

	@Override
	public List<Customer> getAllByHotelCode(String code) {
		return null;// 默认不实现
		// return customerDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<Customer> listPage(PageRequest<Customer> prq) {
		Example<Customer> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(customerDao.findAll(ex, req));
	}

	@Override
	public Customer createOrGetCustomer(GuestInfoBo guestInfoBo) {
		String idCardNum = guestInfoBo.getIdCardNum();
		GuestInfo info = guestInfoService.findByIdCardNum(idCardNum);
		Customer customer = null;
		if (info == null) {
			info = new GuestInfo();
			BeanUtils.copyProperties(guestInfoBo, info);
			info = guestInfoService.add(info);
			customer = initCustomerAndSave(info);
		} else {
			customer = new Customer();
			customer.setGuestInfo(info);
			Example<Customer> ex = Example.of(customer);
			customer = customerDao.findOne(ex).get();
			if (customer == null) {
				customer = initCustomerAndSave(info);
			}
		}
		return customer;
	}

	private Customer initCustomerAndSave(GuestInfo guestInfo) {
		Customer customer = new Customer();
		customer.setGuestInfo(guestInfo);
		customer = add(customer);
		return customer;
	}

	@Override
	public Customer createTempCustomer(String tempName) {
		Customer customer = new Customer();
		customer.setName(tempName);
		customer = add(customer);
		return customer;
	}

}
