package com.kry.pms.api.sys;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.util.HttpClientUtil;
import com.kry.pms.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping(path = "/msg")
public class MsgController {

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @ResponseBody
    @GetMapping(value = "/getVCode")
    public HttpResponse getVCode(String phone, HttpServletRequest request){
        HttpResponse hr = new HttpResponse();
        String vcode = StringUtil.getYzm();
        String content = "客如意验证码"+vcode;
        try {
            String gbk = StringUtil.toGBK(content);
            String result = HttpClientUtil.doGet("http://gateway.iems.net.cn/GsmsHttp?username=72343:admin&password=84119807&to="+phone+"&content="+gbk);
            String ok = result.substring(0,2);
            if("OK".equals(ok)){//发送短信成功
                redisTemplate.opsForValue().set(phone, vcode, 90*1000, TimeUnit.MILLISECONDS);
//                HttpSession session = request.getSession();
//                session.setAttribute(phone, vcode);
//                Timer timer=new Timer();
//                //一分钟后删除验证码
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        session.removeAttribute(phone);
//                        System.out.println("验证码删除成功");
//                        timer.cancel();
//                    }
//                },60*1000);
                hr.ok("验证码发送成功"+vcode);
            }else {
                hr.error("验证码发送失败"+result);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return hr;
    }
    @ResponseBody
    @GetMapping(value = "/verification")
    public HttpResponse verificationCode(String phone, String code, HttpServletRequest request){
        HttpResponse hr = new HttpResponse();
//        HttpSession session = request.getSession();
//        String vcode = (String) session.getAttribute(phone);
        String vcode = redisTemplate.opsForValue().get(phone).toString();
        if(code == null || "".equals(code)){
            hr.error("验证码不能为空");
        }else {
            if(code.equals(vcode)){
                hr.ok("验证通过");
            }else {
                hr.error("验证码错误");
            }
        }
        return hr;
    }
}
