package com.kry.pms.model.persistence.marketing;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.kry.pms.model.annotation.PropertyMsg;
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
	@PropertyMsg("渠道名称")
	private String name;
	@Column
	private String description;
	@Column
	private String type;//Y团队，N散客
	@Column(columnDefinition = "varchar(100) COMMENT '集团编码'")
	private String clusterCode;
	@Column(columnDefinition = "varchar(100) COMMENT '是否启用'")
	private String isUsed;
}
