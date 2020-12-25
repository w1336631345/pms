package com.kry.pms.service.msg;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.msg.MsgAccount;
import com.kry.pms.service.BaseService;

public interface MsgAccountService extends BaseService<MsgAccount>{

    HttpResponse addTo(MsgAccount msgAccount);

    HttpResponse getBalance(String hotelCode);
}
