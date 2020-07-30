package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.MemberIntegral;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface MemberIntegralDao extends BaseDao<MemberIntegral>{

	List<MemberIntegral> findByHotelCode(String hotelCode);

	List<MemberIntegral> findByHotelCodeAndCardNum(String hotelCode, String cardNum);

	@Query(nativeQuery = true, value = " select DISTINCT tmi.card_num \n" +
			" from t_member_integral tmi \n" +
			" where tmi.limitation_date < CURRENT_DATE \n" +
			" and tmi.in_or_out = 'IN' \n" +
			" and tmi.is_overdue = 0 \n" +
			" and tmi.hotel_code = ?1 ")
	List<String> boOverdueCardNumList(String hotelCode);
	@Query(nativeQuery = true, value = " select \n" +
			" tmi.* \n" +
			" from t_member_integral tmi \n" +
			" where tmi.limitation_date < CURRENT_DATE \n" +
			" and tmi.in_or_out = 'IN' \n" +
			" and tmi.is_overdue = 0 \n" +
			" and tmi.hotel_code = ?1 ")
	List<MemberIntegral> boOverdueList(String hotelCode);
	@Query(nativeQuery = true, value = " select \n" +
			" tmi.* \n" +
			" from t_member_integral tmi \n" +
			" where tmi.limitation_date < CURRENT_DATE \n" +
			" and tmi.in_or_out = 'IN' \n" +
			" and tmi.is_overdue = 0 \n" +
			" and tmi.hotel_code = ?1 \n" +
			" and tmi.card_num = ?2  ")
	List<MemberIntegral> boOverdueListByCardNum(String hotelCode, String cardNum);
	@Query(nativeQuery = true, value = " select \n" +
			" IFNULL(sum(tmi.out_integral),0) \n" +
			" from t_member_integral tmi \n" +
			" where 1=1\n" +
			" and tmi.in_or_out = 'OUT' \n" +
			" and tmi.hotel_code = ?1 \n" +
			" and tmi.card_num = ?2  ")
	Double sumOutIntegral(String hotelCode, String cardNum);
	@Query(nativeQuery = true, value = " select \n" +
			" IFNULL(sum(tmi.in_integral),0) \n" +
			" from t_member_integral tmi \n" +
			" where tmi.limitation_date < CURRENT_DATE \n" +
			" and tmi.in_or_out = 'IN' \n" +
			" and tmi.hotel_code = ?1 \n" +
			" and tmi.card_num = ?2  ")
	Double sumInIntegral(String hotelCode, String cardNum);
	@Query(nativeQuery = true, value = " select \n" +
			" IFNULL(sum(tmi.over_integral),0) \n" +
			" from t_member_integral tmi \n" +
			" where 1=1\n" +
			" and tmi.in_or_out = 'OVER' \n" +
			" and tmi.hotel_code = ?1 \n" +
			" and tmi.card_num = ?2  ")
	Double sumOverIntegral(String hotelCode, String cardNum);


	@Modifying
	@Query(nativeQuery = true, value = " update t_member_integral set is_overdue = ?1, update_date = SYSDATE() where id = ?2 ")
	void updateIsOverdue(Integer overdue, String id);


}
