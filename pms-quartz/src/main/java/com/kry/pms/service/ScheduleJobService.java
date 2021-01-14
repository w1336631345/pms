package com.kry.pms.service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.dao.ScheduleJobDaoRepository;
import com.kry.pms.job.ScheduleQuartzJob;
import com.kry.pms.model.ScheduleJobModel;
import com.kry.pms.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class ScheduleJobService {

    // 获取工厂类
    private StdSchedulerFactory sf = new StdSchedulerFactory();//没有交给spring,导致job类无法注入

    @Autowired
    private ScheduleJobDaoRepository scheduleJobDaoRepository;
    @Autowired
    SchedulerFactoryBean schedulerFactoryBean;//实现spring注入功能，交给spring管理
    @Autowired
    Scheduler scheduler;//来至于JobSpringConfig里的bean

    // 项目重启后，初始化原本已经运行的定时任务
    @PostConstruct
    public void init(){
        List<ScheduleJobModel> poList = scheduleJobDaoRepository.findAllByStatus(0);
        poList.forEach(po -> {
            startScheduleByInit(po);
        });
    }

    public List<ScheduleJobModel> findAll(){
        List<ScheduleJobModel> list = scheduleJobDaoRepository.findAll();
        return list;
    }
    public List<ScheduleJobModel> getJobByHotel(String hotelCode) {
        List<ScheduleJobModel> list = scheduleJobDaoRepository.findByHotelCode(hotelCode);
        return list;
    }
    public List<ScheduleJobModel> getJobByHotelAndStatus(String hotelCode) {
//        List<ScheduleJobModel> list = scheduleJobDaoRepository.findByHotelCodeAndStatus(hotelCode, 0);
        List<ScheduleJobModel> list = scheduleJobDaoRepository.findByHotelCodeAndStatusAndType(hotelCode, 0, Constants.quartzType.NIGHT_AUDIT);
        return list;
    }

    public HttpResponse save(ScheduleJobModel scheduleJobModel){
        HttpResponse hr = new HttpResponse();
        scheduleJobModel.setStatus(2);
        scheduleJobModel.setCreateDate(LocalDateTime.now());
        scheduleJobModel.setStartTime("06:00:00");//默认时间早上6点
        List<ScheduleJobModel> poList = scheduleJobDaoRepository.findByGroupNameAndJobName(scheduleJobModel.getGroupName(), scheduleJobModel.getJobName());
        if (!ObjectUtils.isEmpty(poList)){
            return hr.error("group和job名称已存在");
        }
        scheduleJobDaoRepository.save(scheduleJobModel);
        return hr.ok("添加任务成功");
    }

    /**
     * 初始化时开启定时任务
     */
    private void startScheduleByInit(ScheduleJobModel po){
        try {
//            Scheduler scheduler = sf.getScheduler();
//            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            startJob(scheduler, po);
            scheduler.start();
        }catch (Exception e){
            log.error("exception:{}", e);
        }
    }

    /**
     * 功能描述: <br>新增并开启任务
     * 〈〉
     * @Param: [model]
     * @Return: void
     * @Author: huanghaibin
     * @Date: 2019/11/6 17:24
     */
    public void startSchedule(ScheduleJobModel model) {
        if (StringUtils.isEmpty(model.getGroupName()) || StringUtils.isEmpty(model.getJobName()) || StringUtils.isEmpty(model.getCron())){
            throw new RuntimeException("参数不能为空");
        }
        List<ScheduleJobModel> poList = scheduleJobDaoRepository.findByGroupNameAndJobNameAndStatus(model.getGroupName(), model.getJobName(), 0);
        if (!ObjectUtils.isEmpty(poList)){
            throw new RuntimeException("group和job名称已存在");
        }
        try {
//            Scheduler scheduler = sf.getScheduler();
//            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            startJob(scheduler, model);
            scheduler.start();
            ScheduleJobModel scheduleJobPo = new ScheduleJobModel();
            scheduleJobPo.setGroupName(model.getGroupName());
            scheduleJobPo.setJobName(model.getJobName());
            scheduleJobPo.setCron(model.getCron());
            scheduleJobPo.setStatus(0);
            scheduleJobPo.setCreateDate(LocalDateTime.now());
            scheduleJobPo.setUpdateDate(LocalDateTime.now());
            scheduleJobDaoRepository.save(scheduleJobPo);
        }catch (Exception e){
            log.error("exception:{}", e);
        }

    }
    public HttpResponse startOnly(Integer id) {
        HttpResponse hr = new HttpResponse();
        //查询删除的任务
//        ScheduleJobModel po = scheduleJobDaoRepository.findByIdAndStatus(id, 1);
        ScheduleJobModel po = scheduleJobDaoRepository.getById(id);
        if (ObjectUtils.isEmpty(po)){
            return hr.error(4040,"定时任务不存在");
        }
        if(po.getStatus() == 0){
            return hr.ok("任务已经启动");
        }
        List<ScheduleJobModel> list = scheduleJobDaoRepository.findByHotelCode(po.getHotelCode());
        for(int i=0; i<list.size(); i++){
            if(list.get(i).getId() != id && list.get(i).getStatus() == 0){
                scheduleDelete(list.get(i).getId());//开启某个任务，其余任务全部禁止，仅允许同时开启一个任务
            }
        }
        try {
//            Scheduler scheduler = sf.getScheduler();
//            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            startJob(scheduler, po);
            scheduler.start();
            po.setStatus(0);//更新状态为开启0
            po.setUpdateDate(LocalDateTime.now());
            scheduleJobDaoRepository.save(po);
        }catch (Exception e){
            log.error("exception:{}", e);
        }
        return hr.ok("开启成功");
    }

    /**
     * 更新定时任务
     * @param model
     */
    public void scheduleUpdateCorn(ScheduleJobModel model) {
        if (ObjectUtils.isEmpty(model.getId()) || ObjectUtils.isEmpty(model.getCron())){
            throw new RuntimeException("定时任务不存在");
        }
        try {
            ScheduleJobModel po = scheduleJobDaoRepository.findByIdAndStatus(model.getId(), 0);
            // 获取调度对象
//            Scheduler scheduler = sf.getScheduler();
//            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            // 获取触发器
            TriggerKey triggerKey = new TriggerKey(po.getJobName(), po.getGroupName());
            CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            String oldTime = cronTrigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(model.getCron())) {
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(model.getCron());
                CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(po.getJobName(), po.getGroupName())
                        .withSchedule(cronScheduleBuilder).build();
                // 更新定时任务
                scheduler.rescheduleJob(triggerKey, trigger);
                po.setCron(model.getCron());
                // 更新数据库
                scheduleJobDaoRepository.save(po);
            }
        }catch (Exception e){
            log.info("exception:{}", e);
        }

    }
    /**
     * 任务 - 暂停
     */
    public HttpResponse schedulePause(Integer id) {
        HttpResponse hr = new HttpResponse();
        ScheduleJobModel po = scheduleJobDaoRepository.findByIdAndStatus(id, 0);
        if (ObjectUtils.isEmpty(po)){
            return hr.error(4040,"定时任务不存在");
        }
        try {
//            Scheduler scheduler = sf.getScheduler();
//            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = new JobKey(po.getJobName(), po.getGroupName());
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null)
                return hr.error(4040,"没有任务明细");
            scheduler.pauseJob(jobKey);
            po.setStatus(2);
            scheduleJobDaoRepository.save(po);
        }catch (Exception e){
            log.error("exception:{}", e);
        }
        return hr.ok("任务暂停");
    }
    /**
     * 任务 - 恢复
     */

    public HttpResponse scheduleResume(Integer id) {
        HttpResponse hr = new HttpResponse();
        ScheduleJobModel po = scheduleJobDaoRepository.findByIdAndStatus(id, 2);
        if (ObjectUtils.isEmpty(po)){
            return hr.error(4040,"定时任务不存在");
        }
        try {
//            Scheduler scheduler = sf.getScheduler();
//            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = new JobKey(po.getJobName(), po.getGroupName());
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null)
                return hr.error(4040,"没有任务明细");
            scheduler.resumeJob(jobKey);
            po.setStatus(0);
            scheduleJobDaoRepository.save(po);
        }catch (Exception e){
            log.error("exception:{}", e);
        }
        return hr.ok("任务启动");
    }
    /**
     * 任务 - 删除一个定时任务
     */
    public HttpResponse scheduleDelete(Integer id) {
        HttpResponse hr = new HttpResponse();
//        ScheduleJobModel po = scheduleJobDaoRepository.findByIdAndStatus(id, 0);
        ScheduleJobModel po = scheduleJobDaoRepository.getById(id);
        if (ObjectUtils.isEmpty(po)){
            return hr.error(4040,"定时任务不存在");
        }
        if(po.getStatus() == 1){
            return hr.ok("任务已经删除");
        }
        try {
//            Scheduler scheduler = sf.getScheduler();
//            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = new JobKey(po.getJobName(), po.getGroupName());
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null)
                return hr.error(4040,"没有任务明细");
            scheduler.deleteJob(jobKey);
            po.setStatus(1);
            scheduleJobDaoRepository.save(po);
        }catch (Exception e){
            log.error("exception:{}", e);
        }
        return hr.ok("任务删除成功");
    }

    /**
     * 删除所有定时任务
     */
    public void scheduleDeleteAll() {
        try {
//            Scheduler scheduler = sf.getScheduler();
//            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            // 获取有所的组
            List<String> jobGroupNameList = scheduler.getJobGroupNames();
            for (String jobGroupName : jobGroupNameList) {
                GroupMatcher<JobKey> jobKeyGroupMatcher = GroupMatcher.jobGroupEquals(jobGroupName);
                Set<JobKey> jobKeySet = scheduler.getJobKeys(jobKeyGroupMatcher);
                for (JobKey jobKey : jobKeySet) {
                    String jobName = jobKey.getName();
                    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                    if (jobDetail == null)
                        return;
                    scheduler.deleteJob(jobKey);
                    // 更新数据库
                    List<ScheduleJobModel> poList = scheduleJobDaoRepository.findByGroupNameAndJobNameAndStatus(jobGroupName, jobName, 0);
                    poList.forEach(po -> {
                        po.setStatus(1);
                        scheduleJobDaoRepository.save(po);
                    });
                    log.info("group:{}, job:{}", jobGroupName, jobName);
                }
            }
        }catch (Exception e){
            log.error("exception:{}", e);
        }
    }
    // 开启任务
    private void startJob(Scheduler scheduler, ScheduleJobModel sjm)
            throws SchedulerException {
        // 通过JobBuilder构建JobDetail实例，JobDetail规定只能是实现Job接口的实例
        // 在map中可传入自定义参数，在job中使用
        JobDataMap map = new JobDataMap();
        map.put("scheduleJobModel", sjm);

        // JobDetail 是具体Job实例
        JobDetail jobDetail = JobBuilder.newJob(ScheduleQuartzJob.class).withIdentity(sjm.getJobName(), sjm.getGroupName())
                .usingJobData(map)
                .build();
        // 基于表达式构建触发器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(sjm.getCron());
        // CronTrigger表达式触发器 继承于Trigger
        // TriggerBuilder 用于构建触发器实例
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(sjm.getJobName(), sjm.getGroupName())
                .withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }
}
