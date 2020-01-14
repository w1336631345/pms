package com.kry.pms.model.http.response.busi;

import lombok.Data;

@Data
public class SettleInfoVo {
	private String name;
	private String settleType;
	private Double total = 0.0;
	private Double cost = 0.0;
	private Double pay = 0.0;
	private Integer totalSeq;// 总帐序号
	private Double creditLimit = 0.0;
	private Double availableCreditLimit = 0.0;
}
