package com.kry.pms.api.quartz;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.busi.Bill;
import com.kry.pms.model.persistence.quartz.QuartzSet;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.quratz.impl.QuartzSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/quartz/test")
public class QuartzSetController extends BaseController<QuartzSet> {

    @Autowired
    QuartzSetService quartzSetService;

    @PostMapping(path = "/modify")
    public HttpResponse<QuartzSet> modify(@RequestBody QuartzSet quartzSet) {
        return getDefaultResponse().addData(quartzSetService.modify(quartzSet));
    }

    @GetMapping(path = "/quartzSetList")
    public HttpResponse<List<QuartzSet>> getByHotelCode(){
        User user = getUser();
        HttpResponse<List<QuartzSet>> rep = new HttpResponse<List<QuartzSet>>();
        if(user == null){
            return rep.error(403,"未登录");
        }
        List<QuartzSet> data = quartzSetService.getByHotelCode(user);
        rep.setData(data);
        return rep;
    }

    @GetMapping(path = "/isStart")
    public HttpResponse<List<QuartzSet>> findByHotelCodeAndStatus(){
        User user = getUser();
        HttpResponse<List<QuartzSet>> rep = new HttpResponse<List<QuartzSet>>();
        if(user == null){
            return rep.error(403,"未登录");
        }
        List<QuartzSet> data = quartzSetService.findByHotelCodeAndStatus(user.getHotelCode(), "0");
        rep.setData(data);
        return rep;
    }


}
