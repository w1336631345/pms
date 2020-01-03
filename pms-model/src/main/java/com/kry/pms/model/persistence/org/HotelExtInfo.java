package com.kry.pms.model.persistence.org;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_hotel_ext_info")
public class HotelExtInfo extends PersistenceModel {
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '早餐'")
	private String breakfirstInfo;
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '到店离店'")
	private String arriveAndLeavePolicy;
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '小孩'")
	private String childPolicy;
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '宠物'")
	private String petPolicy;
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '支付方式'")
	private String paymentChannel;
}
