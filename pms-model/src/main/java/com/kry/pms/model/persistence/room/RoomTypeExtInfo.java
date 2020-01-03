package com.kry.pms.model.persistence.room;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_room_type_ext_info")
public class RoomTypeExtInfo extends PersistenceModel {
	@Column(columnDefinition = "int(8) COMMENT '面积'")
	private Integer size;
	@Column(columnDefinition = "int(8) COMMENT '最低楼层'")
	private Integer minFoolr;
	@Column(columnDefinition = "int(8) COMMENT '最高楼层'")
	private Integer maxFoolr;
	@Column(columnDefinition = "varchar(100) COMMENT '床尺寸'")
	private String bedSize;
	@Column(columnDefinition = "varchar(400) default NULL COMMENT '全景地址'")
	private String overallView;
	@Column(columnDefinition = "int(8) COMMENT '窗户'")
	private Integer windowSize;
	@Column(columnDefinition = "tinyint(1)  COMMENT '大床房'")
	private Boolean singleBed;
	@Column(columnDefinition = "tinyint(1)  COMMENT '标准间'")
	private Boolean twinBed;
	@Column(columnDefinition = "tinyint(1)  COMMENT '三人间'")
	private Boolean threeBed;
	@Column(columnDefinition = "tinyint(1)  COMMENT '删除状态'")
	private Boolean extraBed;
	@Column(columnDefinition = "int(8) COMMENT '加床费'")
	private Integer extraBedPrice;
	@Column(columnDefinition = "int(8) COMMENT '可住人数'")
	private Integer availablNum;
	@Column(columnDefinition = "varchar(400) default NULL COMMENT '便利信息'")
	private String convenienceInfo;
	@Column(columnDefinition = "varchar(400) default NULL COMMENT '多媒体信息'")
	private String mediaInfo;
	@Column(columnDefinition = "varchar(400) default NULL COMMENT '商品信息'")
	private String goodsInfo;
	@Column(columnDefinition = "varchar(400) default NULL COMMENT '浴室信息'")
	private String washRoomInfo;
	@Column(columnDefinition = "varchar(400) default NULL COMMENT '其他信息'")
	private String otherInfo;
}
