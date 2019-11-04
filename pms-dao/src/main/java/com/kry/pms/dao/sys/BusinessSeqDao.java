package com.kry.pms.dao.sys;

import org.springframework.data.jpa.repository.Query;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.sys.BusinessSeq;

public interface BusinessSeqDao extends BaseDao<BusinessSeq>{

	BusinessSeq findByHotelCodeAndSeqKey(String hotelCode, String seqKey);
	@Query(value = "update t_business_seq set current_seq=1 where hotel_code=?0 and seq_reset_type = 'BD'" ,nativeQuery = true)
	void resetDailySeq(String hotelCode);

}
