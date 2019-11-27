package com.kry.pms.dao.sys;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.Query;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.sys.Shift;

public interface ShiftDao extends BaseDao<Shift>{
	
	@Query(value = "select * from t_shift where shift_code = ?1 and business_date = ?2 and employee_id = ?3",nativeQuery = true)
	Shift findCurrentShift(String shiftCode, LocalDate businessDate, String id);

}
