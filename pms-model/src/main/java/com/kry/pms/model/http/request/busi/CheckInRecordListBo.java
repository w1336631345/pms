package com.kry.pms.model.http.request.busi;

import com.kry.pms.model.persistence.busi.CheckInRecord;
import lombok.Data;

import java.util.List;
@Data
public class CheckInRecordListBo {
    List<CheckInRecord> cirs;
    Boolean isRoomLink = false;
}
