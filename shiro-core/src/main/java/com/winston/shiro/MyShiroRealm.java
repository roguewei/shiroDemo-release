package com.winston.shiro;

import com.winston.entity.Permission;
import com.winston.entity.User;
import com.winston.service.IPermissionService;
import com.winston.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.util.ByteSource;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.*;

public class MyShiroRealm extends AuthorizingRealm {
    @Resource
    private IUserService userService;

    @Resource
    private IPermissionService permissionService;

    @Autowired
    private RedisSessionDAO redisSessionDAO;

//    @Autowired
//    private SecurityProperties securityProperties;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<Permission> permissionList = permissionService.queryByUserName(user.getUsername());
        //权限信息对象info，用来存放查出的用户的所有的角色（role）及权限（permissoin）
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        for (Permission permission : permissionList) {
            info.addStringPermission(permission.getPerurl());
        }
        return info;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取用户的输入账号
        String username = (String) authenticationToken.getPrincipal();
//        if(LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())){
//            user = userService.selectByOpenId(username);
//            if(user == null){
//                user = userService.selectByUsername(username);
//            }
//        }else{
//            user = userService.selectByUsername(username);
//        }
        User query = new User();
        query.setUsername(username);
        User user = userService.queryByUser(query);

        if (user == null) throw new UnknownAccountException();

        if (0 == user.getEnable()) {
            throw new LockedAccountException();//账号被锁定
        }

        SimpleAuthenticationInfo simpleAuthenticationInfo = null;

//        if(LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())
//            && "wechat".equals(securityProperties.getBrowser().getLoginTag())){
//            simpleAuthenticationInfo = new SimpleAuthenticationInfo(
//                    user,//用户
//                    user.getOpenidHex(),//密码
//                    ByteSource.Util.bytes(username),
//                    getName()//reaml name
//            );
//        }else{
//            simpleAuthenticationInfo = new SimpleAuthenticationInfo(
//                    user,//用户
//                    user.getPassword(),//密码
//                    ByteSource.Util.bytes(username),
//                    getName()//reaml name
//            );
//        }
        simpleAuthenticationInfo = new SimpleAuthenticationInfo(
                user,//用户
                user.getPassword(),//密码
                ByteSource.Util.bytes(username),
                getName()//reaml name
        );

        //当验证都通过后，把用户信息放在session里
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("userSession", user);
        session.setAttribute("userSessionId", user.getId());

//        UserContext.setUser(user);
        return simpleAuthenticationInfo;
    }

    /**
     * 根据userId 清除当前session存在的用户的权限缓存
     *
     * @param userIds 已经修改了权限的userId
     */
    public void clearUserAuthByUserId(List<Integer> userIds) {
        if (null == userIds || userIds.size() == 0) return;
        //获取所有session
        Collection<Session> sessions = redisSessionDAO.getActiveSessions();
        //定义返回
        List<SimplePrincipalCollection> list = new ArrayList<SimplePrincipalCollection>();
        for (Session session : sessions) {
            //获取session登录信息。
            Object obj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (null != obj && obj instanceof SimplePrincipalCollection) {
                //强转
                SimplePrincipalCollection spc = (SimplePrincipalCollection) obj;
                //判断用户，匹配用户ID。
                obj = spc.getPrimaryPrincipal();
                if (null != obj && obj instanceof User) {
                    User user = (User) obj;
                    System.out.println("user:" + user);
                    //比较用户ID，符合即加入集合
                    if (null != user && userIds.contains(user.getId())) {
                        list.add(spc);
                    }
                }
            }
        }
        RealmSecurityManager securityManager =
                (RealmSecurityManager) SecurityUtils.getSecurityManager();
        MyShiroRealm realm = (MyShiroRealm) securityManager.getRealms().iterator().next();
        for (SimplePrincipalCollection simplePrincipalCollection : list) {
            realm.clearCachedAuthorizationInfo(simplePrincipalCollection);
        }
    }


}
