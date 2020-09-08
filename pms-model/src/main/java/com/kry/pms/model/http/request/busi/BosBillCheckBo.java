package com.kry.pms.model.http.request.busi;

import com.kry.pms.model.persistence.busi.BosBill;
import com.kry.pms.model.persistence.org.Employee;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class BosBillCheckBo {
	private String accountId;//AccountId,RoomId,主单id,联房id
	private List<BosBill> bosBills;
	private String bosBillId;
	private String hotelCode;
	private Employee employee;
	private String shiftCode;
}
