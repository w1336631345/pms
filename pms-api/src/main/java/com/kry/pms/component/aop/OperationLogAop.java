package com.kry.pms.component.aop;

import com.kry.pms.dao.log.RoomSourceUseLogDao;
import com.kry.pms.dao.sys.OperationLogDao;
import com.kry.pms.model.annotation.Log;
import com.kry.pms.model.func.UseInfoAble;
import com.kry.pms.model.persistence.log.RoomSourceUseLog;
import com.kry.pms.model.persistence.sys.OperationLog;
import com.kry.pms.service.sys.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

//@Slf4j
//@Aspect
//@Component
public class OperationLogAop {
    private static final String NAME_METHOD_FIND_BY_ID = "findById";
    private static final String NAME_METHOD_GET_ID = "getId";

    @Autowired
    OperationLogService operationLogService;
    @Autowired
    OperationLogDao operationLogDao;

    @Around("execution(* com.kry.pms.service.busi.impl.CheckInRecordServiceImpl.modify(..))")
    public Object addLog(ProceedingJoinPoint joinpoint) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Object object = joinpoint.getArgs()[0];
        Signature s = joinpoint.getSignature();
        MethodSignature ms = (MethodSignature) s;
        Method m = ms.getMethod();
        List<OperationLog> logs = createLogs(object, findOldData(joinpoint.getTarget(), object));
        operationLogDao.saveAll(logs);
        try {
            return joinpoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    private Object findOldData(Object target, Object object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = target.getClass().getMethod(NAME_METHOD_FIND_BY_ID, String.class);
        Method getIdMethod = object.getClass().getMethod(NAME_METHOD_GET_ID);
        String id = getIdMethod.invoke(object).toString();
        return method.invoke(target, id);
    }

    private List<OperationLog> createLogs(Object oldData, Object newData) throws IllegalAccessException {
        List<OperationLog> logs = new ArrayList<>();
        Field[] fs = newData.getClass().getDeclaredFields();
        for (Field f : fs) {
            Log l =  f.getAnnotation(Log.class);
            if (l!=null) {
                f.setAccessible(true);
                logs.add(createLog(oldData, newData, f, l));
                f.setAccessible(false);
            }
        }
        return logs;
    }

    private OperationLog createLog(Object oldData, Object newData, Field f, Log l) throws IllegalAccessException {
        OperationLog log = new OperationLog();
        if (l.isBusinessKey()) {
            log.setBusinessKey(f.get(oldData).toString());
        } else {
            log.setNewValue(l.operationName());
            Object oldVal = f.get(oldData);
            if (oldData != null) {
                log.setOldValue(oldData.toString());
            } else {
                log.setOldValue(null);
            }
            Object newVal = f.get(newData);
            if (newData != null) {
                log.setNewValue(newData.toString());
            } else {
                log.setNewValue(null);
            }
        }
        return log;
    }
}
