package com.kry.pms.model.persistence.marketing;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
	private String name;
	@Column
	private String description;
	@OneToOne
	private RoomPriceScheme roomPriceScheme;
}
