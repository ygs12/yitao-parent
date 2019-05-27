package com.gerry.yitao.response;

import lombok.Data;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/24 14:00
 * @Description: 结果类
 */
@Data
public class Result<T> {

    private int code;
    private String msg;
    private T data;

    /**
     *  成功时候的调用
     * */
    public static  <T> Result<T> success(T data){
        Result<T> result = new Result<T>(data);
        result.code = CodeMsg.SUCCESS.getCode();
        return new Result<T>(data);
    }

    /**
     *  失败时候的调用
     * */
    public static  <T> Result<T> error(CodeMsg codeMsg){
        return new Result<T>(codeMsg);
    }

    private Result(T data) {
        this.data = data;
    }

    private Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Result(CodeMsg codeMsg) {
        if(codeMsg != null) {
            this.code = codeMsg.getCode();
            this.msg = codeMsg.getMsg();
        }
    }
}
