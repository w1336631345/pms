package com.kry.pms.service.msg;

import com.kry.pms.model.persistence.msg.MsgTemplate;
import com.kry.pms.service.BaseService;

public interface MsgTemplateService extends BaseService<MsgTemplate>{

    void deleteTrue(String id);
}
