package com.kry.pms.dao.marketing;

import java.util.List;
import java.util.Map;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.marketing.MarketingSources;
import org.springframework.data.jpa.repository.Query;

public interface MarketingSourcesDao extends BaseDao<MarketingSources>{

	List<MarketingSources> findByHotelCodeAndDeleted(String currentHotleCode, int deleted);

	MarketingSources findByHotelCodeAndDeletedAndCode(String hotelCode, int deleted, String code);

	List<MarketingSources> findByHotelCodeAndIsUsedAndDeleted(String hotleCode,String isUsed, int deleted);

	@Query(nativeQuery = true, value = " select t.mname, count(t.type_) countNum from (\n" +
			" select \n" +
			" DISTINCT \n" +
			"  tms.`name` mname,\n" +
			"  tms.hotel_code,\n" +
			"  tcr.type_,\n" +
			"  tcr.guest_room_id\n" +
			" from t_marketing_sources tms \n" +
			" left join t_checkin_record tcr on tms.id = tcr.marketing_sources_id \n" +
			" where tcr.`status` = 'I' and tcr.deleted = 0 \n" +
			" and tcr.type_ = 'C'\n" +
			" and DATE_FORMAT(tcr.actual_time_of_arrive, '%Y-%m-%d') = ?1 \n" +
			" and tms.hotel_code = ?2 \n" +
			" ) t group by t.mname ")
	List<Map<String, Object>> countRoom( String dateTime, String hotelCode);

}
