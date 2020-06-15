package com.kry.pms.model.http.request.busi;

import com.kry.pms.model.persistence.busi.CheckInRecord;
import lombok.Data;

import java.util.List;

@Data
public class CheckInRecordAuditBo {

    Boolean nativeUrl;
    List<CheckInRecord> cirs;
}
