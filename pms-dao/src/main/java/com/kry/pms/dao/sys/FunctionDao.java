package com.kry.pms.dao.sys;

import java.util.List;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.sys.Function;

public interface FunctionDao extends BaseDao<Function>{

	List<Function> findByStatusAndDeleted(String normal, int deletedFalse);

}
