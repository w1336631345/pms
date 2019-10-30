package com.kry.pms.service.sys;

import java.time.LocalDate;

import com.kry.pms.model.persistence.sys.BusinessSeq;
import com.kry.pms.service.BaseService;

public interface BusinessSeqService extends BaseService<BusinessSeq>{

	BusinessSeq fetchNextSeq(String hotelCode, String seqKey);

	LocalDate getBuinessDate(String hotelCode);

}