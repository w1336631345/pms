package com.kry.pms.model.persistence.dict;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;
/**
 * 证件类型
 * @author Louis
 *
 */
@Entity
@Getter
@Setter
@Table(name = "t_certificate_type")
public class CertificateType extends PersistenceModel{
	@Column(columnDefinition = "varchar(64) default NULL COMMENT '名称'")
	private String name;
	@Column(columnDefinition = "varchar(40) default NULL COMMENT '编码'")
	private String code;
	@Column(columnDefinition = "varchar(400) default NULL COMMENT '描述'")
	private String description;
	@Column
	private Integer sortNum;
}
