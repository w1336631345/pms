package com.kry.pms.service.org;

import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.service.BaseService;

public interface EmployeeService extends BaseService<Employee>{

	Employee findByAccount(Account account);

}