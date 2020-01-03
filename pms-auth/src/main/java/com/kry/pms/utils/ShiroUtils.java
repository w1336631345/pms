package com.kry.pms.utils;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import com.kry.pms.model.persistence.sys.User;

public class ShiroUtils {
	
	@Autowired
    private static SessionDAO sessionDAO;

    public static Subject getSubjct() {
    	Subject subject = SecurityUtils.getSubject();
        return subject;
    }
    public static User getUser() {
        Object object = getSubjct().getPrincipal();
        return (User)object;
    }
    public static String getUserId() {
    	User user = getUser();
    	if(user != null) {
    		return getUser().getId();
    	}
    	return null;
    }
    public static void logout() {
        getSubjct().logout();
    }

    public static List<Principal> getPrinciples() {
        List<Principal> principals = null;
//        Collection<Session> sessions = sessionDAO.getActiveSessions();
        return principals;
    }
}
