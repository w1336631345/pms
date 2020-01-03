package com.kry.pms.model.http.request.busi;

import com.kry.pms.model.http.request.room.BaseBo;
import com.kry.pms.model.persistence.org.Employee;

import lombok.Data;
@Data
public class CheckOutBo extends BaseBo{
	
	private String checkInId;
	private String remark;
	private Employee operationEmployee;
	private String type;
	
}
