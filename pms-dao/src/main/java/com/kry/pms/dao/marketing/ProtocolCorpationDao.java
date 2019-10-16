package com.kry.pms.dao.marketing;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.marketing.ProtocolCorpation;

public interface ProtocolCorpationDao extends BaseDao<ProtocolCorpation>{


	@Query(nativeQuery = true,value = "select * from t_protocol_corpation where hotel_code=?2 and deleted = ?3 and name like ?1")
	List<ProtocolCorpation> fetchByKey(String key,String hotleCode,int deleted);
}
