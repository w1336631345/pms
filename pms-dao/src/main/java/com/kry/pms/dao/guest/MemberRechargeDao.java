package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.MemberRecharge;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface MemberRechargeDao extends BaseDao<MemberRecharge>{

	List<MemberRecharge> findByHotelCode(String hotelCode);

    List<MemberRecharge> findByHotelCodeAndCardNum(String hotelCode, String cardNum);

    MemberRecharge findByHotelCodeAndSettledNo(String hotelCode, String settledNo);

    @Query(nativeQuery = true, value = " select DISTINCT tmr.card_num \n" +
            " from t_member_recharge tmr \n" +
            " where tmr.limitation_date < CURRENT_DATE \n" +
            " and tmr.recharge_or_use = 'R' \n" +
            " and tmr.is_overdue = 0 \n" +
            " and tmr.hotel_code = ?1 ")
    List<String> boOverdueCardNumList(String hotelCode);

    @Query(nativeQuery = true, value = " select  tmr.* \n" +
            " from t_member_recharge tmr \n" +
            " where tmr.limitation_date < CURRENT_DATE \n" +
            " and tmr.recharge_or_use = 'R' \n" +
            " and tmr.is_overdue = 0 \n" +
            " and tmr.hotel_code = ?1 " +
            " and tmr.card_num = ?2 ")
    List<MemberRecharge> boOverdueListByCardNum(String hotelCode, String cardNum);

    @Query(nativeQuery = true, value = " select \n" +
            " IFNULL(sum(tmr.use_amount),0) use_amount, \n" +
            " IFNULL(sum(tmr.use_give_amount),0)  use_give_amount \n" +
            "from t_member_recharge tmr \n" +
            "where tmr.limitation_date < CURRENT_DATE \n" +
            "and tmr.recharge_or_use = 'U' \n" +
            "and tmr.hotel_code = ?1 \n" +
            "and tmr.card_num = ?2 ")
    Map<String, Object> sumUseAmount(String hotelCode, String cardNum);
    @Query(nativeQuery = true, value = " select \n" +
            " IFNULL(sum(tmr.amount),0) amount, \n" +
            " IFNULL(sum(tmr.give_amount),0)  give_amount \n" +
            "from t_member_recharge tmr \n" +
            "where tmr.limitation_date < CURRENT_DATE \n" +
            "and tmr.recharge_or_use = 'R' \n" +
            "and tmr.hotel_code = ?1 \n" +
            "and tmr.card_num = ?2 ")
    Map<String, Object> sumAmount(String hotelCode, String cardNum);
    @Query(nativeQuery = true, value = " select \n" +
            " IFNULL(sum(tmr.over_amount),0) over_amount, \n" +
            " IFNULL(sum(tmr.over_give_amount),0)  over_give_amount \n" +
            "from t_member_recharge tmr \n" +
            "where 1=1 \n" +
            "and tmr.recharge_or_use = 'O' \n" +
            "and tmr.hotel_code = ?1 \n" +
            "and tmr.card_num = ?2 ")
    Map<String, Object> overAmount(String hotelCode, String cardNum);
}
