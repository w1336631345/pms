package com.kry.pms.config;

import com.kry.pms.shiro.SessionManager;
import com.kry.pms.shiro.UserRealm;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
	@Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
	@Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/auth/unauth");
//        shiroFilterFactoryBean.setUnauthorizedUrl("/auth/unauth");
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        filterChainDefinitionMap.put("/auth/admin/login", "anon");
        filterChainDefinitionMap.put("/auth/admin/bind_wx", "anon");
        filterChainDefinitionMap.put("/auth/getOpenid", "anon");
        filterChainDefinitionMap.put("/auth/encrypte", "anon");
        filterChainDefinitionMap.put("/auth/wxLogin", "anon");
        filterChainDefinitionMap.put("/api/v1/busi/checkInRecord/noRole/orderNum2", "anon");
        filterChainDefinitionMap.put("/auth/binding", "anon");
        filterChainDefinitionMap.put("/auth/getHotelList", "anon");
        filterChainDefinitionMap.put("/wx/redirect/**", "anon");
//        filterChainDefinitionMap.put("/api/**", "anon");

        filterChainDefinitionMap.put("/user/**", "authc");
        //<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        filterChainDefinitionMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;

    }
	 @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm());
        //缓存设置
        securityManager.setCacheManager(cacheManager());
         //注入记住我管理器(cookie管理)
        securityManager.setRememberMeManager(rememberMeManager());
        securityManager.setSessionManager(sessionManager());//自定义的sessionManager
        return securityManager;
    }

    //shiro session的管理
    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new SessionManager();
        //自定义session时效时间，shiro的session默认失效时间是30分钟
//        sessionManager.setGlobalSessionTimeout(DEFAULT_GLOBAL_SESSION_TIMEOUT * 48);
        sessionManager.setSessionDAO(sessionDAO());
        Collection<SessionListener> listeners = new ArrayList<SessionListener>();
        listeners.add(new BDSessionListener());
        sessionManager.setSessionListeners(listeners);
        return sessionManager;
    }

    @Bean
    public UserRealm userRealm() {
    	UserRealm userRealm = new UserRealm();
        return userRealm;
    }

    @Bean
    public SessionDAO sessionDAO() {
	    return new MemorySessionDAO();
    }

    /**
     * cookie对象;
     * rememberMeCookie()方法是设置Cookie的生成模版，比如cookie的name，cookie的有效时间等等。
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie(){
        //System.out.println("ShiroConfiguration.rememberMeCookie()");
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(259200);
        return simpleCookie;
    }
    /**
     * cookie管理对象;
     * rememberMeManager()方法是生成rememberMe管理器，而且要将这个rememberMe管理器设置到securityManager中
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager(){
        //System.out.println("ShiroConfiguration.rememberMeManager()");
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));
        return cookieRememberMeManager;
    }
    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

//    @Bean
//    public EhCacheManager ehCacheManager() {
//        EhCacheManager em = new EhCacheManager();
//        em.setCacheManager(cacheManager());
//        return em;
//    }

//    @Bean("cacheManager2")
//    CacheManager cacheManager(){
//        return CacheManager.create();
//    }
    @Bean("cacheManager2")
    public CacheManager cacheManager() {
        return new MemoryConstrainedCacheManager();
    }
}
