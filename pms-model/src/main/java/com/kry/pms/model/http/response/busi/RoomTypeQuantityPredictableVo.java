package com.kry.pms.model.http.response.busi;

import java.time.LocalDate;
import lombok.Data;
@Data
public class RoomTypeQuantityPredictableVo {
	private String roomTypeName;
	private LocalDate startDate;
	private LocalDate endDate;
	private Double price;//价格
	private Integer availableTotal;//可用数
	private Integer overReservation;//可超预留数
	private String roomTypeId;
	private String roomPriceSchemeId;
	private String setMealId;
	private String setMealName;
	private Double purchasePrice;//定价
	
}
