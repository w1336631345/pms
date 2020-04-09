package com.kry.pms.service.sys;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.sys.BookkeepingSet;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface BookkeepingSetService extends BaseService<BookkeepingSet>{

    void deleteIsTrue(String id);

    BookkeepingSet isExist(String hotelCode, String accountId, String productId);

    HttpResponse addList(List<BookkeepingSet> list,String accountId, String hotelCode);

    List<BookkeepingSet> findSet(String hotelCode, String accountId);
}