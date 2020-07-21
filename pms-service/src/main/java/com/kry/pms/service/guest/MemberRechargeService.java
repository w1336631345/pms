package com.kry.pms.service.guest;

import com.kry.pms.model.persistence.guest.MemberRecharge;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface MemberRechargeService extends BaseService<MemberRecharge>{


    List<MemberRecharge> getList(String code);
}