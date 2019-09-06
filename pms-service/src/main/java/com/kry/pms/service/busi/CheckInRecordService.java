package com.kry.pms.service.busi;

import java.util.List;

import com.kry.pms.base.DtoResponse;
import com.kry.pms.model.http.request.busi.CheckInBo;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.service.BaseService;

public interface CheckInRecordService extends BaseService<CheckInRecord>{

	public void checkIn(CheckInBo checkInBo, DtoResponse<List<CheckInRecord>> rep);

	public List<CheckInRecord> checkOut(String roomId);
}