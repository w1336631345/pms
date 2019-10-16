package com.kry.pms.model.http.request.busi;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.kry.pms.model.http.request.room.BaseBo;

import lombok.Data;

@Data
public class CheckInItemBo extends BaseBo{
	@NotBlank
	private String roomNum;
	@NotBlank
	private String roomId;
	@Min(value=1)
	private Integer days;
	@Min(value=1)
	private Integer humanCount;
	@Valid
	private List<GuestInfoBo> guests;
	
}
