package com.kry.pms.component.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.kry.pms.model.http.request.room.BaseBo;

@Aspect
@Component
public class SystemLogAop {
	@Before("execution(* com.kry.pms.api.*.*Controller.*(..))")
	public void before() {
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
	}
}
