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
	@Column(columnDefinition="int(8) COMMENT '面积'")
	private Integer size;
	@Column(columnDefinition="int(8) COMMENT '最低楼层'")
	private Integer minFoolr;
	@Column(columnDefinition="int(8) COMMENT '最高楼层'")
	private Integer maxFoolr;
	@Column(columnDefinition="int(8) COMMENT '窗户'")
	private Integer windowSize;
	@Column(columnDefinition="int(8) COMMENT '加床费'")
	private Integer extraBedPrice;
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
