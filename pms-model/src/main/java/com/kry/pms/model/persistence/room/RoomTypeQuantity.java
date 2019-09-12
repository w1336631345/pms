package com.kry.pms.model.persistence.room;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

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
	@Column(columnDefinition="int(8) COMMENT '可预订的'")
	private Integer predictableTotal;// 可预订的
	@Column(columnDefinition="int(8) COMMENT '预定数'")
	private Integer bookingTotal = 0;
	@Column(columnDefinition="int(8) COMMENT '将离'")
	private Integer willLeaveTotal= 0;
	@Column(columnDefinition="int(8) COMMENT '维修房'")
	private Integer repairTotal = 0;
	@Column(columnDefinition="int(8) COMMENT '锁定房'")
	private Integer lockedTotal =0;
	@Column(columnDefinition="int(8) COMMENT '在住房'")
	private Integer usedTotal =0;

}
