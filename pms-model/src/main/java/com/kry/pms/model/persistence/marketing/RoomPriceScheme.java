package com.kry.pms.model.persistence.marketing;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.*;

import com.kry.pms.model.annotation.PropertyMsg;
import com.kry.pms.model.persistence.PersistenceModel;

import com.kry.pms.model.persistence.goods.SetMeal;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_room_price_scheme")
public class RoomPriceScheme extends PersistenceModel {
	@Column
	private LocalDate startTime;
	@Column
	private LocalDate endTime;
	@Column
	private String description;
	@Column
	private String code;
	@Column
	@PropertyMsg("房价方案名称")
	private String name;
	@Column
	private Boolean isDefault;
	@OneToMany(cascade = CascadeType.ALL,fetch=FetchType.LAZY)
	private List<RoomPriceSchemeItem> items;
//	@OneToOne
//	private SetMeal setMeal;//包价
	@ManyToOne(fetch=FetchType.LAZY)
	private MarketingSources marketingSources;//市场
	@ManyToOne(fetch=FetchType.LAZY)
	private DistributionChannel distributionChannel;//渠道
	@Column
	private String isShow;//是否展示：Y是，N否
	@Column
	private String sortNum;//排序
}
