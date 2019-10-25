package com.kry.pms.dao.busi;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.busi.CheckInRecord;

public interface CheckInRecordDao extends BaseDao<CheckInRecord>{
	@Query(value = "select a.* from t_checkin_record a where a.booking_record_id=?1",nativeQuery = true)
	List<CheckInRecord> fingByBookId(String bookId);

}
