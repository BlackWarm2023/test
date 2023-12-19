package com.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: TODO
 * @author: blackwarm
 * @date: 2023年 09月 18日  15:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    //成功结果
    public static <T> Result<T> success(){
        return new Result<>(20000,"success",null);
    }
    public static <T> Result<T> success(T data){
        return new Result<>(20000,"success",data);
    }
    public static <T> Result<T> success(T data, String message){
        return new Result<>(20000,message,data);
    }
    public static <T> Result<T> success(String message){
        return new Result<>(20000,message,null);
    }

    //失败结果
    public static <T> Result<T> fail(){
        return new Result<>(20001,"fail",null);
    }
    public static <T> Result<T> fail(Integer code){
        return new Result<>(code,"fail",null);
    }
    public static <T> Result<T> fail(Integer code, String message){
        return new Result<>(code,message,null);
    }
    public static <T> Result<T> fail(String message){
        return new Result<>(20001,message,null);
    }
}
