package com.kry.pms.component.aop;

import com.kry.pms.dao.log.RoomSourceUseLogDao;
import com.kry.pms.model.annotation.Log;
import com.kry.pms.model.func.UseInfoAble;
import com.kry.pms.model.persistence.log.RoomSourceUseLog;
import com.kry.pms.model.persistence.sys.OperationLog;
import com.kry.pms.service.sys.OperationLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class OperationLogAop {
    private static final String NAME_METHOD_FIND_BY_ID = "findById";
    private static final String NAME_METHOD_GET_ID= "getId";

    @Autowired
    OperationLogService operationLogService;

        @Around("pointCut()")
    public Object addLog(ProceedingJoinPoint joinpoint) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (joinpoint.getArgs()[0] instanceof UseInfoAble) {
            Object object = joinpoint.getArgs()[0];
            Signature s = joinpoint.getSignature();
            MethodSignature ms = (MethodSignature) s;
            Method m = ms.getMethod();
            createLogs(object,findOldData(joinpoint.getTarget(),object));
            try {
                return joinpoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return null;
    }

    @Pointcut("this(com.kry.pms.service.busi.impl.CheckInRecordServiceImpl)")
    public void pointCut() {
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
            if (f.isAnnotationPresent(Log.class)) {
                Log l = f.getAnnotation(Log.class);
                f.setAccessible(true);
                logs.add(createLog(oldData, newData, l));
            }

        }
        return logs;
    }

    private OperationLog createLog(Object oldData, Object newData, Log l) {
        OperationLog log = new OperationLog();
        log.setNewValue(l.operationName());

        return log;
    }
}
