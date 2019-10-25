package com.kry.pms.model.persistence.busi;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kry.pms.model.persistence.PersistenceModel;
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
	private Product product;
	@Column
	private LocalDate billDate;
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
