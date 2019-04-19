package com.emcc.deviceManager.response.model;

/**
 * 没有实际用途，只作为父类类型指向子类对象时使用
 *
 * @author zhanghh
 * @date 2018/5/10 20:33
 */
public class Response {
    private String msg;

    private String code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
