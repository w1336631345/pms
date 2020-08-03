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
import com.kry.pms.dao.sys.ManagerUserDao;
import com.kry.pms.model.persistence.sys.ManagerUser;
import com.kry.pms.service.sys.ManagerUserService;

@Service
public class  ManagerUserServiceImpl implements  ManagerUserService{
	@Autowired
	 ManagerUserDao managerUserDao;
	 
	 @Override
	public ManagerUser add(ManagerUser managerUser) {
		return managerUserDao.saveAndFlush(managerUser);
	}

	@Override
	public void delete(String id) {
		ManagerUser managerUser = managerUserDao.findById(id).get();
		if (managerUser != null) {
			managerUser.setDeleted(Constants.DELETED_TRUE);
		}
		modify(managerUser);
	}

	@Override
	public ManagerUser modify(ManagerUser managerUser) {
		return managerUserDao.saveAndFlush(managerUser);
	}

	@Override
	public ManagerUser findById(String id) {
		return managerUserDao.getOne(id);
	}

	@Override
	public List<ManagerUser> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return managerUserDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<ManagerUser> listPage(PageRequest<ManagerUser> prq) {
		Example<ManagerUser> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(managerUserDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
