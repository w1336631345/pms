package com.kry.pms.model.persistence.room;

import java.util.List;

import javax.persistence.*;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_guest_room")
public class GuestRoom extends PersistenceModel {
	@OneToOne(fetch = FetchType.LAZY)
	private RoomType roomType;
	@ManyToMany
	private List<RoomTag> tags;
	@ManyToOne(fetch = FetchType.LAZY)
	private Floor floor;
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '房间号'")
	private String roomNum;
	@Column(columnDefinition = "varchar(400) default NUll COMMENT '主图'")
	private String mainPicture;
	@Column(columnDefinition = "int(8) COMMENT '床数'")
	private Integer bedCount;
	@Column(columnDefinition = "int(8) COMMENT '允许入住人数'")
	private Integer occupantCapacity;
	@Transient
	private List<String> roomNums;
	@Column
	private String sortNum;
	
}
