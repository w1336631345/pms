package com.kry.pms.dao.busi;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.busi.SettleAccountRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SettleAccountRecordDao extends BaseDao<SettleAccountRecord>{


    /**
     * @desc:查询某账号最后一次结账时间
     * @author: WangXinHao
     * @date: 2021/3/23 0023 11:26
     */
    @Query(nativeQuery = true, value = " select *" +
            "  from t_settle_account_record\n" +
            "  where hotel_code =  :hotelCode  and  account_id = :accountId  order by settle_time desc limit 1")
    SettleAccountRecord  findLastSettleRecord(@Param("hotelCode") String hotelCode, @Param("accountId") String accountId);

}
