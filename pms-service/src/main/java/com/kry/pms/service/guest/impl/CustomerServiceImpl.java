package com.kry.pms.service.guest.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.marketing.RoomPriceScheme;
import com.kry.pms.service.sys.BusinessSeqService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
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

import javax.persistence.criteria.*;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	CustomerDao customerDao;
	@Autowired
	GuestInfoService guestInfoService;
	@Autowired
	BusinessSeqService businessSeqService;

	@Override
	public Customer add(Customer customer) {
		if(customer.getNumCode() == null){
			String num = businessSeqService.fetchNextSeqNum(customer.getHotelCode(), Constants.Key.CUSTOMER_NUM);
			customer.setNumCode(num);
		}
		return customerDao.saveAndFlush(customer);
	}

	@Override
	public String getNum(String hotelCode){
		String num = businessSeqService.fetchNextSeqNum(hotelCode, Constants.Key.CUSTOMER_NUM);
		return num;
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
//		if(customer.getIdCardNum()!=null) {
//			Customer dbc = customerDao.findByHotelCodeAndIdCardNum(customer.getHotelCode(),customer.getIdCardNum());
//			if(dbc!=null) {
//				return dbc;
//			}
//		}
		return customerDao.saveAndFlush(customer);
	}

	@Override
	public Customer salesStrategy(Customer customer) {
		Customer cust = customerDao.getOne(customer.getId());
//		List<String> roomPriceIds = customerDao.getByCustId(customer.getId());
		cust.setRoomPriceSchemes(customer.getRoomPriceSchemes());
		return customerDao.saveAndFlush(cust);
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
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withMatcher("name" ,ExampleMatcher.GenericPropertyMatchers.contains())
				.withMatcher("address" ,ExampleMatcher.GenericPropertyMatchers.contains());
		Example<Customer> ex = Example.of(prq.getExb(),matcher);
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
	public PageResponse<Customer> listPageQuery(PageRequest<Customer> prq) {
		Pageable page = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		Page<Customer> p = null;
		if(("Y").equals(prq.getExb().getIsLike())){
			p = customerDao.queryLike(page, prq.getExb().getName());
		}else {
			ExampleMatcher matcher = ExampleMatcher.matching()
					.withMatcher("name" ,ExampleMatcher.GenericPropertyMatchers.contains())
					.withMatcher("address" ,ExampleMatcher.GenericPropertyMatchers.contains());
			Example<Customer> ex = Example.of(prq.getExb(),matcher);
			org.springframework.data.domain.PageRequest req;
			if (prq.getOrderBy() != null) {
				Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
				req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
			} else {
				req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
			}
			p = customerDao.findAll(ex, req);
		}
		return convent(p);
	}

	@Override
	public Customer createOrGetCustomer(String hotelCode,GuestInfoBo guestInfoBo) {
		String idCardNum = guestInfoBo.getIdCardNum();
		GuestInfo info = guestInfoService.findByIdCardNum(idCardNum);
		Customer customer = null;
		if (info == null) {
			info = new GuestInfo();
			BeanUtils.copyProperties(guestInfoBo, info);
			info = guestInfoService.add(info);
			customer = initCustomerAndSave(hotelCode,info);
		} else {
			customer = new Customer();
			customer.setGuestInfo(info);
			Example<Customer> ex = Example.of(customer);
			customer = customerDao.findOne(ex).get();
			if (customer == null) {
				customer = initCustomerAndSave(hotelCode,info);
			}
		}
		return customer;
	}

	private Customer initCustomerAndSave(String hotelCode,GuestInfo guestInfo) {
		Customer customer = new Customer();
		customer.setGuestInfo(guestInfo);
		customer.setHotelCode(hotelCode);
		customer.setName(guestInfo.getName());
		customer.setIdCardNum(guestInfo.getIdCardNum());
		customer.setMobile(guestInfo.getMobile());
		customer = add(customer);
		return customer;
	}

	@Override
	public Customer createTempCustomer(String hotelCode,String tempName) {
		Customer customer = new Customer();
		customer.setName(tempName);
		customer.setHotelCode(hotelCode);
		customer = add(customer);
		return customer;
	}

	@Override
	public Customer createOrGetCustomer(String hotelCode,String name, String idCardNum, String mobile) {
		GuestInfo info = guestInfoService.findByIdCardNum(idCardNum);
		Customer customer = null;
		if (info == null) {
			info = new GuestInfo();
			info.setName(name);
			info.setHotelCode(hotelCode);
			info.setMobile(mobile);
			info.setIdCardNum(idCardNum);
			info = guestInfoService.add(info);
			customer = initCustomerAndSave(hotelCode,info);
		} else {
			customer = new Customer();
			customer.setGuestInfo(info);
			Example<Customer> ex = Example.of(customer);
			customer = customerDao.findOne(ex).get();
			if (customer == null) {
				customer = initCustomerAndSave(hotelCode,info);
			}
		}
		return customer;
	}

	@Override
	public List<Map<String, Object>> getResverInfo(String customerId) {
		return customerDao.getResverInfo(customerId);
	}

	@Override
	public List<Customer> findByHotelCodeAndNameAndCustomerType(String hotelCode, String name, String customerType) {
		 List<Customer> list = customerDao.findByHotelCodeAndNameAndCustomerType(hotelCode, name, customerType);
		 return list;
	}

	@Override
	public List<Map<String, Object>> getTypeIsB(String hotelCode, String customerType, String name, String numCode) {
		return customerDao.getTypeIsB(hotelCode, customerType, name, numCode);
	}

}
