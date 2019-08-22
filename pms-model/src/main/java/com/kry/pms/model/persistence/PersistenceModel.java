package com.kry.pms.model.persistence;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Louis Lueng
 *
 */
@Getter
@Setter
@MappedSuperclass
public class PersistenceModel implements Serializable {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(columnDefinition = "varchar(64)")
	protected String id;
	@Column(columnDefinition = "datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'")
	protected Date createDate;
	@Column(columnDefinition = "varchar(255) COMMENT '创建人'")
	protected String createUser;
	@Column(columnDefinition = "datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'")
	protected Date updateDate;
	@Column(columnDefinition = "varchar(255) COMMENT '修改人'")
	protected String updateUser;
	@Column(columnDefinition = "varchar(32) default 'draft' COMMENT '状态'")
	protected Boolean deleted;
	@Column(columnDefinition = "tinyint(1) default '0' COMMENT '删除状态'")
	protected String status;
	@Column(columnDefinition = "varchar(64) default '0000' COMMENT '企业编码'")
	protected String corporationCode;
	@Column(columnDefinition = "varchar(64) default '0000' COMMENT '酒店编码'")
	protected String hotelCode;

}
