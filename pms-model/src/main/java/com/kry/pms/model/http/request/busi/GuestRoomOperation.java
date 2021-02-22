package com.kry.pms.model.http.request.busi;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import lombok.Data;
@Data
public class GuestRoomOperation {
	@NotBlank
	private String op;
	@NotBlank
	private String[] roomIds;
	private String currentId;
	private String roomNums;
	private String endToStatus;
	private String reasonId;
	private String operationEmployeeId;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String remark;
}
