package com.kry.pms.model.persistence.marketing;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.annotation.PropertyMsg;
import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;
/**
 * 市场来源
 * @author Louis
 *
 */
@Entity
@Getter
@Setter
@Table(name = "t_marketing_sources")
public class MarketingSources extends PersistenceModel {
	@Column
	private String code;
	@Column
	@PropertyMsg("市场名称")
	private String name;
	@Column
	private String description;
	@OneToOne
	private RoomPriceScheme roomPriceScheme;
	@Column
	private String type;//Y团队，N散客
	@Column
	private String groupType;//组别
	@Column(columnDefinition = "varchar(100) COMMENT '集团编码'")
	private String clusterCode;
	@Column(columnDefinition = "varchar(100) COMMENT '是否启用'")
	private String isUsed;
	@Column(columnDefinition = "varchar(20) COMMENT '权限状态：M：不可删除，N：可删除'")
	private String roleStatus;
	@Column
	private String bottomTable;//底表行
}
