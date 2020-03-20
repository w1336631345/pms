package com.kry.pms.model.http.response.busi;

import com.kry.pms.model.persistence.busi.Bill;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SettleInfoVo {
	private boolean settlEnable =true;
	private String message;
	private String name;
	private String settleType;
	private Double total = 0.0;
	private Double cost = 0.0;
	private Double pay = 0.0;
	private Integer totalSeq = 0;// 总帐序号
	private Double creditLimit = 0.0;
	private Double availableCreditLimit = 0.0;
	private List<Bill> bills = new ArrayList<>();
}
