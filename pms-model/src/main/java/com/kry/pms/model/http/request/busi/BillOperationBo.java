package com.kry.pms.model.http.request.busi;

import lombok.Data;

@Data
public class BillOperationBo {
	private String id;
	private String op;
	private Double val1;
	private Double val2;
	private String remark;
}
