package com.kry.pms.model.persistence.busi;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="t_booking_record")
public class BookingRecord extends PersistenceModel {

}
