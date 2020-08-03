package com.kry.pms.manager.api;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.manager.entity.NewHotelVo;
import com.kry.pms.service.sys.ManagerUserService;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthController {
    @Autowired
    ManagerUserService managerUserService;

    public HttpResponse<String> login(NewHotelVo hewHotelVo) {
        HttpResponse<String> response = new HttpResponse<>();
        return response;
    }
}
