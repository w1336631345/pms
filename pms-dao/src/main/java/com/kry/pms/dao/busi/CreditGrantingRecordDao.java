package com.kry.pms.dao.busi;

import java.util.List;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.busi.CreditGrantingRecord;

public interface CreditGrantingRecordDao extends BaseDao<CreditGrantingRecord>{

	List<CreditGrantingRecord> findByGrantingAccountId(String id);

	List<CreditGrantingRecord> findByAccountId(String id);

}
