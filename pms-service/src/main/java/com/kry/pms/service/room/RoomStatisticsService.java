package com.kry.pms.service.room;

import java.time.LocalDateTime;

import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.RoomType;

public interface RoomStatisticsService {
	/**
	 * 预定 调整客房类型统计表
	 * 
	 * @param roomType    房类型
	 * @param checkInDate 预定入住时间
	 * @param quantity    数量
	 * @param days        入住天数
	 * @return
	 */
	public boolean booking(RoomType roomType, LocalDateTime checkInDate, int quantity, int days);

	/**
	 * 入住 调整客房类型统计表，房间状态表，房间状态统计表
	 * 
	 * @param guestRoom   客房
	 * @param checkInDate 入住时间
	 * @param days        入住天数
	 * @return
	 */

	public boolean checkIn(GuestRoom guestRoom, LocalDateTime checkInDate, int days,boolean isGroup);

	/**
	 * 退房 调整客房类型统计表，房间状态表，房间状态统计表
	 * 
	 * @param guestRoom    客房
	 * @param checkOutDate 退房时间
	 * @return
	 */
	public boolean checkOut(GuestRoom guestRoom, LocalDateTime checkOutDate);

	/**
	 * 换房 调整客房类型统计表，房间状态表，房间状态统计表
	 * 
	 * @param roomRecord   入住记录
	 * @param newGuestRoom 新房间
	 * @return
	 */

	public boolean changeRoom(RoomRecord roomRecord, GuestRoom newGuestRoom);

	/**
	 * 维修 调整客房类型统计表，房间状态表，房间状态统计表
	 * 
	 * @param guestRoom
	 * @return
	 */

	public boolean repairRoom(GuestRoom guestRoom);

	/**
	 * 维修完成  调整客房类型统计表，房间状态表，房间状态统计表
	 * 
	 * @param guestRoom
	 * @return
	 */
	public boolean repairRoomComplete(GuestRoom guestRoom);

	/**
	 * 锁定房间   调整客房类型统计表，房间状态表，房间状态统计表
	 * @param guestRoom
	 * @return
	 */
	public boolean lockedRoom(GuestRoom guestRoom,LocalDateTime checkInDate, int days);

	/**
	 * 解除锁定状态，调整客房类型统计表，房间状态表，房间状态统计表
	 * @param guestRoom
	 * @return
	 */
	public boolean unlockRoom(GuestRoom guestRoom);

	/**
	 * 特指手工将客房置为空脏房，其他操作不算，调整房间状态表，房间状态统计表
	 * @param guestRoom
	 * @return
	 */
	public boolean dirtyRoom(GuestRoom guestRoom);

	/**
	 * 特指手工将客房置为空净房，其他操作不算，调整房间状态表，房间状态统计表
	 * @param guestRoom
	 * @return
	 */
	public boolean cleanedRoom(GuestRoom guestRoom);
}
