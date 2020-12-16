package com.kry.pms.service.sys;

import java.util.List;
import java.util.Map;

import com.kry.pms.model.persistence.sys.Function;
import com.kry.pms.service.BaseService;

public interface FunctionService extends BaseService<Function>{

	List<Function> listAll();

    List<Map<String, Object>> listFather();

    List<Map<String, Object>> listMap();
}