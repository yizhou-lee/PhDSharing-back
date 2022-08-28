package com.lyz.common.lang;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {

    private int code;  // 200 means normal, others mean abnormal
    private String msg;
    private Object data;

    public static Result succ(Object data){
        return succ(20000, "Operate successful", data);
    }

    public static Result succ(int code, String msg, Object data){
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public static Result fail(String msg){
        return fail(400, msg, null);
    }

    public static Result fail(String msg, Object data){
        return fail(400, msg, data);
    }

    public static Result fail(int code, String msg, Object data){
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

}
