package com.kry.pms.model.http.response.busi;

import lombok.Data;

@Data
public class SettleInfoVo {
	private String name;
	private String settleType;
	private Double total;
	private Double cost;
	private Double pay;
	private Integer totalSeq;//总帐序号
	private Double creditLimit;
	private Double availableCreditLimit;
}
