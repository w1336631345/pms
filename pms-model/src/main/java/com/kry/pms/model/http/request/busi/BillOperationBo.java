package com.kry.pms.model.http.request.busi;

import com.kry.pms.model.persistence.goods.Product;
import com.kry.pms.model.persistence.org.Employee;

import lombok.Data;

@Data
public class BillOperationBo {
	private Employee operationEmployee;
	private String shiftCode;
	private String id;
	private String op;
	private Double val1;
	private Double val2;
	private String remark;
	private Product product;
}
