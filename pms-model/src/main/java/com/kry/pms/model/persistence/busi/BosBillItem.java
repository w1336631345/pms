package com.kry.pms.model.persistence.busi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.marketing.DiscountScheme;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "t_bos_bill_item")
public class BosBillItem extends PersistenceModel {
	@ManyToOne
	private BosBill bosBill;
	@JsonIgnore
	public BosBill getBosBill() {
		return bosBill;
	}
	@Column
	private String type;
	@OneToOne
	private RoomRecord roomRecord;
	@Column
	private LocalDate billDate;
	@Column
	private String goodsCode;
	@Column
	private String goodsName;
	@Column
	private String bosGoodsInfoId;
	@OneToOne
	private DiscountScheme discountScheme;
	@Column
	private Double total;
	@Column
	private Double quantity;
	@Column
	private Double price;
	@Column
	private Integer itemSeq;
	@Column
	private String statusPayment;
	
}
