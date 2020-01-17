package com.kry.pms.model.persistence.sys;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_business_seq")
public class BusinessSeq implements Serializable {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(columnDefinition = "varchar(64)")
	protected String id;
	@Column(columnDefinition = "varchar(64) default '0000' COMMENT '企业编码'")
	protected String corporationCode;
	@Column(columnDefinition = "varchar(64) default '0000' COMMENT '酒店编码'")
	protected String hotelCode = "0000";
	@Column(columnDefinition = "tinyint(1) default '0' COMMENT '删除状态'")
	protected Integer deleted = 0;
	@Column(name = "type_")
	private String type;
	@Column(name = "seq_key")
	private String seqKey;
	@Column(name = "current_seq")
	private Integer currentSeq;
	@Column
	private String seqResetType;
	@Column
	private String prefix;
	@Column
	private Integer snLength;
	@Column
	private Integer yearLength;
}
