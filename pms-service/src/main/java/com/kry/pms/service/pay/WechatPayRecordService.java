package com.kry.pms.service.pay;

import com.kry.pms.model.persistence.pay.WechatPayRecord;
import com.kry.pms.service.BaseService;

public interface WechatPayRecordService extends BaseService<WechatPayRecord> {


    void deleteTrue(String id);
}