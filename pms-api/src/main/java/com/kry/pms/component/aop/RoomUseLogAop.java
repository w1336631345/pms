package com.kry.pms.component.aop;

import com.kry.pms.dao.log.RoomSourceUseLogDao;
import com.kry.pms.model.func.UseInfoAble;
import com.kry.pms.model.persistence.log.RoomSourceUseLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class RoomUseLogAop {
    @Autowired
    RoomSourceUseLogDao roomSourceUseLogDao;

    @Around("pointCut()")
    public Object addLog(ProceedingJoinPoint joinpoint) {
        if (joinpoint.getArgs()[0] instanceof UseInfoAble) {
            UseInfoAble model = (UseInfoAble) joinpoint.getArgs()[0];
            try {
                roomSourceUseLogDao.saveAndFlush(new RoomSourceUseLog(model));
                return joinpoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return null;
    }

    @Pointcut("this(com.kry.pms.service.room.impl.RoomStatisticsServiceImpl)")
    public void pointCut() {
    }
}
