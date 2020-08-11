package com.kry.pms.dao.marketing;

import java.util.List;
import java.util.Map;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.marketing.DistributionChannel;
import org.springframework.data.jpa.repository.Query;

public interface DistributionChannelDao extends BaseDao<DistributionChannel>{

	List<DistributionChannel> findByHotelCodeAndDeleted(String currentHotleCode, int deleted);

	DistributionChannel findByHotelCodeAndDeletedAndCode(String hotelCode, int deleted, String code);

	@Query(nativeQuery = true, value = " select t.dname, count(t.type_) countNum from (\n" +
			" select \n" +
			" DISTINCT \n" +
			"  tdc.`name` dname,\n" +
			"  tdc.hotel_code,\n" +
			"  tcr.type_,\n" +
			"  tcr.guest_room_id\n" +
			" from t_distribution_channel tdc \n" +
			" left join t_checkin_record tcr on tdc.id = tcr.distribution_channel_id \n" +
			" where tcr.`status` = 'I' and tcr.deleted = 0 \n" +
			" and tcr.type_ = 'C'\n" +
			" and DATE_FORMAT(tcr.actual_time_of_arrive, '%Y-%m-%d') = ?1 \n" +
			" and tdc.hotel_code = ?2 \n" +
			" ) t group by t.dname ")
	List<Map<String, Object>> countRoom(String dateTime, String hotelCode);
}
