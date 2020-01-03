package com.kry.pms.model.persistence.guest;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_customer_points_record")
public class CustomerPointsRecord extends PersistenceModel {
	@OneToOne
	private Customer customer;

}
