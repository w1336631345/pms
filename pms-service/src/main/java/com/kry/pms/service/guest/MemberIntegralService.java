package com.kry.pms.service.guest;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.guest.MemberIntegral;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.BaseService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface MemberIntegralService extends BaseService<MemberIntegral>{


    HttpResponse reduce(MemberIntegral entity);

    List<MemberIntegral> getList(String code, String cardNum);

    void boOverdueList(String hotelCode);

    void auditInInteger(List<Map<String, Object>> list, LocalDate businessDate, User auditUser);
}