package com.kry.pms.service.audit;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.BaseService;

public interface NightAuditService extends BaseService<RoomRecord> {

    HttpResponse manualAdd(User user, String[] ids);

    HttpResponse addReportAll(User loginUser);
}
