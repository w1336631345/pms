package com.kry.pms.service.report;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.report.GenerateReportsLog;
import com.kry.pms.model.persistence.report.RoomReport;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GenerateReportsLogService extends BaseService<GenerateReportsLog> {

}
