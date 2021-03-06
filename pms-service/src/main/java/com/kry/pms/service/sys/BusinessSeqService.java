package com.kry.pms.service.sys;

import java.time.LocalDate;
import java.util.List;

import com.kry.pms.model.persistence.sys.BusinessSeq;
import com.kry.pms.service.BaseService;

public interface BusinessSeqService extends BaseService<BusinessSeq>{

	BusinessSeq fetchNextSeq(String hotelCode, String seqKey);
	
	String fetchNextSeqNum(String hotelCode,String seqString);

	LocalDate getBuinessDate(String hotelCode);
	
	LocalDate getPlanDate(String hotelCode);

	void plusBuinessDate(String hotelCode);

	String getBuinessString(String hotelCode);

	List<BusinessSeq> batchAdd(List<BusinessSeq> businessSeqs);
}