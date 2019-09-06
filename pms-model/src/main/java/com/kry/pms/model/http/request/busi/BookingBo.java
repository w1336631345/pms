package com.kry.pms.model.http.request.busi;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

import com.kry.pms.model.http.request.room.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class BookingBo extends BaseBo {
	private String groupName;
	@NotBlank(message = "联系人不能为空")
	private String contactName;
	@NotBlank(message = "联系电话不能为空")
	private String contactMobile;
	@NotBlank(message = "销售员不能为空")
	private String marketingId;// 销售员
	@Future(message = "到店时间错误")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime arriveTime;
	private String operationId;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime leaveTime;
	@Min(value = 1, message = "人数必须大于1")
	private Integer humanCount;
	@Min(value = 1, message = "预定房间数必须大于1")
	private Integer roomCount;
	@NotBlank(message = "请选择房间类型")
	private String roomTypeId;
	private String channelId;
	private String protocolId;// 协议单位
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime retainTime;
	@Min(value = 1, message = "预定天数至少大于1")
	private Integer days;
	private String remark;
	private Boolean priceSecret = false;// 房价保密
}
