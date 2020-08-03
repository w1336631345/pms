package com.kry.pms.service.guest;

import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.guest.MemberInfo;
import com.kry.pms.service.BaseService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface MemberInfoService extends BaseService<MemberInfo>{


    List<MemberInfo> getByCreateDate(String hotelCode, String createDate);

    List<Map<String, Object>> countByCreateUser(String hotelCode, String createDate);

    List<Map<String, Object>> rechargeReport(String hotelCode, String rechargeDate);

    List<Map<String, Object>> integralReport(String hotelCode, String consDate);

    List<MemberInfo> byParamsList(String hotelCode, String limitationDate, String birthDay);

    List<MemberInfo> getParmsList(String name, String mobile, String cardNum, String idCardNum, String hotelCode);

    List<Map<String, Object>> list(String hotelCode, String type, String isUsed, String moreParams);

    List<MemberInfo> parmsList(String hotelCode, String type, String isUsed, String moreParams);

    //定时任务：每日计算积分/金额过期内容
    void boOverdueList(String code);
}