package com.kry.pms.service.marketing;

import com.kry.pms.model.persistence.marketing.SalesMen;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.service.BaseService;

public interface SalesMenService extends BaseService<SalesMen>{

	SalesMen createByEmployee(Employee employee);

}