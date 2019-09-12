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
	@Column(columnDefinition = "varchar(400) default NUll COMMENT '描述'")
	private String description;
	@Column (columnDefinition = "bit(1) default NULl COMMENT '锁定'")
	private Boolean locked;// 锁定
	@Column (columnDefinition = "bit(1) default NULl COMMENT '将离'")
	private Boolean willLeave;// 将离
	@Column (columnDefinition = "bit(1) default NULl COMMENT '将到'")
	private Boolean willArrive;// 将到
	@Column (columnDefinition = "bit(1) default NULl COMMENT '钟点房'")
	private Boolean hourRoom;// 钟点房
	@Column (columnDefinition = "bit(1) default NULl COMMENT '免费'")
	private Boolean free;// 免费
	@Column (columnDefinition = "bit(1) default NULl COMMENT '个人'")
	private Boolean personal;// 个人
	@Column(name="group_",columnDefinition = "bit(1) default NULl COMMENT '团队'")
	private Boolean group;// 团队
	@Column (columnDefinition = "bit(1) default NULl COMMENT '联房'")
	private Boolean linkedRoom;// 联房
	@Column (columnDefinition = "bit(1) default NULl COMMENT '维修'")
	private Boolean repairRoom;// 维修
	@Column (columnDefinition = "bit(1) default NULl COMMENT '欠费'")
	private Boolean overdued;// 欠费

}
