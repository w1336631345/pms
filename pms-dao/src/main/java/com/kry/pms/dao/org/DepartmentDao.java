package com.kry.pms.dao.org;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.org.Department;

import java.util.List;

public interface DepartmentDao extends BaseDao<Department>{

    List<Department> findByHotelCode(String code);
}
