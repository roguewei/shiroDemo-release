package com.winston.exception;

import com.winston.utils.result.CodeMsg;
import lombok.Data;

@Data
public class ErrorException extends RuntimeException {

    private Integer status;
    private String msg;

    public ErrorException(Integer status, String msg){
        this.status= status;
        this.msg=msg;
    }
    public ErrorException(String msg){
        this.status= 500;
        this.msg=msg;
    }

    public ErrorException(CodeMsg codeMsg){
        this.status= codeMsg.getStatus();
        this.msg=codeMsg.getMsg();
    }
}
