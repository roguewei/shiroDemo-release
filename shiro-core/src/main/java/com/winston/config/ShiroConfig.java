package com.winston.config;

import com.github.pagehelper.util.StringUtil;
import com.winston.entity.Permission;
import com.winston.properties.SecurityProperties;
import com.winston.service.IPermissionService;
import com.winston.shiro.MyShiroRealm;
import com.winston.shiro.filter.MyAuthenticationFilter;
import com.winston.shiro.filter.PerFilter;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.jboss.logging.Logger;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Winston
 * @title: ShiroConfig
 * @projectName shiroDemo
 * @description:
 * @date 2019/7/24 14:44
 */
@Configuration
public class ShiroConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.expire}")
    private int expire;

    @Value("${spring.redis.jedis.pool.max-wait}")
    private int maxIdle;

    @Value("${spring.redis.jedis.pool.max-wait}")
    private long maxWaitMillis;

    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * @Author Winston
     * @Description 配置Shiro生命周期处理器
     * @Date 14:51 2019/5/9
     * @Param
     * @return
     **/
    @Bean(name = "lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
        System.out.println("###########################ShiroConfiguration.shiroFilter()##########################################");
        ShiroFilterFactoryBean shiroFilterFactoryBean=new ShiroFilterFactoryBean();

        //必须设置SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //如果不设置默认会自动寻找web工程根目录下的“/login.jsp”页面
        shiroFilterFactoryBean.setLoginUrl(securityProperties.getBrowser().getLoginPage());
        //登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/usersPage");
        //未授权界面
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");
        Map<String, Filter> map = new LinkedHashMap<>();
        map.put("perms", perFilter());
        map.put("authc", authenticationFilter());
        shiroFilterFactoryBean.setFilters(map);
        //拦截器
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

        //配置退出 过滤器，其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/druid/**", "anon");
        filterChainDefinitionMap.put("/swagger/*", "anon");
        filterChainDefinitionMap.put("/v2/*", "anon");
        filterChainDefinitionMap.put("/favicon/*", "anon");
        filterChainDefinitionMap.put("/webjars/*", "anon");
        filterChainDefinitionMap.put("/logout","anon");
        filterChainDefinitionMap.put("/css/**","anon");
        filterChainDefinitionMap.put("/js/**","anon");
        filterChainDefinitionMap.put("/img/**","anon");
        filterChainDefinitionMap.put("/font-awesome/**","anon");
        filterChainDefinitionMap.put("/MP_verify_ISgjYtn9aFZNE9xR.txt","anon");
        filterChainDefinitionMap.put("/wxLogin", "anon");
        filterChainDefinitionMap.put("/weixinLogin", "anon");
        filterChainDefinitionMap.put("/weixinRedirect", "anon");

        //<!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        //自定义加载权限资源关系
        List<Permission> permissionList = permissionService.queryAll();
        for (Permission permission :permissionList) {
            if (StringUtil.isNotEmpty(permission.getPerurl())){
                String per = "perms["+permission.getPerurl()+"]";
                filterChainDefinitionMap.put(permission.getPerurl(), per);
            }
        }
        filterChainDefinitionMap.put("/**","authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }
    @Bean
    public DefaultWebSecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置realm
        securityManager.setRealm(myShiroRealm());
        //自定义缓存实现，使用redis
        securityManager.setCacheManager(cacheManager());
        //自定义session管理，使用redis
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }
    @Bean
    public MyShiroRealm myShiroRealm(){
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }

    /**
     * 凭证匹配器
     * 由于我们的密码检验交给Shiro的simpleAuthenticationInfo处理了，
     * 所以我们需要修改下doGetAuthenticationInfo的代码；
     *      * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法：这里使用MD5算法
        hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于md5(md5(""))
        return  hashedCredentialsMatcher;
    }

    @Bean
    public RedisManager redisManager(){
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        redisManager.setTimeout(timeout);
        redisManager.setExpire(expire);//配置缓存过期时间
        redisManager.setPassword(password);
        return redisManager;
    }

    /**
     * cacheManager缓存 redis实现
     * 使用的是shiro-redis开源插件
     * @return
     */
    public RedisCacheManager cacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现，通过redis
     * 使用的是shiro-redis开源插件
     * @return
     */
    @Bean
    public RedisSessionDAO redisSessionDAO(){
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    /**
     * shiro session管理
     * @return
     */
    @Bean
    public DefaultWebSessionManager sessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }

    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * @Author Winston
     * @Description 开启shiro 注解模式
     * 可以在controller中的方法前加上注解
     * 如 @RequiresPermissions("userInfo:add")
     * @Date 16:34 2019/5/9
     * @Param
     * @return
     **/
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * @Author Administrator
     * @Description 记住我功能
     * cookie对象;会话Cookie模板 ,默认为: JSESSIONID 问题: 与SERVLET容器名冲突,重新定义为sid或rememberMe，自定义
     * @Date 2019/5/10 15:44
     * @Param
     * @return
     **/
    @Bean
    public SimpleCookie rememberMeCookie(){
        // 这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        // setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：
        // setcookie()的第七个参数
        // 设为true后，只能通过http访问，javascript无法访问
        // 防止xss读取cookie
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        // 记住我cookie生效时间30天 ,单位秒;
        simpleCookie.setMaxAge(60*60*24*30);
        return simpleCookie;
    }

    /**
     * @Author Administrator
     * @Description cookie管理对象;记住我功能,rememberMe管理器
     * @Date 2019/5/10 15:49
     * @Param
     * @return
     **/
    @Bean
    public CookieRememberMeManager rememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        // ememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        return cookieRememberMeManager;
    }

    @Bean
    public JedisPool redisPoolFactory() {
        Logger.getLogger(getClass()).info("JedisPool注入成功！！");
        Logger.getLogger(getClass()).info("redis地址：" + host + ":" + port);
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);
        return jedisPool;
    }

    /**
      * @Author Winston
      * @Description 配置登录认证过滤器
      * @Date 16:29 2019/7/24
      * @Param
      * @return
      **/
    @Bean
    public MyAuthenticationFilter authenticationFilter() {
        return new MyAuthenticationFilter();
    }

    /**
      * @Author Winston
      * @Description 配置权限过滤器
      * @Date 16:29 2019/7/24
      * @Param
      * @return
      **/

    @Bean
    public PerFilter perFilter(){
        return new PerFilter();
    }

}
