package com.kry.pms.service.dict;

import com.kry.pms.model.persistence.dict.DictData;
import com.kry.pms.service.BaseService;

import java.util.Map;

public interface DictDataService extends BaseService<DictData>{

    Map<String, Map<String, String>> getWebDict(String hotelCode);
}