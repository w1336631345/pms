package com.kry.pms.api.msg;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.service.msg.MsgSendService;
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
@RequestMapping(path = "/msgSend")
public class MsgSendController  extends BaseController {

    @Autowired
    RedisTemplate<String,String> redisTemplate;
    @Autowired
    MsgSendService msgSendService;

    /**
     * 功能描述: <br>获取验证码
     * 〈〉
     * @Param: [phone]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/12/22 14:55
     */
    @ResponseBody
    @GetMapping(value = "/getVCode")
    public HttpResponse getVCode(String phone){
        HttpResponse hr = msgSendService.getVCode(phone);
        return hr;
    }
    /**
     * 功能描述: <br>校验验证码
     * 〈〉
     * @Param: [phone, code]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/12/22 14:55
     */
    @ResponseBody
    @GetMapping(value = "/verification")
    public HttpResponse verificationCode(String phone, String code){
        HttpResponse hr = msgSendService.verificationCode(phone, code);
        return hr;
    }
    /**
     * 功能描述: <br>发送短信
     * 〈〉
     * @Param: [phone, code]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/12/22 14:55
     */
    @ResponseBody
    @GetMapping(value = "/sendMsg")
    public HttpResponse sendMsg(String id, String name, String phone, String content, String sendTime, String typeCode){
        HttpResponse hr = msgSendService.sendMsg(id, name, phone, content, sendTime, getCurrentHotleCode(), typeCode);
        return hr;
    }
}
