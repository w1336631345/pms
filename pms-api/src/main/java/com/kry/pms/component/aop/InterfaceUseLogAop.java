package com.kry.pms.component.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.kry.pms.model.annotation.OperationLog;
import com.kry.pms.model.persistence.log.InterfaceUseLog;
import com.kry.pms.service.log.InterfaceUseLogService;
import com.kry.pms.utils.ShiroUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
public class InterfaceUseLogAop {


    public static final String dateformat = "yyyy:MM:dd HH:mm:ss";

    @Autowired
    InterfaceUseLogService interfaceUseLogService;

    @Pointcut("@annotation(com.kry.pms.model.annotation.OperationLog)")
    public void controllerLog(){

    }

    @Around(value = "controllerLog()")
    public Object process(ProceedingJoinPoint joinPoint) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            String requestURI = request.getRequestURI();
            String requestType = request.getMethod();
            // 获取执行方法的类的名称（包名加类名）
            String className = joinPoint.getTarget().getClass().getName();
            // 获取实例和方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            // 执行方法获取返回值
            Object proceed = joinPoint.proceed();
            InterfaceUseLog iul = new InterfaceUseLog();
            iul.setUrl(requestURI);
            if(ShiroUtils.getUser() != null){
                iul.setHotelCode(ShiroUtils.getUser().getHotelCode());
            }
            iul.setMethedType(requestType);
            iul.setMethedName(method.getName());
            iul.setClassPath(className);
//            Object[] args = joinPoint.getArgs();
//            String params = JSONObject.toJSONStringWithDateFormat(joinPoint.getArgs(), dateformat, SerializerFeature.WriteMapNullValue);
//            iul.setParams(JSON.toJSONString(args));
            JSONObject jsonobject = JSONObject.parseObject(JSON.toJSONString(proceed));
            String code = jsonobject.getString("status");
            String msg = jsonobject.getString("message");
            iul.setResultCode(code);
            iul.setResultMsg(msg);
            Class<?> targetClass = method.getDeclaringClass();
            //获取方法注解OperationLog
            OperationLog methodAnnotation = method.getAnnotation(OperationLog.class);
            iul.setRemark(methodAnnotation.remark());
            interfaceUseLogService.add(iul);
            return proceed;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }



}
