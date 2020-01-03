package com.kry.pms.service.sys;

import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.sys.Shift;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.BaseService;

public interface ShiftService extends BaseService<Shift>{

	Shift createOrUpdate(String shiftCode, User user);

	Shift getCurrentShift(String shiftCode, Employee employee);

}