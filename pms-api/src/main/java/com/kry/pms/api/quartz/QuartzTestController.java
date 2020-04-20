package com.kry.pms.api.quartz;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.quartz.QuartzSet;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.quratz.impl.QuartzSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/quartz/test2")
public class QuartzTestController {

    @GetMapping(path = "/quartzSetList")
    public HttpResponse getByHotelCode(){
        HttpResponse hr = new HttpResponse();
        return hr;
    }

}
