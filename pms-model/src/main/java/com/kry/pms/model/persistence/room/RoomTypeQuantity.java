package com.kry.pms.model.persistence.room;

import java.time.LocalDate;

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
@Table(name = "t_room_type_quantity")
public class RoomTypeQuantity extends PersistenceModel {
	@OneToOne(fetch=FetchType.LAZY)
	private RoomType roomType;
	@Column(columnDefinition = "varchar(40) default NULL COMMENT '房间类型名称'")
	private String roomTypeName;
	@Column(columnDefinition = "varchar(40) default NULL COMMENT '房间类型编码'")
	private String roomTypeCode;
	@Column(columnDefinition = "date DEFAULT NULL COMMENT '数据时间'")
	private LocalDate quantityDate;
	@Column(columnDefinition="int(8) COMMENT '总数'")
	private Integer roomCount;
	@Column(columnDefinition="int(8) COMMENT '可用'")
	private Integer availableTotal;// 可用的
	@Column(columnDefinition="int(8) COMMENT '可预订的（剩余）'")
	private Integer predictableTotal;// 可预定
	@Column(columnDefinition="int(8) COMMENT '预定数(预留)'")
	private Integer reserveTotal;// 已预留
	@Column(columnDefinition="int(8) COMMENT '预定数(已分房)'")
	private Integer bookingTotal ;//已纷繁
	@Column(columnDefinition="int(8) COMMENT '将离'")
	private Integer willLeaveTotal;//将要离开
	@Column(columnDefinition="int(8) COMMENT '将到'")
	private Integer willArriveTotal;//将要到
	@Column(columnDefinition="int(8) COMMENT '维修房'")
	private Integer repairTotal ;//维修
	@Column(columnDefinition="int(8) COMMENT '锁定房'")
	private Integer lockedTotal ;//锁定
	@Column(columnDefinition="int(8) COMMENT '在住房'")
	private Integer usedTotal ;//在住

}
