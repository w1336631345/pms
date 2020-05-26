package com.kry.pms.api.sys;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.pay.WechatPayService;
import com.kry.pms.util.WechatLoginUtil;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.kry.pms.base.Constants;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.controller.SessionController;
import com.kry.pms.model.http.response.sys.UserOnlineVO;
import com.kry.pms.model.persistence.sys.Shift;
import com.kry.pms.service.org.HotelService;
import com.kry.pms.service.sys.AccountService;
import com.kry.pms.service.sys.ShiftService;
import com.kry.pms.service.sys.UserService;
import com.kry.pms.utils.MD5Utils;
import com.kry.pms.utils.ShiroUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.weixin4j.WeixinException;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path = "/auth")
public class AuthController {
    @Autowired
    AccountService accountService;
    @Autowired
    SessionDAO sessionDAO;
    @Autowired
    HotelService hotelService;
    @Autowired
    UserService userService;
    @Autowired
    SessionController sessionController;
    @Autowired
    ShiftService shiftService;
    @Autowired
    WechatPayService wechatPayService;

    @ResponseBody
    @RequestMapping(path = "/admin/login", method = RequestMethod.POST)
    public HttpResponse<String> loginTest(String username, String password, String hotelCode, String shift) {
        HttpResponse<String> response = new HttpResponse<>();
        if(hotelCode==null){
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servletRequestAttributes.getRequest();
            String serverName = request.getServerName();
            if(serverName!=null&&serverName.endsWith(".pms.rooibook.com")){
                hotelCode = serverName.substring(0,serverName.indexOf("."));
            }
        }
        password = MD5Utils.encrypt(username, hotelCode, password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            String hotelCodet = ShiroUtils.getUser().getHotelCode();
//            Shift newShift = shiftService.createOrUpdate(shift, ShiroUtils.getUser());
            subject.getSession().setAttribute(Constants.Key.SESSION_ATTR_SHIFT_CODE, shift);
            String id = (String) subject.getSession().getId();
            response.addData(id);
            return response.ok("登录成功");
        } catch (AuthenticationException e) {
            return response.error(1000, "用户或密码错误");
        }
    }
    /**
     * 功能描述: <br>微信获取openid
     * 〈〉
     * @Param: [code, request]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/5/15 17:32
     */
    @ResponseBody
    @RequestMapping("/getOpenid")
    public HttpResponse getOpenid(String code, HttpServletRequest request) throws IOException, WeixinException {
        HttpResponse hr = new HttpResponse();
        hr = wechatPayService.getOpenId(code, request);
        return hr;
    }
    /**
     * 功能描述: <br>encryptedData解密，获取用户信息（openId, unionId）
     * 〈〉
     * @Param: [encryptedData, sessionKey, iv]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/5/16 10:33
     */
    @ResponseBody
    @GetMapping(value = "/encrypte")
    public HttpResponse login(String encryptedData, String sessionKey, String iv) throws IOException, WeixinException {
        HttpResponse hr = new HttpResponse();
        Map<String, Object> map = WechatLoginUtil.getUserInfo(encryptedData, sessionKey, iv);
        hr.setData(map);
        return hr;
    }
    /**
     * 功能描述: <br>微信登录
     * 〈〉
     * @Param: [encryptedData, sessionKey, iv]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/5/16 10:34
     */
    @ResponseBody
    @GetMapping(value = "/wxLogin")
    public HttpResponse wxLogin(String unionId, String hotelCode){
        HttpResponse hr = new HttpResponse();
        User user = userService.findByUnionIdAndHotelCode(unionId, hotelCode);
        Map<String, Object> map = new HashMap<>();
        if (user == null) {//如果用户为空，表示还没有绑定账号
            hr.setStatus(1);
            hr.setMessage("未绑定该酒店账号");
        } else {
            String sessionId = login(user, "1");
            map.put("token", sessionId);
            map.put("userInfo", user);
            hr.setData(map);
        }
        return hr;
    }
    /**
     * 功能描述: <br>根据unionId查询用户绑定的酒店列表
     * 〈〉
     * @Param: [unionId]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/5/18 10:10
     */
    @ResponseBody
    @GetMapping(value = "/getHotelList")
    public HttpResponse getHotelList(String unionId){
        HttpResponse hr = new HttpResponse();
        List<Map<String, Object>> list = hotelService.getByUnionId(unionId);
        hr.setData(list);
        return hr;
    }
    /**
     * 功能描述: <br>小程序登录绑定unionId
     * 〈〉
     * @Param: [unionId, username, password, hotelCode]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/5/16 11:17
     */
    @ResponseBody
    @GetMapping(value = "/binding")
    public HttpResponse binding(String unionId, String username, String password, String hotelCode){
        HttpResponse hr = new HttpResponse();
        if(hotelCode == null || "".equals(hotelCode)){
            return hr.error("请选择酒店编码");
        }
        password = MD5Utils.encrypt(username, hotelCode, password);
        User user = userService.getAuditUser(username,password, hotelCode);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            userService.bindWxUnionId(ShiroUtils.getUser(),unionId);
            subject.getSession().setAttribute(Constants.Key.SESSION_ATTR_SHIFT_CODE, "1");
            String id = (String) subject.getSession().getId();
            Map<String, Object> map = new HashMap<>();
            map.put("token", id);
            map.put("userInfo", user);
            hr.addData(id);
            return hr.ok("登录成功");
        } catch (AuthenticationException e) {
            return hr.error(1000, "用户或密码错误");
        }
    }

    private String login(User user, String shift) {
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
        String hotelCode = ShiroUtils.getUser().getHotelCode();
//        Shift newShift = shiftService.createOrUpdate(shift, ShiroUtils.getUser());
        subject.getSession().setAttribute(Constants.Key.SESSION_ATTR_SHIFT_CODE, shift);
        return (String) subject.getSession().getId();

    }
    @ResponseBody
    @RequestMapping(path = "/admin/logout", method = RequestMethod.POST)
    public HttpResponse<String> logout() {
        HttpResponse<String> response = new HttpResponse<>();
        SecurityUtils.getSubject().logout();
        return response.ok("成功登出");
    }

    /**
     * 未登录，shiro应重定向到登录界面，此处返回未登录状态信息由前端控制跳转页面
     *
     * @return
     */
    @RequestMapping(value = "/unauth")
    @ResponseBody
    public Object unauth() {
        HttpResponse<String> response = new HttpResponse<>();
        return response.error(403, "未登录");
    }

    /**
     * 功能描述: <br>查询所有在线用户
     * 〈〉
     *
     * @Param: []
     * @Return: com.kry.pms.base.HttpResponse<java.util.List < com.kry.pms.model.http.response.sys.UserOnlineVO>>
     * @Author: huanghaibin
     * @Date: 2019/10/25 17:26
     */
    @GetMapping("/sessionList")
    @ResponseBody
    public HttpResponse<List<UserOnlineVO>> sessionList() {
        List<UserOnlineVO> list = sessionController.getSysLoginUser();
        HttpResponse<List<UserOnlineVO>> rep = new HttpResponse<>();
        rep.addData(list);
        return rep;
    }

    /**
     * 功能描述: <br>下线在线用户
     * 〈〉
     *
     * @Param: [sessionId]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/10/25 17:26
     */
    @GetMapping("/sessionLoginOut")
    @ResponseBody
    public HttpResponse sessionLoginOut(String sessionId) {
        HttpResponse rep = sessionController.sessionLoginOut(sessionId);
        return rep;
    }

    @RequestMapping("/login_wechat_page")
    public ModelAndView loginWxForPage(HttpServletRequest request) throws JSONException, WeixinException {
        WxMpOAuth2AccessToken accessToken = (WxMpOAuth2AccessToken) request.getAttribute("accessToken");
        WxMpUser user = (WxMpUser) request.getAttribute("user");
        User cuser = userService.findByOpenId(user.getOpenId());
        String state = (String) request.getAttribute("state");
        String shift = null;
        String urlHotelCode = null;
        if(state.contains("_")){
            String str[] = state.split("_");
            shift = str[0];
            urlHotelCode = str[1];
        }else{
            shift = state;
        }
        Map<String, String> data = new HashMap<>();
        String unionId = null;
        if(user.getUnionId() != null){
            unionId = user.getUnionId();
            data.put("unionId", user.getUnionId());
        }else if(accessToken.getUnionId() != null) {
            unionId = accessToken.getUnionId();
            data.put("unionId", accessToken.getUnionId());
        } else {
            unionId = wechatPayService.getUnionId(user.getOpenId(), accessToken.getAccessToken());
            data.put("unionId", unionId);
        }
        User u = userService.findByUnionIdAndHotelCode(unionId, urlHotelCode);
        data.put("hotelCode1", "hotelCode没有");
        data.put("hotelCode", urlHotelCode);
        if (u == null) {
            data.put("user", "没哟");
            data.put("status", "1");
            data.put("avatar", user.getHeadImgUrl());
            data.put("openId", user.getOpenId());
            data.put("nickName", user.getNickname());
        } else if(urlHotelCode==null||cuser.getHotelCode().equals(urlHotelCode)){
            String sessionId = login(cuser, shift);
            data.put("status", "0");
            data.put("token", sessionId);
        }else{
            data.put("status", "2");
            data.put("msg","该微信绑定了其他酒店的账号，请确认！");
        }
        ModelAndView mv = new ModelAndView("/wx_login_result",data);
        return mv;
    }
    @ResponseBody
    @RequestMapping(path = "/admin/bind_wx", method = RequestMethod.POST)
    public HttpResponse<String> bindWx(String username, String password, String hotelCode,String openId,String unionId, String shift) {
        HttpResponse<String> response = new HttpResponse<>();
        if(hotelCode==null){
            hotelCode = getUrlHotleCode();
        }
        password = MD5Utils.encrypt(username, hotelCode, password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            String hotelCodet = ShiroUtils.getUser().getHotelCode();
//            userService.bindWx(ShiroUtils.getUser(),openId);
            userService.bindWxUnionId(ShiroUtils.getUser(), unionId);
            Shift newShift = shiftService.createOrUpdate(shift, ShiroUtils.getUser());
            subject.getSession().setAttribute(Constants.Key.SESSION_ATTR_SHIFT_CODE, newShift.getShiftCode());
            String id = (String) subject.getSession().getId();
            response.addData(id);
            return response.ok("登录成功");
        } catch (AuthenticationException e) {
            return response.error(1000, "用户或密码错误");
        }
    }
    public String getUrlHotleCode() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String serverName = request.getServerName();
        if(serverName!=null&&serverName.endsWith(".pms.rooibook.com")){
            return  serverName.substring(0,serverName.indexOf("."));
        }else{
            return null;
        }
    }
}
