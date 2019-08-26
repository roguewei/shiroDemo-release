package com.winston.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.winston.entity.User;
import com.winston.properties.SecurityProperties;
import com.winston.utils.HttpUtil;
import com.winston.utils.redis.SessionRedisKey;
import com.winston.utils.result.CodeMsg;
import com.winston.utils.result.Result;
import com.winston.utils.service.RedisService;
import com.winston.utils.service.TokenService;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * @author Winston
 * @title: AuthenticationFilter
 * @projectName shiro-parent
 * @description: 认证过滤器
 * @date 2019/7/13 11:35
 */
public class MyAuthenticationFilter extends AuthenticatingFilter {

    @Value("${spring.redis.expire}")
    private int timeout;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private RedisService redisService;

    @Autowired
    private TokenService tokenService;

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-Control-Allow-Credentials","true");
        String curOrigin = httpServletRequest.getHeader("Origin");
        httpServletResponse.setHeader("Access-Control-Allow-Origin", curOrigin == null ? "*" : curOrigin);
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }

        return super.preHandle(request, response);
    }

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String uri = httpServletRequest.getRequestURI();
        String method = httpServletRequest.getMethod();
        String propertiesUri = securityProperties.getBrowser().getLoginPage();
        if(propertiesUri != null) {
            if ("/login".equals(uri) && "POST".equals(method)) {//查看是否登录请求
                return executeLogin(request, response);
            } else {
                if (uri.startsWith("/druid")
                        || uri.startsWith("/swagger")
                        || uri.startsWith("/code")
                        || uri.startsWith("/register")//注册
                        || uri.startsWith("/forgetPwd")//忘记密码修改
                        || uri.startsWith("/activation")//激活序列号
                        || uri.startsWith("/v2")
                        || uri.startsWith("/webjars")
                        || uri.startsWith("/favicon")
                        || uri.startsWith("/login")
                        || uri.startsWith("/MP_verify_ISgjYtn9aFZNE9xR.txt")
                        || uri.contentEquals("/authorize")
                        || uri.contentEquals("/author")
                        || uri.contentEquals("/wxLogin")
                        || uri.startsWith("/weixinLogin")
                        || uri.startsWith("/weixinRedirect")
                ) {
                    return super.onPreHandle(request, response, mappedValue);
                } else {
                    // 获取请求头带回来的token
                    String token = httpServletRequest.getHeader(securityProperties.getJwt().getHeader());

                    Subject subject = getSubject(request, response);
                    Session session = redisService.get(SessionRedisKey.JSESSION_KEY, (String) subject.getSession().getId(), Session.class);
                    if (token != null || session != null) {
                        try {
                            boolean exitToken = false;
                            if(token != null){
                                exitToken = tokenService.authToken(httpServletRequest);
                            }
//                            if((token != null && exitToken) || session != null){
                            if(exitToken){
                                return super.onPreHandle(request, response, mappedValue);
                            }
                            outPut(response);
                            return false;
                        } catch (Exception e) {
                            outPut(response);
                            return false;
                        }
                    }
//                    return super.onPreHandle(request, response, mappedValue);
                    outPut(response);
                    return false;
                }

            }
        }
        return false;
    }

    private void outPut(ServletResponse response) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.println(JSON.toJSONString(Result.error(CodeMsg.LOGIN_TIME_PASS)));
            out.flush();
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 执行登录
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        AuthenticationToken token = createToken(request, response);
        if (token == null) {
            throw new IllegalStateException("无法获取： AuthenticationToken");
        }
        try {
            Subject subject = getSubject(request, response);
            subject.login(token);
            subject.getSession().setTimeout(1000*timeout);

//            String username = WebUtils.getCleanParam(request, "username");
//
//            User query = new User();
//            query.setUsername(username);
//            User user = userService.queryByUser(query);
//
//            //进行角色过滤
//            HttpServletRequest request1 =(HttpServletRequest)request;
//            String operatorType = request1.getParameter("operatorType");
//            if (!operatorType.equals(user.getOperatorType())){
//                return this.onLoginFailure(token, new UnknownAccountException(), request, response);
//            }
            redisService.set(SessionRedisKey.JSESSION_KEY, (String) subject.getSession().getId(), subject.getSession());
            return this.onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException e) {
            return this.onLoginFailure(token, e, request, response);
        }
    }

    // 登录成功处理
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        //登陆成功更新登陆时间
        User principal = (User) subject.getPrincipal();
        principal.setLastLogon(new Date().getTime());
//        userService.updateNotNull(principal);
        return true;
    }

    // 登录失败处理
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String exc = e.getClass().getName();
            if (exc.equals(IncorrectCredentialsException.class.getName())) {
                out.println(JSON.toJSONString(Result.error(CodeMsg.PASSWORD_ERROR)));
            } else if (exc.equals(LockedAccountException.class.getName())) {
                out.println(JSON.toJSONString(Result.error(CodeMsg.ACCOUNT_LOCKED)));
            } else if (exc.equals(DisabledAccountException.class.getName())) {
                out.println(JSON.toJSONString(Result.error(CodeMsg.ACCOUNT_STOP)));
            } else if (exc.equals(UnknownAccountException.class.getName())) {
                out.println(JSON.toJSONString(Result.error(CodeMsg.ACCOUNT_NOT_EXIST)));
            }else if (exc.equals(AuthenticationException.class.getName())) {
                out.println(JSON.toJSONString(Result.error(CodeMsg.SERVER_ERROR)));
            }
            out.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse servletResponse) throws Exception {
        String username = WebUtils.getCleanParam(request, "username");
        String password = WebUtils.getCleanParam(request, "password");
        boolean isRememberMe = isRememberMe(request);
        String ip = HttpUtil.getIpAdrress((HttpServletRequest) request);
        return createToken(username, password, isRememberMe, ip);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        return true;
    }
}
