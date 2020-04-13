package com.kry.pms.dao.pay;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.pay.WechatMerchants;
import com.kry.pms.model.persistence.pay.WechatRefundRecord;

import java.util.List;

public interface WechatMerchantsDao extends BaseDao<WechatMerchants> {

    List<WechatMerchants> findByHotelCode(String code);

}
