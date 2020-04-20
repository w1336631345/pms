package com.kry.pms.dao.sys;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.sys.SqlTemplate;

public interface SqlTemplateDao extends BaseDao<SqlTemplate>{

    SqlTemplate findByHotelCodeAndEntityNameAndMethodName(String hotelCode, String entityName, String methodName);

    SqlTemplate findByEntityNameAndMethodName(String entityName, String methodName);

    SqlTemplate findByHotelCodeAndCode(String hotelCode, String code);

    SqlTemplate findByCode(String code);
}
