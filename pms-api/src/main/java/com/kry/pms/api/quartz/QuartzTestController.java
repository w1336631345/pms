package com.kry.pms.api.quartz;

import com.kry.pms.base.HttpResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/quartz/test2")
public class QuartzTestController {

    @GetMapping(path = "/quartzSetList")
    public HttpResponse getByHotelCode(){
        HttpResponse hr = new HttpResponse();
        return hr;
    }

}
