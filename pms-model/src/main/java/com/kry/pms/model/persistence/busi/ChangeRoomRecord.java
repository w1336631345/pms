package com.kry.pms.model.persistence.busi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.dict.RoomChangeReason;
import com.kry.pms.model.persistence.org.Employee;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_change_room_record")
public class ChangeRoomRecord extends PersistenceModel{
	@OneToOne
	private RoomChangeReason reason;
	@OneToOne
	private Employee operationEmployee;
	@OneToOne
	private Employee authorizeEmployee;
	@Column(columnDefinition = "varchar(255) COMMENT '操作员备注'")
	private String operationRemark;
	@Column(columnDefinition = "varchar(255) COMMENT '授权人员备注'")
	private String authorizeRemark;
	
}
