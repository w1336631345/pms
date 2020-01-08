package com.kry.pms.model.http.request.busi;

import java.util.List;

import com.kry.pms.model.persistence.busi.Bill;
import com.kry.pms.model.persistence.org.Employee;

import lombok.Data;

@Data
public class BillCheckBo {
	private String shiftCode;
	private String checkType;//ALL 为全部结账，PART 为部分结账    ROOM 房间结账     GROUP 团队结账   IG 散客团队结账
	private String accountId;
	private String roomNum;
	private String orderNum;
	private List<String> billIds;
	private List<Bill> bills;
	private Employee operationEmployee;
	private String hotelCode;
}
