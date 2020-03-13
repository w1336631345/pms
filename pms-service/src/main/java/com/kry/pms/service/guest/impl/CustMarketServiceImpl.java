package com.kry.pms.service.guest.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.CustMarketDao;
import com.kry.pms.dao.guest.CustomerDao;
import com.kry.pms.dao.sys.AccountDao;
import com.kry.pms.model.persistence.guest.CustMarket;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.service.guest.CustMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustMarketServiceImpl implements CustMarketService {
	@Autowired
	CustMarketDao custMarketDao;
	@Autowired
	AccountDao accountDao;
	@Autowired
	CustomerDao customerDao;

	@Override
	public CustMarket add(CustMarket entity) {
		return custMarketDao.saveAndFlush(entity);
	}

	@Override
	public void delete(String id) {
		custMarketDao.deleteById(id);
	}

	@Override
	public CustMarket modify(CustMarket custMarket) {
		return custMarketDao.saveAndFlush(custMarket);
	}

	@Override
	public CustMarket findById(String id) {
		return custMarketDao.getOne(id);
	}

	@Override
	public List<CustMarket> getAllByHotelCode(String code) {
		return custMarketDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<CustMarket> listPage(PageRequest<CustMarket> prq) {
		Example<CustMarket> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(custMarketDao.findAll(ex, req));
	}

	@Override
	public List<CustMarket> getByCustomerId(String customerId) {
		List<CustMarket> custMarkets = custMarketDao.findByCustomerId(customerId);
		Customer customer = customerDao.getOne(customerId);
		List<Account> account = accountDao.findByCustomerAndType(customer, Constants.Type.ACCOUNT_AR);
		if(account != null && !account.isEmpty()){
			if(custMarkets != null && !custMarkets.isEmpty()){
				custMarkets.get(0).setAccount(account.get(0));
			}else{
				CustMarket custMarket = new CustMarket();
				custMarket.setCustomerId(customerId);
				custMarket.setAccount(account.get(0));
			}
		}
		return custMarketDao.findByCustomerId(customerId);
	}
}
