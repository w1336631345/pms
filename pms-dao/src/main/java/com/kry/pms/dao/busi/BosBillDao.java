package com.kry.pms.dao.busi;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.dto.BillStatistics;
import com.kry.pms.model.persistence.busi.Bill;
import com.kry.pms.model.persistence.busi.BosBill;
import com.kry.pms.model.persistence.sys.Account;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface BosBillDao extends BaseDao<BosBill> {

    public List<BosBill> findByHotelCode(String hotelCode);

    public List<BosBill> findByAccountId(String id);

    @Query(nativeQuery = true, value = " select  \n" +
            " tb.*  \n" +
            " from t_bos_bill tb \n" +
            "  left join t_bos_business_site tbbs on tb.product_id = tbbs.product_id \n" +
            "  left join t_account ta on tb.account_id = ta.id \n" +
            " where 1=1" +
            "  and if(:hotelCode is not null && :hotelCode != '', tb.hotel_code=:hotelCode, 1=1 ) \n"+
            "  and if(:siteId is not null && :siteId != '', tbbs.id=:siteId, 1=1 ) \n"+
            "  and if(:roomNum is not null && :roomNum != '', tb.room_num=:roomNum, 1=1 ) \n"+
            "  and if(:accountCode is not null && :accountCode != '', ta.`code`=:accountCode, 1=1 ) ")
    List<BosBill> findQuery(@Param("hotelCode")String hotelCode, @Param("siteId")String siteId, @Param("roomNum")String roomNum, @Param("accountCode")String accountCode);


}
