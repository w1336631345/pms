package com.kry.pms.service.msg;

import com.kry.pms.base.HttpResponse;

public interface MsgSendService {
    HttpResponse sendMsg(String id, String name, String phone, String content, String sendTime, String hotelCode);

    HttpResponse getVCode(String phone);

    HttpResponse verificationCode(String phone, String code);
}
