package com.kry.pms.model.http.response.marketing;

import java.util.List;

import com.kry.pms.model.http.response.goods.SetMealVo;

import lombok.Data;
@Data
public class SchemeItemVo{
	List<SetMealVo> extrs;
	String roomTypeId;
	private Double price;
	String id;

}
