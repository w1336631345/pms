package com.kry.pms.service.org.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.org.DepartmentDao;
import com.kry.pms.model.persistence.org.Department;
import com.kry.pms.service.org.DepartmentService;

@Service
public class  DepartmentServiceImpl implements  DepartmentService{
	@Autowired
	 DepartmentDao departmentDao;
	 
	 @Override
	public Department add(Department department) {
		return departmentDao.saveAndFlush(department);
	}

	@Override
	public void delete(String id) {
		Department department = departmentDao.findById(id).get();
		if (department != null) {
			department.setDeleted(Constants.DELETED_TRUE);
		}
		departmentDao.saveAndFlush(department);
	}

	@Override
	public Department modify(Department department) {
		return departmentDao.saveAndFlush(department);
	}

	@Override
	public Department findById(String id) {
		return departmentDao.getOne(id);
	}

	@Override
	public List<Department> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return departmentDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<Department> listPage(PageRequest<Department> prq) {
		Example<Department> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(departmentDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
