package com.winston.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.winston.entity.Permission;
import com.winston.entity.User;
import com.winston.service.IPermissionService;
import com.winston.utils.result.CodeMsg;
import com.winston.utils.result.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author Winston
 * @title: PerFilter
 * @projectName shiroDemo
 * @description:
 * @date 2019/7/25 11:55
 */
public class PerFilter extends AuthorizationFilter {

    @Autowired
    private IPermissionService permissionService;

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

    /**
      * @Author Winston
      * @Description 该方法返回true表示验证通过，不再执行下面的onAccessDenied方法
      * 返回false时进入下面的onAccessDenied方法
      * @Date 16:42 2019/7/25
      * @Param
      * @return
      **/
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object o) throws Exception {

        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        CodeMsg codeMsg = null;
        if(user == null){
            codeMsg = CodeMsg.IS_NOT_LOGIN;
        }else{
            List<Permission> permissionList = permissionService.queryByUserName(user.getUsername());
            for(Permission permission : permissionList){
                if(subject.isPermitted(permission.getPerurl())){
                    // 此处不管有没有权限都返回true，所以需要配置异常拦截器CustomExceptionHandler
                    return true;
                }
            }
            codeMsg = CodeMsg.HAS_NOT_PERMISSION;
        }
        outPut(response, codeMsg);
        return false;
    }

    private void outPut(ServletResponse response, CodeMsg codeMsg) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println(JSON.toJSONString(Result.error(codeMsg)));
        out.flush();
        out.close();
    }

    /**
      * @Author Winston
      * @Description 当上一方法isAccessAllowed返回false时进入该方法，该方法返回false表示不再继续往下执行
      * @Date 16:41 2019/7/25
      * @Param
      * @return
      **/
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        return false;
    }
}
