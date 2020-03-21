package com.kry.pms.service.room;

import java.time.LocalDateTime;

import com.kry.pms.model.func.UseInfoAble;
import com.kry.pms.model.other.wrapper.CheckInRecordWrapper;
import com.kry.pms.model.persistence.room.GuestRoom;

public interface RoomStatisticsService {

	/**
	 * 
	 * @param cir checkInRecord的包装类实现了该接口，请使用CheckInRecordWrapper
	 * @return
	 */
	public boolean reserve(UseInfoAble info);

	/**
	 * 
	 * @param cir checkInRecord的包装类实现了该接口，请使用CheckInRecordWrapper
	 * @return
	 */
	public boolean cancleReserve(UseInfoAble info);
	/**
	 * 
	 * @param info checkInRecord的包装类实现了该接口，请使用CheckInRecordWrapper
	 * @return
	 */

	public boolean assignRoom(UseInfoAble info);

	/**
	 * 
	 * @param info checkInRecord的包装类实现了该接口，请使用CheckInRecordWrapper
	 * @return
	 */
	public boolean cancleAssign(UseInfoAble info);

//	/**
//	 * 
//	 * @param cir checkInRecord的包装类实现了该接口，请使用CheckInRecordWrapper 
//	 * @return
//	 */
//
//	public boolean booking(UseInfoAble info);
//
//	/**
//	 * 
//	 * @param cir
//	 * @return
//	 */
//
//	public boolean cancelBooking(UseInfoAble info);

	/**
	 * 入住 调整客房类型统计表，房间状态表，房间状态统计表
	 * 
	 * @param guestRoom   客房
	 * @param checkInDate 入住时间
	 * @param days        入住天数
	 * @return
	 */

	public boolean checkIn(UseInfoAble info);

	/**
	 * 
	 * @param cir
	 * @return
	 */
	public boolean cancleCheckIn(UseInfoAble info);

	/**
	 * 退房 调整客房类型统计表，房间状态表，房间状态统计表
	 * 
	 * @param guestRoom    客房
	 * @param checkOutDate 退房时间
	 * @return
	 */
	public boolean checkOut(UseInfoAble info);

	/**
	 * 
	 * @param cir
	 * @return
	 */
	public boolean cancelCheckOut(UseInfoAble info);

	/**
	 * 维修 调整客房类型统计表，房间状态表，房间状态统计表
	 * 
	 * @param guestRoom
	 * @return
	 */

	public boolean repair(UseInfoAble info);

	/**
	 * 维修完成 调整客房类型统计表，房间状态表，房间状态统计表
	 * 
	 * @param guestRoom
	 * @return
	 */
	public boolean cancelRepair(UseInfoAble info, LocalDateTime cancleDateTime);

	/**
	 * 锁定房间 调整客房类型统计表，房间状态表，房间状态统计表
	 * 
	 * @param guestRoom
	 * @return
	 */
	public boolean lock(UseInfoAble info);

	/**
	 * 解除锁定状态，调整客房类型统计表，房间状态表，房间状态统计表
	 * 
	 * @param guestRoom
	 * @return
	 */
	public boolean cancleLock(UseInfoAble info, LocalDateTime cancleDateTime);

	public boolean checkOut(GuestRoom guestRoom);

}
