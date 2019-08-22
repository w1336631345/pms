package com.kry.pms.service.sys.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.sys.RoleDao;
import com.kry.pms.model.persistence.sys.Role;
import com.kry.pms.service.sys.RoleService;

@Service
public class  RoleServiceImpl implements  RoleService{
	@Autowired
	 RoleDao roleDao;
	 
	 @Override
	public Role add(Role role) {
		return roleDao.saveAndFlush(role);
	}

	@Override
	public void delete(String id) {
		Role role = roleDao.findById(id).get();
		if (role != null) {
			role.setDeleted(true);
		}
		roleDao.saveAndFlush(role);
	}

	@Override
	public Role modify(Role role) {
		return roleDao.saveAndFlush(role);
	}

	@Override
	public Role findById(String id) {
		return roleDao.getOne(id);
	}

	@Override
	public List<Role> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return roleDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<Role> listPage(PageRequest<Role> prq) {
		Example<Role> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(roleDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
