package com.winston.utils.result;

import com.github.pagehelper.PageInfo;
import com.winston.utils.HttpUtil;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;

/**
 * @author weigaosheng
 * @description
 * @CalssName Result
 * @date 2019/2/28
 * @params
 * @return
 */
@Data
public class Result<T> implements Serializable {

    //   自定义serialVersionUID
    private static final long serialVersionUID = 8735132092273200831L;

    private int status;
    private String msg;
    private T data;
    // 统计请求耗时，单位ms
    private Long executetime = 123L;
    // 页码
    private int page;
    // 当前展示条数
    private Integer count = 0;
    // 总条数
    private Long total;

    private Result(T data) {
        this.status = 200;
        this.msg = "success";
        this.data = data;
        this.executetime = getExecTime();
    }

    private Result(T data, PageInfo pageInfo){
        this.status = 200;
        this.msg = "success";
        this.data = data;
        this.executetime = getExecTime();
        if(pageInfo != null){
            this.page = pageInfo.getPageNum();
            this.count = pageInfo.getSize();
            this.total = pageInfo.getTotal();
        }
    }

    private Result(CodeMsg cm) {
        if(cm == null){
            return ;
        }
        this.status = cm.getStatus();
        this.msg = cm.getMsg();
        this.executetime = getExecTime();
    }

    /**
     * @return a
     * @Author weigaosheng
     * @Description 返回单条数据成功时的调用
     * @Date 11:56 2019/2/28
    * @Param
     **/
    public static <T> Result<T> success(T data){
        return new Result<T>(data);
    }

    /**
     * @Author Winston
     * @Description 返回多条列表数据成功时调用
     * @Date 16:27 2019/7/5
     * @Param
     * @return
     **/
    public static <T> Result<T> success(T data, PageInfo pageInfo){
        return new Result<T>(data, pageInfo);
    }

    /**
     * @return a
     * @Author weigaosheng
     * @Description 失败时的调用
     * @Date 11:56 2019/2/28
     * @Param
     **/
    public static <T> Result<T> error(CodeMsg cm){
        return new Result<T>(cm);
    }

    private Long getExecTime(){
        HttpServletRequest request = HttpUtil.getRequest();
        Object startTime = request.getAttribute("startTime");
        if(startTime != null){
            long endTime = new Date().getTime();
            return endTime - (long)startTime;
        }
        return 0L;
    }
}
