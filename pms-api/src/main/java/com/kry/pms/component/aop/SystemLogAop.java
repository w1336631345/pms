package com.kry.pms.component.aop;

import java.time.LocalDateTime;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.kry.pms.base.Constants;
import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.utils.ShiroUtils;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Aspect
@Component
public class SystemLogAop {
	
	@Around("execution(* com.kry.pms.service.*.impl.*ServiceImpl.add(..))")
	public Object aroundAdd(ProceedingJoinPoint joinpoint) {
		PersistenceModel model = (PersistenceModel) joinpoint.getArgs()[0];
		User user = ShiroUtils.getUser();
		model.setHotelCode(user.getHotelCode());
		if(model.getStatus()==null) {
			model.setStatus(Constants.Status.NORMAL);
		}
		if(model.getCreateDate()==null) {
			model.setCreateDate(LocalDateTime.now());
		}
		if(model.getCreateUser()==null) {
			model.setCreateUser(user.getId());
		}
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		try {
			return joinpoint.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
}
