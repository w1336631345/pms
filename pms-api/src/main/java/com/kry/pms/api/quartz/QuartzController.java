package com.kry.pms.api.quartz;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.ScheduleJobModel;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.ScheduleJobService;
import com.kry.pms.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/quartz")
public class QuartzController {

    @Autowired
    private ScheduleJobService scheduleJobService;

    /**
     * 功能描述: <br>新增定时任务
     * 〈〉
     * @Param: [model]
     * @Return: com.kry.pms.base.HttpResponse<java.lang.String>
     * @Author: huanghaibin
     * @Date: 2019/11/6 15:32
     */
    @PostMapping("/save")
    public HttpResponse<String> save(@RequestBody ScheduleJobModel model){
        User user = ShiroUtils.getUser();
        if(user != null){
            model.setHotelCode(user.getHotelCode());
        }
        HttpResponse<String> hr = new HttpResponse<String>();
        scheduleJobService.save(model);
        return hr.ok();
    }
    /**
     * 功能描述: <br>查询所有定时任务（系统维护管理员）
     * 〈〉
     * @Param: []
     * @Return: com.kry.pms.base.HttpResponse<java.util.List<com.kry.pms.model.ScheduleJobModel>>
     * @Author: huanghaibin
     * @Date: 2019/11/6 17:51
     */
    @GetMapping("/getJob")
    public HttpResponse<List<ScheduleJobModel>> getJob(){
        HttpResponse<List<ScheduleJobModel>> hr = new HttpResponse<List<ScheduleJobModel>>();
        List<ScheduleJobModel> list = scheduleJobService.findAll();
        hr.setData(list);
        return hr;
    }
    /**
     * 功能描述: <br>查询当前酒店定时任务列表
     * 〈〉
     * @Param: []
     * @Return: com.kry.pms.base.HttpResponse<java.util.List<com.kry.pms.model.ScheduleJobModel>>
     * @Author: huanghaibin
     * @Date: 2019/11/6 17:51
     */
    @GetMapping("/getJobByHotel")
    public HttpResponse<List<ScheduleJobModel>> getJobByHotel(){
        HttpResponse<List<ScheduleJobModel>> hr = new HttpResponse<List<ScheduleJobModel>>();
        User user = ShiroUtils.getUser();
        if(user == null){
            return hr.error(403,"没有登录");
        }
        List<ScheduleJobModel> list = scheduleJobService.getJobByHotel(user.getHotelCode());
        hr.setData(list);
        return hr;
    }

    /**
     * 开启
     * @param model
     * @return
     */
    @PostMapping("/start")
    public String startSchedule(@RequestBody ScheduleJobModel model){
        scheduleJobService.startSchedule(model);
        return "ok";
    }
    /**
     * 功能描述: <br>开启任务（任务开启之前是删除状态）
     * 〈〉
     * @Param: [id]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/11/6 17:32
     */
    @GetMapping("/startOnly")
    public HttpResponse startOnly(Integer id){
        HttpResponse hr = scheduleJobService.startOnly(id);
        return hr;
    }

    /**
     * 更新
     * @param model
     * @return
     */
    @PostMapping("/update")
    public String scheduleUpdateCorn(@RequestBody ScheduleJobModel model){
        scheduleJobService.scheduleUpdateCorn(model);
        return "ok";
    }

    /**
     * 暂停
     * @param id
     * @return
     */
    @PostMapping("/pause")
    public HttpResponse schedulePause(Integer id){
        HttpResponse hr = scheduleJobService.schedulePause(id);
        return hr;
    }

    /**
     * 恢复
     * @param id
     * @return
     */
    @GetMapping("/resume")
    public HttpResponse scheduleResume(Integer id){
        HttpResponse hr = scheduleJobService.scheduleResume(id);
        return hr;
    }

    /**
     * 删除一个定时任务
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public HttpResponse scheduleDelete(Integer id){
        HttpResponse hr = scheduleJobService.scheduleDelete(id);
        return hr;
    }

    /**
     * 删除所有定时任务
     * @param model
     * @return
     */
    @PostMapping("/deleteAll")
    public String scheduleDeleteAll(@RequestBody ScheduleJobModel model){
        scheduleJobService.scheduleDeleteAll();
        return "ok";
    }
}
