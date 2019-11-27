package com.kry.pms.model.http.request.busi;

import java.util.List;

import com.kry.pms.model.persistence.busi.Bill;
import com.kry.pms.model.persistence.org.Employee;

import lombok.Data;
@Data
public class BillCheckBo {
	private String shiftCode;
	private String checkType;
	private String accountId;
	private List<String>  billIds;
	private List<Bill> bills;
	private Employee operationEmployee;
	private String hotelCode;
}
