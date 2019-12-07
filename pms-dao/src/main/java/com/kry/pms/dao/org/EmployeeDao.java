package com.kry.pms.dao.org;

import java.util.List;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.org.Employee;

public interface EmployeeDao extends BaseDao<Employee>{

	List<Employee> findByDepartmentCodeAndHotelCode(String deptMarketingDefaultCode,String hotelCode);
	
	Employee findByUserId(String userId);

    List<Employee> findByHotelCode(String code);

	List<Employee> findByHotelCodeAndDeleted(String code, Integer deleted);
}
