package com.kry.pms.dao.sys;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.sys.BookkeepingSet;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookkeepingSetDao extends BaseDao<BookkeepingSet>{

    BookkeepingSet findByHotelCodeAndAccountIdAndProductId(String hotelCode, String accountId, String productId);

    List<BookkeepingSet> findByHotelCodeAndAccountId(String hotelCode, String accounId);

}
