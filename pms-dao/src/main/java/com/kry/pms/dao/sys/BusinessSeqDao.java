package com.kry.pms.dao.sys;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.sys.BusinessSeq;

public interface BusinessSeqDao extends BaseDao<BusinessSeq>{

	BusinessSeq findByHotelCodeAndSeqKey(String hotelCode, String seqKey);

}
