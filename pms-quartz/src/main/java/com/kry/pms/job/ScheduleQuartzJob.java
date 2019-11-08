package com.kry.pms.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class ScheduleQuartzJob implements Job {
    @Autowired
    AutomaticNightTrial automaticNightTrial;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String group = context.getJobDetail().getJobDataMap().get("group").toString();
        String name = context.getJobDetail().getJobDataMap().get("name").toString();
        String hotelCode = context.getJobDetail().getJobDataMap().get("hotelCode").toString();
        log.info("执行了task...group:{}, name:{}, hotelCode:{}", group, name, hotelCode);
        // 可在此执行定时任务的具体业务
        // ...
//        automaticNightTrial.accountEntryListAll(hotelCode);
    }
}
