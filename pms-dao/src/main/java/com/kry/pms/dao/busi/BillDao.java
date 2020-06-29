package com.kry.pms.dao.busi;

import java.util.List;
import java.util.Map;

import com.kry.pms.model.dto.BillStatistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.http.request.busi.BillQueryBo;
import com.kry.pms.model.persistence.busi.Bill;
import com.kry.pms.model.persistence.sys.Account;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BillDao extends BaseDao<Bill> {

    public List<Bill> findByHotelCode(String hotelCode);

    public List<Bill> findByAccountId(String id);

    public List<Bill> findByAccountAndStatus(Account account, String status);

    @Query(nativeQuery = true, value = "select count(id) from t_bill where account_id = ?1 and status = 'NEED_SETTLED'")
    int countUnSellteBill(String accountId);
    @Query(nativeQuery = true, value = "select count(id) from t_bill where account_id = ?1 and total!=0 and status = 'NEED_SETTLED'")
    int countUnSellteNotZeroBill(String accountId);

    @Query(value = "select new com.kry.pms.model.dto.BillStatistics(sum(cost),sum(pay)) from Bill where account =?1 and status =?2")
    BillStatistics sumNeedSettle(Account account, String status);

    @Query(nativeQuery = true, value = " select \n" +
            " tb.account_id accountId, ta.`code`, ta.`name`, \n" +
            " IFNULL(sum(tb.cost),0) costTotal, \n" +
            " IFNULL(sum(tb.pay),0) payTotal, \n" +
            " IFNULL(sum(tb.total),0) total, \n" +
            " tb.`status` \n" +
            " from t_bill tb \n" +
            " left join t_account ta on tb.account_id = ta.id \n" +
            " where tb.hotel_code = :hotelCode \n" +
            " and tb.account_id = :accountId \n" +
            " and tb.`status` in (:statusList) \n" +
            " group by tb.account_id, tb.`status`,ta.`code`,ta.`name` ")
    List<Map<String, Object>> getStatusTotal(@Param("hotelCode") String hotelCode, @Param("accountId") String accountId, @Param("statusList") List<String> statusList);
    @Modifying
    @Query(nativeQuery = true, value = "update t_bill set status = 'SETTLED' where account_id = ?1 and total=0")
    int autoSettleZeroBill(String id);
}
