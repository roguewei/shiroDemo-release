package com.winston.utils.result;

import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.io.Serializable;

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
    // 当前展示条数
    private Integer count = 0;

    // 计数器
    private String draw;
    // 查询总条数
    private long recordsTotal;

    private long recordsFiltered;

    private Result(T data) {
        this.status = 200;
        this.msg = "success";
        this.data = data;
    }

    private Result(T data, PageInfo pageInfo){
        this.status = 200;
        this.msg = "success";
        this.data = data;
        if(pageInfo != null){
            this.count = pageInfo.getSize();
            this.recordsTotal = pageInfo.getTotal();
            this.recordsFiltered = pageInfo.getTotal();
        }
    }

    private Result(T data, String draw, PageInfo pageInfo){
        this.status = 200;
        this.msg = "success";
        this.data = data;
        this.draw = draw;
        if(pageInfo != null){
            this.count = pageInfo.getSize();
            this.recordsTotal = pageInfo.getTotal();
            this.recordsFiltered = pageInfo.getTotal();
        }
    }

    private Result(CodeMsg cm) {
        if(cm == null){
            return ;
        }
        this.status = cm.getStatus();
        this.msg = cm.getMsg();
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
      * @Author Winston
      * @Description 返回多条列表数据成功时调用
      * @Date 16:27 2019/7/5
      * @Param
      * @return
      **/
    public static <T> Result<T> success(T data, String draw, PageInfo pageInfo){
        return new Result<T>(data, draw, pageInfo);
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
}
