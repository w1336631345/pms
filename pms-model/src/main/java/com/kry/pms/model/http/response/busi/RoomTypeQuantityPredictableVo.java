package com.kry.pms.model.http.response.busi;

import java.time.LocalDate;
import lombok.Data;
@Data
public class RoomTypeQuantityPredictableVo {
	private String roomTypeName;
	private LocalDate startDate;
	private LocalDate endDate;
	private Double price;
	private Integer availableTotal;
	private String roomTypeId;
	
}
