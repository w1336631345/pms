package com.kry.pms.service.pay;

import com.kry.pms.model.persistence.pay.WechatMerchants;
import com.kry.pms.service.BaseService;

public interface WechatMerchantsService extends BaseService<WechatMerchants> {

    void deleteTrue(String id);
}