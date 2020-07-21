package com.kry.pms.service.guest;

import com.kry.pms.model.persistence.guest.MemberIntegralType;
import com.kry.pms.service.BaseService;

public interface MemberIntegralTypeService extends BaseService<MemberIntegralType>{


    MemberIntegralType addAll(MemberIntegralType entity);
}