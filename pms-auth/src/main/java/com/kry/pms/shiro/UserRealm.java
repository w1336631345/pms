package com.kry.pms.shiro;

import java.util.List;

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
		if (user == null || user.isEmpty()) {
			throw new UnknownAccountException("账号或密码不正确");
		}
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());
		return info;
	}

}
