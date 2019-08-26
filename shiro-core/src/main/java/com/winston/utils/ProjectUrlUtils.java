package com.winston.utils;

import com.winston.entity.Permission;
import com.winston.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Winston
 * @title: ProjectUrlUtils
 * @projectName shiro-parent
 * @description:
 * @date 2019/8/7 17:14
 */
@Component
public class ProjectUrlUtils {

    @Autowired
    WebApplicationContext applicationContext;

    @Autowired
    private IPermissionService permissionService;

    public List<String> addAllUrl(){
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        //获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        List<String> urlList = new ArrayList<>();
        for (RequestMappingInfo info : map.keySet()){
            //获取url的Set集合，一个方法可能对应多个url
            Set<String> patterns = info.getPatternsCondition().getPatterns();
            for (String url : patterns){
                if(url.contains("/web")){
                    urlList.add(url);
                    String newUrl = url.replace("/","");
                    Permission permission = new Permission();
                    permission.setPerurl(url);
                    if(url.length()-newUrl.length()<=2){
                        permission.setType("0");
                    }else{
                        permission.setType("1");
                    }
                    permissionService.addAllUrl(permission);
                }
            }
        }
        return urlList;
    }

}
