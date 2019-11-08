package com.kry.pms.dao.report;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.busi.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface AuditReportDao extends BaseDao<Bill> {

    @Query(nativeQuery = true,
            value = "select  " +
                    " tb.*, tp.name " +
                    " from t_bill tb left join t_product tp " +
                    " on tb.product_id = tp.id " +
                    " where 1=1 " )
    List<Map<String, Object>> unreturnedGuests(@Param("status")String status, @Param("hotelCode")String hotelCode);
}
