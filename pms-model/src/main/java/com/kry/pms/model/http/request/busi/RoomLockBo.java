package com.kry.pms.model.http.request.busi;

import com.kry.pms.model.http.request.room.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class RoomLockBo extends BaseBo {
	private String roomIds;
	private String roomNums;
	private String lockType;
}
