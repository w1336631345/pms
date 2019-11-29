package com.kry.pms.job;

import com.kry.pms.service.report.RoomReportService;
import com.kry.pms.service.sys.BusinessSeqService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Slf4j
public class ScheduleQuartzJob implements Job {
    @Autowired
    AutomaticNightTrial automaticNightTrial;
    @Autowired
    RoomReportService roomReportService;
    @Autowired
    BusinessSeqService businessSeqService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String group = context.getJobDetail().getJobDataMap().get("group").toString();
        String name = context.getJobDetail().getJobDataMap().get("name").toString();
        String hotelCode = context.getJobDetail().getJobDataMap().get("hotelCode").toString();
        Object otype_ = context.getJobDetail().getJobDataMap().get("type_");
        String type_ = null;
        log.info("执行了task...group:{}, name:{}, hotelCode:{}", group, name, hotelCode);
        // 可在此执行定时任务的具体业务
        // ...
//        automaticNightTrial.accountEntryListAll(hotelCode);
        if(type_ == null){
        }else {
            type_ = otype_.toString();
        }
        if(("ALL").equals(type_)){
            LocalDate businessDate = businessSeqService.getBuinessDate(hotelCode);
            //所有酒店都要执行，不用区分hotelCode，实时导入房间状态
//            roomReportService.copyData(businessDate.toString());
//            roomReportService.copyData(LocalDate.now().toString());
        }

    }
}
