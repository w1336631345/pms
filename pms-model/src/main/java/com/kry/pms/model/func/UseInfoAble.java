package com.kry.pms.model.func;

import java.time.LocalDateTime;

import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.RoomType;

public interface UseInfoAble {

    public String uniqueId();

    public Integer getRoomCount();

    public boolean isTodayLeave();

    public boolean isTodayArrive();

    public String getRoomStatus();

    public LocalDateTime getActualStartTime();

    /**
     * 获取房间类型
     *
     * @return
     */
    public RoomType roomType();

    /**
     * 获取房间
     *
     * @return
     */
    public GuestRoom guestRoom();

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

    public boolean isVIP();

    /**
     * 资源使用开始时间
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

    public String nextStatus();

    public String useType();
}
