package com.kry.pms.service.sys;

import java.util.List;

import com.kry.pms.model.persistence.sys.Function;
import com.kry.pms.service.BaseService;

public interface FunctionService extends BaseService<Function>{

	List<Function> listAll();

}