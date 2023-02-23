package com.system.common;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.Data;
@Data
public class Result {
    private int code; //状态码
    private String message; //返回信息
    private Object data; //数据

    //静态方法：操作成功
    public static Result success(int code,String message,Object data){
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static Result success(Object data){
        return success(200,"操作成功",data);
    }

    //静态方法：失败
    public static Result fail(int code, String message,Object data){
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    //失败返回空数据
    public static Result fail(String message,Object data){
        return fail(400,message,data);
    }

    //失败返回空数据
    public static Result fail(String message){
        return fail(400,message,null);
    }
}
