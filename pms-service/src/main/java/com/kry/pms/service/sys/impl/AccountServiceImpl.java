package com.kry.pms.service.sys.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.sys.AccountDao;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.service.sys.AccountService;

@Service
public class  AccountServiceImpl implements  AccountService{
	@Autowired
	 AccountDao accountDao;
	 
	 @Override
	public Account add(Account account) {
		return accountDao.saveAndFlush(account);
	}

	@Override
	public void delete(String id) {
		Account account = accountDao.findById(id).get();
		if (account != null) {
			account.setDeleted(Constants.DELETED_TRUE);
		}
		accountDao.saveAndFlush(account);
	}

	@Override
	public Account modify(Account account) {
		return accountDao.saveAndFlush(account);
	}

	@Override
	public Account findById(String id) {
		return accountDao.getOne(id);
	}

	@Override
	public List<Account> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return accountDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<Account> listPage(PageRequest<Account> prq) {
		Example<Account> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(accountDao.findAll(ex, req));
	}

	@Override
	public Account findTopByMobileOrUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}
	 
	 
	 
	 
}
