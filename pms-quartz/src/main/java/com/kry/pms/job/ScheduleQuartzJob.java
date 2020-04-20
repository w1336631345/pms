package com.kry.pms.job;

import com.kry.pms.base.Constants;
import com.kry.pms.model.ScheduleJobModel;
import com.kry.pms.service.ReflexQuartzService;
import com.kry.pms.service.ScheduleJobService;
import com.kry.pms.service.audit.NightAuditService;
import com.kry.pms.service.report.RoomReportService;
import com.kry.pms.service.sys.BusinessSeqService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
public class ScheduleQuartzJob implements Job {
    @Autowired
    AutomaticNightTrial automaticNightTrial;
    @Autowired
    RoomReportService roomReportService;
    @Autowired
    BusinessSeqService businessSeqService;
    @Autowired
    NightAuditService nightAuditService;
    @Autowired
    ReflexQuartzService reflexQuartzService;
    @Autowired
    ScheduleJobService scheduleJobService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ScheduleJobModel sjm = (ScheduleJobModel) context.getJobDetail().getJobDataMap().get("scheduleJobModel");
        log.info("执行了task...group:{}, name:{}, hotelCode:{}", sjm.getGroupName(), sjm.getJobName(), sjm.getHotelCode());
        // 可在此执行定时任务的具体业务
        // ...
        if(("ALL").equals(sjm.getType_())){
            System.out.println("所有酒店都要执行的定时任务执行代码");
            LocalDate businessDate = businessSeqService.getBuinessDate(sjm.getHotelCode());
            //所有酒店都要执行，不用区分hotelCode，实时导入房间状态，固定时间复制某些数据到临时表
//            roomReportService.copyData(businessDate.toString());
//            roomReportService.copyData(LocalDate.now().toString());
        }
        if(("AUDIT").equals(sjm.getType_())){
            System.out.println("调用了夜审定时任务执行代码");
            //入账到bill
            automaticNightTrial.accountEntryListAll(sjm.getHotelCode());
            //自动生成报表
//            nightAuditService.addReportAllAuto(hotelCode);
        }
        if(Constants.quartzType.NORMAL.equals(sjm.getType_())){
            System.out.println("调用了其他定时任务执行代码");
            reflexQuartzService.reflex(sjm);
        }

    }
}
