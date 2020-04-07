package com.kry.pms.model.persistence.marketing;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;

/**
 * 销售渠道
 * @author Louis
 *
 */
@Entity
@Getter
@Setter
@Table(name = "t_distribution_channel")
public class DistributionChannel extends PersistenceModel {
	@Column
	private String code;
	@Column
	private String name;
	@Column
	private String description;
}
