package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.guest.MemberInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface MemberInfoDao extends BaseDao<MemberInfo>{

	List<MemberInfo> findByHotelCode(String hotelCode);

	List<MemberInfo> findByCustomer(Customer customer);

	MemberInfo findByHotelCodeAndCardNum(String hotelCode, String cardNum);

	MemberInfo findByHotelCodeAndAccountId(String hotelCode, String accountId);

	@Modifying
	@Query(nativeQuery = true, value = " update t_member_info set integral = ?1, update_date = SYSDATE() where id = ?2 ")
	void updateIntegral(Double integral, String id);

	@Query(nativeQuery = true, value = " select tmi.*, te.`name` operator from \n" +
			" t_member_info tmi left join t_employee te on tmi.create_user = te.user_id \n" +
			" where tmi.hotel_code = ?1 and DATE_FORMAT(tmi.create_date,'%Y-%m-%d') = ?2 ")
	List<MemberInfo> getByCreateDate(String hotelCode, String createDate);

	@Query(nativeQuery = true, value = " select IFNULL(count(id),0) from t_member_info tmi \n" +
			" where tmi.hotel_code = ?1 and DATE_FORMAT(tmi.create_date,'%Y-%m-%d') = ?2 ")
	Integer getByCreateDateCount(String hotelCode, String createDate);

	@Query(nativeQuery = true, value = " select t1.create_user, te.`name`, \n" +
			" IFNULL(t1.yearCount,0) yearCount, IFNULL(t2.mouthCount,0) mouthCount, IFNULL(t3.dayCount,0) dayCount \n" +
			" from\n" +
			" (select \n" +
			" create_user,\n" +
			" DATE_FORMAT(create_date,'%Y') times, \n" +
			" count(id) yearCount\n" +
			" from t_member_info \n" +
			" where hotel_code = ?1\n" +
			" and DATE_FORMAT(create_date,'%Y') = substr(?2,1,4) \n" +
			" group by create_user, DATE_FORMAT(create_date,'%Y') ) t1\n" +
			" left join \n" +
			" (select \n" +
			" create_user,\n" +
			" DATE_FORMAT(create_date,'%Y-%m') times, \n" +
			" count(id) mouthCount \n" +
			" from t_member_info \n" +
			" where 1=1\n" +
			" and DATE_FORMAT(create_date,'%Y-%m') = substr(?2,1,7) \n" +
			" group by create_user, DATE_FORMAT(create_date,'%Y-%m') ) t2\n" +
			" on t1.create_user = t2.create_user\n" +
			" left join \n" +
			" (select \n" +
			" create_user,\n" +
			" DATE_FORMAT(create_date,'%Y-%m-%d') times,\n" +
			" count(id) dayCount\n" +
			" from t_member_info \n" +
			" where 1=1\n" +
			" and DATE_FORMAT(create_date,'%Y-%m-%d') = ?2 \n" +
			" group by create_user, DATE_FORMAT(create_date,'%Y-%m') ) t3\n" +
			"  on t2.create_user = t3.create_user\n" +
			" left join t_employee te on t1.create_user = te.user_id\n ")
	List<Map<String, Object>> countByCreateUser(String hotelCode, String createDate);

	@Query(nativeQuery = true, value = " select\n" +
			"  tmi.card_num, tmi.mac_num, tml.remark, tc.`name`, tc.gender, tc.mobile, tmr.amount, tmr.give_amount,\n" +
			"  ta.credit_limit, tp.`name` payType, te.`name` operator, DATE_FORMAT(tmr.recharge_date,'%Y-%m-%d') recharge_date\n" +
			" from t_member_recharge tmr\n" +
			"  left join t_member_info tmi on tmi.id = tmr.member_info_id\n" +
			"  left join t_customer tc on tmi.customer_id = tc.id\n" +
			"  left join t_product tp on tmr.pay_type_id = tp.id\n" +
			"  left join t_member_level tml on tmi.member_level_id = tml.id\n" +
			"  left join t_employee te on tmi.create_user = te.user_id\n" +
			"  left join t_account ta on tmi.account_id = ta.id\n" +
			" where tmr.recharge_or_use = 'R'\n" +
			"  and tmr.hotel_code = ?1 \n" +
			"  and tmr.recharge_date = ?2 ")
	List<Map<String, Object>> rechargeReport(String hotelCode, String rechargeDate);

	@Query(nativeQuery = true, value = " select \n" +
			"  tmi.card_num, tmi.mac_num, tml.remark, tc.`name`, tc.gender,  tc.mobile, tmr.in_integral,\n" +
			"  DATE_FORMAT(tmr.business_date,'%Y-%m-%d') business_date, te.`name` operator, DATE_FORMAT(tmr.cons_date,'%Y-%m-%d') cons_date\n" +
			" from t_member_integral tmr\n" +
			"  left join t_member_info tmi on tmi.id = tmr.member_info_id\n" +
			"  left join t_customer tc on tmi.customer_id = tc.id\n" +
			"  left join t_member_level tml on tmi.member_level_id = tml.id\n" +
			"  left join t_employee te on tmi.create_user = te.user_id\n" +
			" where tmr.in_or_out = 'IN'\n" +
			"  and tmr.hotel_code = ?1 \n" +
			"  and tmr.cons_date = ?2 ")
	List<Map<String, Object>> integralReport(String hotelCode, String consDate);

	@Query(nativeQuery = true, value = " select\n" +
			"  tmi.card_num, tmi.mac_num, tmi.is_used, tml.remark, tc.`name`, tc.gender, tc.mobile, tc.id_card_num, \n" +
			"  DATE_FORMAT(tc.birthday,'%Y-%m-%d') birthday, tsm.`name` saleMen, tmi.balance, tmi.give_price, tmi.type, \n" +
			"  ta.credit_limit, tmi.integral, DATE_FORMAT(tmi.effective_date,'%Y-%m-%d') effective_date, DATE_FORMAT(tmi.limitation_date,'%Y-%m-%d') limitation_date\n" +
			" from t_member_info tmi\n" +
			"  left join t_customer tc on tmi.customer_id = tc.id\n" +
			"  left join t_sales_men tsm on tmi.sales_men_id = tsm.id \n" +
			"  left join t_member_level tml on tmi.member_level_id = tml.id\n" +
			"  left join t_account ta on tmi.account_id = ta.id\n" +
			" where 1=1\n" +
			"  and if(:hotelCode is not null && :hotelCode != '', tmi.hotel_code=:hotelCode, 1=1 ) " +
			"  and if(:type is not null && :type != '', tmi.type=:type, 1=1 ) " +
			"  and if(:isUsed is not null && :isUsed != '', tmi.is_used=:isUsed, 1=1 ) " +
			"  and if(:moreParams is not null && :moreParams != '', " +
			"(tmi.card_num = :moreParams or tmi.mac_num = :moreParams or tc.`name` like '%"+":moreParams"+"%' or tc.mobile = :moreParams), 1=1 ) ")
	List<Map<String, Object>> list(@Param("hotelCode") String hotelCode, @Param("type") String type, @Param("isUsed") String isUsed, @Param("moreParams") String moreParams);

	@Query(nativeQuery = true, value = " select tmi.* \n" +
			" from t_member_info tmi\n" +
			"  left join t_customer tc on tmi.customer_id = tc.id\n" +
			"  left join t_sales_men tsm on tmi.sales_men_id = tsm.id \n" +
			"  left join t_member_level tml on tmi.member_level_id = tml.id\n" +
			"  left join t_account ta on tmi.account_id = ta.id\n" +
			" where 1=1\n" +
			"  and if(:hotelCode is not null && :hotelCode != '', tmi.hotel_code=:hotelCode, 1=1 ) " +
			"  and if(:type is not null && :type != '', tmi.type=:type, 1=1 ) " +
			"  and if(:isUsed is not null && :isUsed != '', tmi.is_used=:isUsed, 1=1 ) " +
			"  and if(:moreParams is not null && :moreParams != '', " +
			" (tmi.card_num = :moreParams or tmi.mac_num = :moreParams or tc.`name` like CONCAT('%',:moreParams,'%') or tc.mobile = :moreParams), 1=1 ) ")
	List<MemberInfo> parmsList(@Param("hotelCode") String hotelCode, @Param("type") String type, @Param("isUsed") String isUsed, @Param("moreParams") String moreParams);

	MemberInfo findByIdAndPassword(String id, String password);

	@Query(nativeQuery = true, value = " select tmi.* \n" +
			" from t_member_info tmi \n" +
			"  left join t_customer tc on tmi.customer_id = tc.id \n" +
			" where 1=1\n" +
			"  and if(:hotelCode is not null && :hotelCode != '', tmi.hotel_code=:hotelCode, 1=1 ) " +
			"  and if(:mobile is not null && :mobile != '', tc.mobile=:mobile, 1=1 ) ")
	MemberInfo getByHotelAndMobile(@Param("hotelCode") String hotelCode, @Param("mobile") String mobile);

	@Query(nativeQuery = true, value = " select tmi.id, tmi.`name`, tc.mobile, tmi.account_id \n" +
			"  from t_member_info tmi, t_customer tc \n" +
			" where tmi.customer_id = tc.id \n" +
			"  and if(:hotelCode is not null && :hotelCode != '', tmi.hotel_code=:hotelCode, 1=1 ) \n" +
			"  and if(:tDay is not null && :tDay != '', tmi.account_id in (select tb.account_id from t_bill tb where  tb.create_date >= DATE_SUB(SYSDATE(),INTERVAL 30 DAY)),1=1) \n" +
			"  and if(:sDay is not null && :sDay != '', tmi.account_id in (select tb.account_id from t_bill tb where  tb.create_date >= DATE_SUB(SYSDATE(),INTERVAL 180 DAY)),1=1) \n" +
			"  and if(coalesce(:leavelIds, null) is not null, tmi.member_level_id in (:leavelIds), 1=1 ) ")
	List<Map<String, Object>> getSendMsgList(@Param("hotelCode") String hotelCode, @Param("tDay")String tDay, @Param("sDay")String sDay, @Param("leavelIds") List<String> leavelIds);

	/**
	 *  查询是否存在该物理账号了
	 * @author: WangXinHao
	 * @date: 2021/3/18 0018 13:50
	 */
	List<MemberInfo>  findByHotelCodeAndMacNum(String hotelCode,String macNum);


	/**
	 * @desc: 查询是否存在该系统卡号了
	 * @author: WangXinHao  
	 * @date: 2021/3/18 0018 14:13
	 */
	@Query(nativeQuery = true, value = " select *" +
			"  from t_member_info\n" +
			"  where hotel_code =  :hotelCode  and  card_num = :cardNum")
	List<MemberInfo>  findByHotelCodeAndCardNum2(@Param("hotelCode") String hotelCode, @Param("cardNum")String cardNum);
}
