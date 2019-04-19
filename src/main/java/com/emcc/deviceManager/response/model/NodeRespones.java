package com.emcc.deviceManager.response.model;

import java.io.Serializable;

public class NodeRespones extends Response implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 响应码
	 */
	private String code = "0x00000000";
	
	/**
	 * 消息
	 */
	private String msg = "operation succeed";
	
	/**
	 * 返回值
	 */
	private Object result;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "BaseResponsev1 [code=" + code + ", msg=" + msg + ", result=" + result + "]";
	}

}
