package com.kry.pms.model.http.response.busi;

import java.util.Collection;

import com.kry.pms.base.Constants;
import com.kry.pms.model.persistence.sys.Account;

import lombok.Data;

@Data
public class AccountSummaryVo {
	private String id;
	private String name;
	private Double total;
	private String roomNum;
	private String type;
	private Double pay;
	private Double cost;
	private String settleType;
	private Collection<AccountSummaryVo> children;

	public AccountSummaryVo() {
		
	}
	public AccountSummaryVo(Account acc) {
		this.id = acc.getId();
		this.name = acc.getName();
		this.total = acc.getTotal();
		this.roomNum = acc.getRoomNum();
		if(acc.getType().equals(Constants.Type.ACCOUNT_CUSTOMER)) {
			this.settleType=Constants.Type.SETTLE_TYPE_ACCOUNT;
		}
		this.type = acc.getType();
		this.cost = acc.getCost();
		this.pay = acc.getPay();
	}
}
