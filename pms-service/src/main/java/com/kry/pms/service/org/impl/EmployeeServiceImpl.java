package com.kry.pms.service.org.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.org.EmployeeDao;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.service.org.EmployeeService;

@Service
public class  EmployeeServiceImpl implements  EmployeeService{
	@Autowired
	 EmployeeDao employeeDao;
	 
	 @Override
	public Employee add(Employee employee) {
		return employeeDao.saveAndFlush(employee);
	}

	@Override
	public void delete(String id) {
		Employee employee = employeeDao.findById(id).get();
		if (employee != null) {
			employee.setDeleted(true);
		}
		employeeDao.saveAndFlush(employee);
	}

	@Override
	public Employee modify(Employee employee) {
		return employeeDao.saveAndFlush(employee);
	}

	@Override
	public Employee findById(String id) {
		return employeeDao.getOne(id);
	}

	@Override
	public List<Employee> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return employeeDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<Employee> listPage(PageRequest<Employee> prq) {
		Example<Employee> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(employeeDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
