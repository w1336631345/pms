package com.kry.pms.model.http.request.busi;

import com.kry.pms.model.persistence.busi.CheckInRecord;
import lombok.Data;

@Data
public class CheckUpdateItemTestBo extends CheckInRecord {

    private String[] ids;
}
