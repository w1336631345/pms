package com.kry.pms.service.org;

import com.kry.pms.base.DtoResponse;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.BaseService;

public interface EmployeeService extends BaseService<Employee> {

	DtoResponse<Employee> createEmployee(Employee employee);

	Employee findByUser(User user);

}