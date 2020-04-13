package com.kry.pms.service.pay;

import com.kry.pms.model.persistence.pay.WechatRefundRecord;
import com.kry.pms.service.BaseService;

public interface WechatRefundRecordService extends BaseService<WechatRefundRecord> {


    void deleteTrue(String id);
}