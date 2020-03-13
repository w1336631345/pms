package com.kry.pms.component.aop;

import com.kry.pms.dao.log.RoomSourceUseLogDao;
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OperationLogAop {
    private static final String NAME_METHOD_FIND_BY_ID = "findById";

    @Autowired
    OperationLogService operationLogService;

    @Around("pointCut()")
    public Object addLog(ProceedingJoinPoint joinpoint) {
        if (joinpoint.getArgs()[0] instanceof UseInfoAble) {
            UseInfoAble model = (UseInfoAble) joinpoint.getArgs()[0];
            Signature s = joinpoint.getSignature();
            MethodSignature ms = (MethodSignature)s;
            Method m = ms.getMethod();
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
    private Object findOldData(Object target,String id) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = target.getClass().getMethod(NAME_METHOD_FIND_BY_ID,String.class);
        return method.invoke(target,id);
    }
}
