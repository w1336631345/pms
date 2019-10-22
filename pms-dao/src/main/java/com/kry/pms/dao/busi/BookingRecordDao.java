package com.kry.pms.dao.busi;

import org.springframework.data.jpa.repository.Query;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.busi.BookingRecord;

public interface BookingRecordDao extends BaseDao<BookingRecord>{
	@Query(value = "select a.* from t_booking_record a ,t_booking_record_check_in_record b where a.id = b.booking_record_id and b.check_in_record_id = ?1",nativeQuery = true)
	BookingRecord findByBookingItemId(String checkInRecordId);

}
