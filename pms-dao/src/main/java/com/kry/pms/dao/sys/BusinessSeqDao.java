package com.kry.pms.dao.sys;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.sys.BusinessSeq;

public interface BusinessSeqDao extends BaseDao<BusinessSeq>{

	BusinessSeq findByHotelCodeAndSeqKey(String hotelCode, String seqKey);
	@Modifying
	@Query(value = "update t_business_seq set current_seq=1 where hotel_code=?1 and seq_reset_type = 'BD'" ,nativeQuery = true)
	int resetDailySeq(String hotelCode,int startSeq);

}
