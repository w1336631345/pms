package com.kry.pms.model.persistence.marketing;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="t_marketing_channel")
public class MarketingChannel extends PersistenceModel{

}
