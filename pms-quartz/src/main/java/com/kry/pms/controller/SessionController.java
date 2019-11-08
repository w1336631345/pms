package com.kry.pms.controller;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.http.response.sys.UserOnlineVO;
import com.kry.pms.model.persistence.org.Hotel;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.org.HotelService;
import com.kry.pms.service.sys.UserService;
import com.kry.pms.utils.ShiroUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class SessionController {

    @Autowired
    SessionDAO sessionDAO;
    @Autowired
    HotelService hotelService;
    @Autowired
    UserService userService;

    /**
     * 功能描述: <br>查询同酒店下登录的用户
     * 〈〉
     * @Param: []
     * @Return: java.util.List<com.kry.pms.model.http.response.sys.UserOnlineVO>
     * @Author: huanghaibin
     * @Date: 2019/11/1 11:04
     */
    public List<UserOnlineVO> getSysLoginUser(){
        User loginUser = ShiroUtils.getUser();
        Hotel hotel = hotelService.getByHotelCode(loginUser.getHotelCode());
        boolean isHotelUser = false;
        List<UserOnlineVO> list = new ArrayList<>();
        Collection<Session> sessions =  sessionDAO.getActiveSessions();
        for (Session session : sessions) {
            UserOnlineVO userOnlineVO = new UserOnlineVO();
            User user;
            SimplePrincipalCollection principalCollection;
            if(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY )== null){
                continue;
            }else {
                principalCollection = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                user = (User) principalCollection.getPrimaryPrincipal();
                if(loginUser.getHotelCode().equals(user.getHotelCode())){
                    isHotelUser = true;
                }
                if(isHotelUser){
                    if(loginUser.getId().equals(user.getId())){
                        userOnlineVO.setIsMyself("本机");
                    }
                    userOnlineVO.setHotelName(hotel.getName());
                    userOnlineVO.setUsername(user.getUsername());
                    userOnlineVO.setUserId(user.getId());
                    userOnlineVO.setPhone(user.getMobile());
                    userOnlineVO.setNickename(user.getNickname());
                }
            }
            if(isHotelUser){
                userOnlineVO.setHost(session.getHost());
                userOnlineVO.setSessionId((String) session.getId());
                userOnlineVO.setStartAccessTime(session.getStartTimestamp());
                userOnlineVO.setLastAccessTime(session.getLastAccessTime());
                list.add(userOnlineVO);
            }
        }
        return list;
    }

    /**
     * 功能描述: <br>根据登录用户，查询同酒店下所有在线用户（sessionId）
     * 〈〉
     * @Param: []
     * @Return: java.util.List<java.lang.String>
     * @Author: huanghaibin
     * @Date: 2019/11/1 11:14
     */
    public List<String> getSysAllLoginSession(){
        User loginUser = ShiroUtils.getUser();
        List<String> sessionIds = new ArrayList<>();
        boolean isHotelUser = false;
        Collection<Session> sessions =  sessionDAO.getActiveSessions();
        for (Session session : sessions) {
            User user;
            SimplePrincipalCollection principalCollection;
            if(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY )== null){
                continue;
            }else {
                principalCollection = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                user = (User) principalCollection.getPrimaryPrincipal();
                if(loginUser.getHotelCode().equals(user.getHotelCode())){
                    isHotelUser = true;
                }
                if(isHotelUser){
                    sessionIds.add((String) session.getId());
                }
            }
        }
        return sessionIds;
    }

    /**
     * 功能描述: <br>根据hotelCode查询所有同酒店下的在线用户
     * 〈〉
     * @Param: [hotelCode]
     * @Return: java.util.List<java.lang.String>
     * @Author: huanghaibin
     * @Date: 2019/11/1 11:16
     */
    public List<String> getSysAllLoginSession(String hotelCode){
        List<String> sessionIds = new ArrayList<>();
        boolean isHotelUser = false;
        Collection<Session> sessions =  sessionDAO.getActiveSessions();
        for (Session session : sessions) {
            User user;
            SimplePrincipalCollection principalCollection;
            if(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY )== null){
                continue;
            }else {
                principalCollection = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                user = (User) principalCollection.getPrimaryPrincipal();
                if(hotelCode.equals(user.getHotelCode())){
                    isHotelUser = true;
                }
                if(isHotelUser){
                    sessionIds.add((String) session.getId());
                }
            }
        }
        return sessionIds;
    }

    /**
     * 功能描述: <br>强制用户下线
     * 〈〉
     * @Param: [sessionId]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/11/1 11:18
     */
    public HttpResponse sessionLoginOut(String sessionId){
        HttpResponse rep = new HttpResponse();
        Session session = sessionDAO.readSession(sessionId);
        sessionDAO.delete(session);
        return rep.ok();
    }
    /**
     * 功能描述: <br>批量下线操作
     * 〈〉
     * @Param: [sessionIds]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/11/1 11:18
     */
    public HttpResponse sessionLoginOut(List<String> sessionIds){
        HttpResponse rep = new HttpResponse();
        for(int i=0; i<sessionIds.size(); i++){
            Session session = sessionDAO.readSession(sessionIds.get(i));
            sessionDAO.delete(session);
        }
        return rep.ok();
    }
}
