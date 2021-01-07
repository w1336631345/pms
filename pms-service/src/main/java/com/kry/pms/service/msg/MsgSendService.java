package com.kry.pms.service.msg;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.busi.CheckInRecord;

public interface MsgSendService {
    HttpResponse sendMsg(String id, String name, String phone, String content, String sendTime, String hotelCode);

    HttpResponse getVCode(String phone);

    HttpResponse verificationCode(String phone, String code);

    //预订成功发送短信
    HttpResponse bookSendMsg(CheckInRecord cir);

    HttpResponse sendMsgAuto(String name, String phone, String content, String hotelCode);
}
