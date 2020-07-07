package com.kry.pms.model.persistence.busi;

import java.time.LocalDate;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.goods.BosGoodsInfo;
import com.kry.pms.model.persistence.goods.Product;
import com.kry.pms.model.persistence.marketing.DiscountScheme;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_bill_item")
public class BillItem extends PersistenceModel {
	@ManyToOne
	private Bill bill;
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
	@JsonIgnore
	public Bill getBill() {
		return bill;
	}
	
}
