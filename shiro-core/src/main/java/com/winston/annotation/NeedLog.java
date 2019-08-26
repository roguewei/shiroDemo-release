package com.winston.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName NeedLog
 * @Author: Winston
 * @Description: TODO
 * @Date:Create：in 2019/8/26 11:06
 * @Version：
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedLog {

    // 是否需要添加日志记录
    boolean isNeed() default true;

    // 操作名称代码
    String operator();

    // 操作名称描述
    String operatorDesc();

}
