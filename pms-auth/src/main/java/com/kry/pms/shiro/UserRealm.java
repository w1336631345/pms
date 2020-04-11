package com.kry.pms.shiro;

import java.util.List;

import com.kry.pms.base.Constants;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.kry.pms.dao.sys.UserDao;
import com.kry.pms.model.persistence.sys.User;

import lombok.extern.java.Log;
@Log
public class UserRealm extends AuthorizingRealm {

	@Autowired
	UserDao userDao;
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal();
		String password = new String((char[]) token.getCredentials());
		// 查询用户信息
		List<User>  user = userDao.getByUsernameAndPassword(username, password);
		// 账号不存在
		if (user == null || user.isEmpty()|| !Constants.Status.NORMAL.equals(user.get(0).getStatus())) {
			throw new UnknownAccountException("账号或密码不正确,或者账号不可登录");
		}
		if(("audit").equals(user.get(0).getAllowLogin())){
			throw new UnknownAccountException("夜审稽核中，禁止登录");
		}
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());
		return info;
	}

}
