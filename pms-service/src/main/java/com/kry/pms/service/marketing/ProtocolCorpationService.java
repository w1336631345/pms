package com.kry.pms.service.marketing;

import java.util.List;

import com.kry.pms.model.persistence.marketing.ProtocolCorpation;
import com.kry.pms.service.BaseService;

public interface ProtocolCorpationService extends BaseService<ProtocolCorpation>{

	List<ProtocolCorpation> queryByNameOrCode(String key, String currentHotleCode);

}