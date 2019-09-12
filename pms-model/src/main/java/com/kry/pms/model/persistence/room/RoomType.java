package com.kry.pms.model.persistence.room;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.sys.StaticResource;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_room_type")
public class RoomType extends PersistenceModel {
	@Column(columnDefinition = "varchar(64) default NULL COMMENT '名称'")
	private String name;
	@Column(columnDefinition = "double COMMENT '定价'")
	private Double price;
	@Column(columnDefinition = "varchar(40) default NULL COMMENT '编码'")
	private String code;
	@Column(columnDefinition = "varchar(400) default NULL COMMENT '描述'")
	private String description;
	@Column(columnDefinition = "int(8) COMMENT '排序'")
	private Integer sortCode;
	@Column(columnDefinition = "int(8) COMMENT '超预留数'")
	private Integer overReservation;
	@Column(columnDefinition = "int(8) COMMENT '房间总数'")
	private Integer roomCount;
	@Column(columnDefinition = "int(8) COMMENT '审核阈值'")
	private Integer bookingVerifyThreshold;
	@OneToOne
	private RoomTypeExtInfo roomTypeExtInfo;
	@Column(columnDefinition = "varchar(400) default NUll COMMENT '主图'")
	private String mainPicture;
	@Column(columnDefinition = "varchar(400) default NUll COMMENT '缩略图'")
	private String thumbnail;
	@OneToMany
	private List<StaticResource> pictures;

}
