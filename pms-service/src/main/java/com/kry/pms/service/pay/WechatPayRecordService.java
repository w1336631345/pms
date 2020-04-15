package com.kry.pms.service.pay;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.pay.WechatPayRecord;
import com.kry.pms.service.BaseService;

public interface WechatPayRecordService extends BaseService<WechatPayRecord> {


    void deleteTrue(String id);

    HttpResponse resultUpdate(String out_trade_no, String trade_state, String trade_state_desc, String currentHotleCode);
}