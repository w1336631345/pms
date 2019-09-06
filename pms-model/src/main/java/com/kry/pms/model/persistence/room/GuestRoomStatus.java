package com.kry.pms.model.persistence.room;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_guest_room_status")
public class GuestRoomStatus extends PersistenceModel {
	@OneToOne(fetch = FetchType.LAZY)
	private GuestRoom guestRoom;
	@Column
	private String roomNum;
	@Column
	private String roomTypeName;
	@Column
	private String summary;
	@Column(columnDefinition = "varchar(64) default 'VC' COMMENT '客房状态'")
	private String roomStatus;
	@Column
	private String description;
	@Column
	private Boolean locked;// 锁定
	@Column
	private Boolean willLeave;// 将离
	@Column
	private Boolean willArrive;// 将到
	@Column
	private Boolean hourRoom;// 钟点房
	@Column
	private Boolean free;// 免费
	@Column
	private Boolean personal;// 个人
	@Column(name="group_")
	private Boolean group;// 团队
	@Column
	private Boolean linkedRoom;// 联房
	@Column
	private Boolean repairRoom;// 维修
	@Column
	private Boolean overdued;// 欠费

}
