package com.kry.pms.controller;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.ScheduleJobModel;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.ScheduleJobService;
import com.kry.pms.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/scheduleJob")
public class ScheduleJobController {

    @Autowired
    ScheduleJobService scheduleJobService;

    @GetMapping(path = "/isStart")
    public HttpResponse<List<ScheduleJobModel>> findByHotelCodeAndStatus(){
        User user = ShiroUtils.getUser();
        HttpResponse<List<ScheduleJobModel>> rep = new HttpResponse<List<ScheduleJobModel>>();
        if(user == null){
            return rep.error(403,"未登录");
        }
        List<ScheduleJobModel> data = scheduleJobService.getJobByHotelAndStatus(user.getHotelCode());
        rep.setData(data);
        return rep;
    }
}
