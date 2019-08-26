package com.winston.interceptor;

import com.winston.annotation.NeedLog;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @ClassName NeedLogInterceptor
 * @Author: Winston
 * @Description: TODO
 * @Date:Create：in 2019/8/26 11:07
 * @Version：
 */
@Component
public class NeedLogInterceptor extends HandlerInterceptorAdapter {

    /**
     * @Author Winston
     * @Description 拦截controller，在执行controller方法之前执行
     * @Date 9:48 2019/5/15
     * @Param
     * @return
     **/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = new Date().getTime();
        request.setAttribute("startTime", startTime);
        return super.preHandle(request, response, handler);
    }

    /**
     * @Author Winston
     * @Description 拦截controller，如果controller方法抛出异常则不执行该拦截方法，
     * 如果没有抛出异常则在controller方法执行完成之后执行该方法
     * @Date 9:48 2019/5/15
     * @Param
     * @return
     **/
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        addLog(request, response, handler, "1");
    }

    /**
     * @Author Winston
     * @Description 无论controller方法是否抛出异常都会执行该拦截器方法
     * @Date 9:54 2019/5/15
     * @Param
     * @return
     **/
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        boolean success = false;
        if(request.getAttribute("success") != null){
            success = (boolean) request.getAttribute("success");
        }

        if(!success){
            addLog(request, response, handler, "2");
        }
    }

    private void addLog(HttpServletRequest request, HttpServletResponse response, Object handler, String resType){
        boolean result = false;
        if(handler instanceof HandlerMethod){

            HandlerMethod method = (HandlerMethod) handler;

            String requestMethod = method.getMethod().getName();
            Integer userId = null;

            // 获取方法中的注解
            NeedLog needLog = method.getMethodAnnotation(NeedLog.class);
            if(needLog != null){
                if(needLog.isNeed()){
//                    if("login".equals(requestMethod)){
//                        User user = userService.selectByUsername(request.getParameter("username"));
//                        userId = user.getId();
//                    }else{
//                        userId = rawAccessJwtToken.getUserId(request);
//                    }
//                    String operator = needLog.operator();
//                    LogInfo logInfo = new LogInfo();
//                    logInfo.setCreateTime(new Date().getTime());
//                    logInfo.setIpAddress(IpUtil.getIpAdrress(request));
//                    logInfo.setLogType(operator);
//                    logInfo.setUserId(userId);
//                    logInfo.setResultType(resType);
//                    logInfo.setOperatordesc(needLog.operatorDesc());
//                    result = logInfoService.addLogInfo(logInfo);
//                    if(result && "1".equals(resType)){
//                        request.setAttribute("success", true);
//                    }else{
//                        request.setAttribute("success", false);
//                    }
                }
            }
        }
    }
}
