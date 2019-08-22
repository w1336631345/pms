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
	@Column
	private String name;
	@Column
	private String description;
	@Column(name="code_")
	private String code;
	@Column
	private Integer sortNum;
}
