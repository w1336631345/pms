package com.kry.pms.service.room;

import java.time.LocalDateTime;

import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.RoomType;

public interface UseInfoAble {

	/**
	 * 获取房间类型
	 * 
	 * @return
	 */
	public RoomType getRoomType();

	/**
	 * 获取房间
	 * 
	 * @return
	 */
	public GuestRoom getGuestRoom();

	/**
	 * 获取摘要信息
	 * 
	 * @return
	 */
	public String getSummaryInfo();

	/**
	 * 业务主键 同一房间唯一
	 * 
	 * @return
	 */
	public String getBusinessKey();

	/**
	 * 是团队
	 * 
	 * @return
	 */
	public boolean isGroup();

	/**
	 * 是OTA
	 * 
	 * @return
	 */
	public boolean isOTA();

	/**
	 * 是免费
	 * 
	 * @return
	 */
	public boolean isFree();

	/**
	 * 是钟点房
	 * 
	 * @return
	 */
	public boolean isHourRoom();

	/**
	 * 是欠费
	 * 
	 * @return
	 */

	public boolean isArrears();

	/**
	 * 开始时间
	 * 
	 * @return
	 */
	public LocalDateTime getStartTime();

	/**
	 * 结束时间
	 * 
	 * @return
	 */
	public LocalDateTime getEndTime();

}
