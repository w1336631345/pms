package com.kry.pms.dao.pay;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.pay.WechatRefundRecord;

import java.util.List;

public interface WechatRefundRecordDao extends BaseDao<WechatRefundRecord> {

    List<WechatRefundRecord> findByHotelCode(String code);

    WechatRefundRecord findByTransactionId(String transaction_id);

}
