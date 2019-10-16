package com.kry.pms.handler;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {
	
	 @ExceptionHandler(UnauthenticatedException.class)
	 @ResponseBody
	 public Map<String, Object> unauthenticatedException(HttpServletRequest req,Exception e) {
		 Map<String, Object> map = new HashMap<>();
		 map.put("code", 10001);
		 map.put("message", "token(sessionId)错误");
		 return map;
	 }

	 @ExceptionHandler(UnauthorizedException.class)
     @ResponseBody
     public Map<String, Object> unauthorizedException(HttpServletRequest req,Exception e){
		 Map<String, Object> map = new HashMap<>();
		 map.put("code", 10002);
		 map.put("message", "用户无权限");
		 return map;
     }
	 
//	 @ExceptionHandler(UnknownAccountException.class)
//     @ResponseBody
//     public Map<String, Object> unknownAccountException(HttpServletRequest req,Exception e){
//		 Map<String, Object> map = new HashMap<>();
//		 map.put("code", 10003);
//		 map.put("message", "未登录");
//		 return map;
//     }
	 
}
