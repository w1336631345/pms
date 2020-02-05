package com.kry.pms.service.org.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.DtoResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.org.EmployeeDao;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.org.EmployeeService;
import com.kry.pms.service.sys.AccountService;
import com.kry.pms.service.sys.RoleService;
import com.kry.pms.service.sys.SystemConfigService;
import com.kry.pms.service.sys.UserService;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	EmployeeDao employeeDao;
	@Autowired
	SystemConfigService systemConfigService;
	@Autowired
	UserService userService;
	@Autowired
	RoleService roleService;

	@Override
	public Employee add(Employee employee) {
		return employeeDao.saveAndFlush(employee);
	}

	@Override
	public void delete(String id) {
		Employee employee = employeeDao.findById(id).get();
		if (employee != null) {
			employee.setDeleted(Constants.DELETED_TRUE);
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
//		return null;// 默认不实现
		 return employeeDao.findByHotelCode(code);
	}

	@Override
	public List<Employee> getByHotelCodeAndDelete(String code) {
		return employeeDao.findByHotelCodeAndDeleted(code, Constants.DELETED_FALSE);
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

	@Override
	public Employee findByUser(User user) {
		return employeeDao.findByUserId(user.getId());
	}

	@Override
	public DtoResponse<Employee> createEmployee(Employee employee) {
		DtoResponse<Employee> rep = new DtoResponse<Employee>();
		User user = new User();
		user.setUsername(employee.getCode());
		user.setNickname(employee.getName());
		user.setMobile(employee.getMobile());
		user.setHotelCode(employee.getHotelCode());
		user.setCorporationCode(employee.getCorporationCode());
		user.setCreateUser(employee.getCreateUser());
		user.setPassword(systemConfigService
				.getByHotelCodeAndKey(employee.getHotelCode(), Constants.SystemConfig.CODE_DEFAULT_ACCOUNT_PASSWORD)
				.getValue());
		user = userService.add(user);
		employee.setUser(user);
		add(employee);
		return rep.addData(employee);
	}

	@Override
	public List<Employee> findEmployeeByDeptCode(String deptMarketingDefaultCode, String currentHotleCode) {
		return employeeDao.findByDepartmentCodeAndHotelCode(deptMarketingDefaultCode,currentHotleCode);
	}

}
