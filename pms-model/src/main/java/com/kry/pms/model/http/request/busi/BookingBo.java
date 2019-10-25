package com.kry.pms.model.http.request.busi;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.kry.pms.model.http.request.room.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class BookingBo extends BaseBo {
	private String name;
	private String groupName;
	@NotBlank(message = "联系人不能为空")
	private String contactName;
	@NotBlank(message = "联系电话不能为空")
	private String contactMobile;
	@NotBlank(message = "销售员不能为空")
	private String marketingId;// 销售员
	@Future(message = "到店时间错误")
	private LocalDate arriveDate;
	private String operationId;
	@Future(message = "离店时间错误")
	private LocalDate leaveDate;
	private Integer humanCount;
	@Min(value = 1, message = "预定房间数必须大于1")
	private Integer roomCount;
	private String roomTypeId;
	private String channelId;
	private String type;
	private String protocolId;// 协议单位
	@Min(value = 1, message = "预定天数至少大于1")
	private Integer days;
	private String remark;
	private String holdTime;
	private Boolean priceSecret = false;// 房价保密
	private List<BookingItemBo> items;
}
