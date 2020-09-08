package com.kry.pms.service.busi;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.http.request.busi.BosBillCheckBo;
import com.kry.pms.model.persistence.busi.Bill;
import com.kry.pms.model.persistence.busi.BosBill;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface BosBillService extends BaseService<BosBill> {


    String addBosBill(Bill bill);

    //转房账
    BosBill transferAccount(String bosBillId, String roomNum, String roomId, String accountId);

    List<BosBill> findQuery(String hotelCode,String siteId, String roomNum, String accountCode);

    HttpResponse<BosBill> cancellation(BosBill bosBill);

    HttpResponse check(BosBillCheckBo bosBillCheckBo);

    BosBill addFlatBosBill(BosBill bosBill, Employee employee, String shiftCode, String orderNum);
}