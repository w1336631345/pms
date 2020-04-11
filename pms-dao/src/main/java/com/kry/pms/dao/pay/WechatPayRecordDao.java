package com.kry.pms.dao.pay;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.pay.WechatPayRecord;

import java.util.List;

public interface WechatPayRecordDao extends BaseDao<WechatPayRecord> {

    List<WechatPayRecord> findByHotelCode(String code);

    WechatPayRecord findByTransactionId(String transaction_id);

}
