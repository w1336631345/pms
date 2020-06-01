package com.kry.pms.dao.org;

import java.util.List;
import java.util.Map;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.org.Employee;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeDao extends BaseDao<Employee>{

	List<Employee> findByDepartmentCodeAndHotelCode(String deptMarketingDefaultCode,String hotelCode);
	
	Employee findByUserId(String userId);

    List<Employee> findByHotelCode(String code);

	List<Employee> findByHotelCodeAndDeleted(String code, Integer deleted);

	@Query(nativeQuery = true, value = " select id, name, user_id from t_employee tm\n" +
			"where deleted = 0 and tm.hotel_code = ?1 ")
	List<Map<String, Object>> getListMap(String hotelCode);

	@Query(nativeQuery = true, value = " select name from t_employee tm\n" +
			"where deleted = 0 and tm.user_id = ?1 ")
	String getName(String userId);
}
