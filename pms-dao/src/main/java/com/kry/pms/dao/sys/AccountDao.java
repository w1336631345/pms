package com.kry.pms.dao.sys;

import java.util.Collection;
import java.util.List;

import com.kry.pms.model.persistence.guest.Customer;
import org.springframework.data.jpa.repository.Query;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.sys.Account;
import org.springframework.data.repository.query.Param;

public interface AccountDao extends BaseDao<Account> {

	@Query(value = "select a.*,c.room_num from t_account a,t_checkin_record b,t_guest_room c where a.id=b.account_id and b.guest_room_id = c.id and b.order_num=?1 and b.deleted=0 and a.type_=?2", nativeQuery = true)
	public List<Account> findByOrderNum(String orderNum, String checkInType);

	@Query(value = "select a.*,c.room_num from t_account a,t_checkin_record b,t_guest_room c where a.id=b.account_id and b.guest_room_id = c.id and b.order_num=?1 and b.deleted=0 and a.type_=?2 and a.status=?3", nativeQuery = true)
	public Collection<Account> findAccountByOrderNumAndStatusAndCheckInType(String orderNum, String checkInType,
			String status);

	List<Account> findByHotelCodeAndType(String hotelCode, String type);

	List<Account> findByCustomerAndType(Customer customer, String type);
	@Query(nativeQuery = true,value = "select count(id) from t_account a,t_checkin_record b where a.id = b.account_id and b.order_num = ?1 and b.status=?2")
	int queryAccounCountByOrderNumAndStatus(String orderNum,String status);

	/**
	 * 查询某个计算出来的账号是否已经存在
	 * @author: WangXinHao
	 * @date: 2021/3/17 0017 15:45
	 */
	public  List<Account> findByHotelCodeAndCode(String hotelCode,String code);

	/**
	 * @desc: 查询同住情况下，除本账号外其他账号的余额总和，用于判断房间是否欠费
	 * @author: WangXinHao
	 * @date: 2021/3/24 0024 9:28
	 */
	@Query(nativeQuery = true, value = " select sum(a.total)" +
			" from t_account a , t_checkin_record  c\n" +
			" where a.id = c.account_id\n" +
			" and c.guest_room_id = :guestRoomId\n" +
			" and c.order_num = :orderNum\n" +
			" and c.hotel_code = :hotelCode\n" +
			" and c.id != :checkinRecordId")
    Double getTotalByTongzhu(@Param("guestRoomId") String guestRoomId, @Param("orderNum") String orderNum,
							 @Param("hotelCode") String hotelCode,@Param("checkinRecordId") String checkinRecordId);

}
