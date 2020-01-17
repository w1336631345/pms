package com.kry.pms.model.http.response.room;

import java.io.Serializable;
import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.springframework.beans.BeanUtils;

import com.kry.pms.model.http.response.busi.CheckInRecordVo;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.RoomLockRecord;
import com.kry.pms.model.persistence.busi.RoomRepairRecord;
import com.kry.pms.model.persistence.room.GuestRoomStatus;

import lombok.Data;

@Data
public class GuestRoomStatusVo implements Serializable {
	private String roomTypeName;
	private String id;
	private String roomNum;
	private String summary;
	private String guestRoomId;
	private String roomStatus;
	private Boolean locked;// 锁定
	private Boolean willLeave;// 将离
	private Boolean willArrive;// 将到
	private Boolean hourRoom;// 钟点房
	private Boolean free;// 免费
	private Boolean personal;// 个人
	private Boolean group;// 团队
	private Boolean linkedRoom;// 联房
	private Boolean repairRoom;// 维修
	private Boolean overdued;// 欠费
	private Boolean ota;
	private List<CheckInRecordVo> currentCheckInRecord;
	private List<CheckInRecordVo> willCheckInRecord;
	private List<RoomLockRecordVo> lockRecord;
	private List<RoomRepairRecordVo> repairRecord;
	
	public GuestRoomStatusVo() {
		super();
	}

	public static GuestRoomStatusVo covert(GuestRoomStatus grs) {
		GuestRoomStatusVo grsv = new GuestRoomStatusVo();
		BeanUtils.copyProperties(grs, grsv);
		grsv.setGuestRoomId(grs.getGuestRoom().getId());
		if (grs.getCurrentCheckInRecords() != null) {
			grsv.setCurrentCheckInRecord(CheckInRecordVo.convert(grs.getCurrentCheckInRecords()));
		}
		if (grs.getWillCheckInRecords() != null) {
			grsv.setWillCheckInRecord(CheckInRecordVo.convert(grs.getWillCheckInRecords()));
		}
		if (grs.getLockRecords() != null) {
			grsv.setLockRecord(RoomLockRecordVo.convert(grs.getLockRecords()));
		}
		if (grs.getRepairRecords() != null) {
			grsv.setRepairRecord(RoomRepairRecordVo.convert(grs.getRepairRecords()));
		}
		return grsv;
	}

	public static GuestRoomStatusVo covert(GuestRoomStatus grs, boolean withDetail) {
		GuestRoomStatusVo grsv = new GuestRoomStatusVo();
		BeanUtils.copyProperties(grs, grsv);
		grsv.setGuestRoomId(grs.getGuestRoom().getId());
		if (withDetail) {
			if (grs.getCurrentCheckInRecords() != null) {
				grsv.setCurrentCheckInRecord(CheckInRecordVo.convert(grs.getCurrentCheckInRecords()));
			}
			if (grs.getWillCheckInRecords() != null) {
				grsv.setWillCheckInRecord(CheckInRecordVo.convert(grs.getWillCheckInRecords()));
			}
			if (grs.getLockRecords() != null) {
				grsv.setLockRecord(RoomLockRecordVo.convert(grs.getLockRecords()));
			}
			if (grs.getRepairRecords() != null) {
				grsv.setRepairRecord(RoomRepairRecordVo.convert(grs.getRepairRecords()));
			}
		}
		return grsv;
	}

	public GuestRoomStatusVo(String roomTypeName, String id, String roomNum, String summary, String guestRoomId,
			String roomStatus, Boolean locked, Boolean willLeave, Boolean willArrive, Boolean hourRoom, Boolean free,
			Boolean personal, Boolean group, Boolean linkedRoom, Boolean repairRoom, Boolean overdued, Boolean ota) {
		super();
		this.roomTypeName = roomTypeName;
		this.id = id;
		this.roomNum = roomNum;
		this.summary = summary;
		this.guestRoomId = guestRoomId;
		this.roomStatus = roomStatus;
		this.locked = locked;
		this.willLeave = willLeave;
		this.willArrive = willArrive;
		this.hourRoom = hourRoom;
		this.free = free;
		this.personal = personal;
		this.group = group;
		this.linkedRoom = linkedRoom;
		this.repairRoom = repairRoom;
		this.overdued = overdued;
		this.ota = ota;
	}
}
