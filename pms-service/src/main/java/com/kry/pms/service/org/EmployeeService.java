package com.kry.pms.service.org;

import java.util.List;
import java.util.Map;

import com.kry.pms.base.DtoResponse;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.BaseService;

public interface EmployeeService extends BaseService<Employee> {

	DtoResponse<Employee> createEmployee(Employee employee);

    List<Employee> getByHotelCodeAndDelete(String code);

    Employee findByUser(User user);

	List<Employee> findEmployeeByDeptCode(String deptMarketingDefaultCode, String currentHotleCode);

    List<Map<String, Object>> getListMap(String hotelCode);
}