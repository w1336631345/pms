package com.kry.pms.model.http.response.marketing;

import com.kry.pms.model.http.response.goods.SetMealVo;

import lombok.Data;
@Data
public class SchemeItemVo{
	SetMealVo extrs;
	String roomTypeId;
	Double roomTypePrice;
	private Double price;
	String id;

}
