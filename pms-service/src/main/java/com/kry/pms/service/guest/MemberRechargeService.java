package com.kry.pms.service.guest;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.guest.MemberRecharge;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface MemberRechargeService extends BaseService<MemberRecharge>{


    HttpResponse recharge(MemberRecharge entity);

    HttpResponse use(MemberRecharge entity);

    HttpResponse useAmount(String hotelCode, String cardNum, Double amount, String settledNo);

    //取消使用金额
    HttpResponse cancelUseAmount(String hotelCode, String settledNo);

    List<MemberRecharge> getList(String code);

    List<MemberRecharge> getByHotelCodeAndCardNum(String code, String cardNum);

    List<MemberRecharge> findByMemberInfoId(String memberId);

    void boOverdueList(String hotelCode);
}