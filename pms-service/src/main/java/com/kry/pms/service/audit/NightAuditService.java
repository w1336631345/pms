package com.kry.pms.service.audit;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.BaseService;

public interface NightAuditService extends BaseService<RoomRecord> {

    HttpResponse manualAdd(User user, String[] ids, String shiftCode);

    HttpResponse addReportAll(User loginUser);

    //报表导入各个统计-自动
    HttpResponse addReportAllAuto(String hotelCode);

    HttpResponse storedProcedure(String hotelCode);
}
