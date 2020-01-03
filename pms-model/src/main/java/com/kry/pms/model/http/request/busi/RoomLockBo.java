package com.kry.pms.model.http.request.busi;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;

import com.kry.pms.model.http.request.room.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoomLockBo extends BaseBo {
	@NotBlank
	private String roomIds;
	private String roomNums;
	@NotBlank
	private String lockReasonId;
	private LocalDateTime startTime;
	@Future
	private LocalDateTime endTime;
}
