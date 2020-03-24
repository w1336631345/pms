package com.kry.pms.dao.busi;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.http.request.busi.BillQueryBo;
import com.kry.pms.model.persistence.busi.Bill;
import com.kry.pms.model.persistence.sys.Account;
import org.springframework.data.jpa.repository.Query;

public interface BillDao extends BaseDao<Bill> {

    public List<Bill> findByHotelCode(String hotelCode);

    public List<Bill> findByAccountId(String id);

    public List<Bill> findByAccountAndStatus(Account account, String status);

    @Query(nativeQuery = true, value = "select count(id) from t_bill where account_id = ?1 and status = 'NEED_SETTLED'")
    int countUnSellteBill(String accountId);
}
