package com.kry.pms.service.guest;

import com.kry.pms.model.persistence.guest.MemberIntegral;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface MemberIntegralService extends BaseService<MemberIntegral>{


    MemberIntegral reduce(MemberIntegral entity);

    List<MemberIntegral> getList(String code, String cardNum);
}