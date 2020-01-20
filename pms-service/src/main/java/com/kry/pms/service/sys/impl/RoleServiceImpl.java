package com.kry.pms.service.sys.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.sys.RoleDao;
import com.kry.pms.model.persistence.sys.Function;
import com.kry.pms.model.persistence.sys.Role;
import com.kry.pms.service.sys.FunctionService;
import com.kry.pms.service.sys.RoleService;

@Service
public class  RoleServiceImpl implements  RoleService{
	@Autowired
	 RoleDao roleDao;
	@Autowired
	FunctionService functionService;
	 
	 @Override
	public Role add(Role role) {
		return roleDao.saveAndFlush(role);
	}

	@Override
	public void delete(String id) {
		Role role = roleDao.findById(id).get();
		if (role != null) {
			role.setDeleted(Constants.DELETED_TRUE);
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
	@Override
	public Role modifyFunction(String id, String[] functions) {
		Role role = findById(id);
		List<Function> list = new ArrayList<>();
		for (int i = 0; i < functions.length; i++) {
			list.add(functionService.findById(functions[i]));
		}
		role.setFunctions(list);;
		return modify(role);
		
	}
	 
	 
	 
}
